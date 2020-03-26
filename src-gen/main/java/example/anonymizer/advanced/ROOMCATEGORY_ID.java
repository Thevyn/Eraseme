// Generated with g9 DBmasker.

package example.anonymizer.advanced;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IInput;
import no.esito.anonymizer.ITransformation;
import no.esito.anonymizer.column.NumberColumn;
import no.esito.anonymizer.core.AbstractMasking;
import no.esito.anonymizer.core.PropagateUpdate;
import no.esito.anonymizer.mask.MaskSequence;

public class ROOMCATEGORY_ID extends AbstractMasking {

    @Override
    public PropagateUpdate[] getPropagatedUpdates() {
        return new PropagateUpdate[] {
            new PropagateUpdate("HOTELROOMCATEGORY", "ROOMCATEGORY_ID", "ROOMCATEGORY_ID"),
            new PropagateUpdate("BOOKING", "ROOMCATEGORY_ID", "ROOMCATEGORY_ID"),
            new PropagateUpdate("ROOM", "CATEGORY_ID", "CATEGORY_ID"),
        };
    }

    @Override
    public IColumn getColumn() {
        return new NumberColumn("ID");
    }

    @Override
    public IInput[] getInputs() {
        return new IInput[] {
            new MaskSequence(-1, 1),
        };
    }

    @Override
    public ITransformation getTransformation() {
        return null;
    }

    @Override
    public String getFormat() {
        return "%s";
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getTempId() {
        return "999999";
    }
}
