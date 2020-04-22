package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.TreatException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.types.SequenceType;

public class TreatAsClosure implements Function<Item, Boolean> {
    private SequenceType sequenceType;
    private ExceptionMetadata metadata;
    private boolean shouldThrowTreatException;
    private static final long serialVersionUID = 1L;

    public TreatAsClosure(SequenceType sequenceType, boolean shouldThrowTreatException, ExceptionMetadata metadata) {
        this.sequenceType = sequenceType;
        this.shouldThrowTreatException = shouldThrowTreatException;
        this.metadata = metadata;
    }

    @Override
    public Boolean call(Item input) throws Exception {
        if (!input.isTypeOf(this.sequenceType.getItemType())) {
            String message = input.getDynamicType().toString()
                + " cannot be treated as type "
                + this.sequenceType.getItemType().toString()
                + this.sequenceType.getArity().getSymbol();
            throw this.shouldThrowTreatException
                ? new TreatException(message, this.metadata)
                : new UnexpectedTypeException(message, this.metadata);
        }
        if (this.sequenceType.isEmptySequence()) {
            String message = input.getDynamicType().toString()
                + " cannot be treated as type empty-sequence()";
            throw this.shouldThrowTreatException
                ? new TreatException(message, this.metadata)
                : new UnexpectedTypeException(message, this.metadata);
        }
        return true;
    }
}
