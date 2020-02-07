package sparksoniq.jsoniq.runtime.iterator.functions.durations;

import org.joda.time.Period;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.item.DurationItem;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.AtomicTypes;

import java.util.List;

public class DurationFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private Item _durationStringItem = null;

    public DurationFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            try {
                Period period = DurationItem.getDurationFromString(
                    this._durationStringItem.getStringValue(),
                    AtomicTypes.DurationItem
                );
                return ItemFactory.getInstance().createDurationItem(period);
            } catch (UnsupportedOperationException | IllegalArgumentException e) {
                String message = String.format(
                    "\"%s\": value of type %s is not castable to type %s",
                    this._durationStringItem.serialize(),
                    "string",
                    "duration"
                );
                throw new CastException(message, getMetadata());
            }
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " duration function",
                    getMetadata()
            );
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this._durationStringItem = this._children.get(0)
            .materializeFirstItemOrNull(this._currentDynamicContextForLocalExecution);
        this._hasNext = this._durationStringItem != null;
    }
}
