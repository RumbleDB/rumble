package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.TreatException;
import org.rumbledb.types.ItemTypes;
import org.rumbledb.types.SequenceType;

public class TreatAsClosure implements Function<Item, Boolean> {
    private SequenceType sequenceType;
    private ExceptionMetadata metadata;

    public TreatAsClosure(SequenceType sequenceType, ExceptionMetadata metadata) {
        this.sequenceType = sequenceType;
        this.metadata = metadata;
    }

    @Override
    public Boolean call(Item input) throws Exception {
        if (!input.isTypeOf(this.sequenceType.getItemType())) {
            throw new TreatException(
                    " "
                        + ItemTypes.getItemTypeName(input.getClass().getSimpleName())
                        + " cannot be treated as type "
                        + ItemTypes.getItemTypeName(this.sequenceType.getItemType().toString())
                        + this.sequenceType.getArity().getSymbol(),
                    this.metadata
            );
        }
        if (this.sequenceType.isEmptySequence()) {
            throw new TreatException(
                    ItemTypes.getItemTypeName(input.getClass().getSimpleName())
                        + " cannot be treated as type empty-sequence()",
                    this.metadata
            );
        }
        return true;
    }
}
