/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import no.esito.anonymizer.core.FileUtils;

/**
 * This utility handles runtime properties for the Anonymizer.<br>
 * It has methods to read and write config file and get the Properties object in order to manipulate the values.
 */
public class ConfigUtil {

    /**
     * Config file name to use.
     */
    public static String CONFIG_FILE = "config.properties";

    private static Properties config;

    private static String connectionName;

    /**
     * Get current configuration properties.
     * If not present the properties are loaded
     *
     * @return Properties object
     * @throws IOException when problems reading config file
     */
    public static Properties getConfig() throws IOException {
        if (config == null)
            config = ConfigUtil.readConfig();
        return config;
    }

    /**
     * Set current connection name.
     *
     * @param name name
     */
    public static void setConnectionName(String name) {
        connectionName = name;
    }

    /**
     * Read properties file from current directory or if not available the internal one.
     *
     * @return Properties object
     * @throws IOException when problems reading config file
     */
    public static Properties readConfig() throws IOException{
        Properties prop = new Properties();
        File file = new File(CONFIG_FILE);
        if (file.exists()) {
            InputStream input = new FileInputStream(file);
            Log.info("Reading local config from " + file.getCanonicalPath());
            prop.load(input);
            return prop;
        }
        return readInternalConfig();
    }

    /**
     * Prints the PING results to log.
     *
     * @param connection Connection
     */
    public static void printConnectionInfo(Connection connection) {
        Log.info(getConnectionInfo(connection));
    }

    /**
     * Prints the PING results.
     *
     * @param connection Connection
     * @return JDBC Connection object
     */
    public static String getConnectionInfo(Connection connection) {
        try {
            DatabaseMetaData dbmd = connection.getMetaData();
            return String.format("DBMS: %s - %s\nDriver: %s - %s %d.%d\nURL: %s", dbmd.getDatabaseProductName(),
                dbmd.getDatabaseProductVersion(), dbmd.getDriverName(), dbmd.getDriverVersion(),
                dbmd.getDriverMajorVersion(), dbmd.getDriverMinorVersion(), dbmd.getURL());
        } catch (Exception e) {
            Log.error(e);
            return "Error:" + e.getMessage();
        }
    }

    /**
     * Force config to be re-read.
     */
    public static void resetConfig() {
        config = null;
    }

    private static Properties readInternalConfig() throws IOException {
        Properties prop = new Properties();
        Log.info("Reading internal " + CONFIG_FILE);
        InputStream input = FileUtils.getResourceAsStream(CONFIG_FILE);
        prop.load(input);
        return prop;
    }

    /**
     * Write the current config to current directory.
     * @throws IOException when problems writing config file
     */
    public static void writeConfig() throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("# Copy of current config: ");
        sb.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:MM:SS")));
        sb.append("\n");
        InputStream input = FileUtils.getResourceAsStream(CONFIG_FILE);
        Log.info("Reading internal " + CONFIG_FILE);
        sb.append(FileUtils.stream2String(input));
        File file = new File(CONFIG_FILE);
        Log.info("Writing to local config " + file.getCanonicalPath());
        FileUtils.string2File(sb.toString(), file);
     }

    /**
     * Gets the current passkey.
     *
     * @return passkey String
     * @throws IOException when problems reading properties
     */
    public static String getPassKey() throws IOException {
        return getConfig().getProperty("file.encryptionkey");
    }

    /**
     * Gets the schema name being used.
     *
     * @return schema name
     * @throws IOException when problems reading schema file
     */
    public static String getSchema() throws IOException {
        return getConfig().getProperty("connection." + (connectionName == null ? "" : (connectionName + ".")) + "schema");
    }

}
