package sparksoniq.jsoniq.runtime.iterator.functions.sequences.general;

import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;

public class TypePromotionClosure implements Function<Item, Item> {
    private String _exceptionMessage;
    private SequenceType sequenceType;
    private IteratorMetadata metadata;

    public TypePromotionClosure(String exceptionMessage, SequenceType sequenceType, IteratorMetadata metadata) {
        this._exceptionMessage = exceptionMessage;
        this.sequenceType = sequenceType;
        this.metadata = metadata;
    }

    @Override
    public Item call(Item input) throws Exception {
        if (input != null && !input.isTypeOf(sequenceType.getItemType())) {
            if (input.canBePromotedTo(sequenceType.getItemType())) {
                return input.promoteTo(sequenceType.getItemType());
            }
            throw new UnexpectedTypeException(
                    _exceptionMessage
                        +
                        ItemTypes.getItemTypeName(input.getClass().getSimpleName())
                        + " cannot be promoted to type "
                        + ItemTypes.getItemTypeName(sequenceType.getItemType().getType().toString())
                        + sequenceType.getArity().getSymbol(),
                    metadata
            );
        }
        return input;
    }
}
