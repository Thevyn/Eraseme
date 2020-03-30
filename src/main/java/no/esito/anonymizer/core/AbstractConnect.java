/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Handles database connection.
 */
public class AbstractConnect {

    /**
     * Create / get connection.
     *
     * @param name of file
     * @param config Properties object
     * @return Connection
     * @throws Throwable SQL error
     */
    public Connection makeConnection(String name,Properties config) throws Throwable {
        String prefix = "connection." + ((name == null || name.isEmpty()) ? "" : name + ".");
        String host = getProperty(config, prefix+"host", "HOST");
        String port = getProperty(config, prefix+"port", "PORT");
        String db = getProperty(config, prefix+"db", "DB");
        String url = getProperty(config, prefix+"url", "URL");
        String driver = getProperty(config, prefix+"driverClass", "DRIVER");
        String user = config.getProperty(prefix+"user");
        String pass = config.getProperty(prefix+"password");

        // Handle starting of network service
        checkNetworkService(host, port);

        // Initialize Driver
        Class.forName(driver);

        // Connect
        String connstr = url.replace("<host>", host).replace("<port>", port).replace("<db>", db);
        boolean hasLogin = user != null && !user.isEmpty();
        Connection connection = hasLogin?
        DriverManager.getConnection(connstr,user,pass):
        DriverManager.getConnection(connstr);
        return connection;
    }

    /**
     * Override this for special handling like starting a data server.
     *
     * @param host hostname
     * @param port number
     * @throws Throwable exception
     */
    protected void checkNetworkService(String host, String port) throws Throwable {
        // Hook
    }

    private String getProperty(Properties config, String property, String uneditValue) throws Exception {
        String value = config.getProperty(property);
        if (value == null || value.equals(uneditValue)) {
            throw new Exception("Missing connection property: " + property);
        }
        return value;
    }

}
