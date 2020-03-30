/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.noise;

import java.time.LocalDate;

import no.esito.anonymizer.Log;

/**
 * Noise for a Date. <br>
 * Noise numbers are rounded up to a day.
 */
public class NoiseDate extends AbstractNoise {

    double offset;

    double fixed;

    /**
     * Noise constructor.
     *
     * @param offset simply added
     * @param fixed deviation
     * @param percentage unused
     */
    public NoiseDate(Double offset, Double fixed, Double percentage) {
        this.offset = offset;
        this.fixed = fixed;
    }

    @Override
    public Object randomize(Object input) {
        try {
            LocalDate dt = (LocalDate) input;
            double r = getRandom().nextGaussian();
            return dt.plusDays(Math.round(offset + fixed * r));
        } catch (NumberFormatException e) {
            Log.error(e);
        }
        return null;
    }

}
