// Generated with g9 DBmasker.

package example.anonymizer.anonymize;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IConversion;
import no.esito.anonymizer.column.NumberColumn;
import no.esito.anonymizer.conversions.String2Decimal;
import no.esito.anonymizer.core.AbstractRandomization;
import no.esito.anonymizer.noise.AbstractNoise;
import no.esito.anonymizer.noise.NoiseDecimal;

/**
 * Add 1% gaussian noise to hide the value from search
 */
public class ROOMCATEGORY_INITIALPRICE extends AbstractRandomization {


    @Override
    public IColumn getColumn() {
        return new NumberColumn("INITIALPRICE");
    }

    @Override
    public String getFormat() {
        return "%.2f";
    }

    @Override
    public IConversion getConversion() {
        return new String2Decimal();
    }

    @Override
    public AbstractNoise getNoise() {
        return new NoiseDecimal(0.0,0.0,1.0);
    }
}
