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

package org.rumbledb.types;

import java.io.Serial;
import java.util.List;
import java.util.Set;

import lombok.Getter;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;

/** The item type denoted by an XQuery {@code schema-element(E)} test. */
public final class SchemaElementNodeItemType extends AbstractItemType {

    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    private final Name declarationName;

    @Getter
    private final List<ElementNodeItemType> alternatives;

    public SchemaElementNodeItemType(Name declarationName, List<ElementNodeItemType> alternatives) {
        if (declarationName == null) {
            throw new IllegalArgumentException("A schema element declaration name cannot be null.");
        }
        this.declarationName = declarationName;
        this.alternatives = List.copyOf(alternatives);
    }

    @Override
    protected Object equalityKey() {
        return structuralTypeKey(SchemaElementNodeItemType.class, this.declarationName, this.alternatives);
    }

    @Override
    public boolean hasName() {
        return false;
    }

    @Override
    public Name getName() {
        throw new UnsupportedOperationException("A schema-element item type has no type QName.");
    }

    @Override
    public boolean isNodeItemType() {
        return true;
    }

    @Override
    public boolean isSubtypeOf(ItemType superType) {
        if (this.equals(superType)
                || superType.equals(BuiltinTypesCatalogue.item)
                || superType.equals(BuiltinTypesCatalogue.nodeItem)
                || superType.equals(BuiltinTypesCatalogue.elementNode)) {
            return true;
        }
        if (superType.isUnionType()) {
            for (ItemType member : superType.getTypes()) {
                if (isSubtypeOf(member)) {
                    return true;
                }
            }
        }
        if (superType instanceof SchemaElementNodeItemType other) {
            return this.alternatives.stream()
                    .allMatch(alternative -> other.alternatives.stream().anyMatch(alternative::isSubtypeOf));
        }
        return !this.alternatives.isEmpty()
                && this.alternatives.stream().allMatch(alternative -> alternative.isSubtypeOf(superType));
    }

    @Override
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        if (this.equals(other)) {
            return this;
        }
        if (other instanceof ElementNodeItemType || other instanceof SchemaElementNodeItemType) {
            return BuiltinTypesCatalogue.elementNode;
        }
        ItemType current = this;
        ItemType candidate = other;
        while (candidate.getTypeTreeDepth() > current.getTypeTreeDepth()) {
            candidate = candidate.getBaseType();
        }
        while (candidate.getTypeTreeDepth() < current.getTypeTreeDepth()) {
            current = current.getBaseType();
        }
        while (!current.equals(candidate)) {
            current = current.getBaseType();
            candidate = candidate.getBaseType();
        }
        return current;
    }

    @Override
    public int getTypeTreeDepth() {
        return 2;
    }

    @Override
    public ItemType getBaseType() {
        return BuiltinTypesCatalogue.elementNode;
    }

    @Override
    public Set<ConstrainingFacetTypes> getAllowedFacets() {
        throw new UnsupportedOperationException("schema-element item types do not support facets");
    }

    @Override
    public boolean isResolved() {
        return true;
    }

    @Override
    public void resolve(StaticContext context, ExceptionMetadata metadata) {
        // Resolved from the module's XML Schema catalog during translation.
    }

    @Override
    public void resolve(DynamicContext context, ExceptionMetadata metadata) {
        // Resolved from the module's XML Schema catalog during translation.
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleConfiguration configuration) {
        return false;
    }

    @Override
    public String toString() {
        return "schema-element(" + this.declarationName + ")";
    }
}
