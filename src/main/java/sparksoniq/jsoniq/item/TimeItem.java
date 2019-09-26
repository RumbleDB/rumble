package sparksoniq.jsoniq.item;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.AtomicType;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;


public class TimeItem extends AtomicItem {

    private static final long serialVersionUID = 1L;
    private DateTime _value;

    public TimeItem() { super(); }

    public TimeItem(DateTime _value) {
        super();
        this._value = _value;
    }


    public DateTime getValue() {
        return _value;
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
    public boolean hasDateOrTime() {
        return true;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof Item)) {
            return false;
        }
        Item otherItem = (Item) otherObject;
        if (otherItem.isTime()) {
            TimeItem otherTime = (TimeItem) otherItem;
            return this.getValue().isEqual(otherTime.getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }

    @Override
    public boolean isCastableAs(AtomicType type) {
        return type.getType().equals(AtomicTypes.TimeItem) ||
                type.getType().equals(AtomicTypes.StringItem);
    }

    @Override
    public AtomicItem castAs(AtomicItem atomicItem) {
        return atomicItem.createFromTime(this);
    }

    @Override
    public AtomicItem createFromString(StringItem stringItem) {
        return ItemFactory.getInstance().createTimeItem(DateTimeItem.getDateTimeFromString(stringItem.getStringValue(), AtomicTypes.TimeItem));
    }

    @Override
    public AtomicItem createFromDateTime(DateTimeItem dateTimeItem) {
        String value = dateTimeItem.getValue().toString();
        int dateTimeSeparatorIndex = value.indexOf("T");
        return ItemFactory.getInstance().createTimeItem(DateTimeItem.getDateTimeFromString(value.substring(dateTimeSeparatorIndex+1), AtomicTypes.TimeItem));
    }

    @Override
    public AtomicItem createFromTime(TimeItem timeItem) {
        return timeItem;
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.TimeItem) || super.isTypeOf(type);
    }

    @Override
    public Item add(Item other) {
        Period period;
        if (other.isDayTimeDuration()) period = ((DayTimeDurationItem)other).getValue();
        else throw new ClassCastException();
        return ItemFactory.getInstance().createTimeItem(this.getValue().plus(period));
    }

    @Override
    public Item subtract(Item other, boolean negated) {
        Period period;
        if (other.isTime()) {
            period = new Period(((TimeItem)other).getValue(), this.getValue(), PeriodType.dayTime());
            return ItemFactory.getInstance().createDayTimeDurationItem(period);
        }
        period = ((DayTimeDurationItem)other).getValue();
        return ItemFactory.getInstance().createTimeItem(this.getValue().minus(period));
    }

    @Override
    public int compareTo(Item other) {
        if (other.isTime()) {
            TimeItem otherDuration = (TimeItem) other;
            return this.getValue().compareTo(otherDuration.getValue());
        }
        throw new IteratorFlowException("Cannot compare item of type " + ItemTypes.getItemTypeName(this.getClass().getSimpleName()) +
                " with item of type " + ItemTypes.getItemTypeName(other.getClass().getSimpleName()));
    }

    @Override
    public Item compareItem(Item other, OperationalExpressionBase.Operator operator, IteratorMetadata metadata) {
        if (!other.isTime()) {
            throw new UnexpectedTypeException("\"" + ItemTypes.getItemTypeName(this.getClass().getSimpleName())
                    + "\": invalid type: can not compare for equality to type \""
                    + ItemTypes.getItemTypeName(other.getClass().getSimpleName()) + "\"", metadata);
        }
        return operator.apply(this, other);
    }

    @Override
    public String serialize() {
        String value = this.getValue().toString();
        int dateTimeSeparatorIndex = value.indexOf("T");
        return value.substring(dateTimeSeparatorIndex+1);
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
