/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.distributions;

import java.util.HashSet;
import java.util.List;

import no.esito.anonymizer.IDistribution;
import no.esito.anonymizer.core.CreateParent;
import no.esito.anonymizer.mask.AbstractRandom;

public final class AllCombinations extends AbstractRandom implements IDistribution {

    public static final String LABEL = "AllCombinations - All combinations of parents are satisfied at least once";

    int[] atot;

    int[] mtot;

    int tot = 1;

    HashSet<Integer> exist = new HashSet<>();

    @Override
    public int calculateNewRows(CreateParent[] parents, int numExistRows, List<String[]> rows) {
        atot = new int[parents.length];
        mtot = new int[parents.length];
        for (int i = 0; i < parents.length; i++) {
            CreateParent parent = parents[i];
            atot[i] = parent.count.length;
            mtot[i] = tot;
            tot *= parent.count.length;
        }
        for (int j = 0; j < numExistRows; j++) {
            int key = 0;
            for (int i = 0; i < atot.length; i++) {
                key += mtot[i] * parents[i].mapping[j];
            }
            exist.add(key);
        }
        return Math.max(tot - exist.size(), 0);
    }

    @Override
    public void distribute(List<String> columns, CreateParent[] parents, List<String[]> rows) {
        int x = 0;
        for (int j = 0; j < tot; j++) {
            if (exist.contains(j))
                continue;
            String[] row = rows.get(x++);
            int key = j;
            for (int i = 0; i < parents.length; i++) {
                int mod = key % atot[i];
                key = (key - mod) / atot[i];
                IDistribution.assignRow(parents[i], columns, row, mod);
            }
        }
        while (x < rows.size()) {
            randomAssignRow(columns, parents, rows.get(x++));
        }
    }
}