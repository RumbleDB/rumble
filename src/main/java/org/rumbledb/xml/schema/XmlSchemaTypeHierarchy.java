/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.xml.schema;

import java.util.ArrayList;
import java.util.List;

import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.rumbledb.context.Name;

/** Builds type-derivation hierarchies across XML Schema and XDM-defined types. */
public final class XmlSchemaTypeHierarchy {

    private XmlSchemaTypeHierarchy() {
    }

    public static List<Name> forXdmType(Name name) {
        if (!Name.XS_NS.equals(name.getNamespace())) {
            return List.of();
        }
        Name anyType = xsName("anyType");
        Name anySimpleType = xsName("anySimpleType");
        Name anyAtomicType = xsName("anyAtomicType");
        return switch (name.getLocalName()) {
            case "untyped" -> List.of(name, anyType);
            case "anyAtomicType" -> List.of(name, anySimpleType, anyType);
            case "untypedAtomic" -> List.of(name, anyAtomicType, anySimpleType, anyType);
            case "dayTimeDuration", "yearMonthDuration" -> List.of(
                name,
                xsName("duration"),
                anyAtomicType,
                anySimpleType,
                anyType
            );
            default -> List.of();
        };
    }

    public static List<Name> forSchemaType(XSTypeDefinition type, Name typeName) {
        List<Name> result = new ArrayList<>();
        result.add(typeName);
        XSTypeDefinition current = type.getBaseType();
        while (current != null && current != type) {
            if (!current.getAnonymous() && current.getName() != null) {
                String namespace = current.getNamespace();
                Name currentName = new Name(
                        namespace,
                        Name.XS_NS.equals(namespace) ? "xs" : null,
                        current.getName()
                );
                if (!result.contains(currentName)) {
                    result.add(currentName);
                }
            }
            XSTypeDefinition base = current.getBaseType();
            if (base == current) {
                break;
            }
            current = base;
        }
        if (
            type instanceof XSSimpleTypeDefinition simpleType
                && simpleType.getVariety() == XSSimpleTypeDefinition.VARIETY_ATOMIC
        ) {
            Name anyAtomicType = xsName("anyAtomicType");
            if (!result.contains(anyAtomicType)) {
                int anySimpleTypeIndex = result.indexOf(xsName("anySimpleType"));
                result.add(anySimpleTypeIndex < 0 ? result.size() : anySimpleTypeIndex, anyAtomicType);
            }
        }
        Name anyType = xsName("anyType");
        if (!result.contains(anyType)) {
            result.add(anyType);
        }
        return List.copyOf(result);
    }

    public static Name declaredNameOf(XSTypeDefinition type) {
        if (type.getAnonymous() || type.getName() == null) {
            return null;
        }
        String namespace = type.getNamespace();
        return new Name(namespace, Name.XS_NS.equals(namespace) ? "xs" : null, type.getName());
    }

    private static Name xsName(String localName) {
        return new Name(Name.XS_NS, "xs", localName);
    }
}
