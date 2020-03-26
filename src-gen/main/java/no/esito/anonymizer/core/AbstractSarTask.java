/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import no.esito.anonymizer.IAnonymization;
import no.esito.anonymizer.IChildRelation;
import no.esito.anonymizer.IChildren;
import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IContext;
import no.esito.anonymizer.IContext.RunType;
import no.esito.anonymizer.IUpdateColumns;
import no.esito.anonymizer.Log;

/**
 * Base functionality for an Erase type task.
 */
public abstract class AbstractSarTask extends AbstractTask implements IChildren, IUpdateColumns {

    @Override
    public void run(IContext context) throws Throwable {
        start(context, prefix);
        String where = getWhere(getWhere(), context.getRunParams());
        sarRecurse(context, getTable(), where, getChildren(), getIndexColumns(), getAnonymizations(), getLabel(),
            getComment(), true);
        finish(context, prefix);
    }

    @Override
    public boolean shouldRun(IContext context) {
        return context.getRunType() == RunType.SAR;
    }

    private void sarRecurse(IContext context, String table, String where, IChildRelation[] children, IColumn[] index,
        IAnonymization[] anonymizations, String label, String comment, boolean top) throws Throwable {
        // Configure columns to read
        Collection<IColumn> cols = new LinkedHashSet<>();
        for (IColumn col : index) {
            cols.add(col);
        }
        for (IAnonymization ano : anonymizations) {
            cols.add(ano.getColumn());
        }
        for (IChildRelation dt : children) {
            for (IColumn col : dt.getParentColumns()) {
                cols.add(col);
            }
        }
        List<String> readcolumns = IColumn.listNames(cols);
        prepareInputs(context, anonymizations);
        List<String> columns = IColumn.listNames(new ArrayList<IColumn>(cols));
        List<String[]> rows = sqlReadRows(context.getConnection(), table, columns, where);
        if (top && rows.isEmpty()) {
            Log.info("No rows was found in " + table + ". Nothing was reported");
            return;
        }
        List<String[]> original=new ArrayList<>();
        for (String[] strings : rows) {
            original.add(Arrays.copyOf(strings,strings.length));
        }
        Collection<SarColumn> updatecolumns = new LinkedHashSet<>();
        for (IAnonymization anonymization : anonymizations) {
            updatecolumns.add((SarColumn) anonymization);
            anonymization.run(context, readcolumns, rows);
        }

        for (int i=0;i<original.size(); i++) {
            String[] row=rows.get(i);
            context.getSarWriter().push(table, label, comment);
            for (SarColumn sarcol : updatecolumns) {
                IColumn column = sarcol.getColumn();
                String colname = column.getName();
                int n = readcolumns.indexOf(colname);
                context.getSarWriter().write(colname, sarcol.getLabel(), sarcol.getComment(), row[n]);
            }
            for (IChildRelation dt : children) {
                traverseChild(context, readcolumns, original.get(i), (CascadeSar) dt, table);
            }
            context.getSarWriter().pop();
        }
    }

    protected void traverseChild(IContext context, List<String> readcolumns, String[] row, CascadeSar dt, String table) throws Throwable {
        String link = dt.getLink(readcolumns, row);
        sarRecurse(context, dt.getTable(), link, dt.getChildren(), dt.getChildColumns(), dt.getAnonymizations(), dt.getLabel(), dt.getComment(), false);
    }

    @Override
    protected void initCols(Collection<IColumn> cols) {
        for (IColumn col : getIndexColumns()) {
            cols.add(col);
        }
        registerAnonymizations(cols, getAnonymizations());
    }

    /**
     * @return - Label for the column
     */
    public abstract String getLabel();

    /**
     * @return - Comment for the column
     */
    public abstract String getComment();

}
