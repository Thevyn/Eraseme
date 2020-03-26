// Generated with g9 DBmasker.

package example.anonymizer.create;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IInput;
import no.esito.anonymizer.ITransformation;
import no.esito.anonymizer.column.TextColumn;
import no.esito.anonymizer.core.AbstractMasking;
import no.esito.anonymizer.mask.MaskFileRandom;

public class HOTEL_LOCATION extends AbstractMasking {

    @Override
    public IColumn getColumn() {
        return new TextColumn("LOCATION");
    }

    @Override
    public IInput[] getInputs() {
        return new IInput[] {
            new MaskFileRandom("town.txt",null),
        };
    }

    @Override
    public ITransformation getTransformation() {
        return null;
    }

    @Override
    public String getFormat() {
        return "%sstown";
    }

    @Override
    public boolean isUnique() {
        return false;
    }
}
