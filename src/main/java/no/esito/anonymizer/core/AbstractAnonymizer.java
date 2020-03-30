/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import no.esito.anonymizer.ConfigUtil;
import no.esito.anonymizer.IAnonymizer;
import no.esito.anonymizer.IContext;
import no.esito.anonymizer.ITask;
import no.esito.anonymizer.ITasks;
import no.esito.anonymizer.Log;

/**
 * Anonymizer base. Has methods for handling tasks by name. <Br>
 * <Br>
 * - Use ContextFactory factory methods for creation of Context with connection from config.properties <Br>
 * - Use AbstractContext factory methods for creation of Context with other connection <Br>
 * <br>
 * implements the IAnonymizer interface for external control.
 */
public abstract class AbstractAnonymizer implements IAnonymizer {

    private static long startRun(IContext context) throws Throwable {
        long time1 = System.currentTimeMillis();
        Log.info(context.getLogBegin() + "START " + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.now())
            + "(" + (context.isAutoCommit() ? "AutoCommit" : "Task commit") + ")");
        context.getConnection().setAutoCommit(context.isAutoCommit());
        return time1;
    }

    private static void endRun(IContext context, long time1) throws Throwable {
        if (!context.isAutoCommit()) {
            context.getConnection().commit();
        }
        Log.info(context.getLogEnd() + "FINISHED " + (System.currentTimeMillis() - time1) + "ms");
    }

    @Override
    public void runTasks(IContext context, List<String> tasklist) {
        List<String> include = new ArrayList<String>();
        for (String task : tasklist) {
            include.add(task.toLowerCase());
        }
        List<ITask> tasks = new ArrayList<ITask>();
        boolean[] found = new boolean[include.size()];
        ITask[] children = getTaskRoot().getTasks();
        recurseTasks(include, found, tasks, children);
        ArrayList<String> notfound = new ArrayList<>();
        for (int i = 0; i < include.size(); i++) {
            if (!found[i]) {
                notfound.add(tasklist.get(i));
            }
        }
        if (notfound.isEmpty()) {
            ConfigUtil.resetConfig();
            try {
                try {
                    long time1 = startRun(context);
                    for (ITask task : tasks) {
                        if (task != null && task.shouldRun(context)) {
                            Log.info("Running " + task.getClass().getName());
                            task.setLogPrefix(context.getLogIndentation());
                            task.run(context);
                        }
                    }
                    endRun(context, time1);
                } catch (Throwable e) {
                    context.getConnection().rollback();
                    if (e instanceof SQLException || e instanceof IOException) {
                        throw e;
                    }
                    Log.error(e);
                }
            } catch (SQLException e) {
                Log.warning("SQL-ERROR:", e);
            } catch (FileNotFoundException e) {
                Log.warning("FILE-IO-ERROR:", e);
            } catch (Throwable e) {
                Log.error(e);
            }
        } else {
            Log.info("Unknown tasks? " + String.join(", ", notfound));
        }
    }

    private static int included(List<String> include, String name) {
        int i = 0;
        for (String task : include) {
            if (name.endsWith(task)
                && (name.length() == task.length() || name.charAt(name.length() - task.length() - 1) == '.')) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private void recurseTasks(List<String> include, boolean[] found, List<ITask> tasks, ITask[] children) {
        for (ITask task : children) {
            String name = task.getClass().getName().toLowerCase();
            int i = included(include, name);
            if (i >= 0) {
                tasks.add(task);
                found[i] = true;
            }
            if (task instanceof ITasks) {
                recurseTasks(include, found, tasks, ((ITasks) task).getTasks());
            }
        }
    }

}
