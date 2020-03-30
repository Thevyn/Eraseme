/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.mask;

import no.esito.anonymizer.IConversion;

/**
 * Read lines from a file and pick sequentially.
 */
public class MaskFileSequence extends MaskFile {

    int num = 0;

    public MaskFileSequence(String filename, IConversion conversion) {
        super(filename, conversion);
    }

    @Override
    public String next() throws Exception {
        String[] list = getList();
        if (num >= list.length)
            num = 0;
        return list[num++];
    }

}
