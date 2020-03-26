// Generated with g9 DBmasker.

package example.anonymizer.advanced;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IConversion;
import no.esito.anonymizer.column.DateTimeColumn;
import no.esito.anonymizer.conversions.String2DateTime;
import no.esito.anonymizer.core.AbstractRandomization;
import no.esito.anonymizer.noise.AbstractNoise;
import no.esito.anonymizer.noise.NoiseDateTime;

public class BOOKING_BOOKINGCREATED extends AbstractRandomization {


    @Override
    public IColumn getColumn() {
        return new DateTimeColumn("BOOKINGCREATED");
    }

    @Override
    public String getFormat() {
        return "%1$tF %1$tT";
    }

    @Override
    public IConversion getConversion() {
        return new String2DateTime();
    }

    @Override
    public AbstractNoise getNoise() {
        return new NoiseDateTime(10.0,0.0,0.0);
    }
}
