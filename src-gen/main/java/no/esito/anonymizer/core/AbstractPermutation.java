/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IContext;

/**
 * Base functionality for a simple shuffle.
 */
public abstract class AbstractPermutation extends AbstractAnonymization {

    @Override
    public void run(IContext context, List<String> columns, List<String[]> rows)  throws Exception, SQLException{
        IColumn column = getColumn();
        int col = columns.indexOf(column.getName());
        ArrayList<String> shuffle = new ArrayList<>();
        for (String[] txt : rows) {
            shuffle.add(txt[col]);
        }
        Collections.shuffle(shuffle,context.isRepeatableRandom() ? new Random(0L) : new Random());
        initMappingFile();
        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);
            String before = row[col];
            String after = shuffle.get(i);
            if (!hasMapping(before)) {
                addMapping(before, after);
            }
            else {
            	after = getMapping(before);
            }
            addMapping(before, after);
            row[col] = after;
        }
        saveMappingFile();
    }
}
