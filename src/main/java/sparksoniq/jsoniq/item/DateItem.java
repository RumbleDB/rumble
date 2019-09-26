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
import sparksoniq.semantics.types.AtomicType;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

public class DateItem extends AtomicItem {

    private static final long serialVersionUID = 1L;
    private DateTime _value;

    public DateItem() { super(); }

    public DateItem(DateTime _value) {
        super();
        this._value = _value.dayOfMonth().roundFloorCopy();
    }

    public DateTime getValue() {
        return _value;
    }

    @Override
    public boolean isDate() {
        return true;
    }

    @Override
    public boolean hasDateOrTime() {
        return true;
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
            DateItem otherDate = (DateItem) otherItem;
            return this.getValue().isEqual(otherDate.getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }

    @Override
    public boolean isCastableAs(AtomicType type) {
        return type.getType().equals(AtomicTypes.DateItem) ||
                type.getType().equals(AtomicTypes.DateTimeItem) ||
                type.getType().equals(AtomicTypes.StringItem);
    }

    @Override
    public AtomicItem castAs(AtomicItem atomicItem) {
        return atomicItem.createFromDate(this);
    }

    @Override
    public AtomicItem createFromString(StringItem stringItem) {
        return ItemFactory.getInstance().createDateItem(DateTimeItem.getDateTimeFromString(stringItem.getStringValue(), AtomicTypes.DateItem));
    }

    @Override
    public AtomicItem createFromDateTime(DateTimeItem dateTimeItem) {
        String value = dateTimeItem.getValue().toString();
        int dateTimeSeparatorIndex = value.indexOf("T");
        String zone = dateTimeItem.getValue().getZone().toString();
        return ItemFactory.getInstance().createDateItem(DateTimeItem.getDateTimeFromString(
                value.substring(0,  dateTimeSeparatorIndex) + zone, AtomicTypes.DateItem));
    }

    @Override
    public AtomicItem createFromDate(DateItem dateItem) {
        return dateItem;
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.DateItem) || super.isTypeOf(type);
    }

    @Override
    public Item add(Item other) {
        Period period;
        if (other.isYearMonthDuration()) period = ((YearMonthDurationItem)other).getValue();
        else if (other.isDayTimeDuration()) period = ((DayTimeDurationItem)other).getValue();
        else throw new ClassCastException();
        return ItemFactory.getInstance().createDateItem(this.getValue().plus(period));
    }

    @Override
    public Item subtract(Item other, boolean negated) {
        Period period;
        if (other.isDate()) {
            period = new Period(((DateItem)other).getValue(), this.getValue(), PeriodType.dayTime());
            return ItemFactory.getInstance().createDayTimeDurationItem(period);
        }
        if (other.isYearMonthDuration()) period = ((YearMonthDurationItem)other).getValue();
        else period = ((DayTimeDurationItem)other).getValue();
        return ItemFactory.getInstance().createDateItem(this.getValue().minus(period));
    }

    @Override
    public int compareTo(Item other) {
        if (other.isDate()) {
            DateItem otherDuration = (DateItem) other;
            return this.getValue().compareTo(otherDuration.getValue());
        }
        throw new IteratorFlowException("Cannot compare item of type " + ItemTypes.getItemTypeName(this.getClass().getSimpleName()) +
                " with item of type " + ItemTypes.getItemTypeName(other.getClass().getSimpleName()));
    }

    @Override
    public Item compareItem(Item other, OperationalExpressionBase.Operator operator, IteratorMetadata metadata) {
        if (!other.isDate()) {
            throw new UnexpectedTypeException("\"" + ItemTypes.getItemTypeName(this.getClass().getSimpleName())
                    + "\": invalid type: can not compare for equality to type \""
                    + ItemTypes.getItemTypeName(other.getClass().getSimpleName()) + "\"", metadata);
        }
        return operator.apply(this, other);
    }

    @Override
    public String serialize() {
        String value = this.getValue().toString();
        String zone = this.getValue().getZone() == DateTimeZone.UTC ? "Z" : this.getValue().getZone().toString();
        int dateTimeSeparatorIndex = value.indexOf("T");
        return value.substring(0,  dateTimeSeparatorIndex) + zone;
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
