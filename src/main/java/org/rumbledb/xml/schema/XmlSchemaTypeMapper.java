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

import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.xml.XmlSchemaTypeAnnotation;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;

/** Maps Xerces type definitions to the XML and atomic type models used by RumbleDB. */
final class XmlSchemaTypeMapper {

    private static final String ANONYMOUS_TYPE_NAMESPACE = "http://rumbledb.org/anonymous-schema-types";
    private final Map<XSTypeDefinition, Optional<ItemType>> atomicTypes = new IdentityHashMap<>();
    private final Map<XSTypeDefinition, XmlSchemaTypeAnnotation> typeAnnotations = new IdentityHashMap<>();
    private int nextAnonymousTypeId;

    XmlSchemaTypeAnnotation getTypeAnnotation(XSTypeDefinition schemaType) {
        return this.typeAnnotations.computeIfAbsent(schemaType, this::createTypeAnnotation);
    }

    Optional<ItemType> getAtomicType(XSTypeDefinition schemaType) {
        if (!(schemaType instanceof XSSimpleTypeDefinition simpleType)) {
            return Optional.empty();
        }
        if (simpleType.getVariety() != XSSimpleTypeDefinition.VARIETY_ATOMIC) {
            return Optional.empty();
        }
        return this.atomicTypes.computeIfAbsent(schemaType, this::createAtomicType);
    }

    private Optional<ItemType> createAtomicType(XSTypeDefinition schemaType) {
        Name name = declaredNameOf(schemaType);
        if (name != null && BuiltinTypesCatalogue.typeExists(name)) {
            return Optional.of(BuiltinTypesCatalogue.getItemTypeByName(name));
        }
        return getAtomicType(schemaType.getBaseType())
            .map(baseType -> ItemTypeFactory.createXmlSchemaAtomicType(name, baseType));
    }

    private XmlSchemaTypeAnnotation createTypeAnnotation(XSTypeDefinition schemaType) {
        Name name = declaredNameOf(schemaType);
        if (name == null) {
            name = new Name(
                    ANONYMOUS_TYPE_NAMESPACE,
                    null,
                    "anonymousType" + this.nextAnonymousTypeId++
            );
        }
        return new XmlSchemaTypeAnnotation(name, varietyOf(schemaType), contentTypeOf(schemaType));
    }

    private static XmlSchemaTypeAnnotation.Variety varietyOf(XSTypeDefinition schemaType) {
        if (!(schemaType instanceof XSSimpleTypeDefinition simpleType)) {
            return XmlSchemaTypeAnnotation.Variety.COMPLEX;
        }
        return switch (simpleType.getVariety()) {
            case XSSimpleTypeDefinition.VARIETY_ABSENT -> XmlSchemaTypeAnnotation.Variety.ANY_SIMPLE;
            case XSSimpleTypeDefinition.VARIETY_ATOMIC -> XmlSchemaTypeAnnotation.Variety.ATOMIC;
            case XSSimpleTypeDefinition.VARIETY_LIST -> XmlSchemaTypeAnnotation.Variety.LIST;
            case XSSimpleTypeDefinition.VARIETY_UNION -> XmlSchemaTypeAnnotation.Variety.UNION;
            default -> throw new OurBadException("Xerces returned an unknown XML Schema simple type variety.");
        };
    }

    private static XmlSchemaTypeAnnotation.ContentType contentTypeOf(XSTypeDefinition schemaType) {
        if (!(schemaType instanceof XSComplexTypeDefinition complexType)) {
            return XmlSchemaTypeAnnotation.ContentType.NOT_APPLICABLE;
        }
        return switch (complexType.getContentType()) {
            case XSComplexTypeDefinition.CONTENTTYPE_EMPTY -> XmlSchemaTypeAnnotation.ContentType.EMPTY;
            case XSComplexTypeDefinition.CONTENTTYPE_SIMPLE -> XmlSchemaTypeAnnotation.ContentType.SIMPLE;
            case XSComplexTypeDefinition.CONTENTTYPE_ELEMENT -> XmlSchemaTypeAnnotation.ContentType.ELEMENT_ONLY;
            case XSComplexTypeDefinition.CONTENTTYPE_MIXED -> XmlSchemaTypeAnnotation.ContentType.MIXED;
            default -> throw new OurBadException("Xerces returned an unknown complex content type.");
        };
    }

    private static Name declaredNameOf(XSTypeDefinition schemaType) {
        if (schemaType.getAnonymous() || schemaType.getName() == null) {
            return null;
        }
        String namespace = schemaType.getNamespace();
        String prefix = Name.XS_NS.equals(namespace) ? "xs" : null;
        return new Name(namespace, prefix, schemaType.getName());
    }
}
