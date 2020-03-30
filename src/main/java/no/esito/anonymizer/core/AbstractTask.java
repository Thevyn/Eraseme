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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

import no.esito.anonymizer.ConfigUtil;
import no.esito.anonymizer.IAnonymization;
import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IContext;
import no.esito.anonymizer.IInput;
import no.esito.anonymizer.IPreScan;
import no.esito.anonymizer.IRandom;
import no.esito.anonymizer.IUpdateColumns;
import no.esito.anonymizer.IWorkTask;
import no.esito.anonymizer.Log;
import no.esito.anonymizer.column.BooleanColumn;
import no.esito.anonymizer.column.DateColumn;
import no.esito.anonymizer.column.NumberColumn;
import no.esito.anonymizer.column.TextColumn;
import no.esito.anonymizer.column.TimeColumn;
import no.esito.anonymizer.column.TimestampColumn;
import no.esito.anonymizer.mask.MaskColumn;

/**
 * Base functionality for JDBC reads, updates, inserts.
 */
public abstract class AbstractTask implements IWorkTask {

    protected String prefix = "";

    @Override
    public void setLogPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String getWhere() {
        return null;
    }

    @Override
    public IColumn[] getAllColumns() {
        return new IColumn[0];
    }

    @Override
    public String getDescription() {
        return null;
    }

    private IColumn[] columns;

    long time1;

    public void start(IContext context, String indentation) {
        Log.info(context.getLogBegin() + (indentation + getName()));
        time1 = System.currentTimeMillis();
    }

    public void finish(IContext context, String indentation) {
        long time2 = System.currentTimeMillis();
        Log.info(context.getLogEnd() + (indentation + getName()) + " (" + (time2 - time1) + "ms)");
    }

    /**
     * Gets an array of columns necessary to read from the table.
     *
     * @return array of Column descriptors
     */
    public IColumn[] getColumns() {
        if (columns == null) {
            Collection<IColumn> cols = new LinkedHashSet<>();
            initCols(cols);
            columns = cols.toArray(new IColumn[cols.size()]);
        }
        return columns;
    }

    /**
     * Abstract method to assemble list of columns necessary.
     *
     * @param cols list of column definitions
     */
    protected abstract void initCols(Collection<IColumn> cols);

    /**
     * Utility method to register anonymization methods and if necessary add the column to the list.
     *
     * @param cols columns
     * @param anos anonymizations
     */
    protected static void registerAnonymizations(Collection<IColumn> cols, IAnonymization[] anos) {
        for (IAnonymization ano : anos) {
            cols.add(ano.getColumn());
            if (ano instanceof AbstractMasking) {
                for (IInput input : ((AbstractMasking) ano).getInputs()) {
                    if (input instanceof MaskColumn) {
                        cols.add(((MaskColumn) input).getColumn());
                    }
                }
            }
        }
    }

    /**
     * Adds schema prefix to table if necessary.
     *
     * @param table name
     * @return schema prefix name
     * @throws IOException when problems reading config file
     */
    public static String schemaPrefix(String table) throws IOException {
        String schema = ConfigUtil.getSchema();
        if (table.contains(" "))
            table = "[" + table + "]";
        return (schema != null && !schema.isEmpty()) ? schema + "." + table : table;
    }

    /**
     * Wraps/qutoes in [ ] if it contains spaces.
     *
     * @param name table/column name to be wrapped
     * @return wrapped version if necessary
     */
    public static String wrap(String name) {
        return name.contains(" ") ? "[" + name + "]" : name;
    }

    /**
     * Count rows.
     *
     * @param conn Connection
     * @return number of rows
     * @throws IOException when problems reading config file
     */
    protected int sqlCountRows(Connection conn) throws IOException {
        int count = 0;
        try {
            String cmd = "SELECT COUNT(*) FROM " + schemaPrefix(getTable());
            ResultSet rs = sqlExecuteQuery(conn, cmd);
            if (rs != null) {
                while (rs.next()) {
                    count = rs.getInt(1);
                }
                rs.close();
            }
        } catch (SQLException e) {
            Log.error(e);
        }
        return count;
    }

