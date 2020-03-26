// Generated with g9 DBmasker.

package example.anonymizer.anonymize;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IInput;
import no.esito.anonymizer.ITransformation;
import no.esito.anonymizer.column.TextColumn;
import no.esito.anonymizer.core.AbstractMasking;
import no.esito.anonymizer.mask.MaskColumn;
import no.esito.anonymizer.mask.MaskFileRandom;
import no.esito.anonymizer.transformations.Email;

/**
 * Create email based on the newly created name
 */
public class CUSTOMER_EMAIL extends AbstractMasking {

    @Override
    public IColumn getColumn() {
        return new TextColumn("EMAIL");
    }

    @Override
    public IInput[] getInputs() {
        return new IInput[] {
            new MaskColumn(new TextColumn("NAME"),null),
            new MaskFileRandom("email.txt",null),
        };
    }

    @Override
    public ITransformation getTransformation() {
        return new Email();
    }

    @Override
    public String getFormat() {
        return "%s@%s";
    }

    @Override
    public boolean isUnique() {
        return true;
    }
}
