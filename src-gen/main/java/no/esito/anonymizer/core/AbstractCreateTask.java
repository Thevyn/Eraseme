/*
 * Copyright 2018-2020 Esito AS
 * Licensed under the g9 Anonymizer Runtime License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://download.esito.no/licenses/anonymizerruntimelicense.html
 */
package no.esito.anonymizer.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import no.esito.anonymizer.IAnonymization;
import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IContext;
import no.esito.anonymizer.IRandom;
import no.esito.anonymizer.IContext.RunType;
import no.esito.anonymizer.IUpdateColumns;
import no.esito.anonymizer.Log;

/**
 * Base functionality for Create type task.
 */
public abstract class AbstractCreateTask extends AbstractTask implements IUpdateColumns {

    /**
     * @return - array of Foreign key groups
     */
    public abstract CreateFkGroup[] getFkGroups();

    /**
     * @return - minimum number of new records
     */
    public int getMinimum() {
        return 0;
    }

    @Override
    public void run(IContext context) throws Throwable {
        start(context, prefix);
        createTask(context);
        finish(context, prefix);
    }

    private void createTask(IContext context) throws Throwable {
        try {
            List<String> columns = IColumn.listNames(getColumns());
            IAnonymization[] anonymizations = getAnonymizations();
            CreateFkGroup[] groups = getFkGroups();
            ArrayList<String> cols = new ArrayList<>();
            for (CreateFkGroup fkgroup : groups) {
                try {
                    if (fkgroup.getDistribution() instanceof IRandom)
                        ((IRandom) fkgroup.getDistribution())
                            .setRandom(context.isRepeatableRandom() ? new Random(0L) : new Random());
                    for (CreateParent ct1 : fkgroup.getParents()) {
                        // First gather parent column data
                        for (String[] row : sqlReadRows(context.getConnection(), ct1.parent,
                            Arrays.asList(ct1.parentColumns), null)) {
                            ct1.parentRows.add(row);
                        }
                        if (ct1.parentRows.isEmpty())
                            return;
                        // Build list of necessary columns
                        for (IColumn memcol : ct1.fkColumns) {
                            if (!cols.contains(memcol.getName()))
                                cols.add(memcol.getName());
                        }
                    }
                } catch (Exception e) {
                    Log.error(e);
                }
            }
            ArrayList<String[]> rows = new ArrayList<>();
            if (cols.isEmpty()) {
                for (int i = 0; i < getMinimum() - sqlCountRows(context.getConnection()); i++) {
                    rows.add(new String[columns.size()]);
                }
            } else {
                List<String[]> existing = new ArrayList<>();
                existing = sqlReadRows(context.getConnection(), getTable(), cols, null);
                int numrows = existing.size();
                int nnew = Math.max(getMinimum() - numrows, 0);

                // Calculate how many new are required
                for (CreateFkGroup fkgroup : groups) {
                    CreateParent[] parents = fkgroup.getParents();
                    for (CreateParent ct : parents) {
                        ct.doCount(existing, cols);
                    }
                    nnew = Math.max(nnew, fkgroup.getDistribution().calculateNewRows(parents, numrows, existing));
                }
                // Add the rows
                for (int i = 0; i < nnew; i++) {
                    rows.add(new String[columns.size()]);
                }
                // assign the FK columns
                for (CreateFkGroup fkgroup : groups) {
                    fkgroup.getDistribution().distribute(columns, fkgroup.getParents(), rows);
                }
            }
            prepareInputs(context, anonymizations);
            for (IAnonymization anonymization : anonymizations) {
                anonymization.run(context, columns, rows);
            }
            sqlInsertRows(context.getConnection(), columns, rows);
        } catch (Exception e) {
            Log.error(e);
        }
    }

    @Override
    public boolean shouldRun(IContext context) {
        return context.getRunType() == RunType.RUN;
    }

    @Override
    protected void initCols(Collection<IColumn> cols) {
        registerAnonymizations(cols, getAnonymizations());
        for (CreateFkGroup fkgroup : getFkGroups()) {
            for (CreateParent parent : fkgroup.getParents()) {
                for (IColumn col : parent.fkColumns) {
                    cols.add(col);
                }
            }
        }
    }
}
