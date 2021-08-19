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
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.types;


import org.apache.spark.sql.types.DataType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ItemType extends Serializable {

    long serialVersionUID = 1L;

    /**
     * Tests for itemType equality.
     *
     * @param other another object.
     * @return true it is equal to other, false otherwise.
     */
    boolean equals(Object other);


    /**
     * Tests for itemType equality.
     *
     * @param otherType another item type.
     * @return true it is equal to other, false otherwise.
     */
    default boolean isEqualTo(ItemType otherType) {
        if (this instanceof FunctionItemType || otherType instanceof FunctionItemType) {
            if (!(this instanceof FunctionItemType) || !(otherType instanceof FunctionItemType)) {
                return false;
            }
            return this.toString().equals(otherType.toString());
        }
        if (this.getName() == null || otherType.getName() == null) {
            return this == otherType;
        }
        return this.getName().equals(otherType.getName());
    }
    // region kind

    /**
     * @return true it [this] is a subtype of an atomic item type.
     */
    default boolean isAtomicItemType() {
        return false;
    }

    /**
     * @return true it [this] is an object item type.
     */
    default boolean isObjectItemType() {
        return false;
    }

    /**
     * @return true it [this] is an array item type.
     */
    default boolean isArrayItemType() {
        return false;
    }

    /**
     * @return test if [this] is a subptype of a json item type
     */
    default boolean isJsonItemType() {
        return this.equals(BuiltinTypesCatalogue.JSONItem) || isObjectItemType() || isArrayItemType();
    }

    default boolean isUnionType() {
        return false;
    }

    /**
     * @return true it [this] is a function item type.
     */
    default boolean isFunctionItemType() {
        return false;
    }

    /**
     * @return true it [this] is a type compatible with DataFrames.
     */
    default boolean isDataFrameType() {
        return false;
    }

    /**
     *
     * @return [true] if this is a numeric item type, false otherwise
     */
    default boolean isNumeric() {
        return false;
    }

    // endregion

    // region concrete-specific-function

    /**
     * Tests for QName.
     *
     * @return true if [this] item type has a QName
     */
    default boolean hasName() {
        return false;
    }


    /**
     *
     * @return the itemtype QName if available
     */
    default Name getName() {
        throw new UnsupportedOperationException("getName operation is not supported for this itemType");
    }

    /**
     *
     * @return the signature of the function item type if available
     */
    default FunctionSignature getSignature() {
        throw new UnsupportedOperationException("getSignature operation is not supported for non-function item types");
    }

    // endregion

    // region hierarchy

    /**
     *
     * @param superType another item type
     * @return true if [this] is a subtype of [superType], any type is considered a subtype of itself
     */
    default boolean isSubtypeOf(ItemType superType) {
        // the default methods works fine for all non-function types
        // we exploit the fact that the type system is a tree (except for union types, that needs special checks)

        // special check for unions
        if (superType.isUnionType()) {
            for (ItemType unionItemType : superType.getUnionContentFacet().getTypes()) {
                if (this.isSubtypeOf(unionItemType)) {
                    return true;
                }
            }
        }

        // get up the type tree and check for equality
        ItemType current = this;
        while (current.getTypeTreeDepth() > superType.getTypeTreeDepth()) {
            current = current.getBaseType();
        }

        return current.equals(superType);
    }

    /**
     *
     * @param other another item type
     * @return the common supertype between [this] and [other], that would be the LCA in the item type tree of [this]
     *         and [other] (does not take into account union types as common ancestor, but only the type tree)
     */
    default ItemType findLeastCommonSuperTypeWith(ItemType other) {
        ItemType current = this;
        while (other.getTypeTreeDepth() > current.getTypeTreeDepth()) {
            other = other.getBaseType();
        }
        while (other.getTypeTreeDepth() < current.getTypeTreeDepth()) {
            current = current.getBaseType();
        }
        while (!current.equals(other)) {
            current = current.getBaseType();
            other = other.getBaseType();
        }
        return current;
    }

    /**
     *
     * @return an int representing the depth of the item type in the type tree ('item' is the root with depth 0)
     */
    int getTypeTreeDepth();

    /**
     *
     * @return the base type for a type, return null for the topmost item type
     */
    ItemType getBaseType();

    /**
     * Check at static time if [this] could be casted to [other] item type, requires [this] to be an atomic type
     *
     * @param other a strict subtype of atomic item type to which we are trying to cast
     * @return true if it is possible at static time to cast [this] to [other], false otherwise
     */
    default boolean isStaticallyCastableAs(ItemType other) {
        throw new UnsupportedOperationException(
                "isStaticallyCastableAs operation is not supported for non-atomic item types"
        );
    }

    /**
     *
     * @param itemType another item type
     * @return true if [this] can be promoted to [itemType]
     */
    default boolean canBePromotedTo(ItemType itemType) {
        return false;
    }

    // endregion

    // region user-defined

    /**
     *
     * @return [true] if it is a user-defined type, false otherwise
     */
    default boolean isUserDefined() {
        return false;
    }

    /**
     *
     * @return [true] if it is a primitive type
     */
    default boolean isPrimitive() {
        return true;
    }

    /**
     *
     * @return the primitive type for a derived type, throw an error for primitive types
     */
    default ItemType getPrimitiveType() {
        throw new UnsupportedOperationException("getPrimitiveType operation is supported only for non-primitive types");
    }

    /**
     *
     * @return a set containing the allowed facets for restricting the type
     */
    public Set<FacetTypes> getAllowedFacets();

    /**
     *
     * @return the list of possible values for [this] item type or null if the enumeration facet is not set
     */
    default List<Item> getEnumerationFacet() {
        throw new UnsupportedOperationException(
                "enumeration facet is allowed only for atomic, object and array item types"
        );
    }

    /**
     *
     * @return the list of constraints in the implementation-defined language for [this] item type (note that this facet
     *         is cumulative) or an empty list if the constraints facet is not set
     */
    default List<String> getConstraintsFacet() {
        throw new UnsupportedOperationException(
                "constraints facet is allowed only for atomic, object and array item types"
        );
    }

    /**
     *
     * @return the minimum length facet value for [this] item type or null if the restriction is not set
     */
    default Integer getMinLengthFacet() {
        throw new UnsupportedOperationException(
                "minimum length facet is not allowed for " + this.toString() + " item type"
        );
    }

    /**
     *
     * @return the length facet value for [this] item type or null if the restriction is not set
     */
    default Integer getLengthFacet() {
        throw new UnsupportedOperationException("length facet is not allowed for " + this.toString() + " item type");
    }

    /**
     *
     * @return the maximum length facet value for [this] item type or null if the restriction is not set
     */
    default Integer getMaxLengthFacet() {
        throw new UnsupportedOperationException(
                "maximum length facet is not allowed for " + this.toString() + " item type"
        );
    }

    /**
     *
     * @return an item representing the minimum possible value (excluded) for [this] item type or null if the
     *         restriction is not set
     */
    default Item getMinExclusiveFacet() {
        throw new UnsupportedOperationException(
                "minimum exclusive facet is not allowed for " + this.toString() + " item types"
        );
    }

    /**
     *
     * @return an item representing the minimum possible value (included) for [this] item type or null if the
     *         restriction is not set
     */
    default Item getMinInclusiveFacet() {
        throw new UnsupportedOperationException(
                "minimum inclusive facet is not allowed for " + this.toString() + " item types"
        );
    }

    /**
     *
     * @return an item representing the maximum possible value (excluded) for [this] item type or null if the
     *         restriction is not set
     */
    default Item getMaxExclusiveFacet() {
        throw new UnsupportedOperationException(
                "maximum exclusive facet is not allowed for " + this.toString() + " item types"
        );
    }

    /**
     *
     * @return an item representing the maximum possible value (included) for [this] item type or null if the
     *         restriction is not set
     */
    default Item getMaxInclusiveFacet() {
        throw new UnsupportedOperationException(
                "maximum inclusive facet is not allowed for " + this.toString() + " item types"
        );
    }

    /**
     *
     * @return the total digits facet value for [this] item type or null if the restriction is not set
     */
    default Integer getTotalDigitsFacet() {
        throw new UnsupportedOperationException(
                "total digits facet is not allowed for " + this.toString() + " item types"
        );
    }

    /**
     *
     * @return the fraction digits facet value for [this] item type or null if the restriction is not set
     */
    default Integer getFractionDigitsFacet() {
        throw new UnsupportedOperationException(
                "fraction digits facet is not allowed for " + this.toString() + " item types"
        );
    }

    /**
     *
     * @return the explicit timezone facet value for [this] item type or null if the restriction is not set
     */
    default TimezoneFacet getExplicitTimezoneFacet() {
        throw new UnsupportedOperationException(
                "explicit timezone facet is not allowed for " + this.toString() + " item types"
        );
    }

    /**
     *
     * @return content facet value for object item types (cumulative facet)
     */
    default Map<String, FieldDescriptor> getObjectContentFacet() {
        throw new UnsupportedOperationException(
                "object content facet is allowed only for object item types (class "
                    + this.getClass().getCanonicalName()
                    + ")"
        );
    }

    /**
     *
     * @return closed facet value for object item types
     */
    default boolean getClosedFacet() {
        throw new UnsupportedOperationException("closed facet is not allowed only for object item types");
    }

    /**
     *
     * @return content facet value for array item types
     */
    default ItemType getArrayContentFacet() {
        throw new UnsupportedOperationException(
                "array content facet is allowed only for array item types (class "
                    + this.getClass().getCanonicalName()
                    + ")"
        );
    }

    /**
     *
     * @return content facet value for union item types
     */
    default UnionContentDescriptor getUnionContentFacet() {
        throw new UnsupportedOperationException("union content facet is allowed only for union item types");
    }

    // endregion

    /**
     *
     * @return a String that uniquely identify an item type
     */
    default String getIdentifierString() {
        if (!this.hasName()) {
            return "<anonymous>";
        }
        return this.getName().toString();
    }

    /**
     * Checks compatibility with DataFrames.
     * 
     * @return true if compatible with DataFrames and false otherwise.
     */
    default boolean isCompatibleWithDataFrames() {
        return false;
    }

    String toString();

    default DataType toDataFrameType() {
        throw new UnsupportedOperationException(
                "toDataFrameType method is not supported for " + this.toString() + " item types"
        );
    }

    default boolean isResolved() {
        return true;
    }

    default void resolve(DynamicContext context, ExceptionMetadata metadata) {
        return;
    }

    default void resolve(StaticContext context, ExceptionMetadata metadata) {
        return;
    }
}
