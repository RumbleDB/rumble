package sparksoniq.jsoniq.runtime.iterator.functions;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.List;

public class StringJoinFunction extends LocalFunctionCallIterator {
    public StringJoinFunction(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
        if (arguments.size() != 2)
            throw new SparksoniqRuntimeException("Incorrect number of arguments for string-join function; " +
                    "Exactly 2 arguments are required.");
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            List<Item> strings = getItemsFromIteratorWithCurrentContext(this._children.get(0));
            StringItem joinString = getSingleItemOfTypeFromIterator(this._children.get(1), StringItem.class);
            StringBuilder stringBuilder = new StringBuilder("");
            for (Item item : strings) {
                if (!(item instanceof StringItem))
                    throw new UnexpectedTypeException("String item expected", this._children.get(0).getMetadata());
                stringBuilder = !stringBuilder.toString().isEmpty() ? stringBuilder.append(joinString.getStringValue()) : stringBuilder;
                stringBuilder = stringBuilder.append(((StringItem) item).getStringValue());
            }
            this._hasNext = false;
            return new StringItem(stringBuilder.toString(), ItemMetadata.fromIteratorMetadata(getMetadata()));
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " string-join function", getMetadata());
    }
}
