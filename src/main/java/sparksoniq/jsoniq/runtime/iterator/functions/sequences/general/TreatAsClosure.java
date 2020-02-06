package sparksoniq.jsoniq.runtime.iterator.functions.sequences.general;

import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.TreatException;

import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;

public class TreatAsClosure implements Function<Item, Boolean> {
    private SequenceType sequenceType;
    private ExceptionMetadata metadata;

    public TreatAsClosure(SequenceType sequenceType, ExceptionMetadata metadata) {
        this.sequenceType = sequenceType;
        this.metadata = metadata;
    }

    @Override
    public Boolean call(Item input) throws Exception {
        if (!input.isTypeOf(sequenceType.getItemType()))
            throw new TreatException(
                    " "
                        + ItemTypes.getItemTypeName(input.getClass().getSimpleName())
                        + " cannot be treated as type "
                        + ItemTypes.getItemTypeName(sequenceType.getItemType().getType().toString())
                        + sequenceType.getArity().getSymbol(),
                    metadata
            );
        if (sequenceType.isEmptySequence())
            throw new TreatException(
                    ItemTypes.getItemTypeName(input.getClass().getSimpleName())
                        + " cannot be treated as type empty-sequence()",
                    metadata
            );
        return true;
    }
}
