package sparksoniq.jsoniq.runtime.iterator.functions.datetime.components;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.exceptions.UnknownFunctionCallException;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.item.TimeItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class MinutesFromTimeFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private TimeItem _dateTimeItem = null;

    public MinutesFromTimeFunctionIterator(
            List<RuntimeIterator> arguments,
            IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            return ItemFactory.getInstance().createIntegerItem(_dateTimeItem.getTimeValue().getMinuteOfHour());
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " minutes-from-time function",
                    getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        try {
            _dateTimeItem = this.getSingleItemOfTypeFromIterator(
                    this._children.get(0),
                    TimeItem.class,
                    new UnknownFunctionCallException("minutes-from-time", this._children.size(), getMetadata()));
        } catch (UnexpectedTypeException e) {
            throw new UnexpectedTypeException(e.getJSONiqErrorMessage() + "? of function minutes-from-time()", this._children.get(0).getMetadata());
        } catch (UnknownFunctionCallException e) {
            throw new UnexpectedTypeException(" Sequence of more than one item can not be promoted to parameter type time? of function minutes-from-time()", getMetadata());
        }
        this._hasNext = _dateTimeItem != null;
    }
}
