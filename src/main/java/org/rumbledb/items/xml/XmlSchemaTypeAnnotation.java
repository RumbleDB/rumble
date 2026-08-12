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

package org.rumbledb.items.xml;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.rumbledb.context.Name;
import org.rumbledb.types.ItemType;

/** The XML Schema type assigned to an element or attribute node. */
public record XmlSchemaTypeAnnotation(Name name, List<Name> typeHierarchy) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public XmlSchemaTypeAnnotation {
        Objects.requireNonNull(name, "A schema type annotation must have a name.");
        Objects.requireNonNull(typeHierarchy, "A schema type hierarchy cannot be null.");
        typeHierarchy = List.copyOf(typeHierarchy);
        if (typeHierarchy.isEmpty() || !name.equals(typeHierarchy.get(0))) {
            throw new IllegalArgumentException("A schema type hierarchy must start with the annotated type.");
        }
    }

    public boolean isDerivedFrom(Name typeName) {
        return this.typeHierarchy.contains(typeName);
    }

    /** Creates an annotation for the built-in atomic validation path. */
    public static XmlSchemaTypeAnnotation forAtomicItemType(ItemType itemType) {
        Objects.requireNonNull(itemType, "The atomic item type cannot be null.");
        if (!itemType.isAtomicItemType()
                || !itemType.hasName()
                || !Name.XS_NS.equals(itemType.getName().getNamespace())) {
            throw new IllegalArgumentException("A schema atomic type annotation requires a named atomic item type.");
        }

        List<Name> hierarchy = new ArrayList<>();
        ItemType current = itemType;
        while (current != null) {
            if (current.hasName() && current.isAtomicItemType() && !hierarchy.contains(current.getName())) {
                hierarchy.add(current.getName());
            }
            current = current.getBaseType();
        }
        addIfAbsent(hierarchy, xsName("anyAtomicType"));
        addIfAbsent(hierarchy, xsName("anySimpleType"));
        addIfAbsent(hierarchy, xsName("anyType"));
        return new XmlSchemaTypeAnnotation(itemType.getName(), hierarchy);
    }

    private static void addIfAbsent(List<Name> names, Name name) {
        if (!names.contains(name)) {
            names.add(name);
        }
    }

    private static Name xsName(String localName) {
        return new Name(Name.XS_NS, "xs", localName);
    }
}
