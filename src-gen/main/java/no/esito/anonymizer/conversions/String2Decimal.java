/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.conversions;

import no.esito.anonymizer.IConversion;
import no.esito.anonymizer.Log;

/**
 * Parse a Double from the string input.
 */
public class String2Decimal implements IConversion {

    public static final String LABEL = "String2Decimal - Convert to Double";

    @Override
    public Double convert(String txt) {
        if (txt != null && !txt.isEmpty()) {
            try {
                return Double.parseDouble(txt);
            } catch (NumberFormatException e) {
                Log.warning("Decimal? '" + txt + "'", e);
            }
        }
        return 0.0;
    }

}
