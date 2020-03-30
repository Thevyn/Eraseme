/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer;

/**
 * Interface for a cascading erase or delete.
 */
public interface IChildren {

    /**
     * Get related child tables.
     *
     * @return array of child tables
     */
    IChildRelation[] getChildren();

}
