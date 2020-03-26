// Generated with g9 DBmasker.

package example.anonymizer.anonymize;

import no.esito.anonymizer.IAnonymization;
import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.column.NumberColumn;
import no.esito.anonymizer.column.TextColumn;
import no.esito.anonymizer.core.AbstractUpdateTask;

/**
 * Anonymize - Mask various fields
 */
public class Anonymize_CUSTOMER extends AbstractUpdateTask {

    @Override
    public String getName() {
        return "Anonymize_CUSTOMER";
    }

    @Override
    public String getDescription() {
        return "Anonymize - Mask various fields";
    }

    @Override
    public String getTable() {
        return "CUSTOMER";
    }

    @Override
    public String getSchema() {
        return "";
    }


    @Override
    public IColumn[] getAllColumns() {
        return new IColumn[] {
            new TextColumn("CREDITCARD"),
            new NumberColumn("CUSTOMERNO"),
            new TextColumn("EMAIL"),
            new TextColumn("NAME"),
            new TextColumn("PASSWORD"),
            new TextColumn("PHONE"),
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
            new NumberColumn("CUSTOMERNO"),
        };
    }

    @Override
    public IAnonymization[] getAnonymizations() {
        return new IAnonymization[] {
            new CUSTOMER_PHONE(),new CUSTOMER_NAME(),new CUSTOMER_EMAIL(),new CUSTOMER_CREDITCARD(),
        };
    }
}
