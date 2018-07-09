package sparksoniq.jsoniq.runtime.iterator.functions;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.DoubleItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.EmptySequenceIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.List;
import java.lang.Math;

public class CosFunctionIterator extends LocalFunctionCallIterator {
    public CosFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            RuntimeIterator iterator = this._children.get(0);
            //TODO refactor empty items
            if (iterator.getClass() == EmptySequenceIterator.class) {
                return null;
            }
            else {
                Item radians = this.getSingleItemOfTypeFromIterator(iterator, Item.class);
                if (Item.isNumeric(radians)) {
                    Double result = Math.cos(Item.getNumericValue(radians, Double.class));
                    this._hasNext = false;
                    return new DoubleItem(result,
                            ItemMetadata.fromIteratorMetadata(getMetadata()));
                }
                else {
                    throw new UnexpectedTypeException("Cos expression has non numeric args " +
                            radians.serialize(), getMetadata());
                }
            }
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " cos function", getMetadata());
    }


}
