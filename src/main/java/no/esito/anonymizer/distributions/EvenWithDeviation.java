/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.distributions;

import java.util.List;

import no.esito.anonymizer.IDistribution;
import no.esito.anonymizer.core.CreateParent;
import no.esito.anonymizer.mask.AbstractRandom;

public final class EvenWithDeviation extends AbstractRandom implements IDistribution {

    public static final String LABEL = "EvenWithDeviation - Even spread with deviation for occurrence of parents";

    public static final String PARENT_LABEL = "Deviation +/-";

    float[] dev;

    @Override
    public int calculateNewRows(CreateParent[] parents, int numExistRows, List<String[]> rows) {
        dev = new float[parents.length];
        for (int i = 0; i < parents.length; i++) {
            dev[i] = Float.valueOf(parents[i].params);
        }
        return 0;
    }

    @Override
    public void distribute(List<String> columns, CreateParent[] parents, List<String[]> rows) {
        for (int i = 0; i < parents.length; i++) {
            CreateParent parent = parents[i];
            int irow = 0;
            int[] a = parent.count;
            for (int icol = 0; icol < a.length; icol++) {
                int curr = a[icol];
                int sugg = calc(dev[i], rows.size() / a.length);
                for (int j = curr; j < sugg; j++) {
                    if (irow >= rows.size())
                        break;
                    IDistribution.assignRow(parent, columns, rows.get(irow++), icol);
                }
            }
            int size = parent.parentRows.size();
            while (size > 0 && irow < rows.size()) {
                randomAssignColumn(columns, rows.get(irow++), parent);
            }
        }
    }

    int calc(float dev, float ave) {
        float variation = 2 * dev * getRandom().nextFloat() - dev;
        return Math.round(ave + variation);
    }

}