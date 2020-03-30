/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer;

/**
 * Use the ITransformation interface to implement custom correction algorithms.<br>
 * It is a simple string in-out method to implement<br>
 * <code>String transform(String);</code>
 * <p>
 * The following constants will be discovered by reflection on class and used for presentation in the Anonymizer model:
 * <ul>
 * <li>public static final String LABEL - label in the drop-down for Anonymization when selecting Transformation type
 * </ul>
 */
public interface ITransformation {

    /**
     * Method to transform a value.
     *
     * @param input Input String
     * @return Transformed String
     * @throws Exception of any type
     */
    String transform(String input) throws Exception;

}
