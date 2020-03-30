/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

import java.util.ArrayList;
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
public abstract class AbstractEraseTask extends AbstractTask implements IChildren, IUpdateColumns {

    @Override
    public void run(IContext context) throws Throwable {
        start(context, prefix);
        String where = getWhere(getWhere(), context.getRunParams());
        eraseRecurse(context, getTable(), where, getChildren(), getIndexColumns(), getAnonymizations());
        finish(context, prefix);
    }

    @Override
    public boolean shouldRun(IContext context) {
        return context.getRunType() == RunType.ERASE;
    }

    private void eraseRecurse(IContext context, String table, String where, IChildRelation[] children, IColumn[] index,
        IAnonymization[] anonymizations) throws Throwable {
        ArrayList<String> fields = new ArrayList<>();
        for (IChildRelation dt : children) {
            for (IColumn owncol : dt.getParentColumns()) {
                if (!fields.contains(owncol.getName()))
                    fields.add(owncol.getName());
            }
        }
        if (!fields.isEmpty()) {
            List<String[]> prows = sqlReadRows(context.getConnection(), table, fields, where);
            if (prows.isEmpty()) {
                Log.info("No rows was found in " + table + ". Nothing was erased");
                return;
            }
            for (IChildRelation ict : children) {
            	CascadeErase dt=(CascadeErase) ict;
                for (String[] row : prows) {
                    String link = dt.getLink(fields,row);
                    eraseRecurse(context, dt.getTable(), link, dt.getChildren(), dt.getChildColumns(), dt.getAnonymizations());
                    if (dt.setNull()) {
                        sqlExecuteUpdate(context.getConnection(), dt.addSetNull() + (link != null ? " WHERE " + link : ""));
                    }
                }
            }
        }
        if (anonymizations.length == 0) {
            String cmd = "DELETE FROM " + schemaPrefix(table) + " WHERE " + where;
            sqlExecuteUpdate(context.getConnection(), cmd);
        } else {
            Collection<IColumn> cols = new LinkedHashSet<>();
            for (IColumn col : index) {
                cols.add(col);
            }
            registerAnonymizations(cols, anonymizations);
            List<String> readcolumns = IColumn.listNames(cols);
            try {
                prepareInputs(context, anonymizations);
                List<String> columns = IColumn.listNames(new ArrayList<IColumn>(cols));
                List<String[]> rows = sqlReadRows(context.getConnection(), table, columns, where);
                Collection<String> updatecolumns = new LinkedHashSet<String>();
                for (IAnonymization anonymization : anonymizations) {
                    updatecolumns.add(anonymization.getColumn().getName());
                    anonymization.run(context, readcolumns, rows);
                }
                sqlUpdateRows(context.getConnection(), table, new ArrayList<IColumn>(cols), updatecolumns, rows, where);
            } catch (Exception e) {
                Log.error(e);
            }
        }
    }

    @Override
    protected void initCols(Collection<IColumn> cols) {
        for (IColumn col : getIndexColumns()) {
            cols.add(col);
        }
        registerAnonymizations(cols, getAnonymizations());
    }

}
