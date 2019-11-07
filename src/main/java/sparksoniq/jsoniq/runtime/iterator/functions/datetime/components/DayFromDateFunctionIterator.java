package sparksoniq.jsoniq.runtime.iterator.functions.datetime.components;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.exceptions.UnknownFunctionCallException;
import sparksoniq.jsoniq.item.DateItem;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class DayFromDateFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private DateItem _dateItem = null;

    public DayFromDateFunctionIterator(
            List<RuntimeIterator> arguments,
            IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            return ItemFactory.getInstance().createIntegerItem(_dateItem.getDateValue().getDayOfMonth());
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " day-from-date function",
                    getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        try {
            _dateItem = this.getSingleItemOfTypeFromIterator(
                    this._children.get(0),
                    DateItem.class,
                    new UnknownFunctionCallException("day-from-date", this._children.size(), getMetadata()));
        } catch (UnexpectedTypeException e) {
            throw new UnexpectedTypeException(e.getJSONiqErrorMessage() + "? of function day-from-date()", this._children.get(0).getMetadata());
        } catch (UnknownFunctionCallException e) {
            throw new UnexpectedTypeException(" Sequence of more than one item can not be promoted to parameter type date? of function day-from-date()", getMetadata());
        }
        this._hasNext = _dateItem != null;
    }
}
