/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

import java.sql.SQLException;

import no.esito.anonymizer.IContext;
import no.esito.anonymizer.ITask;
import no.esito.anonymizer.ITasks;
import no.esito.anonymizer.Log;

public abstract class AbstractTasks implements ITasks {

    private ITask[] tasks = new ITask[0];

    private String prefix = "";

    /**
     * Either set the tasks using constructor or set using getter.
     */
    public AbstractTasks() {
    }

    @Override
    public ITask[] getTasks() {
        return tasks;
    }

    public AbstractTasks(ITask[] tasks) {
        this.tasks = tasks;
    }

    @Override
    public void setLogPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void run(IContext context) throws Throwable {
        String tname = getName();
        Log.info(context.getLogBegin() + (prefix + tname));
        long time1 = System.currentTimeMillis();
        ITask[] tasks = getTasks();
        try {
            for (ITask task : tasks) {
                if (task.shouldRun(context)) {
                    task.setLogPrefix(prefix + context.getLogIndentation());
                    task.run(context);
                }
            }
        } catch (Throwable e) {
            context.getConnection().rollback();
            throw e;
        }
        if (!context.isAutoCommit()) {
            try {
                context.getConnection().commit();
            } catch (SQLException e) {
                e.setNextException(new SQLException("Commit task: " + tname));
                throw e;
            }
        }
        long time2 = System.currentTimeMillis();
        Log.info(context.getLogEnd() + (prefix + tname) + " (" + (time2 - time1) + "ms)");
    }

    @Override
    public boolean shouldRun(IContext context) {
        return true;
    }

    public static boolean sql(IContext context, String cmd) throws SQLException, Throwable {
        try {
            Log.debug(cmd);
            return context.getConnection().createStatement().execute(cmd);
        } catch (SQLException e) {
            e.setNextException(new SQLException("Statement: " + cmd));
            throw e;
        }
    }

}