    /**
     * Reads rows with a list of columns from table using specified where clause.
     *
     * @param conn connection
     * @param table name
     * @param columns list of column names
     * @param where string - can be null
     * @return List of rows - each row is a String array
     * @throws SQLException when SQL issues
     * @throws IOException when problems reading config file
     */
    protected static List<String[]> sqlReadRows(Connection conn, String table, List<String> columns, String where)
        throws SQLException, IOException {
        ArrayList<String[]> rows = new ArrayList<>();
        String cWhere = where != null ? " WHERE " + where : "";
        String cmd = "SELECT " + sqlColumnsWrap(columns) + " FROM " + schemaPrefix(table) + cWhere;
        ResultSet rs = sqlExecuteQuery(conn, cmd);
        if (rs != null) {
            while (rs.next()) {
                String[] row = new String[columns.size()];
                rows.add(row);
                for (int i = 0; i < columns.size(); i++) {
                    row[i] = rs.getString(columns.get(i));
                }
            }
            rs.close();
        }
        return rows;
    }

    protected static String sqlColumnsWrap(Collection<String> columns) {
        StringBuilder cols = new StringBuilder();
        for (String name : columns) {
            if (cols.length() > 0)
                cols.append(", ");
            cols.append(wrap(name));
        }
        return cols.toString();
    }

