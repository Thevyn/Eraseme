/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import no.esito.anonymizer.IAnonymization;
import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IContext;
import no.esito.anonymizer.IContext.RunType;
import no.esito.anonymizer.IUpdateColumns;
import no.esito.anonymizer.Log;

public abstract class AbstractUpdateTask extends AbstractTask implements IUpdateColumns {

    private class Key {
        String[] key;
        Key (String[] key) {
            this.key = key;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Key))
                return false;
            String[] other = ((Key)obj).key;
            if (key == other)
                return true;
            if (key == null || other == null)
                return false;
            if (key.length != other.length)
                return false;
            for (int i = 0; i < key.length; i++) {
                if (!key[i].equals(other[i])) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            int res = 0;
            if (key != null) {
                for (String k : key) {
                    res += k.hashCode();
                }
            }
            return res;
        }
    }

    ArrayList<Key> keysbefore;

    String[][] keyswhere;

    @Override
    public void run(IContext context) throws Throwable {
        start(context, prefix);
        updateTask(context);
        finish(context, prefix);
    }

    private void updateTask(IContext context) throws Throwable {
        IAnonymization[] anonymizations = getAnonymizations();
        if (anonymizations.length == 0) {
            return;
        }
        IColumn[] columns = getColumns();
        List<String> colnames = IColumn.listNames(columns);
        prepareInputs(context, anonymizations);
        List<String[]> rows = sqlReadRows(context.getConnection(), getTable(), colnames, getWhere());
        if (rows.isEmpty()) {
            return;
        }
        IColumn[] index = getIndexColumns();
        IAnonymization[] keys = getKeyAno(anonymizations, index);
        if (keys != null) {
            // We are updating the primary key and needs to remember the original values
            int[] keycolumnnums = new int[index.length];
            for (int j = 0; j < index.length; j++) {
                keycolumnnums[j] = colnames.indexOf(index[j].getName());
            }
            keysbefore = new ArrayList<Key>(rows.size());
            for (String[] row : rows) {
                String[] keyvalues = new String[index.length];
                for (int j = 0; j < index.length; j++) {
                    keyvalues[j] = row[keycolumnnums[j]];
                }
                keysbefore.add(new Key(keyvalues));
            }
        }
        keyswhere = new String[index.length][];
        for (int i = 0; i < index.length; i++) {
            IColumn col = index[i];
            int colnum = colnames.indexOf(col.getName());
            String[] wc = new String[rows.size()];
            keyswhere[i] = wc;
            for (int j = 0; j < rows.size(); j++) {
                wc[j] = rows.get(j)[colnum];
            }
        }
        Collection<String> updatecolumns = new LinkedHashSet<String>();
        for (IAnonymization anonymization : anonymizations) {
            updatecolumns.add(anonymization.getColumn().getName());
            anonymization.run(context, colnames, rows);
        }
        if (keys != null) {
            // case of autogenerated keys
            if (keys.length == 1 && keys[0] instanceof AbstractMasking && ((AbstractMasking) keys[0]).getInputs().length == 0) {
                sqlUpdateRowsWithKeyAuto(context.getConnection(), colnames, updatecolumns, rows, keys[0]);
            } else {
                sqlUpdateRowsWithKey(context.getConnection(), colnames, updatecolumns, rows, keys);
            }
        } else {
            if (keyswhere.length == 0) {
                sqlUpdateRowsNoIndex(context.getConnection(), columns, new ArrayList<String>(updatecolumns), rows);
            } else {
                sqlUpdateRows(context.getConnection(), new ArrayList<String>(updatecolumns), rows, keyswhere);
            }
        }
    }

    @Override
    public boolean shouldRun(IContext context) {
        return context.getRunType() == RunType.RUN;
    }

    private static IAnonymization[] getKeyAno(IAnonymization[] anonymizations, IColumn[] indexcolumns) {
        List<IAnonymization> keys = new ArrayList<>();
        for (IColumn column : indexcolumns) {
            IAnonymization anonymization = getAno(anonymizations, column);
            if (anonymization != null) {
                keys.add(anonymization);
            }
        }
        return keys.isEmpty() ? null : keys.toArray(new IAnonymization[keys.size()]);
    }

    private static IAnonymization getAno(IAnonymization[] anonymizations, IColumn column) {
        for (IAnonymization anonymization : anonymizations) {
                if (column.getName().equals(anonymization.getColumn().getName())) {
                    return anonymization;
            }
        }
        return null;
    }

    /**
     * Update a key column needs to update all related references.
     *
     * @param conn Connection
     * @param colnames list of columns
     * @param updatecolumns c\list of column names to be updated
     * @param rows data
     * @param keys keys
     * @throws SQLException when SQL issues
     * @throws IOException when problems reading config file
     */
    public void sqlUpdateRowsWithKey(Connection conn, List<String> colnames, Collection<String> updatecolumns,
            List<String[]> rows, IAnonymization keys[]) throws SQLException, IOException {
            String table = getTable();
            IColumn[] types = getColumns();
            IColumn[] index = getIndexColumns();
            String[] indexcolumnnames = new String[index.length];
            int[] indexcolumnnums = new int[index.length];

            for (int j = 0; j < index.length; j++) {
                indexcolumnnames[j] = index[j].getName();
                indexcolumnnums[j] = colnames.indexOf(indexcolumnnames[j]);
            }
            ArrayList<Key> indexbefore = new ArrayList<Key>(keysbefore);
            ArrayList<Key> indexafter = new ArrayList<Key>(rows.size());
            for (String[]row : rows) {
                String[]key = new String[index.length];
                for (int j = 0; j < index.length; j++) {
                    key[j] = row[indexcolumnnums[j]];
                }
                indexafter.add(new Key(key));
            }

            HashSet<Key> todobefore = new HashSet<Key>(indexbefore);
            HashSet<Key> todoafter =  new HashSet<Key>(indexafter);

            // Upodate all where the index is unchanged
            for (int i = 0; i < indexafter.size(); i++) {
                Key newkey = indexafter.get(i);
                Key oldkey = indexbefore.get(i);
                if (newkey.equals(oldkey)) {
                    List<String[]> row = new ArrayList<String[]>(1);
                    row.add(rows.get(i));
                    String[][] where = new String[index.length][];
                    for (int j = 0; j < index.length; j++) {
                        IColumn col = index[j];
                        int colnum = colnames.indexOf(col.getName());
                        String[] wc = new String[1];
                        where[j] = wc;
                        wc[0] = row.get(0)[colnum];
                    }
                       sqlUpdateRows(conn, new ArrayList<String>(updatecolumns), row, where);
                       todobefore.remove(oldkey);
                       todoafter.remove(newkey);
                }
            }
            // Update the rest by doing insert and delete
            while(!todoafter.isEmpty()) {
                List<Key> todo = new ArrayList<Key>();
                Key newkey = todoafter.iterator().next();
                todo.add(0,newkey);
                boolean needTemp = false;
                while (todobefore.contains(newkey)) {
                    int i = indexbefore.indexOf(newkey);
                    newkey = indexafter.get(i);
                    if (todo.contains(newkey)) {
                        needTemp = true;
                        break;
                    }
                    todo.add(0,newkey);
                }
                Key startkey = todo.get(0);
                if (needTemp) {
                    int i = indexafter.indexOf(startkey);
                    String[] row = rows.get(i);
                    String[] keybefore = indexbefore.get(i).key;
                    String[] keyafter = new String[index.length];
                    boolean exists = true;
                    for (int j = 0; j < index.length; j++) {
                        IAnonymization key = getAno(keys, index[j]);
                        if (key != null) {
                            keyafter[j] = key.getTempId();
                            if (keyafter[j] != keybefore[j]) {
                                exists = false;
                            }
                            row[indexcolumnnums[j]] = keyafter[j];
                        }
                        else {
                            keyafter[j] = row[indexcolumnnums[j]];
                        }
                    }
                    if (exists) {
                        throw new RuntimeException("DataSet already contains temp value (" + keyafter + ")");
                    }
                    moveRow(conn, colnames, keys, table, types, indexcolumnnames, row, keybefore, keyafter);
                    todobefore.remove(indexbefore.get(i));
                    todoafter.remove(startkey);
                }
                for (int j = needTemp ? 1 : 0; j < todo.size(); j++) {
                    // Todo gives list of those after values not currently occupied
                    Key key = todo.get(j);
                    int i = indexafter.indexOf(key);
                    String[] row = rows.get(i);
                    String[] keybefore = indexbefore.get(i).key;
                    String[] keyafter = indexafter.get(i).key;
                    moveRow(conn, colnames, keys, table, types, indexcolumnnames, row, keybefore, keyafter);
                    todobefore.remove(indexbefore.get(i));
                    todoafter.remove(key);
                }
                if (needTemp) {
                    int i = indexafter.indexOf(startkey);
                    String[] keybefore = new String[index.length];
                    String[] keyafter = startkey.key;
                    String[] row = rows.get(i);
                    for (int j = 0; j < index.length; j++) {
                        row[indexcolumnnums[j]] = keyafter[j];
                        IAnonymization key = getAno(keys, index[j]);
                        if (key != null) {
                            keybefore[j] = key.getTempId();
                        }
                        else {
                            keybefore[j] = row[indexcolumnnums[j]];
                        }
                    }
                    moveRow(conn, colnames, keys, table, types, indexcolumnnames, row, keybefore, keyafter);
                }
            }
        }

    private void moveRow(Connection conn, List<String> readcolumns, IAnonymization keys[], String table,
        IColumn[] types, String[] keycolumnnames, String[] row, String[] before, String[] after) throws SQLException, IOException {
        String[] befores2 = new String[keycolumnnames.length];
        String[] afters2 = new String[keycolumnnames.length];
        for (int j = 0; j < keycolumnnames.length; j ++) {
            IColumn lookup = IColumn.lookup(keycolumnnames[j], types);
            befores2[j] = lookup.addQuotes(before[j]);
            afters2[j] = lookup.addQuotes(after[j]);
        }
        sqlExecuteUpdate(conn,
            "INSERT INTO " + schemaPrefix(table) + sqlAssignInsert(types, readcolumns, readcolumns, row));
        for (PropagateUpdate uc : keys[0].getPropagatedUpdates()) {
            updateChild(conn, uc, befores2[0], afters2[0]);
        }
        String deletecmd = "DELETE FROM " + schemaPrefix(table);
        IColumn[] index = ((IUpdateColumns) this).getIndexColumns();
        for (int i = 0; i < index.length; i++) {
            deletecmd += (i == 0 ? " WHERE " : " AND ") + index[i].getName() + (keyswhere[i] == null ? " IS NULL" : (" = " + index[i].addQuotes(before[i])));
        }
        sqlExecuteUpdate(conn, deletecmd);
     }

    /**
     * Update with returned autogenerated value as key.
     *
     * @param conn Connection
     * @param colnames list of names
     * @param updatecolumns list of names
     * @param rows data
     * @param anonymization anonymization
     * @throws SQLException when SQL issues
     * @throws IOException when problems reading mapping file
      */
    public void sqlUpdateRowsWithKeyAuto(Connection conn, List<String> colnames, Collection<String> updatecolumns,
        List<String[]> rows, IAnonymization anonymization) throws IOException, SQLException {
        String table = getTable();
        IColumn[] types = getColumns();
        String keycolumnname = anonymization.getColumn().getName();
        int keycolnum = colnames.indexOf(keycolumnname);
        ArrayList<String> keysafter = new ArrayList<>();
        for (String[] row : rows) {
            keysafter.add(row[keycolnum]);
        }
        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);
            String before = keysbefore.get(i).key[0];
            ArrayList<String> minuskey = new ArrayList<>(colnames);
            minuskey.remove(keycolumnname);
            String cmd = "INSERT INTO " + schemaPrefix(table) + sqlAssignInsert(types, colnames, minuskey, row);
            Log.debug(cmd);
            String autores = "";
            PreparedStatement stmt = conn.prepareStatement(cmd, new String[] { keycolumnname });
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                autores = rs.getString(1);
            }
            rs.close();
            stmt.close();
            String before2 = IColumn.lookup(keycolumnname, types).addQuotes(before);
            String after2 = IColumn.lookup(keycolumnname, types).addQuotes(autores);
            for (PropagateUpdate uc : anonymization.getPropagatedUpdates()) {
                updateChild(conn, uc, before2, after2);
            }
            sqlExecuteUpdate(conn,
                "DELETE FROM " + schemaPrefix(table) + " WHERE " + wrap(keycolumnname) + (before2 == null ? " IS NULL" : "=" + before2));
        }
    }

    private static String sqlAssignInsert(IColumn[] types, List<String> readcolumns, Collection<String> updatecolumns,
        String[] row) {
        StringBuilder assign = new StringBuilder();
        assign.append(" (" + AbstractTask.sqlColumnsWrap(updatecolumns) + ") VALUES (");
        boolean start = true;
        for (int i = 0; i < readcolumns.size(); i++) {
            String name = readcolumns.get(i);
            if (updatecolumns.contains(name)) {
                String val = row[i];
                assign.append(start ? "" : ", ");
                start = false;
                assign.append(types[i].addQuotes(val));
            }
        }
        assign.append(")");
        return assign.toString();
    }

    private static void updateChild(Connection conn, PropagateUpdate uc, String before, String after) throws IOException, SQLException {
        String cmd = "UPDATE " + schemaPrefix(uc.table) + " " + " SET " + wrap(uc.fkColumn) + "=" + after + " WHERE "
            + wrap(uc.fkColumn) + (before == null ? " IS NULL" : "=" + before);
        sqlExecuteUpdate(conn, cmd);
    }

    @Override
    protected void initCols(Collection<IColumn> cols) {
        for (IColumn col : getIndexColumns()) {
            cols.add(col);
        }
        registerAnonymizations(cols, getAnonymizations());
        for (IColumn col : getAllColumns()) {
            cols.add(col);
        }
    }

}
