package org.rumbledb.types;

import java.io.Serial;
import java.util.HashSet;
import java.util.List;

import lombok.Getter;

import org.rumbledb.context.Name;

/** A global element declaration and its concrete, valid substitution-group members. */
public class SchemaElementNodeItemType extends ElementNodeItemType {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Name declarationName;

    @Getter
    private final List<ElementNodeItemType> alternatives;

    public SchemaElementNodeItemType(Name declarationName, List<ElementNodeItemType> alternatives) {
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
        throw new UnsupportedOperationException("Schema element tests have no builtin QName");
    }

    @Override
    public boolean isSubtypeOf(ItemType superType) {
        if (this.equals(superType)) {
            return true;
        }
        if (superType instanceof SchemaElementNodeItemType schemaType) {
            return new HashSet<>(schemaType.alternatives).containsAll(this.alternatives);
        }
        // Even an abstract declaration with no concrete members is an element test.
        return super.isSubtypeOf(superType)
                || (!this.alternatives.isEmpty()
                        && this.alternatives.stream().allMatch(type -> type.isSubtypeOf(superType)));
    }

    @Override
    public String toString() {
        return "schema-element(" + this.declarationName + ")";
    }
}
