// Generated with g9 DBmasker.

package example.anonymizer.anonymize;

import no.esito.anonymizer.IAnonymization;
import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.column.NumberColumn;
import no.esito.anonymizer.column.TextColumn;
import no.esito.anonymizer.core.AbstractUpdateTask;

/**
 * Simple Generalization
 */
public class Fix_ADDRESS extends AbstractUpdateTask {

    @Override
    public String getName() {
        return "Fix_ADDRESS";
    }

    @Override
    public String getDescription() {
        return "Simple Generalization";
    }

    @Override
    public String getTable() {
        return "ADDRESS";
    }

    @Override
    public String getSchema() {
        return "";
    }


    @Override
    public IColumn[] getAllColumns() {
        return new IColumn[] {
            new TextColumn("HOMEADDRESS"),
            new NumberColumn("POSTALCODE"),
            new NumberColumn("ID"),
            new NumberColumn("LOCK_FLAG"),
            new NumberColumn("CUSTOMER_CUSTOMERNO"),
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
            new ADDRESS_POSTALCODE(),
        };
    }
}
