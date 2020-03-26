// Generated with g9 DBmasker.

package example.anonymizer.create;

import example.anonymizer.distributions.MinPerParent;

import no.esito.anonymizer.IAnonymization;
import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.column.NumberColumn;
import no.esito.anonymizer.core.AbstractCreateTask;
import no.esito.anonymizer.core.CreateFkGroup;
import no.esito.anonymizer.core.CreateParent;

/**
 * Creating records in child table
 */
public class Create_HOTEL extends AbstractCreateTask {

    @Override
    public String getName() {
        return "Create_HOTEL";
    }

    @Override
    public String getDescription() {
        return "Creating records in child table";
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
    public CreateFkGroup[] getFkGroups() {
        return new CreateFkGroup[] {
            // Divide Hotels per Chain with a deviation of 1
            new CreateFkGroup(new MinPerParent(), new CreateParent[] {
                // Divide Hotels per Chain with a deviation of 1
                new CreateParent("HOTELCHAIN", new String[] {"ID"}, new IColumn[] {
                    new NumberColumn("CHAIN_ID"),
                }, "2"),
            }),
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
            new HOTEL_ID(),new HOTEL_NAME(),new HOTEL_LOCATION(),new HOTEL_LOGO(),
        };
    }
}
