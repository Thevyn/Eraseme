/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import no.esito.anonymizer.ISarWriter;

import java.util.Stack;
import java.util.TreeMap;

/**
 * SAR writer.
 */
public abstract class AbstractSarWriter extends OutputStreamWriter implements ISarWriter {

    private class Table {

        final String name;

        final String comment;

        final String label;

        final String prefix;

        List<String> props = new ArrayList<>();

        Map<String, List<String>> children = new TreeMap<>();

        public Table(String name, String label, String comment, String prefix) {
            this.name = name;
            this.label = label;
            this.comment = comment;
            this.prefix = prefix;
        }

        public List<String> getInner() {
            List<String> inner = new ArrayList<>();
            for (Entry<String, List<String>> e : children.entrySet()) {
                String txt = wrapTables(e.getKey(), e.getValue());
                inner.add(txt);
            }
            return inner;
        }

    }

    protected Stack<Table> stack;

    protected String indent;

    protected String nullRepresentation;

    /**
     * Indent should be for each level of tables.
     *
     * @param indent string
     * @param nullRepresetation string
     */
    protected void init(String indent, String nullRepresetation) {
        setIndentation(indent);
        setNullRepresentation(nullRepresetation);
        clear();
    }

    protected void clear() {
        stack = new Stack<>();
        stack.push(new Table("Root", "", "", ""));
        push("SAR", "", "");
    }

    /**
     * See OutputStream.
     *
     * @param out OutputStream
     * @param cs Charset
     */
    public AbstractSarWriter(OutputStream out, Charset cs) {
        super(out, cs);
    }

    /**
     * See OutputStream.
     *
     * @param out OutputStream
     * @param enc CharsetEncoder
     */
    public AbstractSarWriter(OutputStream out, CharsetEncoder enc) {
        super(out, enc);
    }

    /**
     * See OutputStream.
     *
     * @param out OutputStream
     * @param charsetName String
     * @throws UnsupportedEncodingException exception
     */
    public AbstractSarWriter(OutputStream out, String charsetName) throws UnsupportedEncodingException {
        super(out, charsetName);
    }

    /**
     * See OutputStream.
     *
     * @param out OutputStream
     */
    public AbstractSarWriter(OutputStream out) {
        super(out);
    }

    @Override
    public void setNullRepresentation(String nullRepresentation) {
        this.nullRepresentation = nullRepresentation;
    }

    @Override
    public void setIndentation(String indent) {
        this.indent = indent;
    }

    @Override
    public void push(String table, String label, String comment) {
        Table peek = stack.peek();
        stack.add(new Table(table, label, comment, peek.prefix + indent));
    }

    @Override
    public void pop() {
        Table pop = stack.pop();
        Table peek = stack.peek();
        if (!pop.props.isEmpty() || !pop.children.isEmpty()) {
            String s = writeTable(pop.name, pop.label, pop.comment, wrapColumns(pop.props),
                indentBlock(wrapInner(pop.getInner()), indent));
            List<String> child = peek.children.get(pop.name);
            if (child == null) {
                child = new ArrayList<>();
                peek.children.put(pop.name, child);
            }
            child.add(s);
        }
    }

    @Override
    public void flush() throws IOException {
        if (stack.size() > 1) {
            pop();
            write(writeRoot(wrapInner(stack.get(0).getInner())));
        }
        super.flush();
        clear();
    }

    @Override
    public void close() throws IOException {
        flush();
        super.close();
    }

    @Override
    public String wrapTables(String table, List<String> list) {
        return String.join("", list);
    }

    @Override
    public String wrapColumns(List<String> list) {
        return String.join("", list);
    }

    @Override
    public String wrapInner(List<String> list) {
        return String.join("", list);
    }

    @Override
    public String indentBlock(String inner, String indent) {
        if (inner.isEmpty()) {
            return inner;
        }
        StringBuilder sb = new StringBuilder();
        String[] split = inner.split("\\n");
        for (String s : split) {
            sb.append(indent + s + "\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return writeRoot(wrapInner(stack.get(0).getInner()));
    }

    @Override
    public void write(String colname, String label, String comment, String value) {
        String prop = writeColumn(colname, label, comment, value);
        if (prop != null) {
            stack.peek().props.add(prop);
        }
    }

    @Override
    public String escape(String string) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < string.length(); i += 1) {
            sb.append(escape(string.charAt(i)));
        }
        return sb.toString();
    }

    @Override
    public String id(String name) {
        return name.replace(" ", "_");
    }

    @Override
    public String escape(char c) {
        return "" + c;
    }

    public static String escapeJson(char c) {
        switch (c) {
            case '\\':
            case '/':
            case '"':
                return "\\" + c;
            case '\b':
                return "\\b";
            case '\t':
                return "\\t";
            case '\n':
                return "\\n";
            case '\f':
                return "\\f";
            case '\r':
                return "\\r";
            default:
                return "" + c;
        }
    }

    public static String escapeXml(char c) {
        switch (c) {
            case '"':
                return "&quot;";
            case '\'':
                return "&apos;";
            case '<':
                return "&lt;";
            case '>':
                return "&gt;";
            case '&':
                return "&amp;";
            case '\n':
                return "&#10;";
            default:
                return "" + c;
        }
    }

}
