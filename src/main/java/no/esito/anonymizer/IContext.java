/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer;

import java.sql.Connection;

public interface IContext {

    /**
     * RunType specifies the type of tasks actually executed when running tasks.
     * <ul>
     * <li><b>RUN</b> All tasks except SAR and Erase
     * <li><b>ERASE</b> Erase tasks only
     * <li><b>SAR</b> SAR tasks only
     * </ul>
     */
    public static enum RunType {
        RUN,
        ERASE,
        SAR
    }

    /**
     * Get a JDBC connection based on the configuration.
     *
     * @return connection based on the configuration
     * @throws Throwable SQL error
     */
    Connection getConnection() throws Throwable;

    /**
     * Is it using auto-commit mode?<br>
     * Please note that many operations involving setting unique keys may not work in autocommit mode.
     *
     * @return true if autocommit
     */
    boolean isAutoCommit();

    /**
     * Sets the AutoCommit mode.<br>
     * AutoCommit (true) - commits for every insert/update and is easier to debug <br>
     * Commit (false) - uses batch inserts/updates which is generally faster
     *
     * @param autocommit true if autocommit should be used
     */
    void setAutoCommit(boolean autocommit);

    /**
     * Sets the values of the list of %PARAMETER% parameters to be used in Erase or Sar tasks.
     *
     * @param parameter parameter
     */
    void setRunParams(String[] parameter);

    /**
     * Returns the parameter values to be used by Erase or Sar tasks.
     *
     * @return erase/sar parameters
     */
    String[] getRunParams();

    /**
     * Set type of tasks to execute when tasks is executed by ITask.run(IContext).
     *
     * @param run RunType
     */
    void setRunType(RunType run);

    /**
     * Get type of tasks that will be executed by ITask.run(IContext).
     *
     * @return RunType
     */
    RunType getRunType();

    /**
     * Set to TRUE if random sequence should repeat each time it is run, in order to get predictable results.
     *
     * @param repeatable should it be repeatable
     */
    void setRepeatableRandom(boolean repeatable);

    /**
     * TRUE if random sequence should repeat each time it is run, in order to get predictable results.
     *
     * @return true if repeatable random
     */
    boolean isRepeatableRandom();

    /**
     * LOG_INDENTATION: Indentation used in log messages reporting progress when running tasks.<br>
     * Default is two spaces. To change indentation assign new indentation string to this variable.
     */
    public static String LOG_INDENTATION = "  ";

    /**
     * Get log indentation string.
     *
     * @return indentation
     */
    default String getLogIndentation() {
        return LOG_INDENTATION;
    }

    /**
     * LOG_BEGIN: Begin prefix used in log messages to mark start of actions when running tasks.<br>
     * Default is "&gt;&gt;&gt; ". To change begin prefix assign new prefix string to this variable.
     */
    public static String LOG_BEGIN = ">>> ";

    /**
     * Get task begin prefix.
     *
     * @return prefix
     */
    default String getLogBegin() {
        return LOG_BEGIN;
    }

    /**
     * LOG_END: End prefix used in log messages to mark end of actions when running tasks.<br>
     * Default is "&lt;&lt;&lt; ". To change end prefix assign new prefix string to this variable.
     */
    public static String LOG_END = "<<< ";

    /**
     * Get task end prefix.
     *
     * @return prefix
     */
    default String getLogEnd() {
        return LOG_END;
    }

    /**
     * Writer to receive output to Subject Access Request.
     *
     * @return writer
     */
    ISarWriter getSarWriter();

}
