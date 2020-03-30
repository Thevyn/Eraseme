/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer;

import java.io.IOException;
import java.util.List;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.core.AbstractTask;

/**
 * Interface for a cascading erase or delete.
 */
public interface IChildRelation {

    /**
     * Columns in parent used to identify the children.
     *
     * @return list of IColumn
     */
    IColumn[] getParentColumns();

    /**
     * Get name of table to be erase/deleted.
     *
     * @return table name
     */
    String getTable();

    /**
     * Columns in child used to identify parent.
     * @return IColumn[]
     */
    IColumn[] getChildColumns();

    /**
     * Children relations
     * @return IChildRelation[]
     */
    IChildRelation[] getChildren();

    default boolean setNull() {
        return getChildren()==null;
    }

    default String getLink(List<String> columns, String[] ids) {
        IColumn[] pcols = getParentColumns();
        IColumn[] ccols = getChildColumns();
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < pcols.length; i++) {
            if(sb.length()>0)
                sb.append(" AND ");
            sb.append(ccols[i].getName());
            String value = pcols[i].addQuotes(ids[columns.indexOf(pcols[i].getName())]);
            sb.append(value == null ? " IS NULL" : " = ");
            sb.append(value);
        }
        return sb.toString();
    }

    default String getParentLink() {
        IColumn[] pcols = getParentColumns();
        IColumn[] ccols = getChildColumns();
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < pcols.length; i++) {
            if(sb.length()>0)
                sb.append(" AND ");
            sb.append(pcols[i].getName());
            sb.append(" = ");
            sb.append(getTable()+"."+ccols[i].getName());
        }
        return sb.toString();
    }

    default String addSetNull() throws IOException {
        StringBuilder sb=new StringBuilder();
        IColumn[] ccols = getChildColumns();
        for (IColumn col : ccols) {
            sb.append(sb.length() == 0 ? "UPDATE "+AbstractTask.schemaPrefix(getTable())+" SET " : ", ");
            sb.append(col.getName() + " = NULL");
        }
        return sb.toString();
    }

}
