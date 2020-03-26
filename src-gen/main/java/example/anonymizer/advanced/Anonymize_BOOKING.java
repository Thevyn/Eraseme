// Generated with g9 DBmasker.

package example.anonymizer.advanced;

import no.esito.anonymizer.IAnonymization;
import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.column.DateColumn;
import no.esito.anonymizer.column.DateTimeColumn;
import no.esito.anonymizer.column.NumberColumn;
import no.esito.anonymizer.column.TimeColumn;
import no.esito.anonymizer.core.AbstractUpdateTask;

/**
 * Update PK with auto-generated value
 */
public class Anonymize_BOOKING extends AbstractUpdateTask {

    @Override
    public String getName() {
        return "Anonymize_BOOKING";
    }

    @Override
    public String getDescription() {
        return "Update PK with auto-generated value";
    }

    @Override
    public String getTable() {
        return "BOOKING";
    }

    @Override
    public String getSchema() {
        return "";
    }


    @Override
    public IColumn[] getAllColumns() {
        return new IColumn[] {
            new DateColumn("FROMDATE"),
            new DateColumn("TODATE"),
            new DateTimeColumn("BOOKINGCREATED"),
            new TimeColumn("EARLIESTCHECKINTIME"),
            new NumberColumn("ID"),
            new NumberColumn("LOCK_FLAG"),
            new NumberColumn("CUSTOMER_CUSTOMERNO"),
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
            new NumberColumn("ID"),
        };
    }

    @Override
    public IAnonymization[] getAnonymizations() {
        return new IAnonymization[] {
            new BOOKING_ID(),new BOOKING_FROMDATE(),new BOOKING_TODATE(),new BOOKING_BOOKINGCREATED(),new BOOKING_EARLIESTCHECKINTIME(),
        };
    }
}
