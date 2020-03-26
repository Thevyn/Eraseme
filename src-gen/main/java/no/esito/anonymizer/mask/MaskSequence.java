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
import no.esito.anonymizer.IPreScan;
import no.esito.anonymizer.Log;

/**
 * Provide numbers sequentially from given starting point. <br>
 * "-1" Auto - has special meaning. It will scan all current highest value and continue from there.
 */
public class MaskSequence implements IInput, IPreScan {

    final private int increment;

    private int count;

    public MaskSequence(int start, int increment) {
        this.increment = increment;
        this.count = start;
    }

    @Override
    public Object next(List<String> columns, String[] row) throws Exception {
        int val = count;
        count += increment;
        return val;
    }

    @Override
    public void scan(int col, List<String[]> rows) {
        if (count == -1) {
            for (String[] row : rows) {
                try {
                    count = Math.max(count, Integer.parseInt(row[col]));
                } catch (NumberFormatException e) {
                    Log.error(e);
                }
            }
            count += increment;
        }
    }

}
