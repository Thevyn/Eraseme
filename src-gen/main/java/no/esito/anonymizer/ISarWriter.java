/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer;

import java.io.Closeable;
import java.io.Flushable;
import java.util.List;

/**
 * Interface for SAR report formatting.
 */
public interface ISarWriter extends Closeable, Flushable {

    /**
     * Escape algorithm per character.
     *
     * @param c char
     * @return escaped char
     */
    String escape(char c);

    /**
     * Process illegal characters for ID.
     *
     * @param name input
     * @return legal name
     */
    String id(String name);

    /**
     * Escape string.
     *
     * @param string to escape
     * @return escaped string
     */
    String escape(String string);

    /**
     * Set indentation type. Use the empty string for no indentation.
     *
     * @param indent String
     */
    void setIndentation(String indent);

    /**
     * Set DBMS NULL Representation.
     *
     * @param nullRep used to represent the DBMS null value in the output If value is null the actual column is omitted
     *            from the output
     */
    void setNullRepresentation(String nullRep);

    /**
     * Output the table element.
     *
     * @param table name
     * @param label optional info
     * @param comment optional info
     * @param columns columns
     * @param inner children data
     * @return string
     */
    String writeTable(String table, String label, String comment, String columns, String inner);

    /**
     * Output the column element.
     *
     * @param column name
     * @param label optional info
     * @param comment optional info
     * @param value value
     * @return string
     */
    String writeColumn(String column, String label, String comment, String value);

    /**
     * Write root element.
     *
     * @param inner string
     * @return string
     */
    String writeRoot(String inner);

    /**
     * Indent a block of text.
     *
     * @param block block of text
     * @param indent as spaces
     * @return indented block
     */
    String indentBlock(String block, String indent);

    /**
     * Wrap tables.
     *
     * @param table common table name
     * @param rows rows for the table
     * @return string
     */
    String wrapTables(String table, List<String> rows);

    /**
     * Wrap columns.
     *
     * @param columns as list
     * @return string
     */
    String wrapColumns(List<String> columns);

    /**
     * Wrap children elements.
     *
     * @param children elements
     * @return string
     */
    String wrapInner(List<String> children);

    /**
     * Recurse tables deeper.
     *
     * @param table table name
     * @param label optional label
     * @param comment optional comment
     */
    void push(String table, String label, String comment);

    /**
     * Output attribute.
     *
     * @param colname column name
     * @param label optional label
     * @param comment optional comment
     * @param value value to write
     */
    void write(String colname, String label, String comment, String value);

    /**
     * Return up one level.
     */
    void pop();

}
