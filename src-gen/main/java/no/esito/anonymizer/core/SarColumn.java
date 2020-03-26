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
public class SarColumn extends AbstractAnonymization {

    final IColumn column;

    final String format;

    final ITransformation transformation;

    final String label;

    final String comment;

    public String getLabel() {
        return label;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public IColumn getColumn() {
        return column;
    }

    public SarColumn(IColumn column, String format, ITransformation transformation, String label, String comment) {
        this.column = column;
        this.format = format;
        this.transformation = transformation;
        this.label = label;
        this.comment = comment;
    }

    @Override
    public void run(IContext context, List<String> columns, List<String[]> rows) throws Exception, SQLException {
        int col = columns.indexOf(column.getName());
        if (transformation instanceof IPreScan) {
            ((IPreScan) transformation).scan(col, rows);
        }
        for (String[] row : rows) {
            String before = row[col];
            String after = mask(transformation, format, before);
            row[col] = after;
        }
    }

    private static String mask(ITransformation transformation, String format, String before) throws Exception {
        String text = before == null || "NULL".equals(format) ? null
            : String.format(Locale.US, format==null?"%s":format, new Object[] { before });
        return transformation == null ? text : transformation.transform(text);
    }
}
