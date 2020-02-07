package sparksoniq.jsoniq.runtime.iterator.functions.sequences.general;

import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;

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
                        ItemTypes.getItemTypeName(input.getClass().getSimpleName())
                        + " cannot be promoted to type "
                        + ItemTypes.getItemTypeName(this.sequenceType.getItemType().getType().toString())
                        + this.sequenceType.getArity().getSymbol(),
                    this.metadata
            );
        }
        return input;
    }
}
