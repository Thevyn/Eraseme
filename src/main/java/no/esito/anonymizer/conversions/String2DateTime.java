/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.conversions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import no.esito.anonymizer.IConversion;
import no.esito.anonymizer.Log;

/**
 * Parse a LocalDateTime from the string input.
 */
public class String2DateTime implements IConversion {

    public static final String LABEL = "String2DateTime - Convert to LocalDateTime";

    final static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocalDateTime convert(String txt) {

        if (txt != null && !txt.isEmpty()) {
            try {
                return LocalDateTime.parse(txt.split("\\.")[0], fmt);
            } catch (DateTimeParseException e) {
                Log.warning("DateTime? '" + txt + "'", e);
            }
        }
        return LocalDateTime.now();
    }
}
