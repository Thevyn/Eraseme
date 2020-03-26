/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer;

import java.util.List;

import no.esito.anonymizer.core.CreateParent;

/**
 * Interface for distribution of foreign key values when creating new rows of data.
 * <p>
 * The following constants will be discovered by reflection on class and used for presentation in the Anonymizer model:
 * <ul>
 * <li>public static final String LABEL - label in the drop-down for Create task's Group and Parents when selecting Distribution type
 * <li>public static final String MAIN_LABEL - label for common parameter for the distribution
 * <li>public static final String PARENT_LABEL - label for individual parameter for each parent
 * </ul>
 */
public interface IDistribution {

    /**
     * Returns the number of <b>new</b> rows necessary. <br>
     * (This method should also do all preparation for later assignments). <br>
     * by this time the CreateParent has had its occurrences counted.
     *
     * @param parents parents for foreign key assignments
     * @param numrows number of existing data rows
     * @param existing existing data rows
     * @return number of new rows required
     * @throws Exception of any type
     */
    int calculateNewRows(CreateParent[] parents, int numrows, List<String[]> existing) throws Exception;

    /**
     * Assigns values to the foreign key columns in the new data rows.
     *
     * @param columns column names as used in the rows
     * @param parents parent tables for foreign key assignements
     * @param rows all rows including existing
     * @throws Exception of any type
     */
    void distribute(List<String> columns, CreateParent[] parents, List<String[]> rows) throws Exception;

    /**
     * Assigns values from the parent column to the foreign key column (nkey) in given row (nrow).
     *
     * @param parent Parent object
     * @param columns List of column names corresponding to the rows
     * @param rows List of data rows
     * @param nrow which row
     * @param nkey which parent row
     */
    public static void assignRowCol(CreateParent parent, List<String> columns, List<String[]> rows, int nrow,
        int nkey) {
        for (int k = 0; k < parent.fkColumns.length; k++) {
            int n = columns.indexOf(parent.fkColumns[k].getName());
            rows.get(nrow)[n] = parent.parentRows.get(nkey)[k];
        }
    }

    /**
     * @param parent Parent object
     * @param columns List of column names corresponding to the rows
     * @param row row of data
     * @param nParent which parent
     */
    public static void assignRow(CreateParent parent, List<String> columns, String[] row, int nParent) {
        for (int k = 0; k < parent.fkColumns.length; k++) {
            int n = columns.indexOf(parent.fkColumns[k].getName());
            row[n] = parent.parentRows.get(nParent)[k];
        }
    }
}
