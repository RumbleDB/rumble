package sparksoniq.jsoniq.runtime.iterator.functions.strings;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class StringJoinFunction extends LocalFunctionCallIterator {
    public StringJoinFunction(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            this._hasNext = false;
            return result;
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " string-join function", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException("Runtime iterator cannot be opened twice", getMetadata());
        this._isOpen = true;
        this._currentDynamicContext = context;

        StringItem joinString = new StringItem("", new ItemMetadata(getMetadata().getExpressionMetadata()));
        List<Item> strings = getItemsFromIteratorWithCurrentContext(this._children.get(0));
        if (this._children.size() > 1)
            joinString = getSingleItemOfTypeFromIterator(this._children.get(1), StringItem.class);
        StringBuilder stringBuilder = new StringBuilder("");
        for (Item item : strings) {
            if (!(item instanceof StringItem))
                throw new UnexpectedTypeException("String item expected", this._children.get(0).getMetadata());
            stringBuilder = !stringBuilder.toString().isEmpty() ? stringBuilder.append(joinString.getStringValue()) : stringBuilder;
            stringBuilder = stringBuilder.append(((StringItem) item).getStringValue());
        }
        this.result = new StringItem(stringBuilder.toString(), ItemMetadata.fromIteratorMetadata(getMetadata()));
        this._hasNext = true;
    }

    private Item result;

}
