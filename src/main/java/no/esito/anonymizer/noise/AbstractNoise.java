/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.noise;

import no.esito.anonymizer.mask.AbstractRandom;

/**
 * Noise calculations for Randomization type anonymization. <br>
 * Based on Normal Distribution - Gaussian noise - which is the most common algorithm for adding noise.
 */
public abstract class AbstractNoise extends AbstractRandom {

    /**
     * Randomize an input of type Decimal, Integer, Date, Time, DateTime.
     *
     * @param input Object
     * @return Object from randomization
     */
    public abstract Object randomize(Object input);

}
