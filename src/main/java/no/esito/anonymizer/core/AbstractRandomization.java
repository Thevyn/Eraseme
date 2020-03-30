/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IContext;
import no.esito.anonymizer.IConversion;
import no.esito.anonymizer.noise.AbstractNoise;

/**
 * Base Randomization functionality.
 */
public abstract class AbstractRandomization extends AbstractAnonymization {

    /**
     * The format String to use for writing back to database.
     *
     * @return - Format string
     */
    public String getFormat() {
    	return "%s";
    }

    /**
     * @return - Noise algorithm
     */
    public abstract AbstractNoise getNoise();

    /**
     * @return - Input conversion
     */
    public abstract IConversion getConversion();

    @Override
    public void run(IContext context, List<String> columns, List<String[]> rows) throws Exception, SQLException {
        IColumn column = getColumn();
        AbstractNoise noise = getNoise();
        noise.setRandom(context.isRepeatableRandom() ? new Random(0L) : new Random());
        String format = getFormat();
        initMappingFile();
        int col = columns.indexOf(column.getName());
        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);
            IConversion conversion = getConversion();
            String before = row[col];
            Object input = conversion != null ? conversion.convert(before) : before;
            String after = input != null ? String.format(Locale.US, format, noise.randomize(input)) : null;
            if (!hasMapping(before)) {
                addMapping(before, after);
            }
            else {
            	after = getMapping(before);
            }
            row[col] = after;
        }
        saveMappingFile();
    }

}
