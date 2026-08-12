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

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;

import org.rumbledb.context.Name;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;

/** Maps Xerces atomic type definitions to stable Rumble item-type instances. */
final class XmlSchemaAtomicTypeMapper {

    private final XercesBuiltinAtomicTypeMapper builtinTypeMapper;
    private final Map<XSTypeDefinition, Optional<ItemType>> mappedTypes;
    private final Set<XSTypeDefinition> typesBeingMapped;

    XmlSchemaAtomicTypeMapper() {
        this.builtinTypeMapper = new XercesBuiltinAtomicTypeMapper();
        this.mappedTypes = new IdentityHashMap<>();
        this.typesBeingMapped = Collections.newSetFromMap(new IdentityHashMap<>());
    }

    Optional<ItemType> map(XSTypeDefinition schemaType) {
        if (schemaType == null) {
            return Optional.empty();
        }
        Optional<ItemType> cached = this.mappedTypes.get(schemaType);
        if (cached != null) {
            return cached;
        }
        if (!this.typesBeingMapped.add(schemaType)) {
            throw new OurBadException("Xerces returned a cyclic XML Schema atomic type hierarchy.");
        }

        Optional<ItemType> result;
        try {
            result = create(schemaType);
        } finally {
            this.typesBeingMapped.remove(schemaType);
        }
        this.mappedTypes.put(schemaType, result);
        return result;
    }

    private Optional<ItemType> create(XSTypeDefinition schemaType) {
        Optional<ItemType> builtinType = this.builtinTypeMapper.map(schemaType);
        if (builtinType.isPresent()) {
            return builtinType;
        }
        if (!(schemaType instanceof XSSimpleTypeDefinition simpleType)
                || simpleType.getVariety() != XSSimpleTypeDefinition.VARIETY_ATOMIC
                || simpleType.getAnonymous()
                || simpleType.getName() == null) {
            return Optional.empty();
        }

        Name name = new Name(emptyToNull(simpleType.getNamespace()), null, simpleType.getName());
        return map(simpleType.getBaseType()).map(baseType -> ItemTypeFactory.createXmlSchemaAtomicType(name, baseType));
    }

    private static String emptyToNull(String value) {
        return value == null || value.isEmpty() ? null : value;
    }
}
