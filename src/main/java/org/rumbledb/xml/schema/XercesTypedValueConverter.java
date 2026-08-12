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

import java.util.List;

import org.apache.xerces.xs.XSConstants;
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

    private final XercesBuiltinAtomicTypeMapper typeMapper;

    XercesTypedValueConverter() {
        this.typeMapper = new XercesBuiltinAtomicTypeMapper();
    }

    List<Item> convert(XSValue schemaValue) {
        if (schemaValue == null) {
            throw new OurBadException("A Xerces schema value cannot be null.");
        }
        XSSimpleTypeDefinition schemaType = schemaValue.getTypeDefinition();
        if (schemaType == null) {
            throw new OurBadException("Xerces did not identify the type of its schema value.");
        }
        ItemType itemType = this.typeMapper
                .map(schemaType)
                .orElseThrow(() -> new OurBadException("Xerces returned an unsupported XML Schema atomic type."));
        if (itemType.equals(BuiltinTypesCatalogue.atomicItem)) {
            throw new OurBadException("xs:anyAtomicType cannot label a concrete typed value.");
        }
        if (itemType.equals(BuiltinTypesCatalogue.NOTATIONItem)) {
            throw new OurBadException("xs:NOTATION values do not have a Rumble item representation yet.");
        }

        Item result = itemType.equals(BuiltinTypesCatalogue.QNameItem)
                ? convertQName(schemaValue)
                : convertLexicalValue(schemaValue, itemType);
        if (!result.getDynamicType().equals(itemType)) {
            result = ItemFactory.getInstance().createAnnotatedItem(result, itemType);
        }
        return List.of(result);
    }

    private static Item convertLexicalValue(XSValue schemaValue, ItemType itemType) {
        String normalizedValue = schemaValue.getNormalizedValue();
        if (normalizedValue == null) {
            throw new OurBadException("Xerces did not provide a normalized lexical value.");
        }
        Item result = CastIterator.castItemToType(
                ItemFactory.getInstance().createUntypedAtomicItem(normalizedValue),
                itemType,
                ExceptionMetadata.EMPTY_METADATA);
        if (result == null) {
            throw new OurBadException("A Xerces-validated value could not be converted to a Rumble item.");
        }
        return result;
    }

    private static Item convertQName(XSValue schemaValue) {
        if (schemaValue.getActualValueType() != XSConstants.QNAME_DT
                || !(schemaValue.getActualValue() instanceof XSQName qNameValue)) {
            throw new OurBadException("Xerces did not provide an expanded QName value.");
        }
        var qName = qNameValue.getXNIQName();
        String namespace = emptyToNull(qName.uri);
        String prefix = emptyToNull(qName.prefix);
        return ItemFactory.getInstance().createQNameItem(new Name(namespace, prefix, qName.localpart));
    }

    private static String emptyToNull(String value) {
        return value == null || value.isEmpty() ? null : value;
    }
}
