// Generated with g9 DBmasker.

package example.anonymizer.anonymize;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IInput;
import no.esito.anonymizer.ITransformation;
import no.esito.anonymizer.column.TextColumn;
import no.esito.anonymizer.core.AbstractMasking;
import no.esito.anonymizer.mask.MaskRandomInteger;

/**
 * Create random norwegian phone number
 */
public class CUSTOMER_PHONE extends AbstractMasking {

    @Override
    public IColumn getColumn() {
        return new TextColumn("PHONE");
    }

    @Override
    public IInput[] getInputs() {
        return new IInput[] {
            new MaskRandomInteger("10001000","99909990"),
        };
    }

    @Override
    public ITransformation getTransformation() {
        return null;
    }

    @Override
    public String getFormat() {
        return "+47 %d";
    }

    @Override
    public boolean isUnique() {
        return false;
    }
}
