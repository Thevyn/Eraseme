// Generated with g9 DBmasker.

package example.anonymizer.create;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IInput;
import no.esito.anonymizer.ITransformation;
import no.esito.anonymizer.column.NumberColumn;
import no.esito.anonymizer.core.AbstractMasking;
import no.esito.anonymizer.mask.MaskRandomInteger;

public class ROOM_ROOMNO extends AbstractMasking {

    @Override
    public IColumn getColumn() {
        return new NumberColumn("ROOMNO");
    }

    @Override
    public IInput[] getInputs() {
        return new IInput[] {
            new MaskRandomInteger("101","399"),
        };
    }

    @Override
    public ITransformation getTransformation() {
        return null;
    }

    @Override
    public String getFormat() {
        return "%d";
    }

    @Override
    public boolean isUnique() {
        return false;
    }
}
