/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer;

/**
 * Interface for conversion of String input values from database.
 * <p>
 * The following constants will be discovered by reflection on class and used for presentation in the Anonymizer model:
 * <ul>
 * <li>public static final String LABEL - label in the drop-down for Randomization and Mask-Column when when selecting conversion type
 * </ul>
 */
public interface IConversion {

    /**
     * Processes an input from the data column to something that can be used by Mask or Randomization.
     *
     * @param input string
     * @return converted object
     * @throws Exception of any type
     */
    Object convert(String input) throws Exception;

}
