// Generated with g9 DBmasker.

package example.anonymizer.subjectaccess;

import no.esito.anonymizer.IAnonymization;
import no.esito.anonymizer.IChildRelation;
import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.column.DateColumn;
import no.esito.anonymizer.column.NumberColumn;
import no.esito.anonymizer.column.TextColumn;
import no.esito.anonymizer.core.AbstractSarTask;
import no.esito.anonymizer.core.CascadeSar;
import no.esito.anonymizer.core.SarColumn;

public class SAR_CUSTOMER extends AbstractSarTask {

    @Override
    public String getName() {
        return "SAR_CUSTOMER";
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
    public String getWhere() {
        return "CUSTOMERNO = %PARAMETER%";
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
            new SarColumn(new TextColumn("CREDITCARD"),null,null,null,null),
            new SarColumn(new NumberColumn("CUSTOMERNO"),null,null,null,null),
            new SarColumn(new TextColumn("EMAIL"),null,null,null,null),
            new SarColumn(new TextColumn("NAME"),null,null,null,null),
            new SarColumn(new TextColumn("PASSWORD"),null,null,null,null),
        };
    }

    @Override
    public IChildRelation[] getChildren() {
        return new IChildRelation[] {
            new CascadeSar("ADDRESS", null , null, new IColumn[] {
                    new NumberColumn("CUSTOMERNO"),
                }
                , new IChildRelation[] {
                }, new IColumn[] {
                    new NumberColumn("CUSTOMER_CUSTOMERNO"),
                }, new IAnonymization[] {
                    new SarColumn(new TextColumn("HOMEADDRESS"),null,null,null,null),
                    new SarColumn(new NumberColumn("POSTALCODE"),null,null,null,null),
                    new SarColumn(new NumberColumn("ID"),null,null,null,null),
                }
            ),
            new CascadeSar("BOOKING", null , null, new IColumn[] {
                    new NumberColumn("CUSTOMERNO"),
                }
                , new IChildRelation[] {
                }, new IColumn[] {
                    new NumberColumn("CUSTOMER_CUSTOMERNO"),
                }, new IAnonymization[] {
                    new SarColumn(new DateColumn("FROMDATE"),null,null,null,null),
                    new SarColumn(new DateColumn("TODATE"),null,null,null,null),
                    new SarColumn(new NumberColumn("ID"),null,null,null,null),
                    new SarColumn(new NumberColumn("HOTEL_ID"),null,null,null,null),
                    new SarColumn(new NumberColumn("ROOMCATEGORY_ID"),null,null,null,null),
                }
            ),
        };
    }
    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public String getComment() {
        return null;
    }

}
