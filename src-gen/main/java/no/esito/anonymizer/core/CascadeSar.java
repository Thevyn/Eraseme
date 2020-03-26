/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

import no.esito.anonymizer.IAnonymization;
import no.esito.anonymizer.IChildRelation;
import no.esito.anonymizer.IColumn;

public class CascadeSar extends AbstractChildRelation{

    final private String label;

    final private String comment;

    final private IAnonymization[] anonymizations;

    public CascadeSar(String table, String label, String comment, IColumn[] parentColumns,
        IChildRelation[] children, IColumn[] indexColumns, IAnonymization[] anonymizations) {
        super(table, parentColumns, children, indexColumns);
        this.anonymizations = anonymizations;
        this.label = label;
        this.comment = comment;
    }

    public IAnonymization[] getAnonymizations() {
        return anonymizations;
    }

    /**
     * @return - Label for the column
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return - Comment for the column
     */
    public String getComment() {
        return comment;
    }

}
