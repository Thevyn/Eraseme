/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer;

/**
 * Common Interface for Create Update Delete and Erase tasks.
 */
public interface IWorkTask extends ITask {

    /**
     * Table to be modified.
     *
     * @return table name
     */
    String getTable();

    /**
     * Logical schema for Table to be modified.
     *
     * @return schema name
     */
    String getSchema();

    /**
     * All columns in table.
     *
     * @return array of columns
     */
    IColumn[] getAllColumns();

    /**
     * Where clause as a string.
     *
     * @return where clause string
     */
    String getWhere();

}
