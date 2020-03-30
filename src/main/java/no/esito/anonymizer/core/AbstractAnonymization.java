/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import no.esito.anonymizer.ConfigUtil;
import no.esito.anonymizer.IAnonymization;
import no.esito.anonymizer.Log;

/**
 * Basis functionality for an Anonymization. <br>
 * implements handling of Mapping files.
 */
public abstract class AbstractAnonymization implements IAnonymization {

    @Override
    public PropagateUpdate[] getPropagatedUpdates() {
        return new PropagateUpdate[0];
    }

    @Override
    public String getMappingFile() {
        return null;
    }

    @Override
    public MappingFileUsage getMappingFileUsage() {
        return null;
    }

    @Override
    public String getTempId() {
        return null;
    }

    @Override
    public boolean useEncryption() {
        return false;
    }

    private Properties prop;

    /**
     * Initialize Mapping file for output.
     * @throws Exception when problems reading mapping file
     */
    protected void initMappingFile() throws Exception {
        String file = getMappingFile();
        if (file == null || file.isEmpty())
            return;
    	prop = new Properties();
        MappingFileUsage mapUsage = getMappingFileUsage();
        if (mapUsage == MappingFileUsage.OUTPUT) {
        	return;
        }
        readMappingFile(file);
    }

    protected void readMappingFile(String file) throws Exception {
        File f = new File(file);
        if (!f.exists()) {
        	return;
        }
        FileInputStream fis = null;
        String text;
		try {
			fis = new FileInputStream(file);
			text = FileUtils.stream2String(fis);
			if (useEncryption()) {
 				text = FileUtils.decrypt(text, ConfigUtil.getPassKey());
			}
			InputStream input = FileUtils.string2stream(text);
			prop.load(input);
			input.close();
		}
		finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				// Just ignore
			}
		}
    }

    /**
     * Save the Mapping file.
     */
    protected void saveMappingFile() {
        String file = getMappingFile();
        if (prop != null && getMappingFileUsage() != MappingFileUsage.INPUT && !(file == null || file.isEmpty())) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                prop.store(out, "*** " + getColumn() + " ***");
                String text = new String(out.toByteArray());
                out.close();
                if (useEncryption()) {
                    text = FileUtils.encrypt(text, ConfigUtil.getPassKey());
                }
                File parent = (new File(file)).getParentFile();
                if (parent != null) {
                    parent.mkdirs();
                }
                FileUtils.string2File(text, new File(file));
            } catch (Exception e) {
                Log.error(e);
            }
        }
    }

    /**
     * Add Mapping entry to mapping file.
     *
     * @param before string
     * @param after string
     */
    protected void addMapping(String before, String after) {
        if (prop != null && before != null && after != null && !prop.containsKey(before))
            prop.put(before, after);
    }

    /**
     * Get Mapping value.
     *
     * @param before string
     * @return mapping
     */
    protected String getMapping(String before) {
        return prop.getProperty(before);
    }

    /**
     * Has Mapping entry.
     *
     * @param before string
     * @return true if has mapping
     */
    protected boolean hasMapping(String before) {
        return prop != null && before != null && prop.containsKey(before);
    }
}
