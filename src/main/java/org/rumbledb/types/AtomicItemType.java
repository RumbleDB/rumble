package org.rumbledb.types;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.rumbledb.context.Name;
import org.rumbledb.exceptions.OurBadException;

public class AtomicItemType extends ItemType implements Serializable {

    private static final long serialVersionUID = 1L;

    // TODO: extract array and object into its own types
    public static final AtomicItemType atomicItem = new AtomicItemType(
            new Name(Name.JS_NS, "js", "atomic")
    );
    public static final AtomicItemType stringItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "string")
    );
    public static final AtomicItemType integerItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "integer")
    );
    public static final AtomicItemType decimalItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "decimal")
    );
    public static final AtomicItemType doubleItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "double")
    );
    public static final AtomicItemType floatItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "float")
    );
    public static final AtomicItemType booleanItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "boolean")
    );
    public static final AtomicItemType nullItem = new AtomicItemType(new Name(Name.JS_NS, "js", "null"));
    public static final AtomicItemType durationItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "duration")
    );
    public static final AtomicItemType yearMonthDurationItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "yearMonthDuration")
    );
    public static final AtomicItemType dayTimeDurationItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "dayTimeDuration")
    );
    public static final AtomicItemType dateTimeItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "dateTime")
    );
    public static final AtomicItemType dateItem = new AtomicItemType(new Name(Name.XS_NS, "xs", "date"));
    public static final AtomicItemType timeItem = new AtomicItemType(new Name(Name.XS_NS, "xs", "time"));
    public static final AtomicItemType hexBinaryItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "hexBinary")
    );
    public static final AtomicItemType anyURIItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "anyURI")
    );
    public static final AtomicItemType base64BinaryItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "base64Binary")
    );
    public static final AtomicItemType JSONItem = new AtomicItemType(
            new Name(Name.JS_NS, "xs", "json-item")
    );
    public static final AtomicItemType objectItem = new AtomicItemType(
            new Name(Name.JS_NS, "js", "object")
    );
    public static final AtomicItemType arrayItem = new AtomicItemType(
            new Name(Name.JS_NS, "js", "array")
    );
    public static final AtomicItemType intItem = new AtomicItemType(Name.createVariableInDefaultTypeNamespace("int"));
    public static final AtomicItemType functionItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "function")
    );

    private static List<ItemType> builtInItemTypes = Arrays.asList(
        objectItem,
        atomicItem,
        stringItem,
        integerItem,
        intItem,
        decimalItem,
        doubleItem,
        floatItem,
        booleanItem,
        arrayItem,
        nullItem,
        JSONItem,
        durationItem,
        yearMonthDurationItem,
        dayTimeDurationItem,
        dateTimeItem,
        dateItem,
        timeItem,
        hexBinaryItem,
        anyURIItem,
        base64BinaryItem,
        item,
        functionItem
    );

    public AtomicItemType() {
    }

    private AtomicItemType(Name name) {
        super(name);
    }

    public static boolean typeExists(Name name) {
        for (int i = 0; i < builtInItemTypes.size(); ++i) {
            if (name.getNamespace() != null && name.getNamespace().equals(Name.JSONIQ_DEFAULT_TYPE_NS)) {
                if (builtInItemTypes.get(i).getName().getLocalName().equals(name.getLocalName())) {
                    return true;
                }
            } else {
                if (builtInItemTypes.get(i).getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ItemType getItemTypeByName(Name name) {
        for (int i = 0; i < builtInItemTypes.size(); ++i) {
            if (name.getNamespace() != null && name.getNamespace().equals(Name.JSONIQ_DEFAULT_TYPE_NS)) {
                if (builtInItemTypes.get(i).getName().getLocalName().equals(name.getLocalName())) {
                    return builtInItemTypes.get(i);
                }
            } else {
                if (builtInItemTypes.get(i).getName().equals(name)) {
                    return builtInItemTypes.get(i);
                }
            }
        }
        throw new OurBadException("Type unrecognized: " + name + "(namespace: " + name.getNamespace() + ")");
    }

    // Returns true if [this] is a subtype of [superType], any type is considered a subtype of itself
    @Override
    public boolean isSubtypeOf(ItemType superType) {
        if (superType.equals(AtomicItemType.item)) {
            return true;
        } else if (superType.equals(JSONItem)) {
            return this.equals(objectItem)
                || this.equals(arrayItem)
                || this.equals(JSONItem);
        } else if (superType.equals(atomicItem)) {
            return this.equals(stringItem)
                || this.equals(integerItem)
                || this.equals(decimalItem)
                || this.equals(doubleItem)
                || this.equals(booleanItem)
                || this.equals(nullItem)
                || this.equals(anyURIItem)
                || this.equals(hexBinaryItem)
                || this.equals(base64BinaryItem)
                || this.equals(dateTimeItem)
                || this.equals(dateItem)
                || this.equals(timeItem)
                || this.equals(durationItem)
                || this.equals(yearMonthDurationItem)
                || this.equals(dayTimeDurationItem)
                || this.equals(atomicItem);
        } else if (superType.equals(durationItem)) {
            return this.equals(yearMonthDurationItem)
                || this.equals(dayTimeDurationItem)
                || this.equals(durationItem);
        } else if (superType.equals(decimalItem)) {
            return this.equals(integerItem) || this.equals(decimalItem) || this.equals(intItem);
        }
        return this.equals(superType);
    }

    @Override
    public ItemType findCommonSuperType(ItemType other) {
        if (other.isSubtypeOf(this)) {
            return this;
        } else if (this.isSubtypeOf(other)) {
            return other;
        } else if (this.isSubtypeOf(durationItem) && other.isSubtypeOf(durationItem)) {
            return durationItem;
        } else if (this.isSubtypeOf(atomicItem) && other.isSubtypeOf(atomicItem)) {
            return atomicItem;
        } else if (this.isSubtypeOf(JSONItem) && other.isSubtypeOf(JSONItem)) {
            return JSONItem;
        } else {
            return AtomicItemType.item;
        }
    }

    @Override
    public boolean isStaticallyCastableAs(ItemType other) {
        // anything can be casted to itself
        if (this.equals(other))
            return true;
        // anything can be casted from and to a string (or from one of its supertype)
        if (this.equals(stringItem) || other.equals(stringItem))
            return true;
        // boolean and numeric can be cast between themselves
        if (
            this.equals(booleanItem)
                || this.isNumeric()
        ) {
            if (
                other.equals(booleanItem)
                    ||
                    other.isNumeric()
            )
                return true;
            else
                return false;
        }
        // base64 and hex can be cast between themselves
        if (this.equals(base64BinaryItem) || this.equals(hexBinaryItem)) {
            if (
                other.equals(base64BinaryItem)
                    ||
                    other.equals(hexBinaryItem)
            )
                return true;
            else
                return false;
        }
        // durations can be cast between themselves
        if (this.isSubtypeOf(durationItem)) {
            if (other.isSubtypeOf(durationItem))
                return true;
            else
                return false;
        }
        // DateTime can be cast also to Date or Time
        if (this.equals(dateTimeItem)) {
            if (other.equals(dateItem) || other.equals(timeItem))
                return true;
            else
                return false;
        }
        // Date can be cast also to DateTime
        if (this.equals(dateItem)) {
            if (other.equals(dateTimeItem))
                return true;
            else
                return false;
        }
        // Otherwise this cannot be casted to other
        return false;
    }

    @Override
    public boolean isNumeric() {
        return this.equals(intItem)
            || this.equals(integerItem)
            || this.equals(decimalItem)
            || this.equals(doubleItem)
            || this.equals(floatItem);
    }

    @Override
    public boolean canBePromotedTo(ItemType other) {
        if (other.equals(stringItem)) {
            return this.equals(stringItem) || this.equals(anyURIItem);
        }
        if (other.equals(doubleItem)) {
            return this.isNumeric();
        }
        return false;
    }
}
