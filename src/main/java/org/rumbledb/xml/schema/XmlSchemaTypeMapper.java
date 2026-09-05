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
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;

import org.rumbledb.context.Name;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.xml.XmlSchemaTypeAnnotation;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;

/**
 * Maps Xerces type definitions to Rumble's query types and node annotations.
 */
final class XmlSchemaTypeMapper {

    private static final String ANONYMOUS_TYPE_NAMESPACE = "http://rumbledb.org/anonymous-schema-types";

    private final XercesBuiltinAtomicTypeMapper builtinTypeMapper;
    private final Map<XSTypeDefinition, Optional<ItemType>> mappedTypes;
    private final Map<XSTypeDefinition, XmlSchemaTypeAnnotation> mappedAnnotations;
    private final Set<XSTypeDefinition> typesBeingMapped;

    XmlSchemaTypeMapper() {
        this.builtinTypeMapper = new XercesBuiltinAtomicTypeMapper();
        this.mappedTypes = new IdentityHashMap<>();
        this.mappedAnnotations = new IdentityHashMap<>();
        this.typesBeingMapped = Collections.newSetFromMap(new IdentityHashMap<>());
    }

    /**
     * Maps schema types usable as XQuery atomic-like types
     *
     * @param schemaType the Xerces schema type definition to map
     * @return an optional containing the mapped Rumble type, or empty if the schema
     *         type is not usable as an
     *         atomic-like type
     */
    Optional<ItemType> mapGeneralizedAtomicType(XSTypeDefinition schemaType) {
        if (schemaType == null) {
            return Optional.empty();
        }
        Optional<ItemType> cached = this.mappedTypes.get(schemaType);
        if (cached != null) {
            return cached;
        }
        if (!this.typesBeingMapped.add(schemaType)) {
            throw new OurBadException("Xerces returned a cyclic XML Schema simple type hierarchy.");
        }

        Optional<ItemType> result;
        try {
            result = createGeneralizedAtomicType(schemaType);
        } finally {
            this.typesBeingMapped.remove(schemaType);
        }
        this.mappedTypes.put(schemaType, result);
        return result;
    }

    XmlSchemaTypeAnnotation mapTypeAnnotation(XSTypeDefinition schemaType) {
        if (schemaType == null) {
            throw new OurBadException("A Xerces schema type definition cannot be null.");
        }
        return this.mappedAnnotations.computeIfAbsent(schemaType, this::createTypeAnnotation);
    }

    /**
     * For example:
     * <xs:simpleType name="Integers">
     * <xs:list itemType="xs:integer"/>
     * </xs:simpleType>
     * method returns xs:integer
     */
    Optional<ItemType> getListItemType(XSTypeDefinition schemaType) {
        if (!(schemaType instanceof XSSimpleTypeDefinition simpleType)
                || simpleType.getVariety() != XSSimpleTypeDefinition.VARIETY_LIST) {
            return Optional.empty();
        }
        return mapGeneralizedAtomicType(simpleType.getItemType());
    }

    private Optional<ItemType> createGeneralizedAtomicType(XSTypeDefinition schemaType) {
        Optional<ItemType> builtinType = this.builtinTypeMapper.map(schemaType);
        if (builtinType.isPresent()) {
            // A built-in Xerces atomic type, such as xs:integer, maps to RumbleDB’s existing built-in ItemType.
            return builtinType;
        }
        if (!(schemaType instanceof XSSimpleTypeDefinition simpleType)) {
            return Optional.empty();
        }

        Name name = declaredNameOf(simpleType);
        if (simpleType.getVariety() == XSSimpleTypeDefinition.VARIETY_ATOMIC) {
            /**
             * VARIETY_ATOMIC means the XML Schema simple type represents exactly one indivisible value.
             * For example, xs:integer
             * Means that the type can be modeled as a RumbleDB atomic ItemType, based on its schema base type.
             */
            return mapGeneralizedAtomicType(simpleType.getBaseType())
                    .map(baseType -> ItemTypeFactory.createXmlSchemaAtomicType(name, baseType));
        }

        if (simpleType.getVariety() == XSSimpleTypeDefinition.VARIETY_UNION && isPureUnion(simpleType)) {
            /**
             * A “pure” union, such as:
             * <xs:simpleType name="CodeOrInteger">
             * <xs:union memberTypes="t:Code xs:integer"/>
             * </xs:simpleType>
             * becomes a RumbleDB union type.
             */
            return mapUnionMembers(simpleType.getMemberTypes())
                    .map(memberTypes -> ItemTypeFactory.createXmlSchemaUnionType(name, memberTypes));
        }

        // A complex type, list type, or restricted union returns Optional.empty() because it is not represented as a
        // generalized atomic type.
        return Optional.empty();
    }

