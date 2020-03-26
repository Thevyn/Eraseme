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
 * Input interface for input to a Masking anonymization.
 */
public interface IInput {

    /**
     * Gets a new input value.
     *
     * @param columns List of column names
     * @param row Row of data
     * @return Next object
     * @throws Exception of any type
     */
    public abstract Object next(List<String> columns, String[] row) throws Exception;

}
