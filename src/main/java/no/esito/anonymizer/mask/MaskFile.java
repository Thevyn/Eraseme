/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.mask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import no.esito.anonymizer.IConversion;
import no.esito.anonymizer.IInput;
import no.esito.anonymizer.core.FileUtils;

/**
 * Read lines from a file.
 */
public abstract class MaskFile implements IInput {

    String[] list;

    final String filename;

    final protected IConversion conversion;

    public MaskFile(String filename, IConversion conversion) {
        this.filename = filename;
        this.conversion = conversion;
    }

    /**
     * Parses the text file. <br>
     * ( subclass this to read the lines differently ).
     *
     * @return All lines
     * @throws Exception any type
     */
    public String[] getList() throws Exception {
        if (list == null)
            list = parseLines(filename);
        return list;
    }

    /**
     * Gets new line from file.
     *
     * @return Next line
     * @throws Exception any type
     */
    public abstract String next() throws Exception;

    @Override
    public Object next(List<String> columns, String[] row) throws Exception {
        String val = next();
        return conversion == null ? val : conversion.convert(val);
    }

    public static String[] parseLines(String file1) throws Exception {
        InputStream input = FileUtils.getResourceAsStream(file1);
        String txt = stream2String(input);
        input.close();
        return txt.replaceAll("\\\r", "").split("\\\n");
    }

    public static String stream2String(InputStream is) throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

}
