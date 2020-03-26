/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer;

import java.sql.Connection;

import no.esito.anonymizer.IContext.RunType;
import no.esito.anonymizer.core.AbstractContext;

/**
 * Factory methods for running standalone.
 */
public class ContextFactory {

    /**
     * Factory method for anonymization context.
     *
     * @param connection connection
     * @return anonymization context
     */
    public static IContext createAnonymizeContext(Connection connection) {
        return new AbstractContext(RunType.RUN, null) {

            @Override
            public Connection getConnection() throws Throwable {
                return connection;
            }
        };
    }

    /**
     * Factory method for anonymization erase context.
     *
     * @param connection connection
     * @param params Erase parameters
     * @return erase context
     */
    public static IContext createEraseContext(Connection connection, String[] params) {
        return new AbstractContext(RunType.ERASE, params) {

            @Override
            public Connection getConnection() throws Throwable {
                return connection;
            }
        };
    }

    /**
     * Factory method for anonymization SAR context.
     *
     * @param connection connection
     * @param params SAR parameters
     * @param sarwriter SAR writer
     * @return SAR context
     */
    public static IContext createSarContext(Connection connection, String[] params, ISarWriter sarwriter) {
        return new AbstractContext(RunType.SAR, params) {

            @Override
            public ISarWriter getSarWriter() {
                return sarwriter;
            }

            @Override
            public Connection getConnection() throws Throwable {
                return connection;
            }
        };
    }

}
