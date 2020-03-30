/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

import java.sql.Connection;

import no.esito.anonymizer.ISarWriter;

public class ConsoleContext extends AbstractContext {

    AbstractSarWriter sarwriter = new ConsoleSarWriter();

    public interface IConnectHandler{
        Connection getConnection() throws Throwable;
    }

    private Connection connection;
    private IConnectHandler handler;

    public ConsoleContext(IConnectHandler handler) {
        super();
        this.handler = handler;
    }

    @Override
    public Connection getConnection() throws Throwable {
        if(connection==null)
            connection = handler.getConnection();
        return connection;
    }

    @Override
    public ISarWriter getSarWriter() {
        return sarwriter;
    }

}
