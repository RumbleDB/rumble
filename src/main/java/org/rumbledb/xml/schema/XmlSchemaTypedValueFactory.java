/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.xml.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.ItemPSVI;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSValue;
import org.apache.xerces.xs.datatypes.XSQName;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

/** Builds the XDM typed value contributed by Xerces PSVI information. */
final class XmlSchemaTypedValueFactory {

    private final XmlSchemaTypeMapper typeMapper;

    XmlSchemaTypedValueFactory(XmlSchemaTypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    Optional<List<Item>> create(ItemPSVI psvi, String stringValue) {
        if (psvi instanceof ElementPSVI elementPsvi && elementPsvi.getNil()) {
            return Optional.of(List.of());
        }
        if (psvi.getTypeDefinition() instanceof XSComplexTypeDefinition complexType) {
            switch (complexType.getContentType()) {
                case XSComplexTypeDefinition.CONTENTTYPE_EMPTY:
                    return Optional.of(List.of());
                case XSComplexTypeDefinition.CONTENTTYPE_MIXED:
                    return Optional.of(
                        List.of(ItemFactory.getInstance().createUntypedAtomicItem(stringValue))
                    );
                case XSComplexTypeDefinition.CONTENTTYPE_ELEMENT:
                    return Optional.empty();
                case XSComplexTypeDefinition.CONTENTTYPE_SIMPLE:
                    break;
                default:
                    throw new OurBadException("Xerces returned an unknown complex content type.");
            }
        }

        XSValue schemaValue = psvi.getSchemaValue();
        XSSimpleTypeDefinition simpleType = simpleTypeOf(psvi);
        if (schemaValue == null || simpleType == null) {
            throw new OurBadException("Xerces did not provide the typed value for a simple schema type.");
        }
        if (simpleType.getVariety() == XSSimpleTypeDefinition.VARIETY_UNION) {
            return Optional.of(
                List.of(
                    atomicValue(
                        schemaValue.getNormalizedValue(),
                        schemaValue.getActualValue(),
                        selectedMemberType(psvi, schemaValue)
                    )
                )
            );
        }
        return Optional.of(create(schemaValue, simpleType));
    }

    List<Item> create(XSValue schemaValue, XSSimpleTypeDefinition simpleType) {
        if (schemaValue == null) {
            throw new OurBadException("Xerces did not provide a schema value for a defaulted attribute.");
        }
        if (hasUntypedAtomicValue(simpleType)) {
            return List.of(
                ItemFactory.getInstance().createUntypedAtomicItem(schemaValue.getNormalizedValue())
            );
        }
        return switch (simpleType.getVariety()) {
            case XSSimpleTypeDefinition.VARIETY_ATOMIC -> List.of(
                atomicValue(schemaValue.getNormalizedValue(), schemaValue.getActualValue(), simpleType)
            );
            case XSSimpleTypeDefinition.VARIETY_LIST -> listValue(schemaValue, simpleType);
            case XSSimpleTypeDefinition.VARIETY_UNION -> List.of(
                atomicValue(
                    schemaValue.getNormalizedValue(),
                    schemaValue.getActualValue(),
                    schemaValue.getMemberTypeDefinition()
                )
            );
            default -> throw new OurBadException("Xerces returned an unknown simple type variety.");
        };
    }

    private List<Item> listValue(XSValue schemaValue, XSSimpleTypeDefinition listType) {
        String normalizedValue = schemaValue.getNormalizedValue();
        if (normalizedValue.isEmpty()) {
            return List.of();
        }

        String[] lexicalItems = normalizedValue.split(" ");
        XSSimpleTypeDefinition itemType = listType.getItemType();
        XSObjectList memberTypes = schemaValue.getMemberTypeDefinitions();
        List<?> actualValues = null;
        List<Item> result = new ArrayList<>(lexicalItems.length);
        for (int index = 0; index < lexicalItems.length; index++) {
            XSSimpleTypeDefinition atomicType = itemType;
            if (itemType.getVariety() == XSSimpleTypeDefinition.VARIETY_UNION) {
                if (memberTypes == null || index >= memberTypes.getLength()) {
                    throw new OurBadException("Xerces did not identify a member type for a union list value.");
                }
                atomicType = (XSSimpleTypeDefinition) memberTypes.item(index);
            }
            Object actualValue = null;
            if (isQNameOrNotation(atomicType)) {
                if (actualValues == null) {
                    actualValues = listActualValues(schemaValue, lexicalItems.length);
                }
                actualValue = actualValues.get(index);
            }
            result.add(atomicValue(lexicalItems[index], actualValue, atomicType));
        }
        return result;
    }

    private Item atomicValue(String lexicalValue, Object actualValue, XSSimpleTypeDefinition schemaType) {
        if (schemaType == null) {
            throw new OurBadException("Xerces did not identify the atomic type of a schema value.");
        }
        ItemType itemType = this.typeMapper.getAtomicType(schemaType)
            .orElseThrow(() -> new OurBadException("Xerces returned a non-atomic schema value."));
        if (isQNameOrNotation(schemaType)) {
            return qualifiedNameValue(actualValue, itemType, schemaType.getPrimitiveType().getBuiltInKind());
        }
        return CastIterator.castItemToType(
            ItemFactory.getInstance().createUntypedAtomicItem(lexicalValue),
            itemType,
            ExceptionMetadata.EMPTY_METADATA
        );
    }

    private static List<?> listActualValues(XSValue schemaValue, int expectedSize) {
        if (
            !(schemaValue.getActualValue() instanceof List<?> actualValues)
                || actualValues.size() != expectedSize
        ) {
            throw new OurBadException("Xerces did not provide the values of an XML Schema list.");
        }
        return actualValues;
    }

    private static Item qualifiedNameValue(Object actualValue, ItemType itemType, short primitiveKind) {
        if (!(actualValue instanceof XSQName xsQName)) {
            throw new OurBadException("Xerces did not provide an expanded QName schema value.");
        }
        var qName = xsQName.getXNIQName();
        String namespace = qName.uri == null || qName.uri.isEmpty() ? null : qName.uri;
        String prefix = qName.prefix == null || qName.prefix.isEmpty() ? null : qName.prefix;
        Name name = new Name(namespace, prefix, qName.localpart);
        ItemType primitiveType;
        Item value;
        if (primitiveKind == XSConstants.NOTATION_DT) {
            primitiveType = BuiltinTypesCatalogue.NOTATIONItem;
            value = ItemFactory.getInstance().createNotationItem(name);
        } else {
            primitiveType = BuiltinTypesCatalogue.QNameItem;
            value = ItemFactory.getInstance().createQNameItem(name);
        }
        return itemType.equals(primitiveType)
            ? value
            : ItemFactory.getInstance().createAnnotatedItem(value, itemType);
    }

    private static boolean isQNameOrNotation(XSSimpleTypeDefinition schemaType) {
        short builtInKind = schemaType.getPrimitiveType().getBuiltInKind();
        return builtInKind == XSConstants.QNAME_DT || builtInKind == XSConstants.NOTATION_DT;
    }

    private static XSSimpleTypeDefinition selectedMemberType(ItemPSVI psvi, XSValue schemaValue) {
        XSSimpleTypeDefinition memberType = psvi.getMemberTypeDefinition();
        return memberType == null ? schemaValue.getMemberTypeDefinition() : memberType;
    }

    private static XSSimpleTypeDefinition simpleTypeOf(ItemPSVI psvi) {
        if (psvi.getTypeDefinition() instanceof XSSimpleTypeDefinition simpleType) {
            return simpleType;
        }
        if (
            psvi.getTypeDefinition() instanceof XSComplexTypeDefinition complexType
                && complexType.getContentType() == XSComplexTypeDefinition.CONTENTTYPE_SIMPLE
        ) {
            return complexType.getSimpleType();
        }
        return null;
    }

    private static boolean hasUntypedAtomicValue(XSSimpleTypeDefinition simpleType) {
        if (simpleType.getVariety() == XSSimpleTypeDefinition.VARIETY_ABSENT) {
            return true;
        }
        return Name.XS_NS.equals(simpleType.getNamespace()) && "anyAtomicType".equals(simpleType.getName());
    }
}
