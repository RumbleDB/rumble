package sparksoniq.jsoniq.runtime.iterator.functions.durations;

import org.joda.time.Period;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.CastException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnknownFunctionCallException;
import sparksoniq.jsoniq.item.DurationItem;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.AtomicTypes;


import java.util.List;

public class DurationFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;

    public DurationFunctionIterator(
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
                throw new UnknownFunctionCallException("duration", 0, getMetadata());
            }

            try {
                Period period = DurationItem.getDurationFromString(stringItem.getStringValue(), AtomicTypes.DurationItem);
                return ItemFactory.getInstance().createDurationItem(period);
            } catch (IllegalArgumentException e) {
                String message = String.format("\"%s\": value of type %s is not castable to type %s",
                        stringItem.serialize(), "string", "duration");
                throw new CastException(message, getMetadata());
            }
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " duration function",
                    getMetadata());
    }
}