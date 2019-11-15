package sparksoniq.jsoniq.runtime.iterator.functions.datetime;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.CastException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.exceptions.UnknownFunctionCallException;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.AtomicTypes;


import java.util.List;

public class DateTimeFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private StringItem _dateTimeStringItem = null;

    public DateTimeFunctionIterator(
            List<RuntimeIterator> arguments,
            IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            try {
                return ItemFactory.getInstance().createDateTimeItem(_dateTimeStringItem.getStringValue());
            } catch (UnsupportedOperationException | IllegalArgumentException e) {
                String message = String.format("\"%s\": value of type %s is not castable to type %s",
                        _dateTimeStringItem.serialize(), "string", "dateTime");
                throw new CastException(message, getMetadata());
            }
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " dateTime function",
                    getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        try {
            _dateTimeStringItem = this.getSingleItemOfTypeFromIterator(
                    this._children.get(0),
                    StringItem.class,
                    new UnknownFunctionCallException("dateTime", this._children.size(), getMetadata()));
        } catch (UnknownFunctionCallException e) {
            throw new UnexpectedTypeException(" Sequence of more than one item can not be cast to type with quantifier '1' or '?'", getMetadata());
        }
        this._hasNext = _dateTimeStringItem != null;
    }
}