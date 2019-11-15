package sparksoniq.jsoniq.runtime.iterator.functions.datetime.components;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.exceptions.UnknownFunctionCallException;
import sparksoniq.jsoniq.item.DateTimeItem;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.math.BigDecimal;
import java.util.List;

public class SecondsFromDateTimeFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private DateTimeItem _dateTimeItem = null;

    public SecondsFromDateTimeFunctionIterator(
            List<RuntimeIterator> arguments,
            IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            return ItemFactory.getInstance().createDecimalItem(BigDecimal.valueOf(_dateTimeItem.getDateTimeValue().getSecondOfMinute() + _dateTimeItem.getDateTimeValue().getMillisOfSecond()*1.0/1000));
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " seconds-from-dateTime function",
                    getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        try {
            _dateTimeItem = this.getSingleItemOfTypeFromIterator(
                    this._children.get(0),
                    DateTimeItem.class,
                    new UnknownFunctionCallException("seconds-from-dateTime", this._children.size(), getMetadata()));
        } catch (UnexpectedTypeException e) {
            throw new UnexpectedTypeException(e.getJSONiqErrorMessage() + "? of function seconds-from-dateTime()", this._children.get(0).getMetadata());
        } catch (UnknownFunctionCallException e) {
            throw new UnexpectedTypeException(" Sequence of more than one item can not be promoted to parameter type dateTime? of function seconds-from-dateTime()", getMetadata());
        }
        this._hasNext = _dateTimeItem != null;
    }
}
