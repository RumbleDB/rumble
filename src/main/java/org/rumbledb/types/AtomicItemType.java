package org.rumbledb.types;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.Name;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.rumbledb.types.BuiltinTypesCatalogue.*;

/**
 * This class describes all the primitive built-in atomic types in the JSONiq data model.
 */
public class AtomicItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    private Name name;
    private Set<ConstrainingFacetTypes> allowedFacets;
    private WhitespaceFacet whiteSpace;

    private OrderedFacetValue ordered;
    private Boolean bounded;
    private CardinalityFacetValue cardinality;
    private Boolean numeric;
    /**
     * Regular expressions describing the lexical space of this primitive type.
     * An empty list means that no additional regex-based restriction is modeled.
     */
    private List<String> lexicalSpacePatterns;

    public AtomicItemType() {
    }

    AtomicItemType(Name name, Set<ConstrainingFacetTypes> allowedFacets) {
        this(name, allowedFacets, WhitespaceFacet.COLLAPSE, null);
    }

    AtomicItemType(
            Name name,
            Set<ConstrainingFacetTypes> allowedFacets,
            WhitespaceFacet whiteSpace
    ) {
        this(name, allowedFacets, whiteSpace, null);
    }

    AtomicItemType(
            Name name,
            Set<ConstrainingFacetTypes> allowedFacets,
            WhitespaceFacet whiteSpace,
            List<String> lexicalSpacePatterns
    ) {
        this.name = name;
        this.allowedFacets = allowedFacets;
        this.whiteSpace = whiteSpace;
        this.lexicalSpacePatterns = lexicalSpacePatterns == null ? Collections.emptyList() : lexicalSpacePatterns;
    }

    AtomicItemType(
            Name name,
            Set<ConstrainingFacetTypes> allowedFacets,
            WhitespaceFacet whiteSpace,
            OrderedFacetValue ordered,
            Boolean bounded,
            CardinalityFacetValue cardinality,
            Boolean numeric,
            List<String> lexicalSpacePatterns
    ) {
        this.name = name;
        this.allowedFacets = allowedFacets;
        this.whiteSpace = whiteSpace;
        this.ordered = ordered;
        this.bounded = bounded;
        this.cardinality = cardinality;
        this.numeric = numeric;
        this.lexicalSpacePatterns = lexicalSpacePatterns == null ? Collections.emptyList() : lexicalSpacePatterns;
    }

    @Override
    public void write(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Output output) {
        kryo.writeObjectOrNull(output, this.name, Name.class);
        kryo.writeObjectOrNull(output, this.allowedFacets, HashSet.class);
        kryo.writeObjectOrNull(output, this.whiteSpace, WhitespaceFacet.class);
        kryo.writeObjectOrNull(output, this.ordered, OrderedFacetValue.class);
        kryo.writeObjectOrNull(output, this.bounded, Boolean.class);
        kryo.writeObjectOrNull(output, this.cardinality, CardinalityFacetValue.class);
        kryo.writeObjectOrNull(output, this.numeric, Boolean.class);
        kryo.writeObjectOrNull(output, this.lexicalSpacePatterns, java.util.ArrayList.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Input input) {
        this.name = kryo.readObjectOrNull(input, Name.class);
        this.allowedFacets = kryo.readObjectOrNull(input, HashSet.class);
        this.whiteSpace = kryo.readObjectOrNull(input, WhitespaceFacet.class);
        this.ordered = kryo.readObjectOrNull(input, OrderedFacetValue.class);
        this.bounded = kryo.readObjectOrNull(input, Boolean.class);
        this.cardinality = kryo.readObjectOrNull(input, CardinalityFacetValue.class);
        this.numeric = kryo.readObjectOrNull(input, Boolean.class);
        this.lexicalSpacePatterns = kryo.readObjectOrNull(input, java.util.ArrayList.class);
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
        } else if (this.isNumeric()) {
            return 3;
        } else {
            return 2;
        }
    }

    @Override
    public ItemType getBaseType() {
        if (this.equals(atomicItem)) {
            return BuiltinTypesCatalogue.item;
        } else if (this.isNumeric()) {
            return numericItem;
        } else {
            return atomicItem;
        }
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public ItemType getPrimitiveType() {
        return this;
    }

    @Override
    public boolean isCastingPrimitive() {
        return true;
    }

    @Override
    public ItemType getCastingPrimitiveType() {
        return this;
    }

    /**
     * Primitive-type castability matrix from XPath/XQuery F&O 3.1 §19.1
     * "Casting from primitive types to primitive types" (table rows 15281–15303
     * in the W3C Recommendation text).
     *
     * The table is expressed in terms of the short codes defined by the spec:
     *
     * uA = xs:untypedAtomic
     * str = xs:string
     * flt = xs:float
     * dbl = xs:double
     * dec = xs:decimal
     * int = xs:integer
     * dur = xs:duration
     * yMD = xs:yearMonthDuration
     * dTD = xs:dayTimeDuration
     * dT = xs:dateTime
     * tim = xs:time
     * dat = xs:date
     * gYM = xs:gYearMonth
     * gYr = xs:gYear
     * gMD = xs:gMonthDay
     * gDay = xs:gDay
     * gMon = xs:gMonth
     * bool = xs:boolean
     * b64 = xs:base64Binary
     * hxB = xs:hexBinary
     * aURI = xs:anyURI
     * QN = xs:QName
     * NOT = xs:NOTATION
     *
     * The matrix below uses rows as targets (T) and columns as sources (S),
     * following the spec's "S\\T" convention. Each cell contains:
     * 'Y' — cast always supported,
     * 'M' — cast may succeed or fail depending on the value,
     * 'N' — cast is never supported.
     *
     * In this implementation we treat both 'Y' and 'M' as statically
     * castable (returning true from isStaticallyCastableAs), and 'N' as
     * statically non-castable.
     *
     * NOTE: While the spec matrix includes namespace-sensitive types
     * xs:QName and xs:NOTATION, RumbleDB does not currently support casts
     * to these types. We therefore override the matrix for such targets
     * and report them as not statically castable, even when the matrix
     * entry would otherwise be 'Y' or 'M'.
     */
    private static final int PRIM_UA = 0;
    private static final int PRIM_STR = 1;
    private static final int PRIM_FLT = 2;
    private static final int PRIM_DBL = 3;
    private static final int PRIM_DEC = 4;
    private static final int PRIM_INT = 5;
    private static final int PRIM_DUR = 6;
    private static final int PRIM_YMD = 7;
    private static final int PRIM_DTD = 8;
    private static final int PRIM_DT = 9;
    private static final int PRIM_TIM = 10;
    private static final int PRIM_DAT = 11;
    private static final int PRIM_GYM = 12;
    private static final int PRIM_GYR = 13;
    private static final int PRIM_GMD = 14;
    private static final int PRIM_GDAY = 15;
    private static final int PRIM_GMON = 16;
    private static final int PRIM_BOOL = 17;
    private static final int PRIM_B64 = 18;
    private static final int PRIM_HXB = 19;
    private static final int PRIM_AURI = 20;
    private static final int PRIM_QN = 21;
    private static final int PRIM_NOT = 22;

    // PRIMITIVE_CAST_MATRIX[targetIndex][sourceIndex]
    // uA str flt dbl dec int dur yMD dTD dT tim dat gYM gYr gMD gDay gMon bool b64 hxB aURI QN NOT
    private static final char[][] PRIMITIVE_CAST_MATRIX = {
        /* uA */ {
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y' },
        /* str */ {
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y',
            'Y' },
        /* flt */ {
            'M',
            'M',
            'Y',
            'Y',
            'Y',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N' },
        /* dbl */ {
            'M',
            'M',
            'Y',
            'Y',
            'Y',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N' },
        /* dec */ {
            'M',
            'M',
            'M',
            'M',
            'Y',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N' },
        /* int */ {
            'M',
            'M',
            'M',
            'M',
            'Y',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N' },
        /* dur */ {
            'M',
            'M',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'Y',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N' },
        /* yMD */ {
            'M',
            'M',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'Y',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N' },
        /* dTD */ {
            'M',
            'M',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'Y',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N' },
        /* dT */ {
            'M',
            'M',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'N',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N' },
        /* tim */ {
            'M',
            'M',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N' },
        /* dat */ {
            'M',
            'M',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'N',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N' },
        /* gYM */ {
            'M',
            'M',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'N',
            'Y',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N' },
        /* gYr */ {
            'M',
            'M',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'N',
            'Y',
            'N',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N' },
        /* gMD */ {
            'M',
            'M',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'N',
            'Y',
            'N',
            'N',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N' },
        /* gDay */ {
            'M',
            'M',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'N',
            'Y',
            'N',
            'N',
            'N',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N' },
        /* gMon */ {
            'M',
            'M',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'N',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N' },
        /* bool */ {
            'M',
            'M',
            'Y',
            'Y',
            'Y',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'N',
            'N',
            'N',
            'N',
            'N' },
        /* b64 */ {
            'M',
            'M',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'Y',
            'N',
            'N',
            'N' },
        /* hxB */ {
            'M',
            'M',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'Y',
            'N',
            'N',
            'N' },
        /* aURI */ {
            'M',
            'M',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'N',
            'N' },
        /* QN */ {
            'M',
            'M',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'Y',
            'Y' },
        /* NOT */ {
            'M',
            'M',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'N',
            'M',
            'M' }
    };

    private static int primitiveIndex(ItemType primitiveType) {
        if (primitiveType.equals(untypedAtomicItem)) {
            return PRIM_UA;
        }
        if (primitiveType.equals(stringItem)) {
            return PRIM_STR;
        }
        if (primitiveType.equals(floatItem)) {
            return PRIM_FLT;
        }
        if (primitiveType.equals(doubleItem)) {
            return PRIM_DBL;
        }
        if (primitiveType.equals(decimalItem)) {
            return PRIM_DEC;
        }
        if (primitiveType.equals(integerItem)) {
            return PRIM_INT;
        }
        if (primitiveType.equals(durationItem)) {
            return PRIM_DUR;
        }
        if (primitiveType.equals(yearMonthDurationItem)) {
            return PRIM_YMD;
        }
        if (primitiveType.equals(dayTimeDurationItem)) {
            return PRIM_DTD;
        }
        if (primitiveType.equals(dateTimeItem)) {
            return PRIM_DT;
        }
        if (primitiveType.equals(timeItem)) {
            return PRIM_TIM;
        }
        if (primitiveType.equals(dateItem)) {
            return PRIM_DAT;
        }
        if (primitiveType.equals(gYearMonthItem)) {
            return PRIM_GYM;
        }
        if (primitiveType.equals(gYearItem)) {
            return PRIM_GYR;
        }
        if (primitiveType.equals(gMonthDayItem)) {
            return PRIM_GMD;
        }
        if (primitiveType.equals(gDayItem)) {
            return PRIM_GDAY;
        }
        if (primitiveType.equals(gMonthItem)) {
            return PRIM_GMON;
        }
        if (primitiveType.equals(booleanItem)) {
            return PRIM_BOOL;
        }
        if (primitiveType.equals(base64BinaryItem)) {
            return PRIM_B64;
        }
        if (primitiveType.equals(hexBinaryItem)) {
            return PRIM_HXB;
        }
        if (primitiveType.equals(anyURIItem)) {
            return PRIM_AURI;
        }
        if (primitiveType.equals(QNameItem)) {
            return PRIM_QN;
        }
        if (primitiveType.equals(NOTATIONItem)) {
            return PRIM_NOT;
        }
        return -1;
    }

    static boolean isCastableBetweenCastingPrimitives(ItemType sourcePrimitive, ItemType targetPrimitive) {
        // anything can be casted to itself
        if (sourcePrimitive.equals(targetPrimitive)) {
            return true;
        }

        // xs:anyAtomicType (atomicItem) is abstract and must not be a cast target.
        if (targetPrimitive.equals(atomicItem)) {
            return false;
        }

        // Namespace-sensitive target types (xs:QName, xs:NOTATION) are never valid cast
        // targets in this implementation (beyond the identity case handled above),
        // even though the spec matrix contains entries for them.
        if (targetPrimitive.equals(QNameItem) || targetPrimitive.equals(NOTATIONItem)) {
            return false;
        }

        int sourceIndex = primitiveIndex(sourcePrimitive);
        int targetIndex = primitiveIndex(targetPrimitive);

        if (sourceIndex != -1 && targetIndex != -1) {
            char entry = PRIMITIVE_CAST_MATRIX[targetIndex][sourceIndex];
            return entry == 'Y' || entry == 'M';
        }

        // Fallback rules for item types that are outside the F&O primitive matrix
        // (for example, js:null) while preserving sensible behavior.

        // anything can be casted from and to a string or untypedAtomic (or from one of its supertypes),
        // including types derived by restriction from them (via the primitive type).
        if (
            sourcePrimitive.equals(stringItem)
                || targetPrimitive.equals(stringItem)
                || sourcePrimitive.equals(untypedAtomicItem)
                || targetPrimitive.equals(untypedAtomicItem)
        ) {
            return true;
        }

        // boolean and numeric can be cast between themselves
        if (sourcePrimitive.equals(booleanItem) || sourcePrimitive.isNumeric()) {
            return targetPrimitive.equals(booleanItem) || targetPrimitive.isNumeric();
        }

        // base64 and hex can be cast between themselves
        if (sourcePrimitive.equals(base64BinaryItem) || sourcePrimitive.equals(hexBinaryItem)) {
            return targetPrimitive.equals(base64BinaryItem) || targetPrimitive.equals(hexBinaryItem);
        }

        // durations can be cast between themselves
        if (sourcePrimitive.isSubtypeOf(durationItem)) {
            return targetPrimitive.isSubtypeOf(durationItem);
        }

        // Otherwise this cannot be casted to other
        return false;
    }

    @Override
    public boolean isStaticallyCastableAs(ItemType other) {
        // Normalize using casting-specific primitive semantics (F&O §19).
        ItemType sourcePrimitive = this.getCastingPrimitiveType();
        ItemType targetPrimitive = other.getCastingPrimitiveType();
        return isCastableBetweenCastingPrimitives(sourcePrimitive, targetPrimitive);
    }

    @Override
    public boolean isNumeric() {
        ItemType primitive = this.getPrimitiveType();
        return primitive.equals(decimalItem)
            || primitive.equals(floatItem)
            || primitive.equals(doubleItem);
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
    public Set<ConstrainingFacetTypes> getAllowedFacets() {
        return this.allowedFacets;
    }

    @Override
    public List<Item> getEnumerationFacet() {
        if (!this.getAllowedFacets().contains(ConstrainingFacetTypes.ENUMERATION)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the enumeration facet"
            );
        }
        return null;
    }

    @Override
    public List<String> getConstraintsFacet() {
        if (!this.getAllowedFacets().contains(ConstrainingFacetTypes.CONSTRAINTS)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the constraints facet"
            );
        }
        return Collections.emptyList();
    }

    @Override
    public Integer getMinLengthFacet() {
        if (!this.getAllowedFacets().contains(ConstrainingFacetTypes.MINLENGTH)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the minimum length facet"
            );
        }
        return null;
    }

    @Override
    public Integer getLengthFacet() {
        if (!this.getAllowedFacets().contains(ConstrainingFacetTypes.LENGTH)) {
            throw new UnsupportedOperationException(this.toString() + " item type does not support the length facet");
        }
        return null;
    }

    @Override
    public Integer getMaxLengthFacet() {
        if (!this.getAllowedFacets().contains(ConstrainingFacetTypes.MAXLENGTH)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the maximum length facet"
            );
        }
        return null;
    }

    @Override
    public Item getMinExclusiveFacet() {
        if (!this.getAllowedFacets().contains(ConstrainingFacetTypes.MINEXCLUSIVE)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the minimum exclusive facet"
            );
        }
        return null;
    }

    @Override
    public Item getMinInclusiveFacet() {
        if (!this.getAllowedFacets().contains(ConstrainingFacetTypes.MININCLUSIVE)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the minimum inclusive facet"
            );
        }
        return null;
    }

    @Override
    public Item getMaxExclusiveFacet() {
        if (!this.getAllowedFacets().contains(ConstrainingFacetTypes.MAXEXCLUSIVE)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the maximum exclusive facet"
            );
        }
        return null;
    }

    @Override

    public Item getMaxInclusiveFacet() {
        if (!this.getAllowedFacets().contains(ConstrainingFacetTypes.MAXINCLUSIVE)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the maximum inclusive facet"
            );
        }
        return null;
    }

    @Override
    public Integer getTotalDigitsFacet() {
        if (!this.getAllowedFacets().contains(ConstrainingFacetTypes.TOTALDIGITS)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the total digits facet"
            );
        }
        return null;
    }

    @Override
    public Integer getFractionDigitsFacet() {
        if (!this.getAllowedFacets().contains(ConstrainingFacetTypes.FRACTIONDIGITS)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the fraction digits facet"
            );
        }
        return null;
    }

    @Override
    public TimezoneFacet getExplicitTimezoneFacet() {
        if (!this.getAllowedFacets().contains(ConstrainingFacetTypes.EXPLICITTIMEZONE)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the explicit timezone facet"
            );
        }
        return null;
    }

    @Override
    public WhitespaceFacet getWhitespaceFacet() {
        return this.whiteSpace;
    }

    @Override
    public List<String> getPatternFacet() {
        if (!this.getAllowedFacets().contains(ConstrainingFacetTypes.PATTERN)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the pattern facet"
            );
        }
        return null;
    }

    @Override
    public OrderedFacetValue getOrderedFacet() {
        return this.ordered;
    }

    @Override
    public Boolean getBoundedFacet() {
        return this.bounded;
    }

    @Override
    public CardinalityFacetValue getCardinalityFacet() {
        return this.cardinality;
    }

    @Override
    public Boolean getNumericFacet() {
        return this.numeric;
    }

    @Override
    public List<String> getLexicalSpacePatterns() {
        return this.lexicalSpacePatterns;
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
        if (this.getPrimitiveType().equals(untypedAtomicItem)) {
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
