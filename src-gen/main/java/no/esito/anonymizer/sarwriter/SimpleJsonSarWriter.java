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

public class SimpleJsonSarWriter extends AbstractSarWriter {

    /**
     * See OutputStream.
     *
     * @param out OutputStream
     */
    public SimpleJsonSarWriter(OutputStream out) {
        super(out, StandardCharsets.UTF_8);
        init();
    }

    private void init() {
        init("  ", "null");
    }

    @Override
    public String writeColumn(String column, String label, String comment, String value) {
        String v = value != null ? "\"" + escape(value) + "\"" : nullRepresentation;
        return v != null ? indent + "\"" + id(column) + "\": " + v + "" : null;
    }

    @Override
    public String writeTable(String table, String label, String comment, String props, String inner) {
        return "\n" + "{\n" + props + ((!props.isEmpty() && !inner.isEmpty()) ? ",\n" : "") + inner
            + ((!props.isEmpty() && inner.isEmpty()) ? "\n" : "") + "}";
    }

    @Override
    public String wrapTables(String table, List<String> list) {
        return "\"" + id(table) + "\":" + " [" + (list.isEmpty() ? "" : String.join(",", list)) + "\n" + "]";

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
