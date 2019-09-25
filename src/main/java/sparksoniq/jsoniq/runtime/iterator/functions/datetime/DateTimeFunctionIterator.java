package sparksoniq.jsoniq.runtime.iterator.functions.datetime;

import org.joda.time.DateTime;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.CastException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnknownFunctionCallException;
import sparksoniq.jsoniq.item.DateTimeItem;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;


import java.util.List;

public class DateTimeFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;

    public DateTimeFunctionIterator(
            List<RuntimeIterator> arguments,
            IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;

            StringItem stringItem = this.getSingleItemOfTypeFromIterator(
                    this._children.get(0),
                    StringItem.class);

            if (stringItem == null) {
                throw new UnknownFunctionCallException("dateTime", 0, getMetadata());
            }

            try {
                DateTime dateTime = DateTimeItem.getDateTimeFromString(stringItem.getStringValue());
                return ItemFactory.getInstance().createDateTimeItem(dateTime);
            } catch (IllegalArgumentException e) {
                String message = String.format("\"%s\": value of type %s is not castable to type %s",
                        stringItem.serialize(), "string", "dateTime");
                throw new CastException(message, getMetadata());
            }
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " dateTime function",
                    getMetadata());
    }
}
