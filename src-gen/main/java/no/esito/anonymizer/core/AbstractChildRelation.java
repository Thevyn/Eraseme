/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

import no.esito.anonymizer.IChildRelation;
import no.esito.anonymizer.IColumn;

public abstract class AbstractChildRelation implements IChildRelation {

    final private IColumn[] parentColumns;

    final private String table;

    final private IChildRelation[] children;

    final private IColumn[] childColumns;

    /**
     * Delete constructor
     * @param table name
     * @param parentColumns IColumn[]
     * @param children IChildRelation[]
     * @param childColumns IColumn[]
     */
    public AbstractChildRelation(String table, IColumn[] parentColumns, IChildRelation[] children, IColumn[] childColumns) {
        this.parentColumns = parentColumns;
        this.table = table;
        this.children = children;
        this.childColumns = childColumns;
    }

    @Override
    public IColumn[] getParentColumns() {
        return parentColumns;
    }

    @Override
    public String getTable() {
        return table;
    }

    @Override
    public IColumn[] getChildColumns() {
        return childColumns;
    }

    @Override
    public IChildRelation[] getChildren() {
        return children;
    }

}