    /**
     * Updates rows from a list of columns from table using specified where clause.
     *
     * @param conn Connection
     * @param table name
     * @param columns list of column names
     * @param updatecolumns columns to be updated
     * @param rows data rows
     * @param where string clause
     * @throws SQLException when SQL issues
     * @throws IOException when problems reading config file
     */
    protected static void sqlUpdateRows(Connection conn, String table, List<IColumn> columns,
        Collection<String> updatecolumns, List<String[]> rows, String where) throws IOException, SQLException {
        for (String[] row : rows) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < columns.size(); i++) {
                IColumn column = columns.get(i);
                String name = column.getName();
                if (updatecolumns.contains(name)) {
                    String val = row[i];
                    sb.append(sb.length() == 0 ? "SET " : ", ");
                    sb.append(wrap(name) + "= " + column.addQuotes(val));
                }
            }
            String cmd = "UPDATE " + schemaPrefix(table) + " " + sb.toString() + " WHERE " + where;
            sqlExecuteUpdate(conn, cmd);
        }
    }

    private static ResultSet sqlExecuteQuery(Connection conn, String cmd) throws SQLException {
        try {
            Log.debug(cmd);
            return conn.createStatement().executeQuery(cmd);
        } catch (SQLException e) {
            e.setNextException(new SQLException("Statement: " + cmd));
            throw e;
        }
    }


    /**
     * Updates rows with a list of columns from table using specified where clause.
     *
     * @param conn Connection
     * @param updatecolumns list of names
     * @param rows data to update
     * @param where Array of [index-column,rows]
     * @throws SQLException when SQL issues
     * @throws IOException when problems reading config file
     */
    protected void sqlUpdateRows(Connection conn, List<String> updatecolumns, List<String[]> rows, String[][] where)
            throws IOException, SQLException {
        StringBuilder ass1 = new StringBuilder();
        for (String name : updatecolumns) {
            ass1.append(ass1.length() > 0 ? ", " : " SET ");
            ass1.append(wrap(name));
            ass1.append(" = ?");
        }
        String table = getTable();
        String cmd = "UPDATE " + schemaPrefix(table) + ass1.toString();
        IColumn[] index = ((IUpdateColumns) this).getIndexColumns();
        for (int i = 0; i < index.length; i++) {
            cmd += (i == 0 ? " WHERE " : " AND ") + index[i].getName() + " = ?";
        }
        executeBatch(conn, getColumns(), updatecolumns, rows, cmd, index, where);
    }

    /**
     * Updates rows with a list of columns from table using specified where clause.
     *
     * @param conn Connection
     * @param columns column definitions
     * @param updatecolumns list of names
     * @param rows data to update
     * @throws SQLException when SQL issues
     * @throws IOException when problems reading config file
     */
    protected void sqlUpdateRowsNoIndex(Connection conn, IColumn[] columns, List<String> updatecolumns,
        List<String[]> rows) throws IOException, SQLException {
        StringBuilder ass1 = new StringBuilder();
        for (String name : updatecolumns) {
            ass1.append(ass1.length() > 0 ? ", " : " SET ");
            ass1.append(wrap(name));
            ass1.append(" = ?");
        }
        String table = getTable();
        String cmd = "SELECT " + sqlColumnsWrap(updatecolumns) + " FROM " + schemaPrefix(table);
        Log.debug(cmd);
        ResultSet stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)
            .executeQuery(cmd);
        int irow = 0;
        if (stmt != null) {
            while (stmt.next()) {
                String[] row = rows.get(irow++);
                for (IColumn type : columns) {
                    int icol = updatecolumns.indexOf(type.getName());
                    if (icol >= 0) {
                        String val = row[icol];
                        int n = icol + 1;
                        if (type instanceof TextColumn) {
                            if (val == null)
                                stmt.updateNull(n);
                            else
                                stmt.updateString(n, val);
                        } else if (type instanceof NumberColumn) {
                            if (val == null)
                                stmt.updateNull(n);
                            else
                                stmt.updateDouble(n, Double.parseDouble(val));
                        } else if (type instanceof BooleanColumn) {
                            if (val == null)
                                stmt.updateNull(n);
                            else
                                stmt.updateBoolean(n, "1".equals(val));
                        } else if (type instanceof DateColumn) {
                            if (val == null)
                                stmt.updateNull(n);
                            else
                                stmt.updateDate(n, Date.valueOf(val));
                        } else if (type instanceof TimestampColumn) {
                            if (val == null)
                                stmt.updateNull(n);
                            else
                                stmt.updateTimestamp(n, Timestamp.valueOf(val));
                        } else if (type instanceof TimeColumn) {
                            if (val == null)
                                stmt.updateNull(n);
                            else
                                stmt.updateTime(n, Time.valueOf(val));
                        }
                    }
                }
                stmt.updateRow();
            }
            stmt.close();
        }
    }

    /**
     * Inserts rows with a list of updatecolumns.
     *
     * @param conn Connection
     * @param updatecolumns list of names
     * @param rows of data
     * @throws SQLException when SQL issues
     * @throws IOException when problems reading config file
      */
    protected void sqlInsertRows(Connection conn, List<String> updatecolumns, List<String[]> rows) throws IOException, SQLException  {
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (String name : updatecolumns) {
            sb1.append(sb1.length() > 0 ? "," : "");
            sb1.append(wrap(name));
            sb2.append(sb2.length() > 0 ? "," : "");
            sb2.append("?");
        }
        String cmd = "INSERT INTO " + schemaPrefix(getTable()) + " (" + sb1.toString() + ") VALUES (" + sb2.toString()
            + ")";
        executeBatch(conn, getColumns(), updatecolumns, rows, cmd, new IColumn[0], null);
    }

    /**
     * Executing SQL statement using batch mode.
     *
     * @param conn Connection
     * @param columns Columns
     * @param updcols list of names of colums to be updated
     * @param rows of data
     * @param cmd SQL command line
     * @param index array of index columns
     * @param where string clause
     * @throws SQLException SQL error
     */
    protected static void executeBatch(Connection conn, IColumn[] columns, List<String> updcols, List<String[]> rows,
        String cmd, IColumn[] index, String[][] where) throws SQLException {
        Log.debug(cmd);
        List<String> colnames = IColumn.listNames(columns);
        try {
            PreparedStatement stmt = conn.prepareStatement(cmd);
            int nupd = updcols.size();
            for (int x = 0; x < rows.size(); x++) {
                StringBuilder sb = new StringBuilder();
                String[] row = rows.get(x);
                for (int i = 0; i < nupd; i++) {
                    int n = colnames.indexOf(updcols.get(i));
                    if (n > -1) {
                        String val = row[n];
                        assignCell(stmt, i, columns[n], val);
                        sb.append(i == 0 ? "[" : ",");
                        sb.append(columns[n].addQuotes(val));
                    }
                }
                for (int i = 0; i < index.length; i++) {
                    String val = where[i][x];
                    assignCell(stmt, nupd + i, index[i], val);
                    sb.append(",");
                    sb.append(index[i].addQuotes(val));
                }
                sb.append("]");
                Log.debug(sb.toString());
                stmt.addBatch();
            }
            stmt.executeBatch();
            stmt.close();
        } catch (SQLException e) {
            e.setNextException(new SQLException("Statement: " + cmd));
            throw e;
        }
    }

    /**
     * Assigning cell for SQL Batch operation.
     *
     * @param stmt prepared statement
     * @param n cell number
     * @param type column type
     * @param val value to assign
     * @throws SQLException if there's error in JDBC operation
     */
    protected static void assignCell(PreparedStatement stmt, int n, IColumn type, String val) throws SQLException {
        n++;
        if (type instanceof TextColumn) {
            if (val == null)
                stmt.setNull(n, Types.VARCHAR);
            else
                stmt.setString(n, val);
        } else if (type instanceof NumberColumn) {
            if (val == null)
                stmt.setNull(n, Types.NUMERIC);
            else
                stmt.setDouble(n, Double.parseDouble(val));
        } else if (type instanceof BooleanColumn) {
            if (val == null)
                stmt.setNull(n, Types.BOOLEAN);
            else
                stmt.setBoolean(n, "1".equals(val));
        } else if (type instanceof DateColumn) {
            if (val == null)
                stmt.setNull(n, Types.DATE);
            else
                stmt.setDate(n, Date.valueOf(val));
        } else if (type instanceof TimestampColumn) {
            if (val == null)
                stmt.setNull(n, Types.TIMESTAMP);
            else
                stmt.setTimestamp(n, Timestamp.valueOf(val));
        } else if (type instanceof TimeColumn) {
            if (val == null)
                stmt.setNull(n, Types.TIME);
            else
                stmt.setTime(n, Time.valueOf(val));
        }
    }

    protected static void sqlExecuteUpdate(Connection conn, String cmd) throws SQLException {
        Log.debug(cmd);
        conn.createStatement().executeUpdate(cmd);
    }

    /**
     * Prepare inputs that are based on current values.
     *
     * @param context contains Connection
     * @param anonymizations array of anonymizations
     * @throws SQLException SQL error
     */
    protected void prepareInputs(IContext context, IAnonymization[] anonymizations) throws Throwable {
        ArrayList<String> pfs = new ArrayList<>();
        for (IAnonymization ano : anonymizations) {
            if (ano instanceof AbstractMasking) {
                AbstractMasking anomask = (AbstractMasking) ano;
                anomask.inputs = anomask.getInputs();
                for (IInput input : anomask.inputs) {
                    if (input instanceof IPreScan) {
                        String colname = ano.getColumn().getName();
                        if (!pfs.contains(colname))
                            pfs.add(colname);
                    }
                    if (input instanceof IRandom) {
                        ((IRandom) input).setRandom(context.isRepeatableRandom() ? new Random(0L) : new Random());
                    }
                }
            }
        }
        if (!pfs.isEmpty()) {
            List<String[]> rows = sqlReadRows(context.getConnection(), getTable(), pfs, null);
            for (IAnonymization ano : anonymizations) {
                if (ano instanceof AbstractMasking) {
                    AbstractMasking anomask = (AbstractMasking) ano;
                    for (IInput input : anomask.inputs) {
                        if (input instanceof IPreScan) {
                            ((IPreScan) input).scan(pfs.indexOf(anomask.getColumn().getName()), rows);
                        }
                    }
                }
            }
        }
    }

    /**
     * Substitute parameters in WHERE clause. The parameters are numbered and coded %PARAMETERn% in the WHERE clause.
     * The unnumbered form %PARAMETER% can be used for parameter 1.
     *
     * @param where clause
     * @param parameters array of strings
     * @return WHERE clause with %PARAMETERn% replaced with content from parameter
     */
    protected static String getWhere(String where, String[] parameters) {
        if (parameters.length > 0) {
            where = where.replaceAll("%PARAMETER1?%", parameters[0].trim());
            for (int i = 1; i < parameters.length; i++) {
                where = where.replaceAll("%PARAMETER" + (i + 1) + "%", parameters[i].trim());
            }
        }
        return where;
    }

    public static boolean sql(IContext context, String cmd) throws SQLException, Throwable {
        return AbstractTasks.sql(context, cmd);
    }
}
