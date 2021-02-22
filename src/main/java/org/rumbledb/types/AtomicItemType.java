package org.rumbledb.types;

import org.rumbledb.context.Name;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class AtomicItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    // TODO: extract array and object into its own types
    static final AtomicItemType atomicItem = new AtomicItemType(
            new Name(Name.JS_NS, "js", "atomic"),
            Collections.emptySet()
    );
    static final AtomicItemType stringItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "string"),
            new HashSet<>(Arrays.asList(FacetTypes.ENUMERATION, FacetTypes.CONSTRAINTS, FacetTypes.LENGTH, FacetTypes.MINLENGTH, FacetTypes.MAXLENGTH))
    );
    static final AtomicItemType integerItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "integer"),
            new HashSet<>(Arrays.asList(
                    FacetTypes.ENUMERATION,
                    FacetTypes.CONSTRAINTS,
                    FacetTypes.MININCLUSIVE,
                    FacetTypes.MAXINCLUSIVE,
                    FacetTypes.MINEXCLUSIVE,
                    FacetTypes.MAXINCLUSIVE,
                    FacetTypes.TOTALDIGITS,
                    FacetTypes.FRACTIONDIGITS
            ))
    );
    static final AtomicItemType decimalItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "decimal"),
            new HashSet<>(Arrays.asList(
                    FacetTypes.ENUMERATION,
                    FacetTypes.CONSTRAINTS,
                    FacetTypes.MININCLUSIVE,
                    FacetTypes.MAXINCLUSIVE,
                    FacetTypes.MINEXCLUSIVE,
                    FacetTypes.MAXINCLUSIVE,
                    FacetTypes.TOTALDIGITS,
                    FacetTypes.FRACTIONDIGITS
            ))
    );
    static final AtomicItemType doubleItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "double"),
            new HashSet<>(Arrays.asList(
                    FacetTypes.ENUMERATION,
                    FacetTypes.CONSTRAINTS,
                    FacetTypes.MININCLUSIVE,
                    FacetTypes.MAXINCLUSIVE,
                    FacetTypes.MINEXCLUSIVE,
                    FacetTypes.MAXINCLUSIVE
            ))
    );
    static final AtomicItemType floatItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "float"),
            new HashSet<>(Arrays.asList(
                    FacetTypes.ENUMERATION,
                    FacetTypes.CONSTRAINTS,
                    FacetTypes.MININCLUSIVE,
                    FacetTypes.MAXINCLUSIVE,
                    FacetTypes.MINEXCLUSIVE,
                    FacetTypes.MAXINCLUSIVE
            ))
    );
    static final AtomicItemType booleanItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "boolean"),
            new HashSet<>(Arrays.asList(FacetTypes.ENUMERATION, FacetTypes.CONSTRAINTS))
    );
    static final AtomicItemType nullItem = new AtomicItemType(
            new Name(Name.JS_NS, "js", "null"),
            Collections.emptySet()
    );
    static final AtomicItemType durationItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "duration"),
            new HashSet<>(Arrays.asList(
                    FacetTypes.ENUMERATION,
                    FacetTypes.CONSTRAINTS,
                    FacetTypes.MININCLUSIVE,
                    FacetTypes.MAXINCLUSIVE,
                    FacetTypes.MINEXCLUSIVE,
                    FacetTypes.MAXINCLUSIVE
            ))
    );
    static final AtomicItemType yearMonthDurationItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "yearMonthDuration"),
            new HashSet<>(Arrays.asList(
                    FacetTypes.ENUMERATION,
                    FacetTypes.CONSTRAINTS,
                    FacetTypes.MININCLUSIVE,
                    FacetTypes.MAXINCLUSIVE,
                    FacetTypes.MINEXCLUSIVE,
                    FacetTypes.MAXINCLUSIVE
            ))
    );
    static final AtomicItemType dayTimeDurationItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "dayTimeDuration"),
            new HashSet<>(Arrays.asList(
                    FacetTypes.ENUMERATION,
                    FacetTypes.CONSTRAINTS,
                    FacetTypes.MININCLUSIVE,
                    FacetTypes.MAXINCLUSIVE,
                    FacetTypes.MINEXCLUSIVE,
                    FacetTypes.MAXINCLUSIVE
            ))
    );
    static final AtomicItemType dateTimeItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "dateTime"),
            new HashSet<>(Arrays.asList(
                    FacetTypes.ENUMERATION,
                    FacetTypes.CONSTRAINTS,
                    FacetTypes.MININCLUSIVE,
                    FacetTypes.MAXINCLUSIVE,
                    FacetTypes.MINEXCLUSIVE,
                    FacetTypes.MAXINCLUSIVE,
                    FacetTypes.EXPLICITTIMEZONE
            ))
    );
    static final AtomicItemType dateItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "date"),
            new HashSet<>(Arrays.asList(
                    FacetTypes.ENUMERATION,
                    FacetTypes.CONSTRAINTS,
                    FacetTypes.MININCLUSIVE,
                    FacetTypes.MAXINCLUSIVE,
                    FacetTypes.MINEXCLUSIVE,
                    FacetTypes.MAXINCLUSIVE,
                    FacetTypes.EXPLICITTIMEZONE
            ))
    );
    static final AtomicItemType timeItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "time"),
            new HashSet<>(Arrays.asList(
                    FacetTypes.ENUMERATION,
                    FacetTypes.CONSTRAINTS,
                    FacetTypes.MININCLUSIVE,
                    FacetTypes.MAXINCLUSIVE,
                    FacetTypes.MINEXCLUSIVE,
                    FacetTypes.MAXINCLUSIVE,
                    FacetTypes.EXPLICITTIMEZONE
            ))
    );
    static final AtomicItemType hexBinaryItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "hexBinary"),
            new HashSet<>(Arrays.asList(FacetTypes.ENUMERATION, FacetTypes.CONSTRAINTS, FacetTypes.LENGTH, FacetTypes.MINLENGTH, FacetTypes.MAXLENGTH))
    );
    static final AtomicItemType anyURIItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "anyURI"),
            new HashSet<>(Arrays.asList(FacetTypes.ENUMERATION, FacetTypes.CONSTRAINTS, FacetTypes.LENGTH, FacetTypes.MINLENGTH, FacetTypes.MAXLENGTH))
    );
    static final AtomicItemType base64BinaryItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "base64Binary"),
            new HashSet<>(Arrays.asList(FacetTypes.ENUMERATION, FacetTypes.CONSTRAINTS, FacetTypes.LENGTH, FacetTypes.MINLENGTH, FacetTypes.MAXLENGTH))
    );
    static final AtomicItemType JSONItem = new AtomicItemType(
            new Name(Name.JS_NS, "xs", "json-item"),
            Collections.emptySet()
    );
    static final AtomicItemType objectItem = new AtomicItemType(
            new Name(Name.JS_NS, "js", "object"),
            Collections.emptySet()
    );
    static final AtomicItemType arrayItem = new AtomicItemType(
            new Name(Name.JS_NS, "js", "array"),
            Collections.emptySet()
    );
    static final AtomicItemType intItem = new AtomicItemType(
            Name.createVariableInDefaultTypeNamespace("int"),
            new HashSet<>(Arrays.asList(
                    FacetTypes.ENUMERATION,
                    FacetTypes.CONSTRAINTS,
                    FacetTypes.MININCLUSIVE,
                    FacetTypes.MAXINCLUSIVE,
                    FacetTypes.MINEXCLUSIVE,
                    FacetTypes.MAXINCLUSIVE,
                    FacetTypes.TOTALDIGITS,
                    FacetTypes.FRACTIONDIGITS
            ))
    );

    private Name name;
    private Set<FacetTypes> allowedFacets;

    public AtomicItemType() {
    }

    private AtomicItemType(Name name, Set<FacetTypes> allowedFacets) {
        this.name = name;
        this.allowedFacets = allowedFacets;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ItemType)) {
            return false;
        }
        return this.toString().equals(other.toString());
    }

    @Override
    public boolean isAtomicItemType() {
        return !(this.equals(arrayItem) || this.equals(objectItem));
    }

    @Override
    public boolean isObjectItemType() {
        return this.equals(objectItem);
    }

    @Override
    public boolean isArrayItemType() {
        return this.equals(arrayItem);
    }

    @Override
    public boolean hasName() {
        return true;
    }

    @Override
    public Name getName() {
        return this.name;
    }

    @Override
    public boolean isSubtypeOf(ItemType superType) {
        if (superType.equals(BuiltinTypesCatalogue.item)) {
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
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
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
            return BuiltinTypesCatalogue.item;
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

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return this.allowedFacets;
    }

    @Override
    public String toString() {
        return this.name.toString();
    }
}
