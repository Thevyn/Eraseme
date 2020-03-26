// Generated with g9 DBmasker.

package example.anonymizer.advanced;

import no.esito.anonymizer.IAnonymization;
import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.column.NumberColumn;
import no.esito.anonymizer.column.TextColumn;
import no.esito.anonymizer.core.AbstractUpdateTask;

/**
 * Export anonymization to mapping file
 */
public class Anonymize_HOTEL extends AbstractUpdateTask {

    @Override
    public String getName() {
        return "Anonymize_HOTEL";
    }

    @Override
    public String getDescription() {
        return "Export anonymization to mapping file";
    }

    @Override
    public String getTable() {
        return "HOTEL";
    }

    @Override
    public String getSchema() {
        return "";
    }


    @Override
    public IColumn[] getAllColumns() {
        return new IColumn[] {
            new NumberColumn("ID"),
            new TextColumn("LOCATION"),
            new TextColumn("LOGO"),
            new TextColumn("NAME"),
            new NumberColumn("LOCK_FLAG"),
            new NumberColumn("CHAIN_ID"),
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
            new HOTEL_NAME(),
        };
    }
}
