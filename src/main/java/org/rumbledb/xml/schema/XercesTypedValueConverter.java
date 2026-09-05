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

import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSValue;
import org.apache.xerces.xs.datatypes.XSQName;

import lombok.NonNull;

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

    @NonNull private final XmlSchemaTypeMapper typeMapper;

    XercesTypedValueConverter(@NonNull XmlSchemaTypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    List<Item> convert(@NonNull XSValue schemaValue) {
        XSSimpleTypeDefinition schemaType = schemaValue.getTypeDefinition();
        if (schemaType == null) {
            throw new OurBadException("Xerces did not identify the type of its schema value.");
        }

        return switch (schemaType.getVariety()) {
            case XSSimpleTypeDefinition.VARIETY_ABSENT -> List.of(
                    ItemFactory.getInstance().createUntypedAtomicItem(normalizedValue(schemaValue)));
            case XSSimpleTypeDefinition.VARIETY_ATOMIC -> List.of(
                    this.convertAtomicValue(normalizedValue(schemaValue), schemaValue.getActualValue(), schemaType));
            case XSSimpleTypeDefinition.VARIETY_LIST -> this.convertListValue(schemaValue, schemaType);
            case XSSimpleTypeDefinition.VARIETY_UNION -> List.of(this.convertAtomicValue(
                    normalizedValue(schemaValue), schemaValue.getActualValue(), selectedUnionMember(schemaValue)));
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
            // Checks that Xerces supplied the same number of parsed actualValues
            throw new OurBadException("Xerces did not provide the values of an XML Schema list.");
        }

        XSSimpleTypeDefinition itemType = listType.getItemType();
        XSObjectList selectedMemberTypes = schemaValue.getMemberTypeDefinitions();

        List<Item> result = new ArrayList<>(lexicalItems.length);
        for (int index = 0; index < lexicalItems.length; index++) {
            XSSimpleTypeDefinition atomicType = itemType;
            if (itemType.getVariety() == XSSimpleTypeDefinition.VARIETY_UNION) {
                // If the list item type is a union, Xerces supplies a selected member type for each position:
                if (selectedMemberTypes == null || index >= selectedMemberTypes.getLength()) {
                    throw new OurBadException("Xerces did not identify a member type for a union list value.");
                }
                if (!(selectedMemberTypes.item(index) instanceof XSSimpleTypeDefinition selectedType)) {
                    throw new OurBadException("Xerces returned an invalid member type for a union list value.");
                }
                atomicType = selectedType;
            }
            result.add(this.convertAtomicValue(lexicalItems[index], actualValues.get(index), atomicType));
        }
        return List.copyOf(result);
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
        if (primitiveKind == XSConstants.NOTATION_DT) {
            throw new OurBadException("xs:NOTATION values do not have a Rumble item representation yet.");
        }

        Item result = primitiveKind == XSConstants.QNAME_DT
                ? convertQName(actualValue)
                : convertLexicalValue(lexicalValue, itemType);

        if (!result.getDynamicType().equals(itemType)) {
            // In case the item type is a derived type, we annotate the item with its schema type to preserve the type
            // information for later derivation checks.
            result = ItemFactory.getInstance().createAnnotatedItem(result, itemType);
        }
        return result;
    }

    /**
     * Converts a normalized lexical value into a Rumble item of the given atomic type, throwing an exception if
     * the conversion fails.
     */
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

    private static Item convertQName(Object actualValue) {
        if (!(actualValue instanceof XSQName qNameValue)) {
            throw new OurBadException("Xerces did not provide an expanded QName value.");
        }
        var qName = qNameValue.getXNIQName();
        String namespace = emptyToNull(qName.uri);
        String prefix = emptyToNull(qName.prefix);
        return ItemFactory.getInstance().createQNameItem(new Name(namespace, prefix, qName.localpart));
    }

    /**
     * Return type of the union that was selected for a given schema value, throwing an exception if Xerces did not
     * provide one.
     */
    private static XSSimpleTypeDefinition selectedUnionMember(XSValue schemaValue) {
        XSSimpleTypeDefinition memberType = schemaValue.getMemberTypeDefinition();
        if (memberType == null) {
            throw new OurBadException("Xerces did not identify a member type for a union value.");
        }
        return memberType;
    }

    /**
     * Returns the normalized lexical value of a Xerces schema value, throwing an exception if it is null.
     * For example, xs:token whitespace normalization changes " A B " into "A B"
     */
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
