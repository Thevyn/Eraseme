/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.logging.Level;

import no.esito.anonymizer.ConfigUtil;
import no.esito.anonymizer.IAnonymization;
import no.esito.anonymizer.IAnonymizer;
import no.esito.anonymizer.IContext;
import no.esito.anonymizer.IContext.RunType;
import no.esito.anonymizer.ISarWriter;
import no.esito.anonymizer.ITask;
import no.esito.anonymizer.ITasks;
import no.esito.anonymizer.IWorkTask;
import no.esito.anonymizer.Log;

public class ConsoleCommandHandler {

    IAnonymizer anonymizer;

    public ConsoleCommandHandler(IAnonymizer anonymizer) {
        this.anonymizer = anonymizer;
    }

    /**
     * run Anonymizer as command line.
     *
     * @param context consoleContext
     * @param args command line arguements
     */
    public void run(IContext context, String[] args) {
        Log.setLevel(Level.INFO);
        if (args.length == 0) {
            help();
        } else {
            execCommand(context, args);
        }
    }

    private void readStdIn(IContext context) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.print(">");
                execCommand(context, translateCommandline(br.readLine()));
            }

        } catch (IOException e) {
            Log.error(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Log.error(e);
                }
            }
        }
    }

    /**
     * @param args.
     */
    private void execCommand(IContext context, String[] args) {
        if (args.length == 0)
            return;
        switch (args[0].toLowerCase()) {
            case "?":
            case "/?":
            case "help":
                help();
                break;
            case "trace":
                Log.setLevel(Level.FINE);
                execCommand(context, Arrays.copyOfRange(args, 1, args.length));
                break;
            case "cmd":
                help();
                readStdIn(context);
                break;
            case "ping":
                ping(context);
                break;
            case "config":
                try {
                    ConfigUtil.writeConfig();
                }
                catch (FileNotFoundException e) {
                    Log.warning("FILE_IO-ERROR", e);
                }
                catch (Exception e) {
                    Log.error(e);
                }
                break;
            case "exit":
            case "stop":
            case "quit":
                System.exit(0);
                break;
            case "list":
            case "tasks":
                tasks();
                break;
            case "emptydb":
                emptydb(context);
                break;
            case "erase":
                if (args.length == 1) {
                    Log.info("No erase task specified");
                } else {
                    String[] params = (String[]) Array.newInstance(String.class, args.length - 2);
                    for (int i = 2; i < args.length; i++) {
                        params[i - 2] = args[i];
                    }
                    context.setRunType(RunType.ERASE);
                    context.setRunParams(params);
                    if (context.getRunParams() == null) {
                        context.setRunParams(new String[] {});
                    }
                    anonymizer.runTasks(context, Collections.singletonList(args[1]));
                }
                break;
            case "sar":
                if (args.length == 1) {
                    Log.info("No SAR task specified");
                } else {
                    String[] params = (String[]) Array.newInstance(String.class, args.length - 2);
                    for (int i = 2; i < args.length; i++) {
                        params[i - 2] = args[i];
                    }
                    context.setRunParams(params);
                    String task = args[1];
                    context.setRunType(RunType.SAR);
                    if (context.getRunParams() == null) {
                        context.setRunParams(new String[] {});
                    }
                    try (ISarWriter sw = context.getSarWriter();) {
                        if (sw instanceof ConsoleSarWriter)
                            ((ConsoleSarWriter) sw).setTask(task);
                        anonymizer.runTasks(context, Collections.singletonList(task));
                    } catch (IOException e) {
                        Log.error(e);
                    }
                }
                break;
            case "run":
                context.setRunType(RunType.RUN);
                context.setRunParams(null);
                try {
                    if (args.length > 1) {
                        List<String> list = Arrays.asList(args);
                        anonymizer.runTasks(context, list.subList(1, list.size()));
                    } else {
                        context.getConnection().setAutoCommit(context.isAutoCommit());
                        anonymizer.getTaskRoot().run(context);
                    }
                } catch (Throwable e) {
                    Log.error(e);
                }
                break;
            default:
                Log.info("Unknown command:" + args[0]);
        }
    }

    private static void help() {
        Log.info("Commands:");
        Log.info("trace                 - shows more messages");
        Log.info("help                  - shows this page");
        Log.info("config                - creates a local config.properties used for changing connection parameters");
        Log.info("ping                  - test database connection");
        Log.info("tasks                 - outputs list of tasks");
        Log.info("run                   - runs all tasks except erase and sar tasks");
        Log.info("run <tasks>           - runs a list of space delimited tasks, erase and sar actions are excluded");
        Log.info("erase <task> <params> - runs erase actions for a specified task with a series of space delimited parameters");
        Log.info("                        if parameter contains a space, surround it with quotation marks");
        Log.info("sar <task> <params>   - creates a SAR export for a specified task with a series of space delimited parameters");
        Log.info("                        xml and json files will be created in working directory");
        Log.info("cmd                   - takes continued input from standard input (stdin)");
        Log.info("quit                  - quits program");
    }

    private void tasks() {
        Log.info("Available tasks:");
        printRecurse(anonymizer.getTaskRoot().getTasks(), "");
    }

    private void printRecurse(ITask[] tasks, String prefix) {
        for (ITask task : tasks) {
            StringBuilder sb = new StringBuilder(prefix);
            sb.append(task.getName());
            if (task instanceof IWorkTask) {
                sb.append(" (");
                sb.append(((IWorkTask) task).getTable());
                if (task instanceof AbstractUpdateTask) {
                    TreeSet<String> cols = new TreeSet<>();
                    for (IAnonymization ano : ((AbstractUpdateTask) task).getAnonymizations()) {
                        cols.add(ano.getColumn().getName());
                    }
                    sb.append(" - ");
                    sb.append(String.join(" ", cols));
                }
                sb.append(")");
            }
            Log.info(sb.toString());
            if (task instanceof ITasks) {
                printRecurse(((ITasks) task).getTasks(), prefix + "  ");
            }
        }
    }

    private static void ping(IContext context) {
        ConfigUtil.resetConfig();
        try {
            Connection connection = context.getConnection();
            if(connection==null) {
                Log.info("Connection error");
            } else {
                ConfigUtil.printConnectionInfo(connection);
                Log.info("Connection successful");
            }
        } catch (FileNotFoundException e) {
            Log.warning("FILE-IO-ERROR:", e);
        } catch (Throwable e) {
            Log.error(e);
        }
    }

    private static String[] translateCommandline(String toProcess) {
        final int normal = 0;
        final int inQuote = 1;
        final int inDoubleQuote = 2;
        int state = normal;
        final StringTokenizer tok = new StringTokenizer(toProcess, "\"\' ", true);
        final ArrayList<String> result = new ArrayList<String>();
        final StringBuilder current = new StringBuilder();
        boolean lastTokenHasBeenQuoted = false;

        while (tok.hasMoreTokens()) {
            String nextTok = tok.nextToken();
            switch (state) {
                case inQuote:
                    if ("\'".equals(nextTok)) {
                        lastTokenHasBeenQuoted = true;
                        state = normal;
                    } else {
                        current.append(nextTok);
                    }
                    break;
                case inDoubleQuote:
                    if ("\"".equals(nextTok)) {
                        lastTokenHasBeenQuoted = true;
                        state = normal;
                    } else {
                        current.append(nextTok);
                    }
                    break;
                default:
                    if ("\'".equals(nextTok)) {
                        state = inQuote;
                    } else if ("\"".equals(nextTok)) {
                        state = inDoubleQuote;
                    } else if (" ".equals(nextTok)) {
                        if (lastTokenHasBeenQuoted || current.length() != 0) {
                            result.add(current.toString());
                            current.setLength(0);
                        }
                    } else {
                        current.append(nextTok);
                    }
                    lastTokenHasBeenQuoted = false;
                    break;
            }
        }
        if (lastTokenHasBeenQuoted || current.length() != 0) {
            result.add(current.toString());
        }
        if (state == inQuote || state == inDoubleQuote) {
            Log.info("unbalanced quotes in " + toProcess);
            return new String[0];
        }
        return result.toArray(new String[result.size()]);
    }

    private static void sqlExecuteUpdate(Connection conn, String cmd) throws SQLException {
        Log.debug(cmd);
        try {
            conn.createStatement().executeUpdate(cmd);
        } catch (SQLException e) {
            e.setNextException(new SQLException("Statement: " + cmd));
            throw e;
        }
    }

    private void emptydb(IContext context) {
        ConfigUtil.resetConfig();
        try {
            for (String table : anonymizer.getEmptyDbTables().split(",")) {
                sqlExecuteUpdate(context.getConnection(), "DELETE FROM " + table + "");
            }
            Log.info("Finished!");
        } catch (SQLException e) {
            Log.warning("SQL-ERROR:", e);
        } catch (Throwable e) {
            Log.error(e);
        }
    }

}
