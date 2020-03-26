// Generated with g9 DBmasker.

package example.anonymizer.create;

import no.esito.anonymizer.IAnonymization;
import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.column.NumberColumn;
import no.esito.anonymizer.core.AbstractCreateTask;
import no.esito.anonymizer.core.CreateFkGroup;

/**
 * Creating records in parent table
 */
public class Create_HOTELCHAIN extends AbstractCreateTask {

    @Override
    public String getName() {
        return "Create_HOTELCHAIN";
    }

    @Override
    public String getDescription() {
        return "Creating records in parent table";
    }

    @Override
    public String getTable() {
        return "HOTELCHAIN";
    }

    @Override
    public String getSchema() {
        return "";
    }


    @Override
    public int getMinimum() {
        return 5;
    }

    @Override
    public CreateFkGroup[] getFkGroups() {
        return new CreateFkGroup[] {
        };
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
            new HOTELCHAIN_ID(),new HOTELCHAIN_NAME(),
        };
    }
}
