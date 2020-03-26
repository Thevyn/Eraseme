/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer;

/**
 * Represents a task group (package).
 */
public interface ITasks extends ITask {

    /**
     * Get the tasks that this package consists of.
     *
     * @return array of tasks
     */
    ITask[] getTasks();

}
