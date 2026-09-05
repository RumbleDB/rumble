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
import java.util.Optional;
import javax.xml.validation.Schema;

import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.xs.XSValue;

import lombok.Getter;
import lombok.NonNull;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.items.xml.XmlSchemaTypeAnnotation;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

/**
 * Wrapper around Xerces’s model
 * It can find schema type definitions and map them to RumbleDB ItemTypes or typed values.
 */
public final class XmlSchemaCatalog {

    @Getter
    private final XSModel schemaModel;

    @Getter
    private final Schema validationSchema;

    private final XmlSchemaTypeMapper typeMapper;
    private final XercesTypedValueConverter typedValueConverter;

    XmlSchemaCatalog(@NonNull XSModel schemaModel, @NonNull Schema validationSchema) {
        this.schemaModel = schemaModel;
        this.validationSchema = validationSchema;
        this.typeMapper = new XmlSchemaTypeMapper();
        this.typedValueConverter = new XercesTypedValueConverter(this.typeMapper);
    }

    public Optional<XSTypeDefinition> getTypeDefinition(@NonNull Name name) {
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

    List<Item> convertTypedValue(XSValue schemaValue) {
        return this.typedValueConverter.convert(schemaValue);
    }

    private static String emptyToNull(String value) {
        return value == null || value.isEmpty() ? null : value;
    }
}
