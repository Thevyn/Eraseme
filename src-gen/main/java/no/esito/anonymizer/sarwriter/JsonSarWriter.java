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
import java.util.List;

import no.esito.anonymizer.core.AbstractSarWriter;

public class JsonSarWriter extends AbstractSarWriter {

    /**
     * See OutputStream.
     *
     * @param out OutputStream
     */
    public JsonSarWriter(OutputStream out) {
        super(out, StandardCharsets.UTF_8);
        init();
    }

    private void init() {
        init("  ", "null");
    }

    @Override
    public String writeColumn(String column, String label, String comment, String value) {
        String v = value != null ? "\"" + escape(value) + "\"" : nullRepresentation;
        return v != null
            ? indent + indent + "\"" + id(column) + "\": {\n"
                + (label!=null ? (indent + indent + indent + "\"label\": \"" + escape(label) + "\",\n") : "")
                + (comment!=null ? (indent + indent + indent + "\"comment\": \"" + escape(comment) + "\",\n") : "")
                + indent + indent + indent + "\"value\": " + v + "\n" + indent + indent + "}"
            : null;
    }

    @Override
    public String writeTable(String table, String label, String comment, String props, String inner) {
        return "\n{\n"
        	+ (label!=null ? (indent + "\"label\": \"" + escape(label) + "\",\n") : "")
            + (comment!=null ? (indent + "\"comment\": \"" + escape(comment) + "\",\n") : "")
            + (!props.isEmpty()
                ? (indent + "\"columns\": {\n" + props + "\n" + indent + "}" + (!inner.isEmpty() ? "," : "") + "\n")
                : "")
            + inner + "}";
    }

    @Override
    public String wrapTables(String table, List<String> list) {
        return "\"" + id(table) + "\": [" + String.join(",", list) + "\n]";
    }

    @Override
    public String wrapColumns(List<String> list) {
        return String.join(",\n", list);
    }

    @Override
    public String wrapInner(List<String> list) {
        return String.join(",\n", list);
    }

    @Override
    public String writeRoot(String inner) {
        return "{\n" + indentBlock(inner, indent) + "}\n";
    }

    @Override
    public String escape(char c) {
        return escapeJson(c);
    }

}
