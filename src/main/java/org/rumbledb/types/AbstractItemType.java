/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.rumbledb.context.Name;

/**
 * Centralizes Java equality for item types.
 *
 * <p>Named types compare by expanded name, anonymous nominal types compare by identity, and
 * structural types override {@link #equalityKey()} with a canonical structural key.
 */
public abstract class AbstractItemType implements ItemType {
    @Serial private static final long serialVersionUID = 1L;

    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AbstractItemType otherType)) {
            return false;
        }
        return this.equalityKey().equals(otherType.equalityKey());
    }

    @Override
    public final int hashCode() {
        return this.equalityKey().hashCode();
    }

    protected Object equalityKey() {
        if (hasName()) {
            return namedTypeKey(getName());
        }
        return new IdentityTypeKey(this);
    }

    protected static Object equalityKeyOf(ItemType itemType) {
        if (itemType instanceof AbstractItemType abstractItemType) {
            return abstractItemType.equalityKey();
        }
        return new IdentityTypeKey(itemType);
    }

    protected static Object namedTypeKey(Name name) {
        return new NamedTypeKey(Objects.requireNonNull(name));
    }

    protected static Object structuralTypeKey(
            Class<? extends ItemType> typeClass, Object... components) {
        return new StructuralTypeKey(typeClass, Arrays.asList(components));
    }

    private record NamedTypeKey(Name name) {}

    private record StructuralTypeKey(
            Class<? extends ItemType> typeClass, List<Object> components) {}

    private static final class IdentityTypeKey {
        private final ItemType itemType;

        private IdentityTypeKey(ItemType itemType) {
            this.itemType = itemType;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof IdentityTypeKey otherKey && this.itemType == otherKey.itemType;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(this.itemType);
        }
    }
}
