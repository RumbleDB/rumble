/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.xml.schema;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.rumbledb.context.Name;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;

/** Maps atomic XML Schema types to the existing Rumble atomic type system. */
final class XmlSchemaAtomicTypeMapper {

    private final Map<XSTypeDefinition, Optional<ItemType>> types = new IdentityHashMap<>();

    Optional<ItemType> getAtomicType(XSTypeDefinition schemaType) {
        if (!(schemaType instanceof XSSimpleTypeDefinition simpleType)) {
            return Optional.empty();
        }
        if (simpleType.getVariety() != XSSimpleTypeDefinition.VARIETY_ATOMIC) {
            return Optional.empty();
        }
        return this.types.computeIfAbsent(schemaType, this::createAtomicType);
    }

    private Optional<ItemType> createAtomicType(XSTypeDefinition schemaType) {
        Name name = nameOf(schemaType);
        if (name != null && BuiltinTypesCatalogue.typeExists(name)) {
            return Optional.of(BuiltinTypesCatalogue.getItemTypeByName(name));
        }

        return getAtomicType(schemaType.getBaseType())
            .map(baseType -> ItemTypeFactory.createXmlSchemaAtomicType(name, baseType));
    }

    private static Name nameOf(XSTypeDefinition schemaType) {
        if (schemaType.getAnonymous() || schemaType.getName() == null) {
            return null;
        }
        String namespace = schemaType.getNamespace();
        String prefix = Name.XS_NS.equals(namespace) ? "xs" : null;
        return new Name(namespace, prefix, schemaType.getName());
    }
}
