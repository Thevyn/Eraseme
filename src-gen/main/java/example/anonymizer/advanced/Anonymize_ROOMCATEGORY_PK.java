// Generated with g9 DBmasker.

package example.anonymizer.advanced;

import no.esito.anonymizer.IAnonymization;
import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.column.NumberColumn;
import no.esito.anonymizer.core.AbstractUpdateTask;

/**
 * Update primary key
 */
public class Anonymize_ROOMCATEGORY_PK extends AbstractUpdateTask {

    @Override
    public String getName() {
        return "Anonymize_ROOMCATEGORY_PK";
    }

    @Override
    public String getDescription() {
        return "Update primary key";
    }

    @Override
    public String getTable() {
        return "ROOMCATEGORY";
    }

    @Override
    public String getSchema() {
        return "";
    }


    @Override
    public IColumn[] getAllColumns() {
        return new IColumn[] {
            new NumberColumn("BEDTYPE"),
            new NumberColumn("GUESTS"),
            new NumberColumn("ID"),
            new NumberColumn("INITIALPRICE"),
            new NumberColumn("MAXDISCOUNT"),
            new NumberColumn("ROOMQUALITY"),
            new NumberColumn("LOCK_FLAG"),
        };
    }

    @Override
    public String getWhere() {
        return null;
    }

    @Override
    public IColumn[] getIndexColumns() {
        return new IColumn[] {
            new NumberColumn("ID"),
        };
    }

    @Override
    public IAnonymization[] getAnonymizations() {
        return new IAnonymization[] {
            new ROOMCATEGORY_ID(),
        };
    }
}
