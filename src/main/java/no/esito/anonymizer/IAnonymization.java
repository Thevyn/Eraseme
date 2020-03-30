/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer;

import java.sql.SQLException;
import java.util.List;

import no.esito.anonymizer.core.PropagateUpdate;

/**
 * Interface for an anonymization definition object.
 */
public interface IAnonymization {

    /**
     * MappingFileUsage specifies the usage of an anonymization's mapping file.
     * <ul>
     * <li><b>INPUTOUTPUT</b> Use the file both for input and output.
     * <li><b>INPUT</b> Use the file only for input
     * <li><b>OUTPUT</b> Use the file only for output
     * </ul>
     */
    public static enum MappingFileUsage {
        INPUTOUTPUT,
    	INPUT,
    	OUTPUT
    }

   /**
     * "Children" to propagate the anonymization value to.
     *
     * @return array of propagated updates
     */
    PropagateUpdate[] getPropagatedUpdates();

    /**
     * Name of mapping file (or null if not used).
     *
     * @return name
     */
    String getMappingFile();

    /**
     * Type of usage for the mapping file
     *
     * @return INPUTOUTPUT (input and output), INPUT (only input, not output)  or OUPUT (only output, not input)
     */
    MappingFileUsage getMappingFileUsage();

    /**
     * TempID is necessary for a shuffle operation to store a temporary data row.
     *
     * @return tempid
     */
    String getTempId();

    /**
     * If encryption should be used for log file.
     *
     * @return true if encryption used
     */
    boolean useEncryption();

    /**
     * Name of column to be anonymized.
     *
     * @return name
     */
    IColumn getColumn();

    /**
     * Run method for anonymization.
     *
     * @param context contains Connection
     * @param columns List of columns
     * @param rows List of rows
     * @throws Exception Exception of any type
     * @throws SQLException SQL Exception
     */
    void run(IContext context, List<String> columns, List<String[]> rows) throws Exception, SQLException;

}
