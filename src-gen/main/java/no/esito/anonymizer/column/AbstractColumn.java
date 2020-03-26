/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.column;

import no.esito.anonymizer.IColumn;

/**
 * Column holder has name and methods to deal with the datatype.
 */
public abstract class AbstractColumn implements Comparable<IColumn>, IColumn {

    private final String name;

    @Override
    public String getName() {
        return name;
    }

    public AbstractColumn(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(IColumn o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IColumn)
            return getName().equals(((IColumn) obj).getName());
        return false;
    }
}
