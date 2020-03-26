/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

import java.sql.Connection;

import no.esito.anonymizer.IContext;
import no.esito.anonymizer.ISarWriter;

public abstract class AbstractContext implements IContext {

    boolean autocommit;

    private String[] params;

    private RunType runType;

    private boolean repeatable = true;

    public AbstractContext(RunType runtype, String[] params) {
        this.runType = runtype;
        this.params = params;
    }

    public AbstractContext() {
        //
    }

    @Override
    public boolean isAutoCommit() {
        return autocommit;
    }

    @Override
    public void setAutoCommit(boolean autocommit) {
        this.autocommit = autocommit;
    }

    @Override
    public void setRunParams(String[] params) {
        this.params = params;
    }

    @Override
    public String[] getRunParams() {
        return params;
    }

    @Override
    public void setRunType(RunType run) {
        this.runType = run;

    }

    @Override
    public RunType getRunType() {
        return runType;
    }

    @Override
    public boolean isRepeatableRandom() {
        return repeatable;
    }

    @Override
    public void setRepeatableRandom(boolean repeatable) {
        this.repeatable = repeatable;
    }

    @Override
    public ISarWriter getSarWriter() {
        return null;
    }

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
