// Generated with g9 DBmasker.

package example.anonymizer.advanced;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.column.NumberColumn;
import no.esito.anonymizer.core.AbstractPermutation;

public class ROOM_ID extends AbstractPermutation {

    @Override
    public IColumn getColumn() {
        return new NumberColumn("ID");
    }

    @Override
    public String getTempId() {
        return "555";
    }
}
