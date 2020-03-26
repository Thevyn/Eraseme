/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer;

/**
 * Represents a task to be run.
 */
public interface ITask {

    /**
     * Get the name of the task including path. Path elements are separated by '.'.
     *
     * @return name of task
     */
    String getName();

    /**
     * Get description of the task.
     *
     * @return description
     */
    String getDescription();

    /**
     * Run the task using provided context.
     *
     * @param context Context containing connection
     * @throws Exception of any type
     */
    void run(IContext context) throws Throwable;

    /**
     * Check if the task should run. The result depends on the contexts RunType. <br>
     * Calling run(IContext) on a task should only be performed if this method returns true.
     *
     * @param context Context containing run type
     * @return true if task should be executed within the context it was started in
     */
    boolean shouldRun(IContext context);

    /**
     * Set the prefix for log messages. <br>
     * The prefix will be added to log messages, useful for tasks in a hierarchy.
     *
     * @param prefix Log message prefix
     */
    void setLogPrefix(String prefix);

}
