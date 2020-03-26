// Generated with g9 DBmasker.

package example.anonymizer.anonymize;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IInput;
import no.esito.anonymizer.ITransformation;
import no.esito.anonymizer.column.TextColumn;
import no.esito.anonymizer.core.AbstractMasking;
import no.esito.anonymizer.mask.MaskRandomInteger;
import no.esito.anonymizer.transformations.CreditCard;

/**
 * Create random creditcard with checksum that validates
 */
public class CUSTOMER_CREDITCARD extends AbstractMasking {

    @Override
    public IColumn getColumn() {
        return new TextColumn("CREDITCARD");
    }

    @Override
    public IInput[] getInputs() {
        return new IInput[] {
            new MaskRandomInteger("10001000","99919991"),
        };
    }

    @Override
    public ITransformation getTransformation() {
        return new CreditCard();
    }

    @Override
    public String getFormat() {
        return "41428340%d";
    }

    @Override
    public boolean isUnique() {
        return false;
    }
}
