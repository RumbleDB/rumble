package org.rumbledb.types;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.rumbledb.context.Name;

/**
 * Centralizes Java equality for item types.
 *
 * Named types compare by expanded name, anonymous nominal types compare by identity, and structural
 * types override {@link #equalityKey()} with a canonical structural key.
 */
public abstract class AbstractItemType implements ItemType {
    @Serial
    private static final long serialVersionUID = 1L;

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

    protected static Object structuralTypeKey(Class<? extends ItemType> typeClass, Object... components) {
        return new StructuralTypeKey(typeClass, Arrays.asList(components));
    }

    private record NamedTypeKey(Name name) {
    }

    private record StructuralTypeKey(Class<? extends ItemType> typeClass, List<Object> components) {
    }

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
