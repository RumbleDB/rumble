/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.types;

import java.io.Serial;
import java.util.Set;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;

public class FunctionItemType extends AbstractItemType {

    @Serial
    private static final long serialVersionUID = 1L;

    private final boolean isGeneric;
    private final FunctionSignature signature;

    static final FunctionItemType anyFunctionItem = new FunctionItemType(true);

    FunctionItemType(FunctionSignature signature) {
        if (signature == null) {
            throw new OurBadException("a new function item type must have a signature");
        }
        this.isGeneric = false;
        this.signature = signature;
    }

    // we have a parameter because the empty one is public and inherited
    FunctionItemType(boolean isGeneric) {
        this.isGeneric = true;
        this.signature = null;
    }

    @Override
    public Name getName() {
        return null;
    }

    @Override
    protected Object equalityKey() {
        return structuralTypeKey(FunctionItemType.class, this.isGeneric, this.signature);
    }

    @Override
    public boolean isFunctionItemType() {
        return true;
    }

    @Override
    public FunctionSignature getSignature() {
        return this.signature;
    }

    @Override
    public boolean isResolved() {
        return this.isGeneric
                || (this.signature.getParameterTypes().stream().allMatch(SequenceType::isResolved)
                        && this.signature.getReturnType().isResolved());
    }

    @Override
    public void resolve(StaticContext context, ExceptionMetadata metadata) {
        if (this.isGeneric) {
            return;
        }
        for (SequenceType parameterType : this.signature.getParameterTypes()) {
            if (!parameterType.isResolved()) {
                parameterType.resolve(context, metadata);
            }
        }
        if (!this.signature.getReturnType().isResolved()) {
            this.signature.getReturnType().resolve(context, metadata);
        }
    }

    @Override
    public void resolve(DynamicContext context, ExceptionMetadata metadata) {
        if (this.isGeneric) {
            return;
        }
        for (SequenceType parameterType : this.signature.getParameterTypes()) {
            if (!parameterType.isResolved()) {
                parameterType.resolve(context, metadata);
            }
        }
        if (!this.signature.getReturnType().isResolved()) {
            this.signature.getReturnType().resolve(context, metadata);
        }
    }

    @Override
    public boolean isSubtypeOf(ItemType superType) {
        if (this.equals(superType)
                || superType.equals(anyFunctionItem)
                || superType.equals(BuiltinTypesCatalogue.item)) {
            return true;
        }
        if (this.signature == null) {
            return false;
        }
        if (superType.isFunctionItemType() && this.signature.isSubtypeOf(superType.getSignature())) {
            return true;
        }
        return false;
    }

    @Override
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        if (this.equals(other)) {
            return this;
        }
        if (other.isMapItemType()) {
            return anyFunctionItem;
        }
        if (other.isFunctionItemType()) {
            return anyFunctionItem;
        }
        return BuiltinTypesCatalogue.item;
    }

    @Override
    public int getTypeTreeDepth() {
        return this.equals(anyFunctionItem) ? 1 : 2;
    }

    @Override
    public ItemType getBaseType() {
        return this.equals(anyFunctionItem) ? BuiltinTypesCatalogue.item : anyFunctionItem;
    }

    @Override
    public Set<ConstrainingFacetTypes> getAllowedFacets() {
        throw new UnsupportedOperationException("function item types does not support facets");
    }

    @Override
    public String getIdentifierString() {
        return this.toString();
    }

    @Override
    public String toString() {
        return this.isGeneric ? "function(*)" : this.signature.toString();
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleConfiguration configuration) {
        return false;
    }
}
