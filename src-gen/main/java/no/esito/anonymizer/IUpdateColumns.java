/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer;

import no.esito.anonymizer.IColumn;

/**
 * Interface for applying anonymization and index for updating columns.
 */
public interface IUpdateColumns {

    /**
     * Get index columns.
     *
     * @return list of IColumn
     */
    public IColumn[] getIndexColumns();

    /**
     * Get annonymizations.
     *
     * @return array of IAnonymization
     */
    public IAnonymization[] getAnonymizations();

}
