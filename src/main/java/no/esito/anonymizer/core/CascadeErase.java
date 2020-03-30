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

public class CascadeErase extends AbstractChildRelation {

    final private IAnonymization[] anonymizations;

    public CascadeErase(String table, IColumn[] parentColumns, IChildRelation[] children, IColumn[] childColumns, IAnonymization[] anonymizations) {
        super(table, parentColumns, children, childColumns);
        this.anonymizations = anonymizations;
    }
    public IAnonymization[] getAnonymizations() {
        return anonymizations;
    }

}
