/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.mask;

import java.util.List;

import no.esito.anonymizer.IInput;
import no.esito.anonymizer.Log;

/**
 * Randomly pick a date within given range.
 */
public class MaskRandomDecimal extends AbstractRandom implements IInput {

    private double low;

    private double high;

    public MaskRandomDecimal(String low, String high) {
        try {
            this.low = Double.parseDouble(low);
            this.high = Double.parseDouble(high);
        } catch (NumberFormatException e) {
            Log.error(e);
        }
    }

    @Override
    public Object next(List<String> columns, String[] row) throws Exception {
        return low + getRandom().nextDouble() * (high - low);
    }

}
