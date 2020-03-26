/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Interface for a data column.
 *
 * <br> gives certain behavioral and type information.
 *
 */
public interface IColumn {

    /**
     * Column name in database.
     *
     * @return name
     */
    String getName();

    /**
     * Add quotes if content needs quoting in SQL statement.
     *
     * @param content string clause
     * @return quoted / formatted
     */
    String addQuotes(String content);

    /**
     * Utility method to get names of columns.
     *
     * @param columns List of columns
     * @return list of names
     */
    public static List<String> listNames(IColumn[] columns) {
        ArrayList<String> s = new ArrayList<>();
        for (IColumn col : columns) {
            s.add(col.getName());
        }
        return s;
    }

    /**
     * Utility method to get names of columns.
     *
     * @param columns list of columns
     * @return list of names
     */
    public static List<String> listNames(Collection<IColumn> columns) {
        ArrayList<String> s = new ArrayList<>();
        for (IColumn col : columns) {
            s.add(col.getName());
        }
        return s;
    }

    /**
     * Utility method to get column by name.
     *
     * @param name column name
     * @param columns list of columns
     * @return IColumn column
     */
    public static IColumn lookup(String name, IColumn[] columns) {
        for (IColumn col : columns) {
            if(col.getName().equals(name))
                return col;
        }
        return null;
    }
}
