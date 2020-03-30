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
import java.util.List;
import java.util.Locale;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IContext;
import no.esito.anonymizer.IInput;
import no.esito.anonymizer.IPreScan;
import no.esito.anonymizer.ITransformation;

/**
 * Base functionality for Masking a column.
 */
public abstract class AbstractMasking extends AbstractAnonymization {

    /**
     * @return - Inputs for constructing the value
     */
    public abstract IInput[] getInputs();

    /**
     * @return - Transformation to be used (can be null)
     */
    public abstract ITransformation getTransformation();

    /**
     * @return - Format string for Java Formatter
     */
    public String getFormat() {
        return null;
    }

    /**
     * Unique value means that it will need to check what it currently has. If it exists it will run the inputs again up
     * to 10 times before it creates an Exception.
     *
     * @return - true if this should retry to get a unique value
     */
    public abstract boolean isUnique();

    IInput[] inputs;

    @Override
    public void run(IContext context, List<String> columns, List<String[]> rows) throws Exception, SQLException {
        IColumn column = getColumn();
        ITransformation transformation = getTransformation();
        initMappingFile();
        int col = columns.indexOf(column.getName());
        ArrayList<String> list = isUnique() ? new ArrayList<>() : null;
        for (IInput input : inputs) {
            if (input instanceof IPreScan) {
                ((IPreScan) input).scan(col, rows);
            }
        }
        if (transformation instanceof IPreScan) {
            ((IPreScan) transformation).scan(col, rows);
        }
        for (String[] row : rows) {
            String before = row[col];
            String after = mask(columns, row);
            if (list != null) {
                int loop = 10;
                while (loop-- > 0) {
                    if (list.contains(after)) {
                        after = mask(columns, row);
                        continue;
                    }
                    list.add(after);
                    break;
                }
            }
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

    private String mask(List<String> columns, String[] row) throws Exception {
        ITransformation transformation = getTransformation();
        String format = getFormat();
        Object[] args = new Object[inputs.length];
        boolean isNull = false;
        for (int i = 0; i < inputs.length; i++) {
            args[i] = inputs[i].next(columns, row);
            if (args[i] == null) {
                isNull = true;
                break;
            }
        }
        String text = format == null || isNull || "NULL".equals(format) ? null : String.format(Locale.US, format, args);
        return transformation == null ? text : transformation.transform(text);
    }
}
