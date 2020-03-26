/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.mask;

import java.util.Random;

import no.esito.anonymizer.IConversion;
import no.esito.anonymizer.IRandom;

/**
 * Read lines from a file and pick randomly.
 */
public class MaskFileRandom extends MaskFile implements IRandom {

    private Random random;

    public MaskFileRandom(String filename, IConversion conversion) {
        super(filename, conversion);
    }

    @Override
    public String next() throws Exception {
        String[] list = getList();
        return list[getRandom().nextInt(list.length)];
    }

    @Override
    public void setRandom(Random random) {
        this.random = random;
    }

    @Override
    public Random getRandom() {
        return random;
    }
}
