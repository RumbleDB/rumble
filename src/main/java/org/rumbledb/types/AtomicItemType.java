package org.rumbledb.types;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.Name;

import java.util.*;

import static org.rumbledb.types.BuiltinTypesCatalogue.*;

/**
 * This class describes all the primitive built-in atomic types in the JSONiq data model and the derived DayTimeDuration
 * and YearMonthDuration item types that are derived, but whose derivation cannot be expressed through JSound facets
 */
public class AtomicItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    private Name name;
    private Set<FacetTypes> allowedFacets;

    public AtomicItemType() {
    }

    AtomicItemType(Name name, Set<FacetTypes> allowedFacets) {
        this.name = name;
        this.allowedFacets = allowedFacets;
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
    public ItemType getPrimitiveType() {
        if (this.equals(dayTimeDurationItem) || this.equals(yearMonthDurationItem)) {
            return durationItem;
        }
        return this;
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
            return other.equals(dateItem)
                    || other.equals(timeItem)
                    || other.equals(dateTimeStampItem)
                    || other.equals(gYearItem)
                    || other.equals(gMonthItem)
                    || other.equals(gDayItem);
        }
        // DateTimeStamp can be cast also to Date or Time or DateTime
        if (this.equals(dateTimeStampItem)) {
            return other.equals(dateItem)
                    || other.equals(timeItem)
                    || other.equals(dateTimeItem)
                    || other.equals(gYearItem)
                    || other.equals(gMonthItem)
                    || other.equals(gDayItem);
        }
        // Date can be cast also to DateTime or DateTimeStamp
        if (this.equals(dateItem)) {
            return other.equals(dateTimeItem)
                    || other.equals(dateTimeStampItem)
                    || other.equals(gYearItem)
                    || other.equals(gMonthItem)
                    || other.equals(gDayItem);
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
    public boolean isResolved() {
        return true;
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleRuntimeConfiguration configuration) {
        if (this.getPrimitiveType().equals(atomicItem)) {
            return false;
        }
        if (this.getPrimitiveType().equals(dateItem)) {
            return !configuration.dateWithTimezone(); // xs:date has a time zone but not in DataFrames.
        }
        if (this.getPrimitiveType().equals(timeItem)) {
            return false;
        }
        if (this.getPrimitiveType().equals(dateTimeItem)) {
            return false;
        }
        if (this.getPrimitiveType().equals(durationItem)) {
            return false;
        }
        if (this.getPrimitiveType().equals(dayTimeDurationItem)) {
            return false;
        }
        if (this.getPrimitiveType().equals(yearMonthDurationItem)) {
            return false;
        }
        if (this.getPrimitiveType().equals(anyURIItem)) {
            return false;
        }
        if (this.getPrimitiveType().equals(base64BinaryItem)) {
            return false;
        }
        return true;
    }

    @Override
    public String getSparkSQLType() {
        if (this.getPrimitiveType().equals(stringItem)) {
            return "STRING";
        }
        if (this.getPrimitiveType().equals(booleanItem)) {
            return "BOOLEAN";
        }
        if (this.getPrimitiveType().equals(doubleItem)) {
            return "DOUBLE";
        }
        if (this.getPrimitiveType().equals(floatItem)) {
            return "FLOAT";
        }
        if (this.getPrimitiveType().equals(decimalItem)) {
            return "DECIMAL";
        }
        throw new UnsupportedOperationException("getSparkSQLType is unsupported for " + this.getPrimitiveType());
    }
}
