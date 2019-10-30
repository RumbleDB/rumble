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


public class TimeItem extends AtomicItem {

    private static final long serialVersionUID = 1L;
    private DateTime _value;
    private boolean hasTimeZone;

    public TimeItem() { super(); }

    public TimeItem(DateTime _value) {
        super();
        this.hasTimeZone = _value.getZone() != DateTimeZone.getDefault();
        this._value = hasTimeZone ? _value : _value.withZoneRetainFields(DateTimeZone.UTC);
    }


    public DateTime getValue() {
        return _value;
    }

    @Override
    public DateTime getTimeValue() {
        return this._value;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return false;
    }

    @Override
    public boolean isTime() {
        return true;
    }

    @Override
    public boolean hasDateTime() {
        return true;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof Item)) {
            return false;
        }
        Item otherItem = (Item) otherObject;
        if (otherItem.isTime()) {
            return this.getValue().isEqual(otherItem.getTimeValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }

    @Override
    public boolean isCastableAs(AtomicTypes itemType) {
        return itemType.equals(AtomicTypes.TimeItem) ||
                itemType.equals(AtomicTypes.StringItem);
    }

    @Override
    public Item castAs(AtomicTypes itemType) {
        switch (itemType) {
            case StringItem:
                return ItemFactory.getInstance().createStringItem(this.serialize());
            case TimeItem:
                return this;
            default:
                throw new ClassCastException();
        }
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.TimeItem) || super.isTypeOf(type);
    }

    @Override
    public Item add(Item other) {
        if (other.isDayTimeDuration()) return ItemFactory.getInstance().createTimeItem(this.getValue().plus(other.getDurationValue()));
        throw new ClassCastException();
    }

    @Override
    public Item subtract(Item other) {
        if (other.isTime())
            return ItemFactory.getInstance().createDayTimeDurationItem(new Period(other.getTimeValue(), this.getValue(), PeriodType.dayTime()));
        if (other.isDayTimeDuration())
            return ItemFactory.getInstance().createTimeItem(this.getValue().minus(other.getDurationValue()));
        throw new ClassCastException();
    }

    @Override
    public int compareTo(Item other) {
        if (other.isNull()) return 1;
        if (other.isTime()) {
            return this.getValue().compareTo(other.getTimeValue());
        }
        throw new IteratorFlowException("Cannot compare item of type " + ItemTypes.getItemTypeName(this.getClass().getSimpleName()) +
                " with item of type " + ItemTypes.getItemTypeName(other.getClass().getSimpleName()));
    }

    @Override
    public Item compareItem(Item other, OperationalExpressionBase.Operator operator, IteratorMetadata metadata) {
        if (!other.isTime() && !other.isNull()) {
            throw new UnexpectedTypeException("\"" + ItemTypes.getItemTypeName(this.getClass().getSimpleName())
                    + "\": invalid type: can not compare for equality to type \""
                    + ItemTypes.getItemTypeName(other.getClass().getSimpleName()) + "\"", metadata);
        }
        return operator.apply(this, other);
    }

    @Override
    public String serialize() {
        String value = this.getValue().toString();
        String zoneString = this.getValue().getZone() == DateTimeZone.UTC ? "Z" : value.substring(value.length() - 6);
        value = value.substring(0, value.length() - zoneString.length());
        value = this.getValue().getMillisOfSecond() == 0 ? value.substring(0, value.length()-4) : value;
        int dateTimeSeparatorIndex = value.indexOf("T");
        return value.substring(dateTimeSeparatorIndex+1) + (hasTimeZone ? zoneString : "");
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.getValue());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._value = kryo.readObject(input, DateTime.class);
    }
}
