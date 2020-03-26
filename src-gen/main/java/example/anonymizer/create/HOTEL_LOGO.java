// Generated with g9 DBmasker.

package example.anonymizer.create;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IInput;
import no.esito.anonymizer.ITransformation;
import no.esito.anonymizer.column.TextColumn;
import no.esito.anonymizer.core.AbstractMasking;
import no.esito.anonymizer.mask.MaskFileRandom;

public class HOTEL_LOGO extends AbstractMasking {

    @Override
    public IColumn getColumn() {
        return new TextColumn("LOGO");
    }

    @Override
    public IInput[] getInputs() {
        return new IInput[] {
            new MaskFileRandom("logo.txt",null),
        };
    }

    @Override
    public ITransformation getTransformation() {
        return null;
    }

    @Override
    public String getFormat() {
        return "%s.png";
    }

    @Override
    public boolean isUnique() {
        return false;
    }
}
