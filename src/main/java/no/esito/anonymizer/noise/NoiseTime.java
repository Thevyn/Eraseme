/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.noise;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import no.esito.anonymizer.Log;

/**
 * Noise for Time. <br>
 * Noise numbers are rounded up to a second.
 */
public class NoiseTime extends AbstractNoise {

    double offset;

    double fixed;

    final static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Noise constructor.
     *
     * @param offset simply added
     * @param fixed deviation
     * @param percentage - unused
     */
    public NoiseTime(Double offset, Double fixed, Double percentage) {
        this.offset = offset;
        this.fixed = fixed;
    }

    @Override
    public Object randomize(Object input) {
        try {
            LocalTime time = (LocalTime) input;
            double r = getRandom().nextGaussian();
            return time.plusSeconds(Math.round(offset + fixed * r));
        } catch (NumberFormatException e) {
            Log.error(e);
        }
        return null;
    }

}
