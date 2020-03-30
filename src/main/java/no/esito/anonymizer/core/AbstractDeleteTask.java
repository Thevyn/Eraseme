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
import java.util.List;

import no.esito.anonymizer.IChildRelation;
import no.esito.anonymizer.IChildren;
import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IContext;
import no.esito.anonymizer.IContext.RunType;

/**
 * Base functionality for Delete task.
 */
public abstract class AbstractDeleteTask extends AbstractTask implements IChildren {

    @Override
    public void run(IContext context) throws Throwable {
        start(context, prefix);
        deleteCascade(context, getTable(), getWhere(), getChildren());
        deleteOrphans(context, getTable(), getChildren());
        finish(context, prefix);
    }

    @Override
    public boolean shouldRun(IContext context) {
        return context.getRunType() == RunType.RUN;
    }

    private void deleteOrphans(IContext context, String table, IChildRelation[] children)
        throws Throwable {
        for (IChildRelation ict : children) {
            if(ict instanceof CascadeDelete)
                continue;
            AbstractChildRelation dt=(AbstractChildRelation) ict;
            boolean setNull = dt.setNull();
            StringBuilder sb = new StringBuilder();
            if(setNull) {
                sb.append(dt.addSetNull());
            } else {
                sb.append("DELETE FROM ");
                sb.append(schemaPrefix(dt.getTable()));
            }

            IColumn[] ccols = dt.getChildColumns();
            if(dt instanceof NotExistsDelete) {
                sb.append(" WHERE ");
                for (IColumn ccol : ccols) {
                    sb.append(ccol + " IS NOT NULL AND ");
                }
                sb.append("NOT EXISTS (SELECT * FROM " + table + " WHERE " + dt.getParentLink()+ ")");
            } else if(dt instanceof NotInDelete) {
                String ccol = ccols[0].getName();
                String pcol = dt.getParentColumns()[0].getName();
                sb.append(" WHERE " + ccol + " IS NOT NULL AND " + ccol + " NOT IN (SELECT DISTINCT "+pcol+" FROM " + table + " WHERE "+pcol + " IS NOT NULL)");
            }
            sqlExecuteUpdate(context.getConnection(), sb.toString());

            if(!setNull) {
                deleteOrphans(context, dt.getTable(), dt.getChildren());
            }
        }
    }

    private void deleteCascade(IContext context, String table, String where, IChildRelation[] children)
        throws Throwable {
        ArrayList<String> columns = new ArrayList<>();
        for (IChildRelation dt : children) {
            for (IColumn owncol : dt.getParentColumns()) {
                if (!columns.contains(owncol.getName()))
                    columns.add(owncol.getName());
            }
        }
        String delparent = "DELETE FROM " + schemaPrefix(table) + (where != null ? " WHERE " + where : "");
        if (columns.isEmpty()) {
            sqlExecuteUpdate(context.getConnection(), delparent);
        } else {
            boolean preDelete=false;
            for (IChildRelation dt : children) {
                if(dt instanceof CascadeDelete)
                    preDelete=true;
            }
            if(preDelete) {
                List<String[]> prows = sqlReadRows(context.getConnection(), table, columns, where);
                if (!prows.isEmpty()) {
                    for (IChildRelation dt : children) {
                        if(dt instanceof CascadeDelete) {
                            for (String[] ids : prows) {
                                String link = dt.getLink(columns, ids);
                                if (dt.setNull()) {
                                    sqlExecuteUpdate(context.getConnection(), dt.addSetNull() + (link != null ? " WHERE " + link : ""));
                                } else {
                                    deleteCascade(context, dt.getTable(), link, dt.getChildren());
                                }
                            }
                        }
                    }
                }
            }
            sqlExecuteUpdate(context.getConnection(), delparent);
        }
    }

    @Override
    protected void initCols(Collection<IColumn> cols) {
        //
    }

}
