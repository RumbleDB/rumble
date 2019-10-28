package sparksoniq.jsoniq.item;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.ISODateTimeFormat;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

import java.util.regex.Pattern;

import static org.joda.time.format.ISODateTimeFormat.dateElementParser;

public class DateTimeItem extends AtomicItem {

    private static final String yearFrag = "((-)?(([1-9]\\d\\d(\\d)+)|(0\\d\\d\\d)))";
    private static final String monthFrag = "((0[1-9])|(1[0-2]))";
    private static final String dayFrag = "((0[1-9])|([1-2]\\d)|(3[0-1]))";
    private static final String hourFrag = "(([0-1]\\d)|(2[0-3]))";
    private static final String minuteFrag = "([0-5]\\d)";
    private static final String secondFrag = "(([0-5]\\d)(\\.(\\d)+)?)";
    private static final String endOfDayFrag = "(24:00:00(\\.(0)+)?)";
    private static final String timezoneFrag = "(Z|([+\\-])(((0\\d|1[0-3]):" + minuteFrag + ")|(14:00)))";
    private static final String dateFrag = "(" + yearFrag + '-' + monthFrag + '-' + dayFrag + ")";
    private static final String timeFrag = "((" + hourFrag + ":" + minuteFrag + ":" + secondFrag + ")|(" + endOfDayFrag + "))";

    private static final String dateLexicalRep = "(" + dateFrag + "(" + timezoneFrag +")?)";
    private static final String dateTimeLexicalRep = dateFrag + "T" + timeFrag+ "(" + timezoneFrag + ")?";

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
    public boolean hasDateTime() {
        return true;
    }

    @Override
    public Item castAs(AtomicTypes itemType) {
        switch (itemType) {
            case StringItem:
                return ItemFactory.getInstance().createStringItem(this.serialize());
            case DateTimeItem:
                return this;
            case DateItem:
                return getDateFromDateTime(this);
            default:
                throw new ClassCastException();
        }
    }

    @Override
    public boolean isCastableAs(AtomicTypes itemType) {
        return itemType.equals(AtomicTypes.DateTimeItem)
                || itemType.equals(AtomicTypes.StringItem)
                || itemType.equals(AtomicTypes.DateItem);
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
        return this.getValue().toString();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.getValue());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._value = kryo.readObject(input, DateTime.class);
    }

    private static DateTimeFormatter getDateTimeFormatter(AtomicTypes dateTimeType) {
        switch (dateTimeType) {
            case DateTimeItem:
                return ISODateTimeFormat.dateTimeParser().withOffsetParsed();
            case DateItem:
                DateTimeParser dtParser = new DateTimeFormatterBuilder().appendOptional(
                        ((new DateTimeFormatterBuilder()).appendTimeZoneOffset("Z", true, 2, 4).toFormatter()).getParser()).toParser();
                return (new DateTimeFormatterBuilder()).append(dateElementParser()).appendOptional(dtParser).toFormatter().withOffsetParsed();
            default:
                throw new IllegalArgumentException();
        }
    }

    private static boolean checkInvalidDateTimeFormat(String dateTime, AtomicTypes dateTimeType) {
        switch (dateTimeType) {
            case DateTimeItem:
                return Pattern.compile(dateTimeLexicalRep).matcher(dateTime).matches();
            case DateItem:
                return Pattern.compile(dateLexicalRep).matcher(dateTime).matches();
        }
        return false;
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

    public static DateTime getDateTimeFromString(String dateTime, AtomicTypes dateTimeType) {
        if (!checkInvalidDateTimeFormat(dateTime, dateTimeType)) throw new IllegalArgumentException();
        dateTime = fixEndOfDay(dateTime);
        DateTime dt = DateTime.parse(dateTime, getDateTimeFormatter(dateTimeType));
        if (dt.getZone() == DateTimeZone.getDefault()) {
            return dt.withZoneRetainFields(DateTimeZone.UTC);
        }
        return dt;
    }

    private Item getDateFromDateTime(DateTimeItem dateTimeItem) {
        String value = dateTimeItem.getValue().toString();
        int dateTimeSeparatorIndex = value.indexOf("T");
        String zone = dateTimeItem.getValue().getZone().toString();
        return ItemFactory.getInstance().createDateItem(DateTimeItem.getDateTimeFromString(
                value.substring(0,  dateTimeSeparatorIndex) + zone, AtomicTypes.DateItem));
    }

    @Override
    public Item add(Item other) {
        if (other.isYearMonthDuration() || other.isDayTimeDuration())
            return ItemFactory.getInstance().createDateTimeItem(this.getValue().plus(other.getDurationValue()));
        else throw new ClassCastException();
    }

    @Override
    public Item subtract(Item other) {
        if (other.isDateTime()) {
            return ItemFactory.getInstance().createDayTimeDurationItem(new Period(other.getDateTimeValue(), this.getValue(), PeriodType.dayTime()));
        }
        if (other.isYearMonthDuration() || other.isDayTimeDuration())
            return ItemFactory.getInstance().createDateTimeItem(this.getValue().minus(other.getDurationValue()));
        else throw new ClassCastException();
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
