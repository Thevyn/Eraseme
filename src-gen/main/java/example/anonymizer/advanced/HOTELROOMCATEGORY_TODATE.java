// Generated with g9 DBmasker.

package example.anonymizer.advanced;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IInput;
import no.esito.anonymizer.ITransformation;
import no.esito.anonymizer.column.DateColumn;
import no.esito.anonymizer.core.AbstractMasking;
import no.esito.anonymizer.mask.MaskColumn;

/**
 * Set todate=fromdate
 */
public class HOTELROOMCATEGORY_TODATE extends AbstractMasking {

    @Override
    public IColumn getColumn() {
        return new DateColumn("TODATE");
    }

    @Override
    public IInput[] getInputs() {
        return new IInput[] {
            new MaskColumn(new DateColumn("FROMDATE"),null),
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
