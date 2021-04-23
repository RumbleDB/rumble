package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.TreatException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.types.SequenceType;

public class TreatAsClosure implements Function<Item, Boolean> {
    private SequenceType sequenceType;
    private ExceptionMetadata metadata;
    private ErrorCode errorCode;
    private static final long serialVersionUID = 1L;

    public TreatAsClosure(SequenceType sequenceType, ErrorCode shouldThrowTreatException, ExceptionMetadata metadata) {
        this.sequenceType = sequenceType;
        this.errorCode = shouldThrowTreatException;
        this.metadata = metadata;
    }

    @Override
    public Boolean call(Item input) throws Exception {
        if (!InstanceOfIterator.doesItemTypeMatchItem(this.sequenceType.getItemType(), input)) {
            switch (this.errorCode) {
                case DynamicTypeTreatErrorCode:
                    throw new TreatException(
                            input.getDynamicType().toString()
                                + " cannot be treated as type "
                                + this.sequenceType.getItemType().toString()
                                + this.sequenceType.getArity().getSymbol(),
                            this.metadata
                    );
                case UnexpectedTypeErrorCode:
                    throw new UnexpectedTypeException(
                            input.getDynamicType().toString()
                                + " is not expected here. The expected type is "
                                + this.sequenceType.getItemType().toString()
                                + this.sequenceType.getArity().getSymbol(),
                            this.metadata
                    );
                default:
                    throw new OurBadException("Unexpected error code in treat as iterator.", this.metadata);
            }
        }
        if (this.sequenceType.isEmptySequence()) {
            switch (this.errorCode) {
                case DynamicTypeTreatErrorCode:
                    throw new TreatException(
                            input.getDynamicType().toString()
                                + " cannot be treated as type empty-sequence()",
                            this.metadata
                    );
                case UnexpectedTypeErrorCode:
                    throw new UnexpectedTypeException(
                            input.getDynamicType().toString()
                                + " is not expected here. The expected type is empty-sequence()",
                            this.metadata
                    );
                default:
                    throw new OurBadException("Unexpected error code in treat as iterator.", this.metadata);
            }
        }
        return true;
    }
}
