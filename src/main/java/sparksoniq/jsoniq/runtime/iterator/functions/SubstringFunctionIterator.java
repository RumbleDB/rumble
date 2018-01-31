package sparksoniq.jsoniq.runtime.iterator.functions;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.List;

public class SubstringFunctionIterator extends LocalFunctionCallIterator {
    public SubstringFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
        if (arguments.size() < 2 || arguments.size() > 3)
            throw new SparksoniqRuntimeException("Incorrect number of arguments for substring function");
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            String result;
            StringItem stringItem = this.getSingleItemOfTypeFromIterator(this._children.get(0), StringItem.class);
            IntegerItem indexItem = this.getSingleItemOfTypeFromIterator(this._children.get(1), IntegerItem.class);
            if (this._children.size() > 2) {
                IntegerItem endIndexItem = this.getSingleItemOfTypeFromIterator(this._children.get(2), IntegerItem.class);
                result = stringItem.getStringValue().substring(indexItem.getIntegerValue() - 1,
                        indexItem.getIntegerValue() - 1 + endIndexItem.getIntegerValue());
            } else {
                result = stringItem.getStringValue().substring(indexItem.getIntegerValue() - 1);
            }

            this._hasNext = false;
            return new StringItem(result, ItemMetadata.fromIteratorMetadata(getMetadata()));
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " substring function", getMetadata());
    }
}
