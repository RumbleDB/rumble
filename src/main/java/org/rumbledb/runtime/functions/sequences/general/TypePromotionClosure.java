package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

public class TypePromotionClosure implements Function<Item, Item> {
    private String exceptionMessage;
    private SequenceType sequenceType;
    private ExceptionMetadata metadata;

    public TypePromotionClosure(String exceptionMessage, SequenceType sequenceType, ExceptionMetadata metadata) {
        this.exceptionMessage = exceptionMessage;
        this.sequenceType = sequenceType;
        this.metadata = metadata;
    }

    @Override
    public Item call(Item input) throws Exception {
        if (input != null && !input.isTypeOf(this.sequenceType.getItemType())) {
            if (input.canBePromotedTo(this.sequenceType.getItemType())) {
                return input.promoteTo(this.sequenceType.getItemType());
            }
            throw new UnexpectedTypeException(
                    this.exceptionMessage
                        +
                        ItemType.convertClassNameToItemTypeName(input.getClass().getSimpleName())
                        + " cannot be promoted to type "
                        + ItemType.convertClassNameToItemTypeName(this.sequenceType.getItemType().toString())
                        + this.sequenceType.getArity().getSymbol(),
                    this.metadata
            );
        }
        return input;
    }
}
