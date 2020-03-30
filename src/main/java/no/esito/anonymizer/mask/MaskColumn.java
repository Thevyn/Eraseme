/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.mask;

import java.util.List;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IConversion;
import no.esito.anonymizer.IInput;

/**
 * Column - will provide values from a named data column (could be itself).
 */
public class MaskColumn implements IInput {

    final private IColumn col;

    final private IConversion conversion;

    public IColumn getColumn() {
        return col;
    }

    public MaskColumn(IColumn column, IConversion conversion) {
        this.col = column;
        this.conversion = conversion;
    }

    @Override
    public Object next(List<String> columns, String[] row) throws Exception {
        String val = row[columns.indexOf(col.getName())];
        return conversion == null ? val : conversion.convert(val);
    }

}
