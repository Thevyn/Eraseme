// Generated with g9 DBmasker.

package example.anonymizer.create;

import no.esito.anonymizer.IAnonymization;
import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.column.NumberColumn;
import no.esito.anonymizer.core.AbstractCreateTask;
import no.esito.anonymizer.core.CreateFkGroup;
import no.esito.anonymizer.core.CreateParent;
import no.esito.anonymizer.distributions.AllCombinations;

public class Create_ROOM extends AbstractCreateTask {

    @Override
    public String getName() {
        return "Create_ROOM";
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
    public int getMinimum() {
        return 50;
    }

    @Override
    public CreateFkGroup[] getFkGroups() {
        return new CreateFkGroup[] {
            // Every hotel should have a room of each category
            new CreateFkGroup(new AllCombinations(), new CreateParent[] {
                new CreateParent("ROOMCATEGORY", new String[] {"ID"}, new IColumn[] {
                    new NumberColumn("CATEGORY_ID"),
                }, ""),
                new CreateParent("HOTEL", new String[] {"ID"}, new IColumn[] {
                    new NumberColumn("HOTEL_ID"),
                }, ""),
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
            new ROOM_ROOMNO(),new ROOM_FLOOR(),new ROOM_BALCONY(),new ROOM_HEADING(),new ROOM_ID(),
        };
    }
}
