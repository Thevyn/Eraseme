/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.mask;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import no.esito.anonymizer.IInput;
import no.esito.anonymizer.Log;

/**
 * Randomly pick a time within given range.
 */
public class MaskRandomTime extends AbstractRandom implements IInput {

    private LocalTime low;

    private LocalTime high;

    final static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");

    public MaskRandomTime(String low, String high) {
        try {
            this.low = LocalTime.parse(low, fmt);
            this.high = LocalTime.parse(high, fmt);
        } catch (NumberFormatException e) {
            Log.error(e);
        }
    }

    @Override
    public Object next(List<String> columns, String[] row) throws Exception {
        long range = low.until(high, ChronoUnit.SECONDS);
        return low.plusSeconds(getRandom().nextInt((int) (range + 1)));
    }

}
