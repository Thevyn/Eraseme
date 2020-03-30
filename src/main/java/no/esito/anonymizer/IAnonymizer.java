/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer;

import java.util.List;

/**
 * Interface to use for external programs accessing the JAR file.
 */
public interface IAnonymizer {

    /**
     * Returns the root of the task tree containing the available tasks.
     *
     * @return task group
     */
    ITasks getTaskRoot();

    /**
     * Runs a list of tasks identified by name.<br>
     * The method uses case insensitive comparison. If multiple tasks have the same name a package specification could
     * be used to uniquely identify a single task. If the task SAR_CUSTOMER is child task of package sar then both
     * sar_customer and sar.sar_customer identifies this task. If a task package is specified in the list all tasks
     * inside that package will be run, but with the restriction that only tasks matching the context's RunType will
     * actually be executed.
     *
     * @param context contains Connection
     * @param tasklist list of named tasks
     */
    void runTasks(IContext context, List<String> tasklist);

    /**
     * Returns a list of tables in the order to empty a database.
     *
     * @return list of tables
     */
    String getEmptyDbTables();

}
