// Generated with g9 DBmasker.

package example.anonymizer.forgetme;

import no.esito.anonymizer.IAnonymization;
import no.esito.anonymizer.IChildRelation;
import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.column.NumberColumn;
import no.esito.anonymizer.column.TextColumn;
import no.esito.anonymizer.core.AbstractEraseTask;
import no.esito.anonymizer.core.CascadeErase;
import no.esito.anonymizer.core.EraseColumn;

public class Erase_CUSTOMER extends AbstractEraseTask {

    @Override
    public String getName() {
        return "Erase_CUSTOMER";
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
            new EraseColumn(new TextColumn("NAME"),"firstname lastname",null),
            new EraseColumn(new TextColumn("EMAIL"),"epost@email.com",null),
        };
    }

    @Override
    public IChildRelation[] getChildren() {
        return new IChildRelation[] {
            // Anonymize identifiable columns
            new CascadeErase("ADDRESS", new IColumn[] {
                    new NumberColumn("CUSTOMERNO"),
                }
                , new IChildRelation[] {
                }, new IColumn[] {
                    new NumberColumn("CUSTOMER_CUSTOMERNO"),
                }, new IAnonymization[] {
                    new EraseColumn(new TextColumn("HOMEADDRESS"),"Home address",null),
                    new EraseColumn(new NumberColumn("POSTALCODE"),"1234",null),
                }
            ),
        };
    }
}
