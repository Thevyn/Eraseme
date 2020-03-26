// Generated with g9 DBmasker.

package example.anonymizer.advanced;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.column.TextColumn;
import no.esito.anonymizer.core.AbstractPermutation;

/**
 * Output results to an encrypted file
 */
public class HOTEL_NAME extends AbstractPermutation {

    @Override
    public IColumn getColumn() {
        return new TextColumn("NAME");
    }

    @Override
    public String getMappingFile() {
        return "hotelmap.txt";
    }

    @Override
    public MappingFileUsage getMappingFileUsage() {
        return MappingFileUsage.OUTPUT;
    }

    @Override
    public boolean useEncryption() {
        return true;
    }
}
