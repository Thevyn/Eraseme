// Generated with g9 DBmasker.

package example.anonymizer.advanced;

import no.esito.anonymizer.IColumn;
import no.esito.anonymizer.IInput;
import no.esito.anonymizer.ITransformation;
import no.esito.anonymizer.column.TextColumn;
import no.esito.anonymizer.core.AbstractMasking;
import no.esito.anonymizer.mask.MaskColumn;

public class ADDRESS_HOMEADDRESS extends AbstractMasking {

    @Override
    public IColumn getColumn() {
        return new TextColumn("HOMEADDRESS");
    }

    @Override
    public IInput[] getInputs() {
        return new IInput[] {
            new MaskColumn(new TextColumn("HOMEADDRESS"),null),
        };
    }

    @Override
    public ITransformation getTransformation() {
        return null;
    }

    @Override
    public String getFormat() {
        return "%s";
    }

    @Override
    public boolean isUnique() {
        return false;
    }

    @Override
    public String getMappingFile() {
        return "address_map.txt";
    }

    @Override
    public MappingFileUsage getMappingFileUsage() {
        return MappingFileUsage.INPUT;
    }
}
