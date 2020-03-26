/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.distributions;

import java.util.List;
import java.util.Random;

import no.esito.anonymizer.IDistribution;
import no.esito.anonymizer.core.CreateParent;
import no.esito.anonymizer.mask.AbstractRandom;

public final class SimpleSpread extends AbstractRandom implements IDistribution {

    public static final String LABEL = "SimpleSpread - FK columns are randomly assigned among possible values";

    private Random random;

    @Override
    public void setRandom(Random random) {
        this.random = random;
    }

    @Override
    public Random getRandom() {
        return random;
    }

    @Override
    public int calculateNewRows(CreateParent[] parents, int numExistRows, List<String[]> rows) {
        return 0;
    }

    @Override
    public void distribute(List<String> columns, CreateParent[] parents, List<String[]> rows) {
        for (String[] row : rows) {
            randomAssignRow(columns, parents, row);
        }
    }

}