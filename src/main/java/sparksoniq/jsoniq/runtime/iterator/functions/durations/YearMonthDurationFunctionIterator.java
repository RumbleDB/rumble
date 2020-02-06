package sparksoniq.jsoniq.runtime.iterator.functions.durations;

import org.joda.time.Period;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.IteratorFlowException;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.item.DurationItem;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.AtomicTypes;

import java.util.List;

public class YearMonthDurationFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private Item _durationStringItem = null;

    public YearMonthDurationFunctionIterator(
            List<RuntimeIterator> parameters,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(parameters, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            try {
                Period period = DurationItem.getDurationFromString(
                    _durationStringItem.getStringValue(),
                    AtomicTypes.YearMonthDurationItem
                );
                return ItemFactory.getInstance().createYearMonthDurationItem(period);
            } catch (UnsupportedOperationException | IllegalArgumentException e) {
                String message = String.format(
                    "\"%s\": value of type %s is not castable to type %s",
                    _durationStringItem.serialize(),
                    "string",
                    "yearMonthDuration"
                );
                throw new CastException(message, getMetadata());
            }
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " yearMonthDuration function",
                    getMetadata()
            );
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        _durationStringItem = this._children.get(0).materializeFirstItemOrNull(_currentDynamicContextForLocalExecution);
        this._hasNext = _durationStringItem != null;
    }
}
