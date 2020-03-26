/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class MappingUtil {

    /**
     * Holder for a set of keys, table and column names
     */
    public static class Usage {
        public String table;
        public String column;
        public Collection<String> keys;

        public void printCorrelation(Set<String> common) {
            int tot = keys.size();
            HashSet<String> set = new HashSet<>(keys);
            set.retainAll(common);
            int com = set.size();
            System.out.println((toString() + "                                            ").substring(0, 30)
                               + com + "/" + tot);
        }

        @Override
        public String toString() {
            return table + "." + column;
        }
    }

    /**
     * Reads values from &lt;table&gt;.&lt;column&gt; into a Usage instance
     * @param conn Connection
     * @param table name
     * @param column name
     * @param where clause
     * @return Usage object
     * @throws SQLException exception
     */
    public static Usage sqlRead(Connection conn, String table, String column, String where) throws SQLException {
        TreeSet<String> rows = new TreeSet<>();
        System.out.print("Reading " + table + "." + column);
        Usage use = new Usage();
        use.table = table;
        use.column = column;
        use.keys = rows;
        String cWhere = where != null ? " WHERE " + where : "";
        String cmd = "SELECT " + column + " FROM " + table + cWhere;
        ResultSet rs = sqlExecuteQuery(conn, cmd);
        if (rs != null) {
            while (rs.next()) {
                String key = rs.getString(column);
                if (key != null)
                    rows.add(key);
            }
            rs.close();
        }
        System.out.println("=" + rows.size());
        return use;
    }

    /**
     * Executes a SQL statement and returns a resultset
     * @param conn Connection
     * @param cmd SQL command
     * @return ResultSet
     * @throws SQLException exception
     */
    public static ResultSet sqlExecuteQuery(Connection conn, String cmd) throws SQLException {
        try {
            Log.debug(cmd);
            return conn.createStatement().executeQuery(cmd);
        } catch (SQLException e) {
            e.setNextException(new SQLException("Statement: " + cmd));
            throw e;
        }
    }

    /**
     * Loads keys from an ordered line-delimited file
     * @param filename of file
     * @return LinkedHashSet of keys
     * @throws FileNotFoundException when file missing
     */
    public static Collection<String> loadKeys(String filename) throws FileNotFoundException {
        Scanner input;
        File file;
        LinkedHashSet<String> lines = new LinkedHashSet<String>();
        file = new File(filename);
        input = new Scanner(file);
        while (input.hasNextLine()) {
            String line = input.nextLine();
            lines.add(line);
        }
        input.close();
        return lines;
    }

    /**
     * Saves keys to an ordered line-delimited file
     * @param keys strings
     * @param filename name
     * @throws IOException when problems writing file
     */
    public static void saveKeys(Collection<String> keys, String filename) throws IOException {
        FileWriter fw = new FileWriter(filename);
        for (String name : keys) {
            fw.write(name + "\n");
        }
        fw.close();
    }

    /**
     * Analyzes correlation and returns the combined set of keys
     * @param usages Collection of Usages
     * @return Set of strings
     */
    public static Set<String> analyze(Collection<Usage> usages) {
        System.out.println("************************************");
        Set<String> all = new HashSet<>();
        Set<String> common = new HashSet<>();
        for (Usage use : usages) {
            Set<String> u = new HashSet<>(use.keys);
            u.retainAll(all);
            common.addAll(u);
            all.addAll(use.keys);
        }
        for (Usage use : usages) {
            use.printCorrelation(common);
        }
        System.out.println("TOTAL "+all.size());
        return all;
    }

    /**
     * Creates mappings to prefix with a sequence and saves as a Java properties file
     * @param keys to save
     * @param filename for mapping file
     * @param prefix for properties
     * @throws IOException when problems writing mapping file
     */
    public static void saveMappingFile(Collection<String> keys, String filename, String prefix) throws IOException {
        Properties props = new Properties();
        int i=1;
        for (String key : keys) {
            props.setProperty(key, prefix+i++);
        }
        props.store(new FileWriter(filename), filename);
     }

}
