/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.noise;

/**
 * Noise for Decimal / Double.
 */
public class NoiseDecimal extends AbstractNoise {

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
    public NoiseDecimal(Double offset, Double fixed, Double percentage) {
        this.offset = offset;
        this.fixed = fixed;
        this.percentage = percentage;
    }

    @Override
    public Object randomize(Object input) {
        double number = (double) input;
        double r = getRandom().nextGaussian();
        return number + offset + (fixed * r) + (number * percentage / 100) * r;
    }

}
