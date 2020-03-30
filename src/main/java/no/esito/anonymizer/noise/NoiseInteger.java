/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.noise;

/**
 * Noise for integer. <br>
 * Noise numbers are rounded up to an integer.
 */
public class NoiseInteger extends AbstractNoise {

    double offset;

    double fixed;

    double percentage;

    /**
     * Noise constructor.
     *
     * @param offset simply added
     * @param fixed deviation
     * @param percentage percentage deviation
     */
    public NoiseInteger(Double offset, Double fixed, Double percentage) {
        this.offset = offset;
        this.fixed = fixed;
        this.percentage = percentage;
    }

    @Override
    public Integer randomize(Object input) {
        int number = (int) input;
        double r = getRandom().nextGaussian();
        return number + (int) Math.round(offset + (fixed * r) + (number * percentage / 100) * r);
    }

}
