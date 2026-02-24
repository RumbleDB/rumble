package org.rumbledb.types;

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
            new Name(Name.XS_NS, "xs", "anyAtomicType"),
            Collections.emptySet()
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
            )
    );

    // numeric is a union type for xs:double, xs:float, xs:decimal
    public static final ItemType numericItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "numeric"),
            Collections.emptySet()
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
                        FacetTypes.MAXEXCLUSIVE,
                        FacetTypes.TOTALDIGITS,
                        FacetTypes.FRACTIONDIGITS
                    )
            )
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
                        FacetTypes.MAXEXCLUSIVE
                    )
            )
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
                        FacetTypes.MAXEXCLUSIVE
                    )
            )
    );

    public static final ItemType booleanItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "boolean"),
            new HashSet<>(Arrays.asList(FacetTypes.ENUMERATION, FacetTypes.CONSTRAINTS))
    );
    public static final ItemType nullItem = new AtomicItemType(
            new Name(Name.JS_NS, "js", "null"),
            Collections.emptySet()
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
                        FacetTypes.MAXEXCLUSIVE
                    )
            )
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
                        FacetTypes.MAXEXCLUSIVE
                    )
            )
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
                        FacetTypes.MAXEXCLUSIVE
                    )
            )
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
                        FacetTypes.MAXEXCLUSIVE,
                        FacetTypes.EXPLICITTIMEZONE
                    )
            )
    );
    public static final ItemType dateTimeStampItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "dateTimeStamp"),
            dateTimeItem,
            dateTimeItem,
            Facets.createTimezoneFacets(TimezoneFacet.REQUIRED),
            false
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
                        FacetTypes.MAXEXCLUSIVE,
                        FacetTypes.EXPLICITTIMEZONE
                    )
            )
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
                        FacetTypes.MAXEXCLUSIVE,
                        FacetTypes.EXPLICITTIMEZONE
                    )
            )
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
                        FacetTypes.MAXEXCLUSIVE,
                        FacetTypes.EXPLICITTIMEZONE
                    )
            )
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
                        FacetTypes.MAXEXCLUSIVE,
                        FacetTypes.EXPLICITTIMEZONE
                    )
            )
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
                        FacetTypes.MAXEXCLUSIVE,
                        FacetTypes.EXPLICITTIMEZONE
                    )
            )
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
                        FacetTypes.MAXEXCLUSIVE,
                        FacetTypes.EXPLICITTIMEZONE
                    )
            )
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
                        FacetTypes.MAXEXCLUSIVE,
                        FacetTypes.EXPLICITTIMEZONE
                    )
            )
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
            )
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
            )
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
            )
    );

    public static final ItemType QNameItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "QName"),
            Collections.emptySet()
    );

    public static final ItemType NOTATIONItem = new AtomicItemType(
            new Name(Name.XS_NS, "xs", "NOTATION"),
            Collections.emptySet()
    );

    // String-derived types per XDM 3.1 hierarchy:
    // string -> normalizedString -> token -> (language, NMTOKEN, Name -> NCName -> ID, IDREF, ENTITY)

    public static final ItemType normalizedStringItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "normalizedString"),
            stringItem,
            stringItem,
            // TODO : add facets
            new Facets(),
            false
    );

    public static final ItemType tokenItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "token"),
            normalizedStringItem,
            stringItem,
            // TODO : add facets
            new Facets(),
            false
    );

    public static final ItemType languageItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "language"),
            tokenItem,
            stringItem,
            // TODO : add facets
            new Facets(),
            false
    );

    public static final ItemType NMTOKENItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "NMTOKEN"),
            tokenItem,
            stringItem,
            // TODO : add facets
            new Facets(),
            false
    );

    public static final ItemType NameItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "Name"),
            tokenItem,
            stringItem,
            // TODO : add facets
            new Facets(),
            false
    );

    public static final ItemType NCNameItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "NCName"),
            NameItem,
            stringItem,
            // TODO : add facets
            new Facets(),
            false
    );

    public static final ItemType IDItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "ID"),
            NCNameItem,
            stringItem,
            // TODO : add facets
            new Facets(),
            false
    );

    public static final ItemType IDREFItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "IDREF"),
            NCNameItem,
            stringItem,
            // TODO : add facets
            new Facets(),
            false
    );

    public static final ItemType ENTITYItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "ENTITY"),
            NCNameItem,
            stringItem,
            // TODO : add facets
            new Facets(),
            false
    );

    public static final ItemType integerItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "integer"),
            decimalItem,
            decimalItem,
            Facets.getIntegerFacets(),
            false
    );

    public static final ItemType longItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "long"),
            integerItem,
            decimalItem,
            Facets.createMinMaxFacets(
                new IntegerItem(new BigInteger("-9223372036854775808")),
                new IntegerItem(new BigInteger("9223372036854775807")),
                true
            ),
            false
    );

    public static final ItemType intItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "int"),
            longItem,
            decimalItem,
            Facets.createMinMaxFacets(new IntItem(-2147483648), new IntItem(2147483647), true),
            false
    );

    public static final ItemType shortItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "short"),
            intItem,
            decimalItem,
            Facets.createMinMaxFacets(new IntItem(-32768), new IntItem(32767), true),
            false
    );

    public static final DerivedAtomicItemType byteItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "byte"),
            shortItem,
            decimalItem,
            Facets.createMinMaxFacets(new IntItem(-128), new IntItem(127), true),
            false
    );

    public static final DerivedAtomicItemType nonNegativeIntegerItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "nonNegativeInteger"),
            integerItem,
            decimalItem,
            Facets.createMinFacets(
                new IntegerItem(new BigInteger("0")),
                true
            ),
            false
    );

    public static final DerivedAtomicItemType nonPositiveIntegerItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "nonPositiveInteger"),
            integerItem,
            decimalItem,
            Facets.createMaxFacets(
                new IntegerItem(new BigInteger("0")),
                true
            ),
            false
    );

    public static final DerivedAtomicItemType negativeIntegerItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "negativeInteger"),
            nonPositiveIntegerItem,
            decimalItem,
            Facets.createMaxFacets(
                new IntegerItem(new BigInteger("-1")),
                true
            ),
            false
    );

    public static final DerivedAtomicItemType positiveIntegerItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "positiveInteger"),
            nonNegativeIntegerItem,
            decimalItem,
            Facets.createMinFacets(
                new IntegerItem(new BigInteger("1")),
                true
            ),
            false
    );

    public static final DerivedAtomicItemType unsignedLongItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "unsignedLong"),
            nonNegativeIntegerItem,
            decimalItem,
            Facets.createMinMaxFacets(
                new IntegerItem(new BigInteger("0")),
                new IntegerItem(new BigInteger("18446744073709551615")),
                true
            ),
            false
    );

    public static final DerivedAtomicItemType unsignedIntItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "unsignedInt"),
            unsignedLongItem,
            decimalItem,
            Facets.createMinMaxFacets(
                new IntegerItem(new BigInteger("0")),
                new IntegerItem(new BigInteger("4294967295")),
                true
            ),
            false
    );

    public static final DerivedAtomicItemType unsignedShortItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "unsignedShort"),
            unsignedIntItem,
            decimalItem,
            Facets.createMinMaxFacets(new IntItem(0), new IntItem(65535), true),
            false
    );

    public static final DerivedAtomicItemType unsignedByteItem = new DerivedAtomicItemType(
            new Name(Name.XS_NS, "xs", "unsignedByte"),
            unsignedShortItem,
            decimalItem,
            Facets.createMinMaxFacets(new IntItem(0), new IntItem(255), true),
            false
    );

    // XML node types (per XPath Data Model 3.1, Section 2.7.4)
    public static final ItemType nodeItem = new NodeItemType();
    public static final ItemType elementNode = new XmlNodeItemType(
            Name.createVariableInDefaultTypeNamespace("element")
    );
    public static final ItemType attributeNode = new XmlNodeItemType(
            Name.createVariableInDefaultTypeNamespace("attribute")
    );
    public static final ItemType documentNode = new XmlNodeItemType(
            Name.createVariableInDefaultTypeNamespace("document-node")
    );
    public static final ItemType commentNode = new XmlNodeItemType(
            Name.createVariableInDefaultTypeNamespace("comment")
    );
    public static final ItemType textNode = new XmlNodeItemType(
            Name.createVariableInDefaultTypeNamespace("text")
    );
    public static final ItemType namespaceNode = new XmlNodeItemType(
            Name.createVariableInDefaultTypeNamespace("namespace-node")
    );
    public static final ItemType processingInstructionNode = new XmlNodeItemType(
            Name.createVariableInDefaultTypeNamespace("processing-instruction")
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
            BuiltinTypesCatalogue.item,
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
        nodeItem,
        elementNode,
        attributeNode,
        documentNode,
        commentNode,
        textNode,
        namespaceNode,
        processingInstructionNode,
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
        unsignedByteItem,
        QNameItem,
        NOTATIONItem,
        normalizedStringItem,
        tokenItem,
        languageItem,
        NMTOKENItem,
        NameItem,
        NCNameItem,
        IDItem,
        IDREFItem,
        ENTITYItem
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

