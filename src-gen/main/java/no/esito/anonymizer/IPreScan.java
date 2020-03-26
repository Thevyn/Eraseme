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
 * Implement if it is necessary to scan all rows of data before assigning values.
 */
public interface IPreScan {

    /**
     * Scans all data for this column number.
     *
     * @param col column number in dataset
     * @param rows List of rows
     */
    void scan(int col, List<String[]> rows);

}
