/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.sarwriter;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import no.esito.anonymizer.core.AbstractSarWriter;

public class XmlSarWriter extends AbstractSarWriter {

    /**
     * See OutputStream.
     *
     * @param out OutputStream
     */
    public XmlSarWriter(OutputStream out) {
        super(out, StandardCharsets.UTF_8);
        init();
    }

    private void init() {
        init("  ", null);
    }

    @Override
    public String writeColumn(String column, String label, String comment, String value) {
        String v = value != null ? "'" + escape(value) + "'" : nullRepresentation;
        return v != null
            ? indent + "<" + id(column) + "\n"
                + (label!=null ? (indent + indent + "label=\"" + escape(label) + "\"\n") : "")
                + (comment!=null ? (indent + indent + "comment=\"" + escape(comment) + "\"\n") : "") + indent
                + indent + "value=" + v + "\n" + indent + "/>\n"
            : null;
    }

    @Override
    public String writeTable(String table, String label, String comment, String props, String inner) {
        boolean allempty = label==null && comment==null;
        return "<" + id(table) + (allempty ? "" : "\n")
            + (label!=null ? (indent + "label=\"" + escape(label) + "\"\n") : "")
            + (comment!=null ? (indent + "comment=\"" + escape(comment) + "\"\n") : "") + (!allempty ? indent : "")
            + ">\n" + props + inner + "</" + id(table) + ">\n";
    }

    @Override
    public String writeRoot(String inner) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + inner;
    }

    @Override
    public String escape(char c) {
        return escapeXml(c);
    }

}
