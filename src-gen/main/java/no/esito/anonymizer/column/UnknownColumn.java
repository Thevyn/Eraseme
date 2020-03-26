/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.column;

/**
 * Column holding unknown content indicating there is something wrong in the model.
 */
public class UnknownColumn extends AbstractColumn {

    public UnknownColumn(String name) {
        super(name);
    }

    @Override
    public String addQuotes(String content) {
        return content;
    }
}
