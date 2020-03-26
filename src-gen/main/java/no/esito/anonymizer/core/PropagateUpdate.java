/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

public class PropagateUpdate {

    final String table;

    final String fkColumn;

    final String parentColumn;

    public PropagateUpdate(String table, String fkColumn, String parentColumn) {
        super();
        this.table = table;
        this.fkColumn = fkColumn;
        this.parentColumn = parentColumn;
    }
}
