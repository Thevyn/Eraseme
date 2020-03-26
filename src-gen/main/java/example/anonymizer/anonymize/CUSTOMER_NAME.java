// Generated with g9 DBmasker.

package example.anonymizer.anonymize;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IInput;
import no.esito.anonymizer.ITransformation;
import no.esito.anonymizer.column.TextColumn;
import no.esito.anonymizer.core.AbstractMasking;
import no.esito.anonymizer.mask.MaskFileRandom;

/**
 * Create random name from list of firstnames and lastnames
 */
public class CUSTOMER_NAME extends AbstractMasking {

    @Override
    public IColumn getColumn() {
        return new TextColumn("NAME");
    }

    @Override
    public IInput[] getInputs() {
        return new IInput[] {
            new MaskFileRandom("firstname.txt",null),
            new MaskFileRandom("lastname.txt",null),
        };
    }

    @Override
    public ITransformation getTransformation() {
        return null;
    }

    @Override
    public String getFormat() {
        return "%s %s";
    }

    @Override
    public boolean isUnique() {
        return false;
    }
}
