package sparksoniq.jsoniq.runtime.iterator.functions.durations.components;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.exceptions.UnknownFunctionCallException;
import sparksoniq.jsoniq.item.DurationItem;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class HoursFromDurationFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private DurationItem _durationItem = null;

    public HoursFromDurationFunctionIterator(
            List<RuntimeIterator> arguments,
            IteratorMetadata iteratorMetadata
    ) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            return ItemFactory.getInstance().createIntegerItem(_durationItem.getDurationValue().getHours());
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " hours-from-duration function",
                    getMetadata()
            );
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        try {
            _durationItem = this.getSingleItemOfTypeFromIterator(
                this._children.get(0),
                DurationItem.class,
                new UnknownFunctionCallException("hours-from-duration", this._children.size(), getMetadata())
            );
        } catch (UnexpectedTypeException e) {
            throw new UnexpectedTypeException(
                    e.getJSONiqErrorMessage() + "? of function hours-from-duration()",
                    this._children.get(0).getMetadata()
            );
        } catch (UnknownFunctionCallException e) {
            throw new UnexpectedTypeException(
                    " Sequence of more than one item can not be promoted to parameter type duration? of function hours-from-duration()",
                    getMetadata()
            );
        }
        this._hasNext = _durationItem != null;
    }
}
