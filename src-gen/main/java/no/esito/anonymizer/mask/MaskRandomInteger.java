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
 * Randomly pick an int within given range.
 */
public class MaskRandomInteger extends AbstractRandom implements IInput {

    private int low;

    private int high;

    public MaskRandomInteger(String low, String high) {
        try {
            this.low = Integer.parseInt(low);
            this.high = Integer.parseInt(high);
        } catch (NumberFormatException e) {
            Log.error(e);
        }
    }

    @Override
    public Object next(List<String> columns, String[] row) throws Exception {
        return (int) (low + getRandom().nextInt(high - low + 1));
    }

}
