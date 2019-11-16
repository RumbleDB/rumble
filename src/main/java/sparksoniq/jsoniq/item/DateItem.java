package sparksoniq.jsoniq.item;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

public class DateItem extends AtomicItem {

    private static final long serialVersionUID = 1L;
    private DateTime _value;
    private boolean hasTimeZone = true;

    public DateItem() {
        super();
    }

    DateItem(DateTime _value, boolean hasTimeZone) {
        super();
        this._value = _value;
        this.hasTimeZone = hasTimeZone;
    }

    DateItem(String dateTimeString) {
        this._value = DateTimeItem.parseDateTime(dateTimeString, AtomicTypes.DateItem);
        if (!dateTimeString.endsWith("Z") && _value.getZone() == DateTimeZone.getDefault()) {
            this.hasTimeZone = false;
            this._value = _value.withZoneRetainFields(DateTimeZone.UTC);
        }
    }

    public DateTime getValue() {
        return _value;
    }

    @Override
    public DateTime getDateTimeValue() {
        return this._value;
    }

    @Override
    public boolean isDate() {
        return true;
    }

    @Override
    public boolean hasDateTime() {
        return true;
    }

    @Override
    public boolean hasTimeZone() {
        return hasTimeZone;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return false;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof Item)) {
            return false;
        }
        Item otherItem = (Item) otherObject;
        if (otherItem.isDate()) {
            return this.getValue().isEqual(otherItem.getDateTimeValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }

    @Override
    public boolean isCastableAs(AtomicTypes itemType) {
        return itemType.equals(AtomicTypes.DateItem) ||
                itemType.equals(AtomicTypes.DateTimeItem) ||
                itemType.equals(AtomicTypes.StringItem);
    }

    @Override
    public Item castAs(AtomicTypes itemType) {
        switch (itemType) {
            case DateItem:
                return this;
            case DateTimeItem:
                return ItemFactory.getInstance().createDateTimeItem(this.getValue(), this.hasTimeZone);
            case StringItem:
                return ItemFactory.getInstance().createStringItem(this.serialize());
            default:
                throw new ClassCastException();
        }
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.DateItem) || super.isTypeOf(type);
    }

    @Override
    public Item add(Item other) {
        if (other.isYearMonthDuration() || other.isDayTimeDuration())
            return ItemFactory.getInstance().createDateItem(this.getValue().plus(other.getDurationValue()), this.hasTimeZone);
        throw new ClassCastException();
    }

    @Override
    public Item subtract(Item other) {
        if (other.isDate()) {
            return ItemFactory.getInstance().createDayTimeDurationItem(new Period(other.getDateTimeValue(), this.getValue(), PeriodType.dayTime()));
        }
        if (other.isYearMonthDuration() || other.isDayTimeDuration())
            return ItemFactory.getInstance().createDateItem(this.getValue().minus(other.getDurationValue()), this.hasTimeZone);
        throw new ClassCastException();
    }

    @Override
    public int compareTo(Item other) {
        if (other.isNull()) return 1;
        if (other.isDate()) {
            return this.getValue().compareTo(other.getDateTimeValue());
        }
        throw new IteratorFlowException("Cannot compare item of type " + ItemTypes.getItemTypeName(this.getClass().getSimpleName()) +
                " with item of type " + ItemTypes.getItemTypeName(other.getClass().getSimpleName()));
    }

    @Override
    public Item compareItem(Item other, OperationalExpressionBase.Operator operator, IteratorMetadata metadata) {
        if (!other.isDate() && !other.isNull()) {
            throw new UnexpectedTypeException("\"" + ItemTypes.getItemTypeName(this.getClass().getSimpleName())
                    + "\": invalid type: can not compare for equality to type \""
                    + ItemTypes.getItemTypeName(other.getClass().getSimpleName()) + "\"", metadata);
        }
        return operator.apply(this, other);
    }

    @Override
    public String serialize() {
        String value = this.getValue().toString();
        String zone = this.getValue().getZone() == DateTimeZone.UTC ? "Z" :
                this.getValue().getZone().toString().equals(DateTimeZone.getDefault().toString()) ? "" : this.getValue().getZone().toString();
        int dateTimeSeparatorIndex = value.indexOf("T");
        return value.substring(0,  dateTimeSeparatorIndex) + (hasTimeZone ? zone : "");
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.serialize());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._value = DateTimeItem.parseDateTime(input.readString(), AtomicTypes.DateItem);
    }
}
