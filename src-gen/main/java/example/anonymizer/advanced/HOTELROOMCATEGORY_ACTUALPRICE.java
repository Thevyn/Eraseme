// Generated with g9 DBmasker.

package example.anonymizer.advanced;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IInput;
import no.esito.anonymizer.ITransformation;
import no.esito.anonymizer.column.NumberColumn;
import no.esito.anonymizer.core.AbstractMasking;
import no.esito.anonymizer.mask.MaskRandomDecimal;

public class HOTELROOMCATEGORY_ACTUALPRICE extends AbstractMasking {

    @Override
    public IColumn getColumn() {
        return new NumberColumn("ACTUALPRICE");
    }

    @Override
    public IInput[] getInputs() {
        return new IInput[] {
            new MaskRandomDecimal("499.00","2599.00"),
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
        return false;
    }
}
