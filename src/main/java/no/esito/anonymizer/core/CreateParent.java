/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

import java.util.ArrayList;
import java.util.List;

import no.esito.anonymizer.IColumn;

public class CreateParent {

    /**
     * Parent table name.
     */
    public final String parent;

    /**
     * Associated parent column names.
     */
    public final String[] parentColumns;

    /**
     * Column definitions for the columns to assign values.
     */
    public final IColumn[] fkColumns;

    /**
     * String parameter values.
     */
    public final String params;

    /**
     * Rows of foreign key values.
     */
    public ArrayList<String[]> parentRows = new ArrayList<>();

    /**
     * Number of occurrences of parent values in the existing rows.
     */
    public int[] count;

    public int[] mapping;

    /**
     * Parent for Create operation.
     *
     * @param parent parent
     * @param parentColumns list of column names
     * @param fkColumns foreign key columns
     * @param params parameters
     */
    public CreateParent(String parent, String[] parentColumns, IColumn[] fkColumns, String params) {
        super();
        this.parent = parent;
        this.parentColumns = parentColumns;
        this.fkColumns = fkColumns;
        this.params = params;
    }

    /**
     * Counts number of occurrences of each foreign key value.
     *
     * @param rows data
     * @param columns columns to test - can be multiple
     */
    public void doCount(List<String[]> rows, ArrayList<String> columns) {
        int end = parentRows.size();
        count = new int[end];
        int size = rows.size();
        mapping = new int[size];
        for (int i = 0; i < size; i++) {
            String[] row = rows.get(i);
            int x = 0;
            for (; x < end; x++) {
                String[] fkvalue = parentRows.get(x);
                if (equals(columns, row, fkvalue))
                    break;
            }
            if (x < end) {
                count[x]++;
                mapping[i] = x;
            } else {
                throw new RuntimeException("Expected data not found." + row.toString());
            }
        }
    }

    private boolean equals(ArrayList<String> columns, String[] row, String[] parentRow) {
        for (int i = 0; i < fkColumns.length; i++) {
            String col = fkColumns[i].getName();
            if (!parentRow[i].equals(row[columns.indexOf(col)]))
                return false;
        }
        return true;
    }
}
