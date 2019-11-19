package sparksoniq.jsoniq.runtime.iterator.functions.sequences.general;

import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.TreatException;
import sparksoniq.jsoniq.item.AtomicItem;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;

public class TypePromotionClosure implements Function<Item, Item> {
    private SequenceType sequenceType;
    private IteratorMetadata metadata;

    public TypePromotionClosure(SequenceType sequenceType, IteratorMetadata metadata) {
        this.sequenceType = sequenceType;
        this.metadata = metadata;
    }

    @Override
    public Item call(Item input) throws Exception {
        if (input != null && !input.isTypeOf(sequenceType.getItemType())) {
            if (input.canBePromotedTo(sequenceType.getItemType())) {
                return input.promoteTo(sequenceType.getItemType());
            }
            throw new TreatException(ItemTypes.getItemTypeName(input.getClass().getSimpleName()) + " cannot be treated as type "
                    + ItemTypes.getItemTypeName(sequenceType.getItemType().getType().toString()) + sequenceType.getArity().getSymbol(), metadata);
        }
        return input;
    }
}