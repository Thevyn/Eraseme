// Generated with g9 DBmasker.

package example.anonymizer.advanced;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IConversion;
import no.esito.anonymizer.column.TimeColumn;
import no.esito.anonymizer.conversions.String2Time;
import no.esito.anonymizer.core.AbstractRandomization;
import no.esito.anonymizer.noise.AbstractNoise;
import no.esito.anonymizer.noise.NoiseTime;

public class BOOKING_EARLIESTCHECKINTIME extends AbstractRandomization {


    @Override
    public IColumn getColumn() {
        return new TimeColumn("EARLIESTCHECKINTIME");
    }

    @Override
    public String getFormat() {
        return "%tT";
    }

    @Override
    public IConversion getConversion() {
        return new String2Time();
    }

    @Override
    public AbstractNoise getNoise() {
        return new NoiseTime(10.0,10.0,0.0);
    }
}
