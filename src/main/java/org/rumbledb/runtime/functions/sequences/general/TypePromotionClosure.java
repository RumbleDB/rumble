package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.types.SequenceType;

public class TypePromotionClosure implements Function<Item, Item> {
    private String exceptionMessage;
    private SequenceType sequenceType;
    private ExceptionMetadata metadata;
    private static final long serialVersionUID = 1L;

    public TypePromotionClosure(String exceptionMessage, SequenceType sequenceType, ExceptionMetadata metadata) {
        this.exceptionMessage = exceptionMessage;
        this.sequenceType = sequenceType;
        this.metadata = metadata;
    }

    @Override
    public Item call(Item input) throws Exception {
        if (input != null && !InstanceOfIterator.doesItemTypeMatchItem(this.sequenceType.getItemType(), input)) {
            if (input.getDynamicType().canBePromotedTo(this.sequenceType.getItemType())) {
                Item result = CastIterator.castItemToType(input, this.sequenceType.getItemType(), this.metadata);
                if (result == null) {
                    throw new OurBadException(
                            "We were not able to promote " + input + " to type " + this.sequenceType.getItemType()
                    );
                }
                return result;
            }
            throw new UnexpectedTypeException(
                    this.exceptionMessage
                        +
                        input.getDynamicType().toString()
                        + " cannot be promoted to type "
                        + this.sequenceType.getItemType().toString()
                        + this.sequenceType.getArity().getSymbol(),
                    this.metadata
            );
        }
        return input;
    }
}
