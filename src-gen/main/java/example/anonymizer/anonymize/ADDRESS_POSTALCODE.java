// Generated with g9 DBmasker.

package example.anonymizer.anonymize;

import example.anonymizer.conversions.ParseDigits;
import example.anonymizer.transformations.PostCodeGeneralization;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IInput;
import no.esito.anonymizer.ITransformation;
import no.esito.anonymizer.column.NumberColumn;
import no.esito.anonymizer.core.AbstractMasking;
import no.esito.anonymizer.mask.MaskColumn;

public class ADDRESS_POSTALCODE extends AbstractMasking {

    @Override
    public IColumn getColumn() {
        return new NumberColumn("POSTALCODE");
    }

    @Override
    public IInput[] getInputs() {
        return new IInput[] {
            new MaskColumn(new NumberColumn("POSTALCODE"),new ParseDigits()),
        };
    }

    @Override
    public ITransformation getTransformation() {
        return new PostCodeGeneralization();
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
