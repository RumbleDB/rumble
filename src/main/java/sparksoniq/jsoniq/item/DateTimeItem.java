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

import java.util.regex.Pattern;


public class DateTimeItem extends AtomicItem {

    private static final String yearFrag = "((-)?(([1-9]\\d\\d(\\d)+)|(0\\d\\d\\d)))";
    private static final String monthFrag = "((0[1-9])|(1[0-2]))";
    private static final String dayFrag = "((0[1-9])|([1-2]\\d)|(3[0-1]))";
    private static final String hourFrag = "(([0-1]\\d)|(2[0-3]))";
    private static final String minuteFrag = "([0-5]\\d)";
    private static final String secondFrag = "(([0-5]\\d)(\\.(\\d)+)?)";
    private static final String endOfDayFrag = "(24:00:00(\\.(0)+)?)";
    private static final String timezoneFrag = "(Z|([+\\-])(((0\\d|1[0-3]):" + minuteFrag + ")|(14:00)))";

    private static final String dateTimeLexicalRep = yearFrag + "-" + monthFrag + "-" + dayFrag + "T" +
            "((" + hourFrag + ":" + minuteFrag + ":" + secondFrag + ")|(" + endOfDayFrag + "))(" + timezoneFrag + ")?";

    private static final long serialVersionUID = 1L;
    private DateTime _value;

    public DateTimeItem() { super(); }

    public DateTimeItem(DateTime _value) {
        super();
        this._value = _value;
    }

    public DateTime getValue() {
        return _value;
    }

    @Override
    public DateTime getDateTimeValue() {
        return this.getValue();
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public boolean isDateTime() {
        return true;
    }

    @Override
    public Item castAs(AtomicTypes itemType) {
        switch (itemType) {
            case StringItem:
                return ItemFactory.getInstance().createStringItem(this.serialize());
            case DateTimeItem:
                return this;
            default:
                throw new ClassCastException();
        }
    }

    @Override
    public boolean isCastableAs(AtomicTypes itemType) {
        return itemType.equals(AtomicTypes.DateTimeItem) || itemType.equals(AtomicTypes.StringItem);
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
        if (otherItem.isDateTime()) {
            return this.getValue().isEqual(otherItem.getDateTimeValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.DateTimeItem) || super.isTypeOf(type);
    }

    @Override
    public String serialize() {
        String value = this.getValue().toString();
        if (this.getValue().getZone() == DateTimeZone.UTC) return value.substring(0, value.length()-1);
        return value;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.getValue());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._value = kryo.readObject(input, DateTime.class);
    }

    private static boolean checkInvalidDurationFormat(String dateTime) {
        return Pattern.compile(dateTimeLexicalRep).matcher(dateTime).matches();
    }

    private static String fixEndOfDay(String dateTime) {
        String endOfDay = "24:00:00";
        if (dateTime.contains(endOfDay)) {
            int indexOfT = dateTime.indexOf('T');
            if (indexOfT < 1 || indexOfT != dateTime.indexOf(endOfDay)-1 || !Character.isDigit(dateTime.charAt(indexOfT-1)))
                throw new IllegalArgumentException();
            int dayValue;
            try {
                dayValue = Character.getNumericValue(dateTime.charAt(indexOfT-1));
            } catch (Exception e) {
                throw new IllegalArgumentException();
            }
            return dateTime.substring(0, indexOfT-1) +
                    (dayValue+1) + "T00:00:00" +
                    dateTime.substring(indexOfT + endOfDay.length()+1);
        }
        return dateTime;
    }

    public static DateTime getDateTimeFromString(String dateTime) throws IllegalArgumentException{
        if (!checkInvalidDurationFormat(dateTime)) throw new IllegalArgumentException();
        dateTime = fixEndOfDay(dateTime);
        DateTime dt = DateTime.parse(dateTime);
        if (dt.getZone() == DateTimeZone.getDefault()) {
            return dt.withZoneRetainFields(DateTimeZone.UTC);
        }
        return dt;
    }

    @Override
    public Item add(Item other) {
        Period period;
        if (other.isYearMonthDuration() || other.isDayTimeDuration()) period = other.getDurationValue();
        else throw new ClassCastException();
        return ItemFactory.getInstance().createDateTimeItem(this.getValue().plus(period));
    }

    @Override
    public Item subtract(Item other) {
        Period period;
        if (other.isDateTime()) {
            period = new Period(other.getDateTimeValue(), this.getValue(), PeriodType.dayTime());
            return ItemFactory.getInstance().createDayTimeDurationItem(period);
        }
        if (other.isYearMonthDuration() || other.isDayTimeDuration()) period = other.getDurationValue();
        else throw new ClassCastException();
        return ItemFactory.getInstance().createDateTimeItem(this.getValue().minus(period));
    }

    @Override
    public int compareTo(Item other) {
        if (other.isDateTime()) {
            return this.getValue().compareTo(other.getDateTimeValue());
        }
        throw new IteratorFlowException("Cannot compare item of type " + ItemTypes.getItemTypeName(this.getClass().getSimpleName()) +
                " with item of type " + ItemTypes.getItemTypeName(other.getClass().getSimpleName()));
    }

    @Override
    public Item compareItem(Item other, OperationalExpressionBase.Operator operator, IteratorMetadata metadata) {
        if (!other.isDateTime()) {
            throw new UnexpectedTypeException("\"" + ItemTypes.getItemTypeName(this.getClass().getSimpleName())
                    + "\": invalid type: can not compare for equality to type \""
                    + ItemTypes.getItemTypeName(other.getClass().getSimpleName()) + "\"", metadata);
        }
        return operator.apply(this, other);
    }
}
