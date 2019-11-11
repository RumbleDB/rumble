package sparksoniq.jsoniq.runtime.iterator.functions.datetime.components;

import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.InvalidTimezoneException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.exceptions.UnknownFunctionCallException;
import sparksoniq.jsoniq.item.DateItem;
import sparksoniq.jsoniq.item.DayTimeDurationItem;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class AdjustDateToTimezone extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private DateItem _dateItem = null;
    private DayTimeDurationItem _timezone = null;

    public AdjustDateToTimezone(
            List<RuntimeIterator> arguments,
            IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            if (_timezone == null && this._children.size() == 1)
                return ItemFactory.getInstance().createDateItem(_dateItem.getDateTimeValue().withZone(DateTimeZone.UTC), true);
            if (_timezone == null) {
                if (_dateItem.hasTimeZone())
                    return ItemFactory.getInstance().createDateItem(_dateItem.getDateTimeValue().withZoneRetainFields(_dateItem.getDateTimeValue().getZone()), false);
                return ItemFactory.getInstance().createDateItem(_dateItem.getDateTimeValue(), _dateItem.hasTimeZone());
            }
            else {
                if (this.checkTimeZoneArgument()) throw new InvalidTimezoneException("Invalid timezone", getMetadata());
                if (_dateItem.hasTimeZone())
                    return ItemFactory.getInstance().createDateItem(_dateItem.getDateTimeValue().withZone(DateTimeZone.forOffsetHoursMinutes(_timezone.getDurationValue().getHours(), _timezone.getDurationValue().getMinutes())), true);
                return ItemFactory.getInstance().createDateItem(_dateItem.getDateTimeValue().withZoneRetainFields(DateTimeZone.forOffsetHoursMinutes(_timezone.getDurationValue().getHours(), _timezone.getDurationValue().getMinutes())), true);
            }

        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " adjust-date-to-timezone function",
                    getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        try {
            _dateItem = this.getSingleItemOfTypeFromIterator(
                    this._children.get(0),
                    DateItem.class,
                    new UnknownFunctionCallException("adjust-date-to-timezone", this._children.size(), getMetadata()));
            if (this._children.size() == 2) {
                _timezone = this.getSingleItemOfTypeFromIterator(
                        this._children.get(1),
                        DayTimeDurationItem.class,
                        new UnknownFunctionCallException("adjust-date-to-timezone", this._children.size(), getMetadata()));
            }
        } catch (UnexpectedTypeException e) {
            throw new UnexpectedTypeException(e.getJSONiqErrorMessage() + "? of function adjust-date-to-timezone()", this._children.get(0).getMetadata());
        }
        this._hasNext = _dateItem != null;
    }

    private boolean checkTimeZoneArgument() {
        return (Math.abs(_timezone.getDurationValue().toDurationFrom(Instant.now()).getMillis()) > 50400000) ||
                (Double.compare(_timezone.getDurationValue().getSeconds() + _timezone.getDurationValue().getMillis() * 1.0 / 1000, 0) != 0);
    }
}