/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.conversions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import no.esito.anonymizer.IConversion;
import no.esito.anonymizer.Log;

/**
 * Parse a LocalDate from the string input.
 */
public class String2Date implements IConversion {

    public static final String LABEL = "String2Date - Convert to LocalDate";

    static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate convert(String txt) {
        if (txt != null && !txt.isEmpty()) {
            try {
                return LocalDate.parse(txt, fmt);
            } catch (DateTimeParseException e) {
                Log.warning("Date? '" + txt + "'", e);
            }
        }
        return LocalDate.now();
    }
}
