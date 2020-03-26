/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.mask;

import static no.esito.anonymizer.IDistribution.assignRow;

import java.util.List;
import java.util.Random;

import no.esito.anonymizer.IRandom;
import no.esito.anonymizer.core.CreateParent;

public abstract class AbstractRandom implements IRandom {

    private Random random;

    @Override
    public Random getRandom() {
        return random;
    }

    @Override
    public void setRandom(Random random) {
        this.random = random;
    }

    public void randomAssignRow(List<String> columns, CreateParent[] parents, String[] row) {
        for (CreateParent parent : parents) {
            randomAssignColumn(columns, row, parent);
        }
    }

    public void randomAssignColumn(List<String> columns, String[] row, CreateParent parent) {
        assignRow(parent, columns, row, getRandom().nextInt(parent.count.length));
    }

}
