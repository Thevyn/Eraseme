/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.sql.SQLException;

/**
 * Logging facade to java.util.logging.
 */
public class Log {

    public final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public final static LogManager logManager = LogManager.getLogManager();

    public static Handler SYSOUT_LOGHANDLER = new Handler() {

        @Override
        public void publish(LogRecord record) {
            String msg = record.getMessage();
            if (record.getLevel().intValue() < Level.WARNING.intValue()) {
                System.out.println(msg);
            } else {
                if (msg != null)
                    System.err.println(msg);
                if (record.getLevel().intValue() > Level.WARNING.intValue()) {
                    record.getThrown().printStackTrace();
                } else {
                    System.err.println(msg != null ? record.getThrown().toString() : record.getThrown().getMessage());
                }
            }
        }

        @Override
        public void flush() {
            //
        }

        @Override
        public void close() throws SecurityException {
            //
        }

    };

    /**
     * Standard LOG handling for use when run as standalone JAR.
     */
    public static void configureConsoleLoghandler() {
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }
        logger.addHandler(SYSOUT_LOGHANDLER);
    }

    public static void info(String msg) {
        logger.log(Level.INFO, msg);
    }

    public static void debug(String msg) {
        logger.log(Level.FINE, msg);
    }

    public static void warning(String msg, Throwable e) {
        logger.log(Level.WARNING, msg, e);
        sqlException(e, Level.WARNING);
    }

    public static void error(Throwable e) {
        logger.log(Level.SEVERE, null, e);
        sqlException(e, Level.SEVERE);
    }

    private static void sqlException(Throwable e, Level level) {
        if (e instanceof SQLException) {
            SQLException sqle = ((SQLException) e).getNextException();
            while (sqle != null) {
                logger.log(level, null, sqle);
                sqle = sqle.getNextException();
            }
        }
    }

    public static void setLevel(Level lvl) {
        logger.setLevel(lvl);
    }

}
