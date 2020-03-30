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

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IContext;
import no.esito.anonymizer.IPreScan;
import no.esito.anonymizer.ITransformation;

/**
 * Base functionality for Masking a column.
 */
public class EraseColumn extends AbstractAnonymization {

    final IColumn column;

    final String format;

    final ITransformation transformation;

    @Override
    public IColumn getColumn() {
        return column;
    }

    public EraseColumn(IColumn column, String format, ITransformation transformation) {
        this.column = column;
        this.format = format;
        this.transformation = transformation;
    }

    @Override
    public void run(IContext context, List<String> columns, List<String[]> rows) throws Exception, SQLException {
        initMappingFile();
        int col = columns.indexOf(column.getName());
        if (transformation instanceof IPreScan) {
            ((IPreScan) transformation).scan(col, rows);
        }
        for (String[] row : rows) {
            String before = row[col];
            String after = mask(transformation, format, before);
            addMapping(before, after);
            row[col] = after;
        }
        saveMappingFile();
    }

    private static String mask(ITransformation transformation, String format, String before) throws Exception {
        String text = "NULL".equals(format) ? null : String.format(Locale.US, format==null?"%s":format, new Object[] { before });
        return transformation == null ? text : transformation.transform(text);
    }

}
