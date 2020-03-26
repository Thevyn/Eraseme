/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import no.esito.anonymizer.Log;
import no.esito.anonymizer.sarwriter.JsonSarWriter;
import no.esito.anonymizer.sarwriter.SimpleJsonSarWriter;
import no.esito.anonymizer.sarwriter.SimpleXmlSarWriter;
import no.esito.anonymizer.sarwriter.XmlSarWriter;

public final class ConsoleSarWriter extends AbstractSarWriter {

    AbstractSarWriter[] writers;

    private String task;

    private void init() {
        if (writers == null) {
            try {
                writers = new AbstractSarWriter[] { new SimpleXmlSarWriter(new FileOutputStream(task + "_simple.xml")),
                    new XmlSarWriter(new FileOutputStream(task + "_full.xml")),
                    new SimpleJsonSarWriter(new FileOutputStream(task + "_simple.json")),
                    new JsonSarWriter(new FileOutputStream(task + "_full.json")) };
            } catch (Exception e) {
            	writers = new AbstractSarWriter[] {};
            	Log.error(e);
            }
        }
    }

    public void setTask(String task) {
        this.task = task;
    }

    public ConsoleSarWriter() {
         super(new OutputStream() {

            @Override
            public void write(int b) throws IOException {
                //
            }
        });
    }

    @Override
    public String writeTable(String table, String label, String comment, String columns, String inner) {
        return null;
    }

    @Override
    public String writeColumn(String column, String label, String comment, String value) {
        return null;
    }

    @Override
    public String writeRoot(String inner) {
        return null;
    }

    @Override
    public void write(String colname, String label, String comment, String value) {
        if (writers != null) {
            for (AbstractSarWriter w : writers) {
                w.write(colname, label, comment, value);
            }
        }
    }

    @Override
    public void push(String table, String label, String comment) {
        init();
        if (writers != null) {
        	for (AbstractSarWriter w : writers) {
        		w.push(table, label, comment);
        	}
        }
    }

    @Override
    public void pop() {
        if (writers != null) {
            for (AbstractSarWriter w : writers) {
                w.pop();
            }
        }
    }

    @Override
    public void flush() throws IOException {
        if (writers != null) {
            for (AbstractSarWriter w : writers) {
                try {
                    w.flush();
                } catch (IOException e) {
                    Log.error(e);
                }
            }
        }
    }

    @Override
    public void close() {
        if (writers != null) {
            for (AbstractSarWriter w : writers) {
                try {
                    w.close();
                } catch (IOException e) {
                    Log.error(e);
                }
            }
            writers = null;
        }
    }

}
