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


import org.rumbledb.context.Name;

import java.io.Serializable;

public interface ItemType extends Serializable {

    // protected static final long serialVersionUID = 1L;

    /**
     * Tests for itemType equality.
     *
     * @param other another item type.
     * @return true it is equal to other, false otherwise.
     */
    public boolean equals(Object other);

    /**
     * @return true it [this] is a subtype of an atomic item type.
     */
    public default boolean isSubtypeOfAtomicItem() {
        return false;
    }

    /**
     * @return true it [this] is an object item type.
     */
    public default boolean isObjectItem() {
        return false;
    }

    /**
     * @return true it [this] is an array item type.
     */
    public default boolean isArrayItem() {
        return false;
    }

    /**
     * @return true it [this] is a function item type.
     */
    public default boolean isFunctionItem() {
        return false;
    }

    /**
     *
     * @return [true] if this is a numeric item type, false otherwise
     */
    public default boolean isNumeric() {
        return false;
    }

    /**
     * Tests for QName.
     *
     * @return true if [this] item type has a QName
     */
    public default boolean hasName() {
        return false;
    }


    /**
     *
     * @return the itemtype QName if available
     */
    public default Name getName() {
        throw new UnsupportedOperationException("getName operation is not supported for this itemType");
    }

    /**
     *
     * @return the signature of the function item type if available
     */
    public default FunctionSignature getSignature() {
        throw new UnsupportedOperationException("getSignature operation is not supported for non-function item types");
    }

    /**
     *
     * @param superType another item type
     * @return true if [this] is a subtype of [superType], any type is considered a subtype of itself
     */
    public boolean isSubtypeOf(ItemType superType);

    /**
     *
     * @param other another item type
     * @return the common supertype between [this] and [other], that would be the LCA in the item type tree of [this]
     *         and [other]
     */
    public ItemType findCommonSuperType(ItemType other);

    /**
     * Check at static time if [this] could be casted to [other] item type, requires [this] to be an atomic type
     *
     * @param other a strict subtype of atomic item type to which we are trying to cast
     * @return true if it is possible at static time to cast [this] to [other], false otherwise
     */
    public default boolean isStaticallyCastableAs(ItemType other) {
        throw new UnsupportedOperationException(
                "isStaticallyCastableAs operation is not supported for non-atomic item types"
        );
    }

    /**
     *
     * @param itemType another item type
     * @return true if [this] can be promoted to [itemType]
     */
    public default boolean canBePromotedTo(ItemType itemType) {
        return false;
    }

    public String toString();
}
