// Generated with g9 DBmasker.

package example.anonymizer.advanced;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IInput;
import no.esito.anonymizer.ITransformation;
import no.esito.anonymizer.column.DateColumn;
import no.esito.anonymizer.core.AbstractMasking;
import no.esito.anonymizer.mask.MaskRandomDate;

/**
 * Update a column in multi column PK
 */
public class HOTELROOMCATEGORY_FROMDATE extends AbstractMasking {

    @Override
    public IColumn getColumn() {
        return new DateColumn("FROMDATE");
    }

    @Override
    public IInput[] getInputs() {
        return new IInput[] {
            new MaskRandomDate("2020-01-01","2022-12-31"),
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

    @Override
    public String getTempId() {
        return "1900-01-01";
    }
}
