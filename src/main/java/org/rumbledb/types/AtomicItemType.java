package org.rumbledb.types;

import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;

import java.util.*;

/**
 * This class describes all the primitive built-in atomic types in the JSONiq data model and the derived DayTimeDuration
 * and YearMonthDuration item types that are derived, but whose derivation cannot be expressed through JSound facets
 */
public class AtomicItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    static final AtomicItemType atomicItem = new AtomicItemType(
            new Name(Name.JS_NS, "js", "atomic"),
            Collections.emptySet(),
            DataTypes.BinaryType
    );
    static final AtomicItemType stringItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "string"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.LENGTH,
                        FacetTypes.MINLENGTH,
                        FacetTypes.MAXLENGTH
                    )
            ),
            DataTypes.StringType
    );

    // numeric is an internal type for avoiding function overloading, it is not available in JSONiq
    // it is the base type for xs:decimal, xs:double, and xs:float (those are now treated specially in type functions)
    static final AtomicItemType numericItem = new AtomicItemType(
            new Name(Name.JS_NS, "js", "numeric"),
            Collections.emptySet(),
            DataTypes.BinaryType // TODO: consider if specific type is needed
    );

    static final AtomicItemType decimalItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "decimal"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXEXCLUSIVE,
                        FacetTypes.TOTALDIGITS,
                        FacetTypes.FRACTIONDIGITS
                    )
            ),
            DataTypes.createDecimalType()
    );
    static final AtomicItemType doubleItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "double"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXEXCLUSIVE
                    )
            ),
            DataTypes.DoubleType
    );
    static final AtomicItemType floatItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "float"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXEXCLUSIVE
                    )
            ),
            DataTypes.FloatType
    );

    static final AtomicItemType booleanItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "boolean"),
            new HashSet<>(Arrays.asList(FacetTypes.ENUMERATION, FacetTypes.CONSTRAINTS)),
            DataTypes.BooleanType
    );
    static final AtomicItemType nullItem = new AtomicItemType(
            new Name(Name.JS_NS, "js", "null"),
            Collections.emptySet(),
            DataTypes.NullType // TODO : see appropriate type
    );
    static final AtomicItemType durationItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "duration"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXEXCLUSIVE
                    )
            ),
            DataTypes.BinaryType // TODO : appropriate datatype
    );
    static final AtomicItemType yearMonthDurationItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "yearMonthDuration"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXEXCLUSIVE
                    )
            ),
            DataTypes.BinaryType // TODO : appropriate datatype
    );
    static final AtomicItemType dayTimeDurationItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "dayTimeDuration"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXEXCLUSIVE
                    )
            ),
            DataTypes.BinaryType // TODO : appropriate datatype
    );
    static final AtomicItemType dateTimeItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "dateTime"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXEXCLUSIVE,
                        FacetTypes.EXPLICITTIMEZONE
                    )
            ),
            DataTypes.DateType
    );
    static final AtomicItemType dateTimeStampItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "dateTimeStamp"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXEXCLUSIVE,
                        FacetTypes.EXPLICITTIMEZONE
                    )
            ),
            DataTypes.DateType
    );
    static final AtomicItemType dateItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "date"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXEXCLUSIVE,
                        FacetTypes.EXPLICITTIMEZONE
                    )
            ),
            DataTypes.TimestampType
    );
    static final AtomicItemType timeItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "time"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXEXCLUSIVE,
                        FacetTypes.EXPLICITTIMEZONE
                    )
            ),
            DataTypes.TimestampType
    );
    static final AtomicItemType hexBinaryItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "hexBinary"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.LENGTH,
                        FacetTypes.MINLENGTH,
                        FacetTypes.MAXLENGTH
                    )
            ),
            DataTypes.BinaryType
    );
    static final AtomicItemType anyURIItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "anyURI"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.LENGTH,
                        FacetTypes.MINLENGTH,
                        FacetTypes.MAXLENGTH
                    )
            ),
            DataTypes.StringType
    );
    static final AtomicItemType base64BinaryItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "base64Binary"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.LENGTH,
                        FacetTypes.MINLENGTH,
                        FacetTypes.MAXLENGTH
                    )
            ),
            DataTypes.BinaryType
    );

    private Name name;
    private Set<FacetTypes> allowedFacets;
    private DataType dataFrameType;

    public AtomicItemType() {
    }

    AtomicItemType(Name name, Set<FacetTypes> allowedFacets, DataType dataFrameType) {
        this.name = name;
        this.allowedFacets = allowedFacets;
        this.dataFrameType = dataFrameType;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ItemType)) {
            return false;
        }
        return isEqualTo((ItemType) other);
    }

    @Override
    public boolean isAtomicItemType() {
        return true;
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
    public int getTypeTreeDepth() {
        if (this.equals(atomicItem)) {
            return 1;
        } else if (this.equals(numericItem)) {
            return 2;
        } else if (this.isNumeric()) {
            return 3;
        } else if (this.equals(yearMonthDurationItem) || this.equals(dayTimeDurationItem)) {
            // TODO : check once you remove derived like integer and int
            return 3;
        } else {
            return 2;
        }
    }

    @Override
    public ItemType getBaseType() {
        if (this.equals(atomicItem)) {
            return BuiltinTypesCatalogue.item;
        } else if (this.equals(numericItem)) {
            return atomicItem;
        } else if (this.isNumeric()) {
            return numericItem;
        } else if (this.equals(yearMonthDurationItem) || this.equals(dayTimeDurationItem)) {
            return durationItem;
        } else {
            return atomicItem;
        }
    }

    @Override
    public boolean isPrimitive() {
        return !(this.equals(dayTimeDurationItem) || this.equals(yearMonthDurationItem));
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
        // DateTime can be cast also to Date or Time or DateTimeStamp
        if (this.equals(dateTimeItem)) {
            if (other.equals(dateItem) || other.equals(timeItem) || other.equals(dateTimeStampItem))
                return true;
            else
                return false;
        }
        // DateTimeStamp can be cast also to Date or Time or DateTime
        if (this.equals(dateTimeStampItem)) {
            if (other.equals(dateItem) || other.equals(timeItem) || other.equals(dateTimeItem))
                return true;
            else
                return false;
        }
        // Date can be cast also to DateTime or DateTimeStamp
        if (this.equals(dateItem)) {
            if (other.equals(dateTimeItem) || other.equals(dateTimeStampItem))
                return true;
            else
                return false;
        }
        // Otherwise this cannot be casted to other
        return false;
    }

    @Override
    public boolean isNumeric() {
        return this.equals(decimalItem)
            || this.equals(floatItem)
            || this.equals(doubleItem)
            || this.equals(numericItem);
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
    public List<Item> getEnumerationFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.ENUMERATION)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the enumeration facet"
            );
        }
        return null;
    }

    @Override
    public List<String> getConstraintsFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.CONSTRAINTS)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the constraints facet"
            );
        }
        return Collections.emptyList();
    }

    @Override
    public Integer getMinLengthFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.MINLENGTH)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the minimum length facet"
            );
        }
        return null;
    }

    @Override
    public Integer getLengthFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.LENGTH)) {
            throw new UnsupportedOperationException(this.toString() + " item type does not support the length facet");
        }
        return null;
    }

    @Override
    public Integer getMaxLengthFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.MAXLENGTH)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the maximum length facet"
            );
        }
        return null;
    }

    @Override
    public Item getMinExclusiveFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.MINEXCLUSIVE)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the minimum exclusive facet"
            );
        }
        return null;
    }

    @Override
    public Item getMinInclusiveFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.MININCLUSIVE)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the minimum inclusive facet"
            );
        }
        return null;
    }

    @Override
    public Item getMaxExclusiveFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.MAXEXCLUSIVE)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the maximum exclusive facet"
            );
        }
        return null;
    }

    @Override

    public Item getMaxInclusiveFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.MAXINCLUSIVE)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the maximum inclusive facet"
            );
        }
        return null;
    }

    @Override
    public Integer getTotalDigitsFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.TOTALDIGITS)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the total digits facet"
            );
        }
        return null;
    }

    @Override
    public Integer getFractionDigitsFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.FRACTIONDIGITS)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the fraction digits facet"
            );
        }
        return null;
    }

    @Override
    public TimezoneFacet getExplicitTimezoneFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.EXPLICITTIMEZONE)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the explicit timezone facet"
            );
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name.toString();
    }

    @Override
    public DataType toDataFrameType() {
        return this.dataFrameType;
    }

    @Override
    public boolean isDataFrameType() {
        return true;
    }

    @Override
    public boolean isResolved() {
        return true;
    }
}
