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
import java.util.Optional;

import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;

import org.rumbledb.context.Name;
import org.rumbledb.items.xml.XmlSchemaTypeAnnotation;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

/** The Xerces XML Schema component model available to one XQuery module. */
public final class XmlSchemaCatalog {

    private final XSModel schemaModel;
    private final XmlSchemaTypeMapper typeMapper;

    XmlSchemaCatalog(XSModel schemaModel) {
        this.schemaModel = Objects.requireNonNull(schemaModel, "schemaModel must not be null");
        this.typeMapper = new XmlSchemaTypeMapper();
    }

    public Optional<XSTypeDefinition> getTypeDefinition(Name name) {
        Objects.requireNonNull(name, "name must not be null");
        return Optional.ofNullable(
                this.schemaModel.getTypeDefinition(name.getLocalName(), emptyToNull(name.getNamespace())));
    }

    public boolean containsNamespace(String namespace) {
        return this.schemaModel.getNamespaces().contains(emptyToNull(namespace));
    }

    public List<ItemType> getNamedGeneralizedAtomicItemTypes() {
        XSNamedMap schemaTypes = this.schemaModel.getComponents(XSConstants.TYPE_DEFINITION);
        List<ItemType> result = new ArrayList<>();
        for (int index = 0; index < schemaTypes.getLength(); index++) {
            XSTypeDefinition schemaType = (XSTypeDefinition) schemaTypes.item(index);
            this.typeMapper
                    .mapGeneralizedAtomicType(schemaType)
                    .filter(ItemType::hasName)
                    .filter(type -> !BuiltinTypesCatalogue.typeExists(type.getName()))
                    .ifPresent(result::add);
        }
        return List.copyOf(result);
    }

    Optional<ItemType> getAtomicItemType(XSTypeDefinition schemaType) {
        if (!(schemaType instanceof XSSimpleTypeDefinition simpleType)
                || simpleType.getVariety() != XSSimpleTypeDefinition.VARIETY_ATOMIC) {
            return Optional.empty();
        }
        return this.typeMapper.mapGeneralizedAtomicType(schemaType);
    }

    Optional<ItemType> getGeneralizedAtomicItemType(XSTypeDefinition schemaType) {
        return this.typeMapper.mapGeneralizedAtomicType(schemaType);
    }

    Optional<ItemType> getListItemType(XSTypeDefinition schemaType) {
        return this.typeMapper.getListItemType(schemaType);
    }

    XmlSchemaTypeAnnotation getTypeAnnotation(XSTypeDefinition schemaType) {
        return this.typeMapper.mapTypeAnnotation(schemaType);
    }

    XSModel getSchemaModel() {
        return this.schemaModel;
    }

    private static String emptyToNull(String value) {
        return value == null || value.isEmpty() ? null : value;
    }
}
