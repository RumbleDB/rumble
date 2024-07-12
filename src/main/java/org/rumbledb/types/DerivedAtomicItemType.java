package org.rumbledb.types;

import org.apache.commons.collections.ListUtils;
import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidSchemaException;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.runtime.misc.ComparisonIterator;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.List;
import java.util.Set;

public class DerivedAtomicItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    private ItemType baseType;
    private ItemType primitiveType;
    private int typeTreeDepth;
    private boolean isUserDefined;
    private Name name;
    private Item minInclusive, maxInclusive, minExclusive, maxExclusive;
    private Integer minLength, length, maxLength, totalDigits, fractionDigits;
    private List<String> constraints;
    private List<Item> enumeration;
    private TimezoneFacet explicitTimezone;

    DerivedAtomicItemType() {
    }

    DerivedAtomicItemType(Name name, ItemType baseType, ItemType primitiveType, Facets facets) {
        this(name, baseType, primitiveType, facets, true);
    }
    // TODO : turn builtin derived atomic types into this class

    DerivedAtomicItemType(
            Name name,
            ItemType baseType,
            ItemType primitiveType,
            Facets facets,
            boolean isUserDefined
    ) {
        // TODO : check in item factory that: name not already used or invalid, facets are correct and allowed according
        // to baseType
        this.name = name;
        this.baseType = baseType;
        this.primitiveType = primitiveType;
        this.isUserDefined = isUserDefined;

        this.minInclusive = facets.getMinInclusive();
        this.maxInclusive = facets.getMaxInclusive();
        this.minExclusive = facets.getMinExclusive();
        this.maxExclusive = facets.getMaxExclusive();

        this.length = facets.getLength();
        this.minLength = facets.getMinLength();
        this.maxLength = facets.getMaxLength();
        this.totalDigits = facets.getTotalDigits();
        this.fractionDigits = facets.getFractionDigits();

        this.explicitTimezone = facets.getExplicitTimezone();

        this.constraints = facets.getConstraints();
        this.enumeration = facets.getEnumeration();

        if (this.baseType.isResolved()) {
            processBaseType();
        }

    }

    DerivedAtomicItemType(
            Name name,
            ItemType baseType,
            Facets facets,
            boolean isUserDefined
    ) {
        // TODO : check in item factory that: name not already used or invalid, facets are correct and allowed according
        // to baseType
        this(name, baseType, null, facets, isUserDefined);
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
    public boolean isNumeric() {
        return this.primitiveType.isNumeric();
    }

    @Override
    public boolean hasName() {
        return this.name != null;
    }

    @Override
    public Name getName() {
        return this.name;
    }

    @Override
    public int getTypeTreeDepth() {
        return this.typeTreeDepth;
    }

    @Override
    public boolean isStaticallyCastableAs(ItemType other) {
        // TODO: what about further restrictions like string without num from int?
        return this.primitiveType.isStaticallyCastableAs(other.isPrimitive() ? other : other.getPrimitiveType());
    }

    @Override
    public boolean canBePromotedTo(ItemType other) {
        // TODO : how about restriction types
        if (other.equals(BuiltinTypesCatalogue.stringItem)) {
            return this.isSubtypeOf(BuiltinTypesCatalogue.stringItem)
                || this.isSubtypeOf(BuiltinTypesCatalogue.anyURIItem);
        }
        if (other.equals(BuiltinTypesCatalogue.doubleItem)) {
            return this.isNumeric();
        }
        return false;
    }

    @Override
    public boolean isUserDefined() {
        return this.isUserDefined;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public ItemType getPrimitiveType() {
        return this.primitiveType;
    }

    @Override
    public ItemType getBaseType() {
        return this.baseType;
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return this.primitiveType.getAllowedFacets();
    }

    @Override
    public List<Item> getEnumerationFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.ENUMERATION)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the enumeration facet"
            );
        }
        return this.enumeration == null ? this.baseType.getEnumerationFacet() : this.enumeration;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getConstraintsFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.CONSTRAINTS)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the constraints facet"
            );
        }
        return ListUtils.union(this.baseType.getConstraintsFacet(), this.constraints);
    }

    @Override
    public Integer getMinLengthFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.MINLENGTH)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the minimum length facet"
            );
        }
        return this.minLength == null ? this.baseType.getMinLengthFacet() : this.minLength;
    }

    @Override
    public Integer getLengthFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.LENGTH)) {
            throw new UnsupportedOperationException(this.toString() + " item type does not support the length facet");
        }
        return this.length == null ? this.baseType.getLengthFacet() : this.length;
    }

    @Override
    public Integer getMaxLengthFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.MAXLENGTH)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the maximum length facet"
            );
        }
        return this.maxLength == null ? this.baseType.getMaxLengthFacet() : this.maxLength;
    }

    @Override
    public Item getMinExclusiveFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.MINEXCLUSIVE)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the minimum exclusive facet"
            );
        }
        return this.minExclusive == null ? this.baseType.getMinExclusiveFacet() : this.minExclusive;
    }

    @Override
    public Item getMinInclusiveFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.MININCLUSIVE)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the minimum inclusive facet"
            );
        }
        return this.minInclusive == null ? this.baseType.getMinInclusiveFacet() : this.minInclusive;
    }

    @Override
    public Item getMaxExclusiveFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.MAXEXCLUSIVE)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the maximum exclusive facet"
            );
        }
        return this.maxExclusive == null ? this.baseType.getMaxExclusiveFacet() : this.maxExclusive;
    }

    @Override
    public Item getMaxInclusiveFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.MAXINCLUSIVE)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the maximum inclusive facet"
            );
        }
        return this.maxInclusive == null ? this.baseType.getMaxInclusiveFacet() : this.maxInclusive;
    }

    @Override
    public Integer getTotalDigitsFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.TOTALDIGITS)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the total digits facet"
            );
        }
        return this.totalDigits == null ? this.baseType.getTotalDigitsFacet() : this.totalDigits;
    }

    @Override
    public Integer getFractionDigitsFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.FRACTIONDIGITS)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the fraction digits facet"
            );
        }
        return this.fractionDigits == null ? this.baseType.getFractionDigitsFacet() : this.fractionDigits;
    }

    @Override
    public TimezoneFacet getExplicitTimezoneFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.EXPLICITTIMEZONE)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the explicit timezone facet"
            );
        }
        return this.explicitTimezone == null ? this.baseType.getExplicitTimezoneFacet() : this.explicitTimezone;
    }

    @Override
    public String getIdentifierString() {
        if (this.hasName()) {
            return this.name.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("#anonymous-atomic-base{");
        sb.append(this.baseType.getIdentifierString());
        sb.append("}");

        if (this.minLength != null) {
            sb.append("-ml:");
            sb.append(this.minLength);
        }
        if (this.length != null) {
            sb.append("-l:");
            sb.append(this.length);
        }
        if (this.maxLength != null) {
            sb.append("-Ml:");
            sb.append(this.maxLength);
        }

        if (this.totalDigits != null) {
            sb.append("-td:");
            sb.append(this.totalDigits);
        }
        if (this.fractionDigits != null) {
            sb.append("-fd:");
            sb.append(this.fractionDigits);
        }

        if (this.minInclusive != null) {
            sb.append("-mi:");
            sb.append(this.minInclusive.serialize());
        }
        if (this.minExclusive != null) {
            sb.append("-me:");
            sb.append(this.minExclusive.serialize());
        }
        if (this.maxInclusive != null) {
            sb.append("-Mi:");
            sb.append(this.maxInclusive.serialize());
        }
        if (this.maxExclusive != null) {
            sb.append("-Me:");
            sb.append(this.maxExclusive.serialize());
        }

        if (this.explicitTimezone != null) {
            sb.append("-et:");
            sb.append(this.explicitTimezone.name());
        }


        if (this.enumeration != null) {
            sb.append("-enum{");
            String comma = "";
            for (Item item : this.enumeration) {
                sb.append(comma);
                sb.append(item.serialize());
                comma = ",";
            }
            sb.append("}");
        }

        if (this.constraints.size() > 0) {
            sb.append("-const{");
            String comma = "";
            for (String c : this.constraints) {
                sb.append(comma);
                sb.append("\"");
                sb.append(c);
                sb.append("\"");
                comma = ",";
            }
            sb.append("}");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        // TODO : Consider added facets restriction and base type
        return this.name.toString();
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleRuntimeConfiguration configuration) {
        return this.baseType.isCompatibleWithDataFrames(configuration);
    }

    @Override
    public void resolve(DynamicContext context, ExceptionMetadata metadata) {
        if (!this.baseType.isResolved()) {
            this.baseType.resolve(context, metadata);
            processBaseType();
        }
    }

    @Override
    public void resolve(StaticContext context, ExceptionMetadata metadata) {
        if (!this.baseType.isResolved()) {
            this.baseType.resolve(context, metadata);
            processBaseType();
        }
    }

    public void processBaseType() {
        this.typeTreeDepth = this.baseType.getTypeTreeDepth() + 1;
        if (this.baseType.isAtomicItemType()) {
            this.primitiveType = this.baseType.getPrimitiveType();

            if (this.length == null) {
                if (this.baseType.getAllowedFacets().contains(FacetTypes.LENGTH)) {
                    this.length = this.baseType.getLengthFacet();
                }
            } else {
                if (!this.primitiveType.getAllowedFacets().contains(FacetTypes.LENGTH)) {
                    throw new InvalidSchemaException(
                            "This facet is not applicable to " + this.primitiveType,
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }
                if (this.baseType.getLengthFacet() != null && this.length != this.baseType.getLengthFacet()) {
                    throw new InvalidSchemaException("Incompatible length facet.", ExceptionMetadata.EMPTY_METADATA);
                }
            }

            if (this.enumeration == null) {
                if (this.baseType.getAllowedFacets().contains(FacetTypes.ENUMERATION)) {
                    this.enumeration = this.baseType.getEnumerationFacet();
                }
            } else {
                if (!this.primitiveType.getAllowedFacets().contains(FacetTypes.ENUMERATION)) {
                    throw new InvalidSchemaException(
                            "This facet is not applicable to " + this.primitiveType,
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }
                if (this.enumeration.hashCode() != this.baseType.getEnumerationFacet().hashCode()) {
                    throw new InvalidSchemaException(
                            "Enumeration facet is not valid.",
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }

            }
            // TODO: Check list enumeration with for loop or by hash

            if (this.minLength == null) {
                if (this.baseType.getAllowedFacets().contains(FacetTypes.MINLENGTH)) {
                    this.minLength = this.baseType.getMinLengthFacet();
                }
            } else {
                if (!this.primitiveType.getAllowedFacets().contains(FacetTypes.MINLENGTH)) {
                    throw new InvalidSchemaException(
                            "This facet is not applicable to " + this.primitiveType,
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }
                if (this.baseType.getMinLengthFacet() != null && this.minLength < this.baseType.getMinLengthFacet()) {
                    throw new InvalidSchemaException("Out of bounds minLength.", ExceptionMetadata.EMPTY_METADATA);
                }
            }
            if (this.maxLength == null) {
                if (this.baseType.getAllowedFacets().contains(FacetTypes.MAXLENGTH)) {
                    this.maxLength = this.baseType.getMaxLengthFacet();
                }
            } else {
                if (!this.primitiveType.getAllowedFacets().contains(FacetTypes.MAXLENGTH)) {
                    throw new InvalidSchemaException(
                            "This facet is not applicable to " + this.primitiveType,
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }
                if (this.baseType.getMaxLengthFacet() != null && this.maxLength > this.baseType.getMaxLengthFacet()) {
                    throw new InvalidSchemaException("Out of bounds maxLength.", ExceptionMetadata.EMPTY_METADATA);
                }
            }
            if (this.minInclusive == null) {
                if (this.baseType.getAllowedFacets().contains(FacetTypes.MININCLUSIVE)) {
                    this.minInclusive = this.baseType.getMinInclusiveFacet();
                }
            } else {
                if (!this.primitiveType.getAllowedFacets().contains(FacetTypes.MININCLUSIVE)) {
                    throw new InvalidSchemaException(
                            "This facet is not applicable to " + this.primitiveType,
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }

                if (
                    this.baseType.getMinInclusiveFacet() != null
                        && ComparisonIterator.compareItems(
                            this.minInclusive,
                            this.baseType.getMinInclusiveFacet(),
                            ComparisonExpression.ComparisonOperator.GC_LT,
                            ExceptionMetadata.EMPTY_METADATA
                        ) < 0
                ) {
                    throw new InvalidSchemaException(
                            "Out of bounds minInclusive facet.",
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }
            }
            if (this.maxInclusive == null) {
                if (this.baseType.getAllowedFacets().contains(FacetTypes.MAXINCLUSIVE)) {
                    this.maxInclusive = this.baseType.getMaxInclusiveFacet();
                }
            } else {
                if (!this.primitiveType.getAllowedFacets().contains(FacetTypes.MAXINCLUSIVE)) {
                    throw new InvalidSchemaException(
                            "This facet is not applicable to " + this.primitiveType,
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }

                if (
                    this.baseType.getMaxInclusiveFacet() != null
                        && ComparisonIterator.compareItems(
                            this.maxInclusive,
                            this.baseType.getMaxInclusiveFacet(),
                            ComparisonExpression.ComparisonOperator.GC_LT,
                            ExceptionMetadata.EMPTY_METADATA
                        ) > 0
                ) {
                    throw new InvalidSchemaException(
                            "Out of bounds maxInclusive facet.",
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }

            }
            if (this.minExclusive == null) {
                if (this.baseType.getAllowedFacets().contains(FacetTypes.MINEXCLUSIVE)) {
                    this.minExclusive = this.baseType.getMinExclusiveFacet();
                }
            } else {
                if (!this.primitiveType.getAllowedFacets().contains(FacetTypes.MINEXCLUSIVE)) {
                    throw new InvalidSchemaException(
                            "This facet is not applicable to " + this.primitiveType,
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }
                if (
                    this.baseType.getMinExclusiveFacet() != null
                        && ComparisonIterator.compareItems(
                            this.minExclusive,
                            this.baseType.getMinExclusiveFacet(),
                            ComparisonExpression.ComparisonOperator.GC_LT,
                            ExceptionMetadata.EMPTY_METADATA
                        ) > 0
                ) {
                    throw new InvalidSchemaException(
                            "Out of bounds minExclusive facet.",
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }
            }
            if (this.maxExclusive == null) {
                if (this.baseType.getAllowedFacets().contains(FacetTypes.MAXEXCLUSIVE)) {
                    this.maxExclusive = this.baseType.getMaxExclusiveFacet();
                }
            } else {
                if (!this.primitiveType.getAllowedFacets().contains(FacetTypes.MAXEXCLUSIVE)) {
                    throw new InvalidSchemaException(
                            "This facet is not applicable to " + this.primitiveType,
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }
                if (
                    this.baseType.getMaxExclusiveFacet() != null
                        && ComparisonIterator.compareItems(
                            this.maxExclusive,
                            this.baseType.getMaxExclusiveFacet(),
                            ComparisonExpression.ComparisonOperator.GC_LT,
                            ExceptionMetadata.EMPTY_METADATA
                        ) > 0
                ) {
                    throw new InvalidSchemaException(
                            "Out of bounds maxExclusive facet.",
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }
            }
            if (this.totalDigits == null) {
                if (this.baseType.getAllowedFacets().contains(FacetTypes.TOTALDIGITS)) {
                    this.totalDigits = this.baseType.getTotalDigitsFacet();
                }
            } else {
                if (!this.primitiveType.getAllowedFacets().contains(FacetTypes.TOTALDIGITS)) {
                    throw new InvalidSchemaException(
                            "This facet is not applicable to " + this.primitiveType,
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }
                if (
                    this.baseType.getTotalDigitsFacet() != null
                        && this.totalDigits > this.baseType.getTotalDigitsFacet()
                ) {
                    throw new InvalidSchemaException(
                            "Out of bounds totalDigits facet.",
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }
            }
            if (this.fractionDigits == null) {
                if (this.baseType.getAllowedFacets().contains(FacetTypes.FRACTIONDIGITS)) {
                    this.fractionDigits = this.baseType.getFractionDigitsFacet();
                }
            } else {
                if (!this.primitiveType.getAllowedFacets().contains(FacetTypes.FRACTIONDIGITS)) {
                    throw new InvalidSchemaException(
                            "This facet is not applicable to " + this.primitiveType,
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }
                /*
                 * if (this.fractionDigits > this.baseType.getFractionDigitsFacet()) {
                 * throw new InvalidSchemaException(
                 * "Out of bounds fractionDigits facet.",
                 * ExceptionMetadata.EMPTY_METADATA
                 * );
                 * }
                 */
            }

            return;
        }
        throw new InvalidSchemaException(
                "The base type of a user-defined atomic type must be an atomic type.",
                ExceptionMetadata.EMPTY_METADATA
        );
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.name);
        kryo.writeClassAndObject(output, this.baseType);
        kryo.writeClassAndObject(output, this.primitiveType);
        kryo.writeObject(output, this.typeTreeDepth);
        kryo.writeObject(output, this.isUserDefined);
        kryo.writeClassAndObject(output, this.minInclusive);
        kryo.writeClassAndObject(output, this.maxInclusive);
        kryo.writeClassAndObject(output, this.minExclusive);
        kryo.writeClassAndObject(output, this.maxExclusive);
        kryo.writeObjectOrNull(output, this.minLength, Integer.class);
        kryo.writeObjectOrNull(output, this.length, Integer.class);
        kryo.writeObjectOrNull(output, this.maxLength, Integer.class);
        kryo.writeObjectOrNull(output, this.totalDigits, Integer.class);
        kryo.writeObjectOrNull(output, this.fractionDigits, Integer.class);
        // kryo.writeObject(output, this.constraints);
        // kryo.writeObjectOrNull(output, this.enumeration, ArrayList.class);
        // kryo.writeObject(output, this.explicitTimezone);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.name = kryo.readObject(input, Name.class);
        this.baseType = (ItemType) kryo.readClassAndObject(input);
        this.primitiveType = (ItemType) kryo.readClassAndObject(input);
        this.typeTreeDepth = kryo.readObject(input, Integer.class);
        this.isUserDefined = kryo.readObject(input, Boolean.class);
        this.minInclusive = (Item) kryo.readClassAndObject(input);
        this.maxInclusive = (Item) kryo.readClassAndObject(input);
        this.minExclusive = (Item) kryo.readClassAndObject(input);
        this.maxExclusive = (Item) kryo.readClassAndObject(input);
        this.minLength = kryo.readObjectOrNull(input, Integer.class);
        this.length = kryo.readObjectOrNull(input, Integer.class);
        this.maxLength = kryo.readObjectOrNull(input, Integer.class);
        this.totalDigits = kryo.readObjectOrNull(input, Integer.class);
        this.fractionDigits = kryo.readObjectOrNull(input, Integer.class);
        // this.constraints = kryo.readObject(input, ArrayList.class);
        // this.enumeration = kryo.readObjectOrNull(input, ArrayList.class);
        // this.explicitTimezone = kryo.readObject(input, TimezoneFacet.class);
    }
}
