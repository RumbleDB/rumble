package org.rumbledb.items.xml;

import org.rumbledb.api.Item;
import org.rumbledb.types.ItemType;

import java.util.ArrayList;
import java.util.List;

public interface NodeItem extends Item {
    default List<Item> attributes() {
        return new ArrayList<>();
    }

    default String baseUri() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    default List<Item> children() {
        return new ArrayList<>();
    }

    default String documentUri() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    default boolean isId() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    default boolean isIdRefs() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    default List<Item> namespaceNodes() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    default boolean nilled() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    default String nodeKind() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    default String nodeName() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    default Item parent() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    default String stringValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    default String typeName() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    default ItemType typedValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    default String unparsedEntityPublicId() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    default String unparsedEntityServerId() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    default void setParent(NodeItem parent) {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

}
