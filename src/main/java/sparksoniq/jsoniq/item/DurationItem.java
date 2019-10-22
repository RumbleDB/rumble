package sparksoniq.jsoniq.item;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.AtomicType;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SingleType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationItem extends AtomicItem {

    private static final String prefix = "(-)?P";
    private static final String duYearFrag = "(\\d)+Y";
    private static final String duMonthFrag = "(\\d)+M";
    private static final String duDayFrag = "(\\d)+D";
    private static final String duHourFrag = "(\\d)+H";
    private static final String duMinuteFrag = "(\\d)+M";
    private static final String duSecondFrag = "(((\\d)+)|(\\.(\\d)+)|((\\d)+\\.(\\d)+))S";

    private static final String duYearMonthFrag = "((" + duYearFrag + "(" + duMonthFrag + ")?)|" + duMonthFrag + ")";
    private static final String duTimeFrag = "T((" + duHourFrag + "(" + duMinuteFrag + ")?" + "(" + duSecondFrag + ")?)|" +
            "(" + duMinuteFrag + "(" + duSecondFrag + ")?)|" + duSecondFrag + ")";
    private static final String duDayTimeFrag = "((" + duDayFrag + "(" + duTimeFrag + ")?)|" + duTimeFrag + ")";
    private static final String durationLiteral = prefix + "((" + duYearMonthFrag + "(" + duDayTimeFrag + ")?)|" + duDayTimeFrag +")";
    private static final Pattern pattern = Pattern.compile(durationLiteral);


    private static final long serialVersionUID = 1L;
    private Period _value;

    public DurationItem() {
        super();
    }

    public DurationItem(Period value) {
        super();
        this._value = value.normalizedStandard(PeriodType.yearMonthDayTime());
    }

    public Period getValue() {
        return _value;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public boolean isDuration() {
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
        Instant now = new Instant();
        if (otherItem.isDuration()) {
            DurationItem otherDuration = (DurationItem) otherItem;
            return this._value.toDurationFrom(now).isEqual(otherDuration.getValue().toDurationFrom(now));
        }
        return false;

    }

    @Override
    public int hashCode() {
        return this._value.hashCode();
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.DurationItem) || super.isTypeOf(type);
    }

    @Override
    public boolean isCastableAs(AtomicTypes itemType) {
        return itemType.equals(AtomicTypes.DurationItem) ||
                itemType.equals(AtomicTypes.StringItem);
    }

    @Override
    public Item castAs(AtomicTypes itemType) {
        switch (itemType) {
            case DurationItem:
                return this;
            case StringItem:
                return ItemFactory.getInstance().createStringItem(this.serialize());
            default:
                throw new ClassCastException();
        }
    }

    @Override
    public String serialize() {
        return this._value.toString();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this._value);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._value = kryo.readObject(input, Period.class);
    }

    private static PeriodFormatter getPeriodFormatter(AtomicTypes durationType) {
        switch (durationType) {
            case DurationItem:
                return ISOPeriodFormat.standard();
            default:
                throw new IllegalArgumentException();
        }
    }

    private static boolean checkInvalidDurationFormat(String duration) {
        return pattern.matcher(duration).matches();
    }

    public static Period getDurationFromString(String duration, AtomicTypes durationType) throws IllegalArgumentException{
        if (!checkInvalidDurationFormat(duration) || durationType == null) throw new IllegalArgumentException();
        boolean isNegative = false;
        if (duration.charAt(0) == '-') {
            isNegative = true;
            duration = duration.substring(1);
        }
        PeriodFormatter pf = getPeriodFormatter(durationType);
        Period period = Period.parse(duration, pf);
        return isNegative ? period.negated().normalizedStandard(PeriodType.yearMonthDayTime()) : period.normalizedStandard(PeriodType.yearMonthDayTime());
    }

    @Override
    public int compareTo(Item other) {
        if (other.isNull()) return 1;
        Instant now = new Instant();
        if (other.isDuration()) {
            DurationItem otherDuration = (DurationItem) other;
            return this._value.toDurationFrom(now).compareTo(otherDuration.getValue().toDurationFrom(now));
        }
        throw new IteratorFlowException("Cannot compare item of type duration with item of type " +
                ItemTypes.getItemTypeName(other.getClass().getSimpleName()));
    }

    @Override
    public Item compareItem(Item other, OperationalExpressionBase.Operator operator, IteratorMetadata metadata) {
        if (!other.isDuration() && !other.isNull()) {
            throw new UnexpectedTypeException("Invalid args for duration comparison " + this.serialize() +
                    ", " + other.serialize(), metadata);
        }
        if (other.isNull()) return operator.apply(this, other);
        switch (operator) {
            case VC_EQ:
            case GC_EQ:
            case VC_NE:
            case GC_NE:
                return operator.apply(this, other);
        }
        throw new UnexpectedTypeException("Invalid args for duration comparison " + this.serialize() +
                ", " + other.serialize(), metadata);
    }
}
