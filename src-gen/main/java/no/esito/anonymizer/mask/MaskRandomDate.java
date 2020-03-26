/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.mask;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import no.esito.anonymizer.IInput;
import no.esito.anonymizer.Log;

/**
 * Randomly pick a date within given range.
 */
public class MaskRandomDate extends AbstractRandom implements IInput {

    private LocalDate low;

    private LocalDate high;

    private static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public MaskRandomDate(String low, String high) {
        try {
            this.low = LocalDate.parse(low, fmt);
            this.high = LocalDate.parse(high, fmt);
        } catch (NumberFormatException e) {
            Log.error(e);
        }
    }

    @Override
    public Object next(List<String> columns, String[] row) throws Exception {
        long range = low.until(high, ChronoUnit.DAYS);
        return low.plusDays(getRandom().nextInt((int) (range + 1)));
    }

}
