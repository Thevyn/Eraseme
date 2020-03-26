// Generated with g9 DBmasker.

package example.anonymizer.advanced;

import no.esito.anonymizer.IAnonymization;
import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.column.NumberColumn;
import no.esito.anonymizer.core.AbstractUpdateTask;

/**
 * Shuffle PK - needs a temp key on the Dependencies node
 */
public class Anonymize_ROOM extends AbstractUpdateTask {

    @Override
    public String getName() {
        return "Anonymize_ROOM";
    }

    @Override
    public String getDescription() {
        return "Shuffle PK - needs a temp key on the Dependencies node";
    }

    @Override
    public String getTable() {
        return "ROOM";
    }

    @Override
    public String getSchema() {
        return "";
    }


    @Override
    public IColumn[] getAllColumns() {
        return new IColumn[] {
            new NumberColumn("ROOMNO"),
            new NumberColumn("FLOOR"),
            new NumberColumn("BALCONY"),
            new NumberColumn("HEADING"),
            new NumberColumn("ID"),
            new NumberColumn("LOCK_FLAG"),
            new NumberColumn("CATEGORY_ID"),
            new NumberColumn("HOTEL_ID"),
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
            new ROOM_ID(),
        };
    }
}
