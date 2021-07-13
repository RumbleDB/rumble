package org.rumbledb.types;

import org.apache.spark.sql.types.DataTypes;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.IntItem;
import org.rumbledb.items.IntegerItem;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


public class BuiltinTypesCatalogue {
    public static final ItemType item = new ItemItemType(Name.createVariableInDefaultTypeNamespace("item"));

    public static final ItemType atomicItem = new AtomicItemType(
            new Name(Name.JS_NS, "js", "atomic"),
            Collections.emptySet(),
            DataTypes.BinaryType
    );
    public static final ItemType stringItem = new AtomicItemType(
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
    public static final ItemType numericItem = new AtomicItemType(
            new Name(Name.JS_NS, "js", "numeric"),
            Collections.emptySet(),
            DataTypes.BinaryType // TODO: consider if specific type is needed
    );

    public static final ItemType decimalItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "decimal"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.TOTALDIGITS,
                        FacetTypes.FRACTIONDIGITS
                    )
            ),
            DataTypes.createDecimalType()
    );
    public static final ItemType doubleItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "double"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXINCLUSIVE
                    )
            ),
            DataTypes.DoubleType
    );
    public static final ItemType floatItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "float"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXINCLUSIVE
                    )
            ),
            DataTypes.FloatType
    );

    public static final ItemType booleanItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "boolean"),
            new HashSet<>(Arrays.asList(FacetTypes.ENUMERATION, FacetTypes.CONSTRAINTS)),
            DataTypes.BooleanType
    );
    public static final ItemType nullItem = new AtomicItemType(
            new Name(Name.JS_NS, "js", "null"),
            Collections.emptySet(),
            DataTypes.NullType // TODO : see appropriate type
    );
    public static final ItemType durationItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "duration"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXINCLUSIVE
                    )
            ),
            DataTypes.BinaryType // TODO : appropriate datatype
    );
    public static final ItemType yearMonthDurationItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "yearMonthDuration"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXINCLUSIVE
                    )
            ),
            DataTypes.BinaryType // TODO : appropriate datatype
    );
    public static final ItemType dayTimeDurationItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "dayTimeDuration"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXINCLUSIVE
                    )
            ),
            DataTypes.BinaryType // TODO : appropriate datatype
    );
    public static final ItemType dateTimeItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "dateTime"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.EXPLICITTIMEZONE
                    )
            ),
            DataTypes.DateType
    );
    public static final ItemType dateTimeStampItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "dateTimeStamp"),
            dateTimeItem,
            AtomicItemType.dateTimeItem,
            new Facets(),
            false,
            DataTypes.DateType
    );
    public static final ItemType dateItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "date"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.EXPLICITTIMEZONE
                    )
            ),
            DataTypes.TimestampType
    );
    public static final ItemType timeItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "time"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.EXPLICITTIMEZONE
                    )
            ),
            DataTypes.TimestampType
    );
    public static final ItemType gDayItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "gDay"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.EXPLICITTIMEZONE
                    )
            ),
            DataTypes.DateType
    );
    public static final ItemType gMonthItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "gMonth"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.EXPLICITTIMEZONE
                    )
            ),
            DataTypes.DateType
    );
    public static final ItemType gYearItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "gYear"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.EXPLICITTIMEZONE
                    )
            ),
            DataTypes.DateType
    );
    public static final ItemType gMonthDayItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "gMonthDay"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.EXPLICITTIMEZONE
                    )
            ),
            DataTypes.DateType
    );
    public static final ItemType gYearMonthItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "gYearMonth"),
            new HashSet<>(
                    Arrays.asList(
                        FacetTypes.ENUMERATION,
                        FacetTypes.CONSTRAINTS,
                        FacetTypes.MININCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.MINEXCLUSIVE,
                        FacetTypes.MAXINCLUSIVE,
                        FacetTypes.EXPLICITTIMEZONE
                    )
            ),
            DataTypes.DateType
    );
    public static final ItemType hexBinaryItem = new AtomicItemType(
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
    public static final ItemType anyURIItem = new AtomicItemType(
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
    public static final ItemType base64BinaryItem = new AtomicItemType(
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

    public static final ItemType integerItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "integer"),
            AtomicItemType.decimalItem,
            AtomicItemType.decimalItem,
            Facets.getIntegerFacets(),
            false,
            DataTypes.createDecimalType() // TODO : how to support arbitrary-sized integer
    );

    public static final ItemType longItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "long"),
            integerItem,
            AtomicItemType.decimalItem,
            Facets.createMinMaxFacets(
                new IntegerItem(new BigInteger("-9223372036854775808")),
                new IntegerItem(new BigInteger("9223372036854775807")),
                true
            ),
            false,
            DataTypes.LongType // TODO : how to support arbitrary-sized integer
    );

    public static final ItemType intItem = new DerivedAtomicItemType(
            Name.createVariableInDefaultTypeNamespace("int"),
            longItem,
            AtomicItemType.decimalItem,
            Facets.createMinMaxFacets(new IntItem(-2147483648), new IntItem(2147483647), true),
            false,
            DataTypes.IntegerType // TODO : how to support arbitrary-sized integer
    );

    public static final ItemType shortItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "short"),
            intItem,
            AtomicItemType.decimalItem,
            Facets.createMinMaxFacets(new IntItem(-32768), new IntItem(32767), true),
            false,
            DataTypes.ShortType // TODO : how to support arbitrary-sized integer
    );

    static final DerivedAtomicItemType byteItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "byte"),
            intItem,
            AtomicItemType.decimalItem,
            Facets.createMinMaxFacets(new IntItem(-128), new IntItem(127), true),
            false,
            DataTypes.ByteType // TODO : how to support arbitrary-sized integer
    );

    static final DerivedAtomicItemType nonNegativeIntegerItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "nonNegativeInteger"),
            integerItem,
            AtomicItemType.decimalItem,
            Facets.createMinFacets(
                new IntegerItem(new BigInteger("0")),
                true
            ),
            false,
            DataTypes.IntegerType // TODO : how to support arbitrary-sized integer
    );

    static final DerivedAtomicItemType nonPositiveIntegerItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "nonPositiveInteger"),
            integerItem,
            AtomicItemType.decimalItem,
            Facets.createMaxFacets(
                new IntegerItem(new BigInteger("0")),
                true
            ),
            false,
            DataTypes.IntegerType // TODO : how to support arbitrary-sized integer
    );

    static final DerivedAtomicItemType negativeIntegerItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "negativeInteger"),
            nonPositiveIntegerItem,
            AtomicItemType.decimalItem,
            Facets.createMaxFacets(
                new IntegerItem(new BigInteger("-1")),
                true
            ),
            false,
            DataTypes.IntegerType // TODO : how to support arbitrary-sized integer
    );

    static final DerivedAtomicItemType positiveIntegerItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "positiveInteger"),
            nonNegativeIntegerItem,
            AtomicItemType.decimalItem,
            Facets.createMinFacets(
                new IntegerItem(new BigInteger("1")),
                true
            ),
            false,
            DataTypes.IntegerType // TODO : how to support arbitrary-sized integer
    );

    static final DerivedAtomicItemType unsignedIntItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "unsignedInt"),
            integerItem,
            AtomicItemType.decimalItem,
            Facets.createMinMaxFacets(
                new IntegerItem(new BigInteger("0")),
                new IntegerItem(new BigInteger("4294967295")),
                true
            ),
            false,
            DataTypes.IntegerType // TODO : how to support arbitrary-sized integer
    );

    static final DerivedAtomicItemType unsignedLongItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "unsignedLong"),
            integerItem,
            AtomicItemType.decimalItem,
            Facets.createMinMaxFacets(
                new IntegerItem(new BigInteger("0")),
                new IntegerItem(new BigInteger("18446744073709551615")),
                true
            ),
            false,
            DataTypes.LongType // TODO : how to support arbitrary-sized integer
    );

    static final DerivedAtomicItemType unsignedShortItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "unsignedShort"),
            integerItem,
            AtomicItemType.decimalItem,
            Facets.createMinMaxFacets(new IntItem(0), new IntItem(65535), true),
            false,
            DataTypes.IntegerType // TODO : how to support arbitrary-sized integer
    );

    static final DerivedAtomicItemType unsignedByteItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "unsignedByte"),
            integerItem,
            AtomicItemType.decimalItem,
            Facets.createMinMaxFacets(new IntItem(0), new IntItem(255), true),
            false,
            DataTypes.IntegerType // TODO : how to support arbitrary-sized integer
    );

    public static final ItemType JSONItem = new JsonItemType();
    public static final ItemType objectItem = new ObjectItemType(
            new Name(Name.JS_NS, "js", "object"),
            BuiltinTypesCatalogue.JSONItem,
            false,
            Collections.emptyMap(),
            Collections.emptyList(),
            null
    );
    public static final ItemType arrayItem = new ArrayItemType(
            new Name(Name.JS_NS, "js", "array"),
            BuiltinTypesCatalogue.JSONItem,
            null,
            null,
            null,
            null
    );
    public static final ItemType anyFunctionItem = new FunctionItemType(true);

    public static boolean typeExists(Name name) {
        for (ItemType builtInItemType : builtInItemTypes) {
            if (name.getNamespace() != null && name.getNamespace().equals(Name.JSONIQ_DEFAULT_TYPE_NS)) {
                if (builtInItemType.getName().getLocalName().equals(name.getLocalName())) {
                    return true;
                }
            } else {
                if (builtInItemType.getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static final List<ItemType> builtInItemTypes = Arrays.asList(
        objectItem,
        atomicItem,
        stringItem,
        integerItem,
        intItem,
        decimalItem,
        doubleItem,
        floatItem,
        numericItem,
        booleanItem,
        arrayItem,
        nullItem,
        JSONItem,
        durationItem,
        yearMonthDurationItem,
        dayTimeDurationItem,
        dateTimeItem,
        dateTimeStampItem,
        dateItem,
        timeItem,
        gDayItem,
        gMonthItem,
        gYearItem,
        gMonthDayItem,
        gYearMonthItem,
        hexBinaryItem,
        anyURIItem,
        base64BinaryItem,
        item,
        longItem,
        shortItem,
        byteItem,
        positiveIntegerItem,
        negativeIntegerItem,
        nonPositiveIntegerItem,
        nonNegativeIntegerItem,
        unsignedIntItem,
        unsignedLongItem,
        unsignedShortItem,
        unsignedByteItem
    );

    public static ItemType getItemTypeByName(Name name) {
        for (ItemType builtInItemType : builtInItemTypes) {
            if (name.getNamespace() != null && name.getNamespace().equals(Name.JSONIQ_DEFAULT_TYPE_NS)) {
                if (builtInItemType.getName().getLocalName().equals(name.getLocalName())) {
                    return builtInItemType;
                }
            } else {
                if (builtInItemType.getName().equals(name)) {
                    return builtInItemType;
                }
            }
        }
        throw new OurBadException("Type unrecognized: " + name + "(namespace: " + name.getNamespace() + ")");
    }
}

