/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rumbledb.xml.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

/** Converts a Xerces schema value into an XDM typed-value sequence. */
final class XercesTypedValueConverter {

    private final XmlSchemaTypeMapper typeMapper;

    XercesTypedValueConverter(XmlSchemaTypeMapper typeMapper) {
        this.typeMapper = Objects.requireNonNull(typeMapper, "typeMapper must not be null");
    }

    List<Item> convert(XSValue schemaValue) {
        if (schemaValue == null) {
            throw new OurBadException("A Xerces schema value cannot be null.");
        }
        XSSimpleTypeDefinition schemaType = schemaValue.getTypeDefinition();
        if (schemaType == null) {
            throw new OurBadException("Xerces did not identify the type of its schema value.");
        }

        return switch (schemaType.getVariety()) {
            case XSSimpleTypeDefinition.VARIETY_ABSENT -> List.of(
                    ItemFactory.getInstance().createUntypedAtomicItem(normalizedValue(schemaValue)));
            case XSSimpleTypeDefinition.VARIETY_ATOMIC -> List.of(
                    convertAtomicValue(normalizedValue(schemaValue), schemaValue.getActualValue(), schemaType));
            case XSSimpleTypeDefinition.VARIETY_LIST -> convertListValue(schemaValue, schemaType);
            case XSSimpleTypeDefinition.VARIETY_UNION -> convertUnionValue(schemaValue);
            default -> throw new OurBadException("Xerces returned an unknown XML Schema simple type variety.");
        };
    }

    private List<Item> convertListValue(XSValue schemaValue, XSSimpleTypeDefinition listType) {
        String normalizedValue = normalizedValue(schemaValue);
        if (normalizedValue.isEmpty()) {
            return List.of();
        }

        String[] lexicalItems = normalizedValue.split(" ");
        if (!(schemaValue.getActualValue() instanceof List<?> actualValues)
                || actualValues.size() != lexicalItems.length) {
            throw new OurBadException("Xerces did not provide the values of an XML Schema list.");
        }

        XSSimpleTypeDefinition itemType = listType.getItemType();
        XSObjectList selectedMemberTypes = schemaValue.getMemberTypeDefinitions();
        List<Item> result = new ArrayList<>(lexicalItems.length);
        for (int index = 0; index < lexicalItems.length; index++) {
            XSSimpleTypeDefinition atomicType = itemType;
            if (itemType.getVariety() == XSSimpleTypeDefinition.VARIETY_UNION) {
                if (selectedMemberTypes == null || index >= selectedMemberTypes.getLength()) {
                    throw new OurBadException("Xerces did not identify a member type for a union list value.");
                }
                if (!(selectedMemberTypes.item(index) instanceof XSSimpleTypeDefinition selectedType)) {
                    throw new OurBadException("Xerces returned an invalid member type for a union list value.");
                }
                atomicType = selectedType;
            }
            result.add(convertAtomicValue(lexicalItems[index], actualValues.get(index), atomicType));
        }
        return List.copyOf(result);
    }

    private List<Item> convertUnionValue(XSValue schemaValue) {
        XSSimpleTypeDefinition memberType = selectedUnionMember(schemaValue);
        return switch (memberType.getVariety()) {
            case XSSimpleTypeDefinition.VARIETY_ATOMIC -> List.of(
                    convertAtomicValue(normalizedValue(schemaValue), schemaValue.getActualValue(), memberType));
            case XSSimpleTypeDefinition.VARIETY_LIST -> convertListValue(schemaValue, memberType);
            default -> throw new OurBadException("Xerces selected an unsupported XML Schema union member type.");
        };
    }

    private Item convertAtomicValue(String lexicalValue, Object actualValue, XSSimpleTypeDefinition schemaType) {
        ItemType itemType = this.typeMapper
                .mapGeneralizedAtomicType(schemaType)
                .filter(ItemType::isAtomicItemType)
                .orElseThrow(() -> new OurBadException("Xerces did not identify an atomic schema value."));
        if (itemType.equals(BuiltinTypesCatalogue.atomicItem)) {
            throw new OurBadException("xs:anyAtomicType cannot label a concrete typed value.");
        }
        short primitiveKind = schemaType.getPrimitiveType().getBuiltInKind();
        Item result;
        if (primitiveKind == XSConstants.QNAME_DT) {
            result = convertQualifiedName(actualValue, false);
        } else if (primitiveKind == XSConstants.NOTATION_DT) {
            result = convertQualifiedName(actualValue, true);
        } else {
            result = convertLexicalValue(lexicalValue, itemType);
        }
        if (!result.getDynamicType().equals(itemType)) {
            result = ItemFactory.getInstance().createAnnotatedItem(result, itemType);
        }
        return result;
    }

    private static Item convertLexicalValue(String lexicalValue, ItemType itemType) {
        Item result = CastIterator.castItemToType(
                ItemFactory.getInstance().createUntypedAtomicItem(lexicalValue),
                itemType,
                ExceptionMetadata.EMPTY_METADATA);
        if (result == null) {
            throw new OurBadException("A Xerces-validated value could not be converted to a Rumble item.");
        }
        return result;
    }

    private static Item convertQualifiedName(Object actualValue, boolean notation) {
        if (!(actualValue instanceof XSQName qNameValue)) {
            throw new OurBadException("Xerces did not provide an expanded QName value.");
        }
        var qName = qNameValue.getXNIQName();
        String namespace = emptyToNull(qName.uri);
        String prefix = emptyToNull(qName.prefix);
        Name name = new Name(namespace, prefix, qName.localpart);
        return notation
                ? ItemFactory.getInstance().createNotationItem(name)
                : ItemFactory.getInstance().createQNameItem(name);
    }

    private static XSSimpleTypeDefinition selectedUnionMember(XSValue schemaValue) {
        XSSimpleTypeDefinition memberType = schemaValue.getMemberTypeDefinition();
        if (memberType == null) {
            throw new OurBadException("Xerces did not identify a member type for a union value.");
        }
        return memberType;
    }

    private static String normalizedValue(XSValue schemaValue) {
        String normalizedValue = schemaValue.getNormalizedValue();
        if (normalizedValue == null) {
            throw new OurBadException("Xerces did not provide a normalized lexical value.");
        }
        return normalizedValue;
    }

    private static String emptyToNull(String value) {
        return value == null || value.isEmpty() ? null : value;
    }
}