    /**
     * Maps the member types of a pure union to RumbleDB ItemTypes.
     *
     * If a member is itself a union, it flattens it.
     */
    private Optional<List<ItemType>> mapUnionMembers(XSObjectList schemaMemberTypes) {
        Set<ItemType> memberTypes = new LinkedHashSet<>();
        for (int index = 0; index < schemaMemberTypes.getLength(); index++) {
            Optional<ItemType> memberType = mapGeneralizedAtomicType((XSTypeDefinition) schemaMemberTypes.item(index));
            if (memberType.isEmpty()) {
                return Optional.empty();
            }
            ItemType mappedMember = memberType.get();
            if (mappedMember.isUnionType()) {
                // XQuery defines membership of a pure union transitively.
                memberTypes.addAll(mappedMember.getTypes());
            } else {
                memberTypes.add(mappedMember);
            }
        }
        return Optional.of(new ArrayList<>(memberTypes));
    }

    /**
     * Creates annotation attached to validated XML nodes
     *
     * @param schemaType
     * @return
     */
    private XmlSchemaTypeAnnotation createTypeAnnotation(XSTypeDefinition schemaType) {
        Name name = declaredNameOf(schemaType);
        if (name == null) {
            name = new Name(ANONYMOUS_TYPE_NAMESPACE, null, "anonymousType-" + UUID.randomUUID());
        }

        List<Name> hierarchy = new ArrayList<>();
        hierarchy.add(name);
        XSTypeDefinition current = schemaType;
        while (current != null) {
            XSTypeDefinition baseType = current.getBaseType();
            if (baseType == null || baseType == current) {
                break;
            }
            Name baseName = declaredNameOf(baseType);
            if (baseName != null && !hierarchy.contains(baseName)) {
                // Ignores anonymous base types and avoids duplicates:
                hierarchy.add(baseName);
            }
            current = baseType;
        }

        if (schemaType instanceof XSSimpleTypeDefinition simpleType
                && simpleType.getVariety() == XSSimpleTypeDefinition.VARIETY_ATOMIC) {
            // Because Xerces’s XML Schema hierarchy usually goes from xs:string to xs:anySimpleType,
            // but XQuery also has xs:anyAtomicType between them, so we insert it if it is missing.
            insertBefore(hierarchy, xsName("anyAtomicType"), xsName("anySimpleType"));
        }

        // Every schema type must be derived from xs:anyType
        addIfAbsent(hierarchy, xsName("anyType"));

        return new XmlSchemaTypeAnnotation(name, hierarchy);
    }

    /**
     * A union is considered “pure” only if:
     * - it has no meaningful constraining facets;
     * - none of its members is a list;
     * - nested union members are themselves pure.
     *
     * A pure union can be represented as a RumbleDB union type, while a non-pure union cannot.
     *
     * @param unionType
     * @return
     */
    private static boolean isPureUnion(XSSimpleTypeDefinition unionType) {
        // Xerces reports the fixed whitespace facet on a direct union even though it is
        // not a restriction.
        short constrainingFacets = (short) (unionType.getDefinedFacets() & ~XSSimpleTypeDefinition.FACET_WHITESPACE);
        if (constrainingFacets != XSSimpleTypeDefinition.FACET_NONE) {
            return false;
        }
        XSObjectList memberTypes = unionType.getMemberTypes();
        for (int index = 0; index < memberTypes.getLength(); index++) {
            if (!(memberTypes.item(index) instanceof XSSimpleTypeDefinition memberType)
                    || memberType.getVariety() == XSSimpleTypeDefinition.VARIETY_LIST
                    || (memberType.getVariety() == XSSimpleTypeDefinition.VARIETY_UNION && !isPureUnion(memberType))) {
                return false;
            }
        }
        return true;
    }

    private static Name declaredNameOf(XSTypeDefinition schemaType) {
        if (schemaType.getAnonymous() || schemaType.getName() == null) {
            return null;
        }
        String namespace = emptyToNull(schemaType.getNamespace());
        return new Name(namespace, Name.XS_NS.equals(namespace) ? "xs" : null, schemaType.getName());
    }

    private static void insertBefore(List<Name> names, Name name, Name successor) {
        if (names.contains(name)) {
            return;
        }
        int successorIndex = names.indexOf(successor);
        names.add(successorIndex < 0 ? names.size() : successorIndex, name);
    }

    private static void addIfAbsent(List<Name> names, Name name) {
        if (!names.contains(name)) {
            names.add(name);
        }
    }

    private static Name xsName(String localName) {
        return new Name(Name.XS_NS, "xs", localName);
    }

    private static String emptyToNull(String value) {
        return value == null || value.isEmpty() ? null : value;
    }
}
