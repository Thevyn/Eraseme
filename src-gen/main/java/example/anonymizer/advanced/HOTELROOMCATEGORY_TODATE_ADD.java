// Generated with g9 DBmasker.

package example.anonymizer.advanced;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IConversion;
import no.esito.anonymizer.column.DateColumn;
import no.esito.anonymizer.conversions.String2Date;
import no.esito.anonymizer.core.AbstractRandomization;
import no.esito.anonymizer.noise.AbstractNoise;
import no.esito.anonymizer.noise.NoiseDate;

/**
 * Add a lot of days
 */
public class HOTELROOMCATEGORY_TODATE_ADD extends AbstractRandomization {


    @Override
    public IColumn getColumn() {
        return new DateColumn("TODATE");
    }

    @Override
    public String getFormat() {
        return "%tF";
    }

    @Override
    public IConversion getConversion() {
        return new String2Date();
    }

    @Override
    public AbstractNoise getNoise() {
        return new NoiseDate(365.0,10.0,0.0);
    }
}
