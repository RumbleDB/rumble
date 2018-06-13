package sparksoniq.jsoniq.runtime.iterator.functions;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.DoubleItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
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
            Item radians = this.getSingleItemOfTypeFromIterator(this._children.get(0), Item.class);
            Double result = Math.cos(Item.getNumericValue(radians, Double.class));
            this._hasNext = false;
            return new DoubleItem(result,
                    ItemMetadata.fromIteratorMetadata(getMetadata()));
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " count function", getMetadata());
    }


}
