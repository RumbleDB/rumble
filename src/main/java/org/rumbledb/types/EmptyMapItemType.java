package org.rumbledb.types;

import org.rumbledb.config.RumbleRuntimeConfiguration;

import java.util.Collections;
import java.util.Set;

/**
 * Artificial item type representing the dynamic type of empty XQuery maps.
 *
 * Semantics:
 * - It is considered a map item type.
 * - It is a subtype of every concrete map(K, V) type and of map(*) (via BuiltinTypesCatalogue.mapItem).
 * - It is also a subtype of function(*) through the usual map(*) -> function(*) chain.
 * - It is not a subtype of js:object or other JSON-only types.
 *
 * This type is internal and is not exposed as a user-facing XQuery type.
 */
public class EmptyMapItemType extends MapItemType {

    private static final long serialVersionUID = 1L;

    private static final EmptyMapItemType INSTANCE = new EmptyMapItemType();

    private EmptyMapItemType() {
        super(
            null,
            BuiltinTypesCatalogue.anyFunctionItem,
            BuiltinTypesCatalogue.atomicItem,
            SequenceType.createSequenceType("item*")
        );
    }

    public static EmptyMapItemType getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean hasName() {
        return false;
    }

    @Override
    public boolean isSubtypeOf(ItemType superType) {
        if (superType.isUnionType()) {
            for (ItemType member : superType.getTypes()) {
                if (this.isSubtypeOf(member)) {
                    return true;
                }
            }
        }
        if (equals(superType) || superType.equals(BuiltinTypesCatalogue.item)) {
            return true;
        }
        if (superType.isMapItemType()) {
            // By design, the empty-map type is a subtype of every map(K, V) and map(*).
            return true;
        }
        if (superType.isFunctionItemType()) {
            return superType.equals(BuiltinTypesCatalogue.anyFunctionItem);
        }
        return false;
    }

    @Override
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        if (equals(other)) {
            return this;
        }
        if (other.isMapItemType()) {
            // Any concrete map type, including map(*), is a supertype of the empty-map type.
            return other;
        }
        if (other.isFunctionItemType()) {
            return BuiltinTypesCatalogue.anyFunctionItem;
        }
        return BuiltinTypesCatalogue.item;
    }

    @Override
    public Set<ConstrainingFacetTypes> getAllowedFacets() {
        return Collections.emptySet();
    }

    @Override
    public String getIdentifierString() {
        return "<empty-map>";
    }

    @Override
    public String toString() {
        return "map{}";
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleRuntimeConfiguration configuration) {
        return false;
    }
}

