// Generated with g9 DBmasker.

package example.anonymizer.anonymize;

import no.esito.anonymizer.IChildRelation;
import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.column.NumberColumn;
import no.esito.anonymizer.core.AbstractDeleteTask;
import no.esito.anonymizer.core.CascadeDelete;

/**
 * SUB-SETTING - removes some records including multiple dependencies
 */
public class Delete_HOTELCHAIN extends AbstractDeleteTask {

    @Override
    public String getName() {
        return "Delete_HOTELCHAIN";
    }

    @Override
    public String getDescription() {
        return "SUB-SETTING - removes some records including multiple dependencies";
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
    public String getWhere() {
        return "ID = 0";
    }

    @Override
    public IChildRelation[] getChildren() {
        return new IChildRelation[] {
            new CascadeDelete("HOTEL", new IColumn[] {
                new NumberColumn("ID"),
            }, new IChildRelation[] {
            			new CascadeDelete("BOOKING", new IColumn[] {
            			    new NumberColumn("ID"),
            			}, new IChildRelation[] {
            			}, new IColumn[] {
            			    new NumberColumn("HOTEL_ID"),
            			}),
            			new CascadeDelete("HOTELROOMCATEGORY", new IColumn[] {
            			    new NumberColumn("ID"),
            			}, new IChildRelation[] {
            			}, new IColumn[] {
            			    new NumberColumn("HOTEL_ID"),
            			}),
            			new CascadeDelete("ROOM", new IColumn[] {
            			    new NumberColumn("ID"),
            			}, new IChildRelation[] {
            			}, new IColumn[] {
            			    new NumberColumn("HOTEL_ID"),
            			}),
            }, new IColumn[] {
                new NumberColumn("CHAIN_ID"),
            }),
        };
    }
}
