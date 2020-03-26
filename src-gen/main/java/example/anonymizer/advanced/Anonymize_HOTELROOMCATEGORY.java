// Generated with g9 DBmasker.

package example.anonymizer.advanced;

import no.esito.anonymizer.IAnonymization;
import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.column.DateColumn;
import no.esito.anonymizer.column.NumberColumn;
import no.esito.anonymizer.core.AbstractUpdateTask;

/**
 * Multi column PK
 */
public class Anonymize_HOTELROOMCATEGORY extends AbstractUpdateTask {

    @Override
    public String getName() {
        return "Anonymize_HOTELROOMCATEGORY";
    }

    @Override
    public String getDescription() {
        return "Multi column PK";
    }

    @Override
    public String getTable() {
        return "HOTELROOMCATEGORY";
    }

    @Override
    public String getSchema() {
        return "";
    }


    @Override
    public IColumn[] getAllColumns() {
        return new IColumn[] {
            new NumberColumn("ACTUALPRICE"),
            new DateColumn("FROMDATE"),
            new DateColumn("TODATE"),
            new NumberColumn("LOCK_FLAG"),
            new NumberColumn("HOTEL_ID"),
            new NumberColumn("ROOMCATEGORY_ID"),
        };
    }

    @Override
    public String getWhere() {
        return null;
    }

    @Override
    public IColumn[] getIndexColumns() {
        return new IColumn[] {
            new NumberColumn("HOTEL_ID"),
            new NumberColumn("ROOMCATEGORY_ID"),
            new DateColumn("FROMDATE"),
        };
    }

    @Override
    public IAnonymization[] getAnonymizations() {
        return new IAnonymization[] {
            new HOTELROOMCATEGORY_FROMDATE(),new HOTELROOMCATEGORY_ACTUALPRICE(),new HOTELROOMCATEGORY_TODATE(),new HOTELROOMCATEGORY_TODATE_ADD(),
        };
    }
}
