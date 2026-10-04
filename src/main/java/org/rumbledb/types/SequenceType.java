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
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.functions.FunctionCoercion;

@Log4j2
@Getter
public class SequenceType implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private ItemType itemType;
    private SequenceCardinality cardinality;

    public SequenceType(ItemType itemType, Arity arity) {
        this(itemType, SequenceCardinality.fromArity(arity == null ? Arity.ZeroOrMore : arity));
    }

    public SequenceType(ItemType itemType, SequenceCardinality cardinality) {
        this.cardinality = cardinality;
        this.itemType = cardinality == SequenceCardinality.EMPTY ? BuiltinTypesCatalogue.item : itemType;
        if (this.itemType == null) {
            log.warn("Missing item type in incomplete sequence type {}, defaulting to item.", getArity());
            this.itemType = BuiltinTypesCatalogue.item;
        }
    }

    public SequenceType(ItemType itemType) {
        this(itemType, SequenceCardinality.ONE);
        if (itemType == null) {
            throw new OurBadException("Missing item type in incomplete sequence type " + getArity());
        }
    }

    private SequenceType() {
        this(BuiltinTypesCatalogue.item, SequenceCardinality.EMPTY);
    }

    // Declared occurrence indicators remain a conservative view for existing runtime consumers.
    public Arity getArity() {
        return this.cardinality.toArity();
    }

    public boolean isResolved() {
        return isEmptySequence() || this.itemType.isResolved();
    }

    public void resolve(DynamicContext context, ExceptionMetadata metadata) {
        if (!isEmptySequence()) {
            this.itemType.resolve(context, metadata);
        }
    }

    public void resolve(StaticContext context, ExceptionMetadata metadata) {
        if (!isEmptySequence()) {
            this.itemType.resolve(context, metadata);
        }
    }

    public boolean isEmptySequence() {
        return this.cardinality == SequenceCardinality.EMPTY;
    }

    public boolean isSubtypeOf(SequenceType superType) {
        if (isEmptySequence()) {
            return superType.cardinality.allowsZero();
        }
        if (this.itemType.equals(BuiltinTypesCatalogue.errorItem)) {
            return !this.cardinality.allowsZero() || superType.cardinality.allowsZero();
        }
        return this.itemType.isSubtypeOf(superType.itemType) && this.cardinality.isSubtypeOf(superType.cardinality);
    }

    // Includes automatic promotions and function coercion used for declared parameter types.
    public boolean isSubtypeOfOrCanBePromotedTo(SequenceType superType) {
        if (isEmptySequence()) {
            return superType.cardinality.allowsZero();
        }
        if (this.itemType.equals(BuiltinTypesCatalogue.errorItem)) {
            return !this.cardinality.allowsZero() || superType.cardinality.allowsZero();
        }
        if (this.itemType.isUnionType()) {
            // A member can fit directly while another needs promotion or function coercion.
            return this.itemType.getTypes().stream().allMatch(member -> new SequenceType(member, this.cardinality)
                    .isSubtypeOfOrCanBePromotedTo(superType));
        }
        return this.cardinality.isSubtypeOf(superType.cardinality)
                && (this.itemType.isSubtypeOf(superType.itemType)
                        || this.itemType.canBePromotedTo(superType.itemType)
                        || FunctionCoercion.canItemTypeBeFunctionCoercedTo(this.itemType, superType.itemType));
    }

    public boolean isAritySubtypeOf(Arity superArity) {
        return this.cardinality.isSubtypeOf(SequenceCardinality.fromArity(superArity));
    }

    public boolean hasEffectiveBooleanValue() {
        if (isEmptySequence()) {
            return true;
        } else if (this.itemType.isUnionType()) {
            // Every possible member must allow EBV at this sequence's cardinality.
            return this.itemType.getTypes().stream()
                    .allMatch(member -> new SequenceType(member, this.cardinality).hasEffectiveBooleanValue());
        } else if (this.itemType.isSubtypeOf(BuiltinTypesCatalogue.nodeItem)
                || this.itemType.isSubtypeOf(BuiltinTypesCatalogue.JSONItem)) {
            return true;
        } else {
            return !this.cardinality.allowsMany()
                    && (this.itemType.isNumeric()
                            || this.itemType.isSubtypeOf(BuiltinTypesCatalogue.stringItem)
                            || this.itemType.isSubtypeOf(BuiltinTypesCatalogue.anyURIItem)
                            || this.itemType.isSubtypeOf(BuiltinTypesCatalogue.untypedAtomicItem)
                            || this.itemType.equals(BuiltinTypesCatalogue.nullItem)
                            || this.itemType.equals(BuiltinTypesCatalogue.booleanItem));
        }
    }

    public boolean hasOverlapWith(SequenceType other) {
        if (!this.cardinality.overlaps(other.cardinality)) {
            return false;
        }
        if (isEmptySequence() || other.isEmptySequence()) {
            return true;
        }
        // A union overlaps another item type when at least one member overlaps it.
        if (this.itemType.isUnionType()) {
            return this.itemType.getTypes().stream()
                    .anyMatch(member -> new SequenceType(member, this.cardinality).hasOverlapWith(other));
        }
        if (other.itemType.isUnionType()) {
            return other.hasOverlapWith(this);
        }
        return this.itemType.isSubtypeOf(other.itemType) || other.itemType.isSubtypeOf(this.itemType);
    }

    public SequenceType leastCommonSupertypeWith(SequenceType other) {
        ItemType itemSupertype = isEmptySequence()
                ? other.itemType
                : other.isEmptySequence() ? this.itemType : joinItemTypes(other.itemType);
        return new SequenceType(itemSupertype, this.cardinality.union(other.cardinality));
    }

    private ItemType joinItemTypes(ItemType other) {
        // Preserve mixed atomic/node alternatives across branches, while retaining
        // the existing joins for purely atomic and structured types.
        if (TypeAtomization.isAtomicOrNode(this.itemType)
                && TypeAtomization.isAtomicOrNode(other)
                && (TypeAtomization.containsNode(this.itemType) || TypeAtomization.containsNode(other))
                && !(this.itemType.isSubtypeOf(BuiltinTypesCatalogue.nodeItem)
                        && other.isSubtypeOf(BuiltinTypesCatalogue.nodeItem))) {
            return ItemTypeFactory.createInferredUnionType(List.of(this.itemType, other));
        }
        return this.itemType.findLeastCommonSuperTypeWith(other);
    }

    public SequenceType concatenateWith(SequenceType other) {
        if (isEmptySequence()) {
            return other;
        }
        if (other.isEmptySequence()) {
            return this;
        }
        ItemType contentType = concatenateItemTypes(other.itemType);
        return new SequenceType(contentType, this.cardinality.concatenate(other.cardinality));
    }

    private ItemType concatenateItemTypes(ItemType other) {
        if (this.itemType.equals(other)) {
            return this.itemType;
        }
        if (this.itemType.isObjectItemType() && other.isObjectItemType()) {
            boolean sameShape = !this.itemType.hasName()
                    && !other.hasName()
                    && this.itemType.getBaseType().equals(BuiltinTypesCatalogue.objectItem)
                    && other.getBaseType().equals(BuiltinTypesCatalogue.objectItem)
                    && haveSameObjectFields(this.itemType, other);
            // Keep equivalent object schemas usable as DataFrames; merge field presence conservatively.
            return sameShape
                    ? this.itemType.findLeastCommonSuperTypeLax(other)
                    : this.itemType.findLeastCommonSuperTypeWith(other);
        }
        if (TypeAtomization.isAtomicOrNode(this.itemType)
                && TypeAtomization.isAtomicOrNode(other)
                && !(this.itemType.isSubtypeOf(BuiltinTypesCatalogue.nodeItem)
                        && other.isSubtypeOf(BuiltinTypesCatalogue.nodeItem))) {
            return ItemTypeFactory.createInferredUnionType(List.of(this.itemType, other));
        }
        // Structured types have existing joins used by navigation and native execution.
        return this.itemType.findLeastCommonSuperTypeWith(other);
    }

    private static boolean haveSameObjectFields(ItemType left, ItemType right) {
        if (left.getObjectKeysFacet().size() != right.getObjectKeysFacet().size()) {
            return false;
        }
        for (String key : left.getObjectKeysFacet()) {
            FieldDescriptor leftField = left.getObjectContentFacet(key);
            FieldDescriptor rightField = right.getObjectContentFacet(key);
            if (rightField == null
                    || !leftField.getType().equals(rightField.getType())
                    || leftField.isRequired() != rightField.isRequired()
                    || !Objects.equals(leftField.isUnique(), rightField.isUnique())
                    || !Objects.equals(leftField.getDefaultValue(), rightField.getDefaultValue())) {
                return false;
            }
        }
        return true;
    }

    // Grouping concatenates one or more sequences for each group.
    public SequenceType incrementArity() {
        return new SequenceType(this.itemType, this.cardinality.repeated(SequenceCardinality.ONE_OR_MANY));
    }

    public SequenceType refineCardinalityIfSubtype(SequenceCardinality other) {
        return other.isSubtypeOf(this.cardinality) ? new SequenceType(this.itemType, other) : this;
    }

    public enum Arity {
        OneOrZero {
            @Override
            public String getSymbol() {
                return "?";
            }
        },
        OneOrMore {
            @Override
            public String getSymbol() {
                return "+";
            }
        },
        ZeroOrMore {
            @Override
            public String getSymbol() {
                return "*";
            }
        },
        One {
            @Override
            public String getSymbol() {
                return "";
            }
        },
        Zero {
            @Override
            public String getSymbol() {
                return "<void>";
            }
        };

        public abstract String getSymbol();

        public boolean isSubtypeOf(Arity superArity) {
            return SequenceCardinality.fromArity(this).isSubtypeOf(SequenceCardinality.fromArity(superArity));
        }

        // Declared arities that allow many items also allow one, so the operand order does not matter here.
        public Arity multiplyWith(Arity other) {
            return SequenceCardinality.fromArity(this)
                    .repeated(SequenceCardinality.fromArity(other))
                    .toArity();
        }
    }

    /**
     * Equal sequence types have the same item type and declared occurrence indicator, as their notation shows.
     * Inferred refinements such as MANY are not part of equality; compare getCardinality() where they matter.
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof SequenceType that
                && this.itemType.equals(that.itemType)
                && getArity() == that.getArity();
    }

    @Override
    public int hashCode() {
        return 31 * this.itemType.hashCode() + getArity().hashCode();
    }

    @Override
    public String toString() {
        if (isEmptySequence()) {
            return "()";
        }
        ItemType itemType = this.getItemType();
        StringBuilder result = new StringBuilder();
        if (itemType.hasName()) {
            Name name = itemType.getName();
            if (name != null) {
                result.append(name);
            } else {
                result.append("<anonymous>(").append(itemType).append(")");
            }
        } else {
            result.append(itemType);
        }
        result.append(getArity().getSymbol());
        return result.toString();
    }

    private static final Map<String, SequenceType> sequenceTypes;

    static {
        sequenceTypes = new HashMap<>();
    }

    public static SequenceType createSequenceType(String userFriendlyName) {
        // lazily caching of sequence types
        SequenceType st = sequenceTypes.get(userFriendlyName);
        if (st != null) {
            return st;
        }
        // if not found in cache, create the sequence type on the fly, cache it and return
        switch (userFriendlyName) {
            case "()":
                st = new SequenceType();
                break;
            case "item":
                st = new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.One);
                break;
            case "item?":
                st = new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.OneOrZero);
                break;
            case "item*":
                st = new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore);
                break;
            case "item+":
                st = new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.OneOrMore);
                break;
            case "object":
                st = new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceType.Arity.One);
                break;
            case "object+":
                st = new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceType.Arity.OneOrMore);
                break;
            case "object?":
                st = new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceType.Arity.OneOrZero);
                break;
            case "object*":
                st = new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "json-item":
                st = new SequenceType(BuiltinTypesCatalogue.JSONItem, SequenceType.Arity.One);
                break;
            case "json-item?":
                st = new SequenceType(BuiltinTypesCatalogue.JSONItem, SequenceType.Arity.OneOrZero);
                break;
            case "json-item*":
                st = new SequenceType(BuiltinTypesCatalogue.JSONItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "json-item+":
                st = new SequenceType(BuiltinTypesCatalogue.JSONItem, SequenceType.Arity.OneOrMore);
                break;
            case "array":
                st = new SequenceType(BuiltinTypesCatalogue.xqueryArrayItem, SequenceType.Arity.One);
                break;
            case "array?":
                st = new SequenceType(BuiltinTypesCatalogue.xqueryArrayItem, SequenceType.Arity.OneOrZero);
                break;
            case "array*":
                st = new SequenceType(BuiltinTypesCatalogue.xqueryArrayItem, Arity.ZeroOrMore);
                break;
            case "array+":
                st = new SequenceType(BuiltinTypesCatalogue.xqueryArrayItem, Arity.OneOrMore);
                break;
            case "item-array":
                st = new SequenceType(BuiltinTypesCatalogue.arrayItem, SequenceType.Arity.One);
                break;
            case "item-array?":
                st = new SequenceType(BuiltinTypesCatalogue.arrayItem, SequenceType.Arity.OneOrZero);
                break;
            case "item-array*":
                st = new SequenceType(BuiltinTypesCatalogue.arrayItem, Arity.ZeroOrMore);
                break;
            case "item-array+":
                st = new SequenceType(BuiltinTypesCatalogue.arrayItem, Arity.OneOrMore);
                break;
            case "anyAtomicType":
                st = new SequenceType(BuiltinTypesCatalogue.atomicItem, SequenceType.Arity.One);
                break;
            case "anyAtomicType+":
                st = new SequenceType(BuiltinTypesCatalogue.atomicItem, Arity.OneOrMore);
                break;
            case "anyAtomicType?":
                st = new SequenceType(BuiltinTypesCatalogue.atomicItem, SequenceType.Arity.OneOrZero);
                break;
            case "anyAtomicType*":
                st = new SequenceType(BuiltinTypesCatalogue.atomicItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "error":
                st = new SequenceType(BuiltinTypesCatalogue.errorItem, SequenceType.Arity.One);
                break;
            case "error+":
                st = new SequenceType(BuiltinTypesCatalogue.errorItem, Arity.OneOrMore);
                break;
            case "error?":
                st = new SequenceType(BuiltinTypesCatalogue.errorItem, SequenceType.Arity.OneOrZero);
                break;
            case "error*":
                st = new SequenceType(BuiltinTypesCatalogue.errorItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "string":
                st = new SequenceType(BuiltinTypesCatalogue.stringItem, SequenceType.Arity.One);
                break;
            case "string?":
                st = new SequenceType(BuiltinTypesCatalogue.stringItem, SequenceType.Arity.OneOrZero);
                break;
            case "string*":
                st = new SequenceType(BuiltinTypesCatalogue.stringItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "string+":
                st = new SequenceType(BuiltinTypesCatalogue.stringItem, Arity.OneOrMore);
                break;
            case "integer":
                st = new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.One);
                break;
            case "integer?":
                st = new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.OneOrZero);
                break;
            case "integer*":
                st = new SequenceType(BuiltinTypesCatalogue.integerItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "integer+":
                st = new SequenceType(BuiltinTypesCatalogue.integerItem, Arity.OneOrMore);
                break;
            case "numeric?":
                st = new SequenceType(BuiltinTypesCatalogue.numericItem, SequenceType.Arity.OneOrZero);
                break;
            case "numeric":
                st = new SequenceType(BuiltinTypesCatalogue.numericItem, SequenceType.Arity.One);
                break;
            case "numeric+":
                st = new SequenceType(BuiltinTypesCatalogue.numericItem, Arity.OneOrMore);
                break;
            case "numeric*":
                st = new SequenceType(BuiltinTypesCatalogue.numericItem, Arity.ZeroOrMore);
                break;
            case "decimal":
                st = new SequenceType(BuiltinTypesCatalogue.decimalItem, SequenceType.Arity.One);
                break;
            case "decimal?":
                st = new SequenceType(BuiltinTypesCatalogue.decimalItem, SequenceType.Arity.OneOrZero);
                break;
            case "decimal+":
                st = new SequenceType(BuiltinTypesCatalogue.decimalItem, Arity.OneOrMore);
                break;
            case "decimal*":
                st = new SequenceType(BuiltinTypesCatalogue.decimalItem, Arity.ZeroOrMore);
                break;
            case "double":
                st = new SequenceType(BuiltinTypesCatalogue.doubleItem, SequenceType.Arity.One);
                break;
            case "double?":
                st = new SequenceType(BuiltinTypesCatalogue.doubleItem, SequenceType.Arity.OneOrZero);
                break;
            case "double+":
                st = new SequenceType(BuiltinTypesCatalogue.doubleItem, Arity.OneOrMore);
                break;
            case "double*":
                st = new SequenceType(BuiltinTypesCatalogue.doubleItem, Arity.ZeroOrMore);
                break;
            case "float":
                st = new SequenceType(BuiltinTypesCatalogue.floatItem, SequenceType.Arity.One);
                break;
            case "float?":
                st = new SequenceType(BuiltinTypesCatalogue.floatItem, SequenceType.Arity.OneOrZero);
                break;
            case "float+":
                st = new SequenceType(BuiltinTypesCatalogue.floatItem, Arity.OneOrMore);
                break;
            case "float*":
                st = new SequenceType(BuiltinTypesCatalogue.floatItem, Arity.ZeroOrMore);
                break;
            case "boolean":
                st = new SequenceType(BuiltinTypesCatalogue.booleanItem, SequenceType.Arity.One);
                break;
            case "boolean?":
                st = new SequenceType(BuiltinTypesCatalogue.booleanItem, SequenceType.Arity.OneOrZero);
                break;
            case "boolean+":
                st = new SequenceType(BuiltinTypesCatalogue.booleanItem, Arity.OneOrMore);
                break;
            case "boolean*":
                st = new SequenceType(BuiltinTypesCatalogue.booleanItem, Arity.ZeroOrMore);
                break;
            case "duration":
                st = new SequenceType(BuiltinTypesCatalogue.durationItem, SequenceType.Arity.One);
                break;
            case "duration?":
                st = new SequenceType(BuiltinTypesCatalogue.durationItem, SequenceType.Arity.OneOrZero);
                break;
            case "duration+":
                st = new SequenceType(BuiltinTypesCatalogue.durationItem, Arity.OneOrMore);
                break;
            case "duration*":
                st = new SequenceType(BuiltinTypesCatalogue.durationItem, Arity.ZeroOrMore);
                break;
            case "yearMonthDuration":
                st = new SequenceType(BuiltinTypesCatalogue.yearMonthDurationItem, SequenceType.Arity.One);
                break;
            case "yearMonthDuration?":
                st = new SequenceType(BuiltinTypesCatalogue.yearMonthDurationItem, SequenceType.Arity.OneOrZero);
                break;
            case "yearMonthDuration*":
                st = new SequenceType(BuiltinTypesCatalogue.yearMonthDurationItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "yearMonthDuration+":
                st = new SequenceType(BuiltinTypesCatalogue.yearMonthDurationItem, SequenceType.Arity.OneOrMore);
                break;
            case "dayTimeDuration":
                st = new SequenceType(BuiltinTypesCatalogue.dayTimeDurationItem, SequenceType.Arity.One);
                break;
            case "dayTimeDuration?":
                st = new SequenceType(BuiltinTypesCatalogue.dayTimeDurationItem, SequenceType.Arity.OneOrZero);
                break;
            case "dayTimeDuration*":
                st = new SequenceType(BuiltinTypesCatalogue.dayTimeDurationItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "dayTimeDuration+":
                st = new SequenceType(BuiltinTypesCatalogue.dayTimeDurationItem, SequenceType.Arity.OneOrMore);
                break;
            case "dateTime":
                st = new SequenceType(BuiltinTypesCatalogue.dateTimeItem, SequenceType.Arity.One);
                break;
            case "dateTime?":
                st = new SequenceType(BuiltinTypesCatalogue.dateTimeItem, SequenceType.Arity.OneOrZero);
                break;
            case "dateTime*":
                st = new SequenceType(BuiltinTypesCatalogue.dateTimeItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "dateTime+":
                st = new SequenceType(BuiltinTypesCatalogue.dateTimeItem, SequenceType.Arity.OneOrMore);
                break;
            case "dateTimeStamp":
                st = new SequenceType(BuiltinTypesCatalogue.dateTimeStampItem, SequenceType.Arity.One);
                break;
            case "dateTimeStamp?":
                st = new SequenceType(BuiltinTypesCatalogue.dateTimeStampItem, SequenceType.Arity.OneOrZero);
                break;
            case "dateTimeStamp*":
                st = new SequenceType(BuiltinTypesCatalogue.dateTimeStampItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "dateTimeStamp+":
                st = new SequenceType(BuiltinTypesCatalogue.dateTimeStampItem, SequenceType.Arity.OneOrMore);
                break;
            case "date":
                st = new SequenceType(BuiltinTypesCatalogue.dateItem, SequenceType.Arity.One);
                break;
            case "date?":
                st = new SequenceType(BuiltinTypesCatalogue.dateItem, SequenceType.Arity.OneOrZero);
                break;
            case "date*":
                st = new SequenceType(BuiltinTypesCatalogue.dateItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "date+":
                st = new SequenceType(BuiltinTypesCatalogue.dateItem, SequenceType.Arity.OneOrMore);
                break;
            case "time":
                st = new SequenceType(BuiltinTypesCatalogue.timeItem, SequenceType.Arity.One);
                break;
            case "time?":
                st = new SequenceType(BuiltinTypesCatalogue.timeItem, SequenceType.Arity.OneOrZero);
                break;
            case "time*":
                st = new SequenceType(BuiltinTypesCatalogue.timeItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "time+":
                st = new SequenceType(BuiltinTypesCatalogue.timeItem, SequenceType.Arity.OneOrMore);
                break;
            case "gDay":
                st = new SequenceType(BuiltinTypesCatalogue.gDayItem, SequenceType.Arity.One);
                break;
            case "gDay?":
                st = new SequenceType(BuiltinTypesCatalogue.gDayItem, SequenceType.Arity.OneOrZero);
                break;
            case "gDay*":
                st = new SequenceType(BuiltinTypesCatalogue.gDayItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "gDay+":
                st = new SequenceType(BuiltinTypesCatalogue.gDayItem, SequenceType.Arity.OneOrMore);
                break;
            case "gMonth":
                st = new SequenceType(BuiltinTypesCatalogue.gMonthItem, SequenceType.Arity.One);
                break;
            case "gMonth?":
                st = new SequenceType(BuiltinTypesCatalogue.gMonthItem, SequenceType.Arity.OneOrZero);
                break;
            case "gMonth*":
                st = new SequenceType(BuiltinTypesCatalogue.gMonthItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "gMonth+":
                st = new SequenceType(BuiltinTypesCatalogue.gMonthItem, SequenceType.Arity.OneOrMore);
                break;
            case "gYear":
                st = new SequenceType(BuiltinTypesCatalogue.gYearItem, SequenceType.Arity.One);
                break;
            case "gYear?":
                st = new SequenceType(BuiltinTypesCatalogue.gYearItem, SequenceType.Arity.OneOrZero);
                break;
            case "gYear*":
                st = new SequenceType(BuiltinTypesCatalogue.gYearItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "gYear+":
                st = new SequenceType(BuiltinTypesCatalogue.gYearItem, SequenceType.Arity.OneOrMore);
                break;
            case "gMonthDay":
                st = new SequenceType(BuiltinTypesCatalogue.gMonthDayItem, SequenceType.Arity.One);
                break;
            case "gMonthDay?":
                st = new SequenceType(BuiltinTypesCatalogue.gMonthDayItem, SequenceType.Arity.OneOrZero);
                break;
            case "gMonthDay*":
                st = new SequenceType(BuiltinTypesCatalogue.gMonthDayItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "gMonthDay+":
                st = new SequenceType(BuiltinTypesCatalogue.gMonthDayItem, SequenceType.Arity.OneOrMore);
                break;
            case "gYearMonth":
                st = new SequenceType(BuiltinTypesCatalogue.gYearMonthItem, SequenceType.Arity.One);
                break;
            case "gYearMonth?":
                st = new SequenceType(BuiltinTypesCatalogue.gYearMonthItem, SequenceType.Arity.OneOrZero);
                break;
            case "gYearMonth*":
                st = new SequenceType(BuiltinTypesCatalogue.gYearMonthItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "gYearMonth+":
                st = new SequenceType(BuiltinTypesCatalogue.gYearMonthItem, SequenceType.Arity.OneOrMore);
                break;
            case "language":
                st = new SequenceType(BuiltinTypesCatalogue.languageItem, SequenceType.Arity.One);
                break;
            case "language?":
                st = new SequenceType(BuiltinTypesCatalogue.languageItem, SequenceType.Arity.OneOrZero);
                break;
            case "language*":
                st = new SequenceType(BuiltinTypesCatalogue.languageItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "language+":
                st = new SequenceType(BuiltinTypesCatalogue.languageItem, SequenceType.Arity.OneOrMore);
                break;
            case "anyURI":
                st = new SequenceType(BuiltinTypesCatalogue.anyURIItem, SequenceType.Arity.One);
                break;
            case "anyURI+":
                st = new SequenceType(BuiltinTypesCatalogue.anyURIItem, Arity.OneOrMore);
                break;
            case "anyURI*":
                st = new SequenceType(BuiltinTypesCatalogue.anyURIItem, Arity.ZeroOrMore);
                break;
            case "anyURI?":
                st = new SequenceType(BuiltinTypesCatalogue.anyURIItem, SequenceType.Arity.OneOrZero);
                break;
            case "hexBinary":
                st = new SequenceType(BuiltinTypesCatalogue.hexBinaryItem, SequenceType.Arity.One);
                break;
            case "hexBinary?":
                st = new SequenceType(BuiltinTypesCatalogue.hexBinaryItem, SequenceType.Arity.OneOrZero);
                break;
            case "hexBinary*":
                st = new SequenceType(BuiltinTypesCatalogue.hexBinaryItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "hexBinary+":
                st = new SequenceType(BuiltinTypesCatalogue.hexBinaryItem, SequenceType.Arity.OneOrMore);
                break;
            case "base64Binary":
                st = new SequenceType(BuiltinTypesCatalogue.base64BinaryItem, SequenceType.Arity.One);
                break;
            case "base64Binary?":
                st = new SequenceType(BuiltinTypesCatalogue.base64BinaryItem, SequenceType.Arity.OneOrZero);
                break;
            case "base64Binary*":
                st = new SequenceType(BuiltinTypesCatalogue.base64BinaryItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "base64Binary+":
                st = new SequenceType(BuiltinTypesCatalogue.base64BinaryItem, SequenceType.Arity.OneOrMore);
                break;
            case "null":
                st = new SequenceType(BuiltinTypesCatalogue.nullItem, SequenceType.Arity.One);
                break;
            case "null?":
                st = new SequenceType(BuiltinTypesCatalogue.nullItem, SequenceType.Arity.OneOrZero);
                break;
            case "null*":
                st = new SequenceType(BuiltinTypesCatalogue.nullItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "null+":
                st = new SequenceType(BuiltinTypesCatalogue.nullItem, SequenceType.Arity.OneOrMore);
                break;
            case "function(object*, object) as object*":
                st = new SequenceType(ItemTypeFactory.createFunctionItemType(new FunctionSignature(
                        Arrays.asList(
                                new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceType.Arity.ZeroOrMore),
                                new SequenceType(BuiltinTypesCatalogue.objectItem)),
                        new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceType.Arity.ZeroOrMore))));
                break;
            case "function(item*, item*) as item*":
                st = new SequenceType(ItemTypeFactory.createFunctionItemType(new FunctionSignature(
                        Arrays.asList(
                                new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore),
                                new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore)),
                        new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore))));
                break;
            case "function(item*) as item*":
                st = new SequenceType(ItemTypeFactory.createFunctionItemType(new FunctionSignature(
                        Collections.singletonList(
                                new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore)),
                        new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore))));
                break;
            case "function(item*) as boolean":
                st = new SequenceType(ItemTypeFactory.createFunctionItemType(new FunctionSignature(
                        Collections.singletonList(
                                new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore)),
                        new SequenceType(BuiltinTypesCatalogue.booleanItem))));
                break;
            case "function(item) as item*":
                st = new SequenceType(ItemTypeFactory.createFunctionItemType(new FunctionSignature(
                        Collections.singletonList(new SequenceType(BuiltinTypesCatalogue.item)),
                        new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore))));
                break;
            case "function(item) as boolean":
                st = new SequenceType(ItemTypeFactory.createFunctionItemType(new FunctionSignature(
                        Collections.singletonList(new SequenceType(BuiltinTypesCatalogue.item)),
                        new SequenceType(BuiltinTypesCatalogue.booleanItem))));
                break;
            case "function(item) as anyAtomicType*":
                st = new SequenceType(ItemTypeFactory.createFunctionItemType(new FunctionSignature(
                        Collections.singletonList(new SequenceType(BuiltinTypesCatalogue.item)),
                        new SequenceType(BuiltinTypesCatalogue.atomicItem, SequenceType.Arity.ZeroOrMore))));
                break;
            case "function(object*, object) as function(object*, object) as object*":
                st = new SequenceType(ItemTypeFactory.createFunctionItemType(new FunctionSignature(
                        Arrays.asList(
                                new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceType.Arity.ZeroOrMore),
                                new SequenceType(BuiltinTypesCatalogue.objectItem)),
                        new SequenceType(ItemTypeFactory.createFunctionItemType(new FunctionSignature(
                                Arrays.asList(
                                        new SequenceType(
                                                BuiltinTypesCatalogue.objectItem, SequenceType.Arity.ZeroOrMore),
                                        new SequenceType(BuiltinTypesCatalogue.objectItem)),
                                new SequenceType(BuiltinTypesCatalogue.objectItem, SequenceType.Arity.ZeroOrMore)))))));
                break;
            case "function(anyAtomicType, item*) as item*":
                st = new SequenceType(ItemTypeFactory.createFunctionItemType(new FunctionSignature(
                        Arrays.asList(
                                new SequenceType(BuiltinTypesCatalogue.atomicItem),
                                new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore)),
                        new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore))));
                break;
            case "function(item, item) as item*":
                st = new SequenceType(ItemTypeFactory.createFunctionItemType(new FunctionSignature(
                        Arrays.asList(
                                new SequenceType(BuiltinTypesCatalogue.item),
                                new SequenceType(BuiltinTypesCatalogue.item)),
                        new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore))));
                break;
            case "function(item*, item) as item*":
                st = new SequenceType(ItemTypeFactory.createFunctionItemType(new FunctionSignature(
                        Arrays.asList(
                                new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore),
                                new SequenceType(BuiltinTypesCatalogue.item)),
                        new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore))));
                break;
            case "function(item, item*) as item*":
                st = new SequenceType(ItemTypeFactory.createFunctionItemType(new FunctionSignature(
                        Arrays.asList(
                                new SequenceType(BuiltinTypesCatalogue.item),
                                new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore)),
                        new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.ZeroOrMore))));
                break;
            case "int":
                st = new SequenceType(BuiltinTypesCatalogue.intItem, SequenceType.Arity.One);
                break;
            case "int?":
                st = new SequenceType(BuiltinTypesCatalogue.intItem, SequenceType.Arity.OneOrZero);
                break;
            case "int*":
                st = new SequenceType(BuiltinTypesCatalogue.intItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "int+":
                st = new SequenceType(BuiltinTypesCatalogue.intItem, SequenceType.Arity.OneOrMore);
                break;
            case "long":
                st = new SequenceType(BuiltinTypesCatalogue.longItem, SequenceType.Arity.One);
                break;
            case "long?":
                st = new SequenceType(BuiltinTypesCatalogue.longItem, SequenceType.Arity.OneOrZero);
                break;
            case "long*":
                st = new SequenceType(BuiltinTypesCatalogue.longItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "long+":
                st = new SequenceType(BuiltinTypesCatalogue.longItem, SequenceType.Arity.OneOrMore);
                break;
            case "short":
                st = new SequenceType(BuiltinTypesCatalogue.shortItem, SequenceType.Arity.One);
                break;
            case "short?":
                st = new SequenceType(BuiltinTypesCatalogue.shortItem, SequenceType.Arity.OneOrZero);
                break;
            case "short*":
                st = new SequenceType(BuiltinTypesCatalogue.shortItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "short+":
                st = new SequenceType(BuiltinTypesCatalogue.shortItem, SequenceType.Arity.OneOrMore);
                break;
            case "byte":
                st = new SequenceType(BuiltinTypesCatalogue.byteItem, SequenceType.Arity.One);
                break;
            case "byte?":
                st = new SequenceType(BuiltinTypesCatalogue.byteItem, SequenceType.Arity.OneOrZero);
                break;
            case "byte*":
                st = new SequenceType(BuiltinTypesCatalogue.byteItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "byte+":
                st = new SequenceType(BuiltinTypesCatalogue.byteItem, SequenceType.Arity.OneOrMore);
                break;
            case "positiveInteger":
                st = new SequenceType(BuiltinTypesCatalogue.positiveIntegerItem, SequenceType.Arity.One);
                break;
            case "positiveInteger?":
                st = new SequenceType(BuiltinTypesCatalogue.positiveIntegerItem, SequenceType.Arity.OneOrZero);
                break;
            case "positiveInteger*":
                st = new SequenceType(BuiltinTypesCatalogue.positiveIntegerItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "positiveInteger+":
                st = new SequenceType(BuiltinTypesCatalogue.positiveIntegerItem, SequenceType.Arity.OneOrMore);
                break;
            case "negativeInteger":
                st = new SequenceType(BuiltinTypesCatalogue.negativeIntegerItem, SequenceType.Arity.One);
                break;
            case "negativeInteger?":
                st = new SequenceType(BuiltinTypesCatalogue.negativeIntegerItem, SequenceType.Arity.OneOrZero);
                break;
            case "negativeInteger*":
                st = new SequenceType(BuiltinTypesCatalogue.negativeIntegerItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "negativeInteger+":
                st = new SequenceType(BuiltinTypesCatalogue.negativeIntegerItem, SequenceType.Arity.OneOrMore);
                break;
            case "nonPositiveInteger":
                st = new SequenceType(BuiltinTypesCatalogue.nonPositiveIntegerItem, SequenceType.Arity.One);
                break;
            case "nonPositiveInteger?":
                st = new SequenceType(BuiltinTypesCatalogue.nonPositiveIntegerItem, SequenceType.Arity.OneOrZero);
                break;
            case "nonPositiveInteger*":
                st = new SequenceType(BuiltinTypesCatalogue.nonPositiveIntegerItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "nonPositiveInteger+":
                st = new SequenceType(BuiltinTypesCatalogue.nonPositiveIntegerItem, SequenceType.Arity.OneOrMore);
                break;
            case "nonNegativeInteger":
                st = new SequenceType(BuiltinTypesCatalogue.nonNegativeIntegerItem, SequenceType.Arity.One);
                break;
            case "nonNegativeInteger?":
                st = new SequenceType(BuiltinTypesCatalogue.nonNegativeIntegerItem, SequenceType.Arity.OneOrZero);
                break;
            case "nonNegativeInteger*":
                st = new SequenceType(BuiltinTypesCatalogue.nonNegativeIntegerItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "nonNegativeInteger+":
                st = new SequenceType(BuiltinTypesCatalogue.nonNegativeIntegerItem, SequenceType.Arity.OneOrMore);
                break;
            case "unsignedInt":
                st = new SequenceType(BuiltinTypesCatalogue.unsignedIntItem, SequenceType.Arity.One);
                break;
            case "unsignedInt?":
                st = new SequenceType(BuiltinTypesCatalogue.unsignedIntItem, SequenceType.Arity.OneOrZero);
                break;
            case "unsignedInt*":
                st = new SequenceType(BuiltinTypesCatalogue.unsignedIntItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "unsignedInt+":
                st = new SequenceType(BuiltinTypesCatalogue.unsignedIntItem, SequenceType.Arity.OneOrMore);
                break;
            case "unsignedLong":
                st = new SequenceType(BuiltinTypesCatalogue.unsignedLongItem, SequenceType.Arity.One);
                break;
            case "unsignedLong?":
                st = new SequenceType(BuiltinTypesCatalogue.unsignedLongItem, SequenceType.Arity.OneOrZero);
                break;
            case "unsignedLong*":
                st = new SequenceType(BuiltinTypesCatalogue.unsignedLongItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "unsignedLong+":
                st = new SequenceType(BuiltinTypesCatalogue.unsignedLongItem, SequenceType.Arity.OneOrMore);
                break;
            case "unsignedShort":
                st = new SequenceType(BuiltinTypesCatalogue.unsignedShortItem, SequenceType.Arity.One);
                break;
            case "unsignedShort?":
                st = new SequenceType(BuiltinTypesCatalogue.unsignedShortItem, SequenceType.Arity.OneOrZero);
                break;
            case "unsignedShort*":
                st = new SequenceType(BuiltinTypesCatalogue.unsignedShortItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "unsignedShort+":
                st = new SequenceType(BuiltinTypesCatalogue.unsignedShortItem, SequenceType.Arity.OneOrMore);
                break;
            case "unsignedByte":
                st = new SequenceType(BuiltinTypesCatalogue.unsignedByteItem, SequenceType.Arity.One);
                break;
            case "unsignedByte?":
                st = new SequenceType(BuiltinTypesCatalogue.unsignedByteItem, SequenceType.Arity.OneOrZero);
                break;
            case "unsignedByte*":
                st = new SequenceType(BuiltinTypesCatalogue.unsignedByteItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "unsignedByte+":
                st = new SequenceType(BuiltinTypesCatalogue.unsignedByteItem, SequenceType.Arity.OneOrMore);
                break;
            case "map":
                st = new SequenceType(BuiltinTypesCatalogue.mapItem, SequenceType.Arity.One);
                break;
            case "map?":
                st = new SequenceType(BuiltinTypesCatalogue.mapItem, SequenceType.Arity.OneOrZero);
                break;
            case "map*":
                st = new SequenceType(BuiltinTypesCatalogue.mapItem, Arity.ZeroOrMore);
                break;
            case "map+":
                st = new SequenceType(BuiltinTypesCatalogue.mapItem, Arity.OneOrMore);
                break;
            case "function":
                st = new SequenceType(BuiltinTypesCatalogue.anyFunctionItem, Arity.One);
                break;
            case "function(*)":
                st = new SequenceType(BuiltinTypesCatalogue.anyFunctionItem, Arity.One);
                break;
            case "function?":
                st = new SequenceType(BuiltinTypesCatalogue.anyFunctionItem, Arity.OneOrZero);
                break;
            case "function(*)?":
                st = new SequenceType(BuiltinTypesCatalogue.anyFunctionItem, Arity.OneOrZero);
                break;
            case "function*":
                st = new SequenceType(BuiltinTypesCatalogue.anyFunctionItem, Arity.ZeroOrMore);
                break;
            case "function(*)*":
                st = new SequenceType(BuiltinTypesCatalogue.anyFunctionItem, Arity.ZeroOrMore);
                break;
            case "function+":
                st = new SequenceType(BuiltinTypesCatalogue.anyFunctionItem, Arity.OneOrMore);
                break;
            case "function(*)+":
                st = new SequenceType(BuiltinTypesCatalogue.anyFunctionItem, Arity.OneOrMore);
                break;
            case "QName":
                st = new SequenceType(BuiltinTypesCatalogue.QNameItem, SequenceType.Arity.One);
                break;
            case "QName?":
                st = new SequenceType(BuiltinTypesCatalogue.QNameItem, SequenceType.Arity.OneOrZero);
                break;
            case "QName*":
                st = new SequenceType(BuiltinTypesCatalogue.QNameItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "QName+":
                st = new SequenceType(BuiltinTypesCatalogue.QNameItem, SequenceType.Arity.OneOrMore);
                break;
            case "NCName":
                st = new SequenceType(BuiltinTypesCatalogue.NCNameItem, SequenceType.Arity.One);
                break;
            case "NCName?":
                st = new SequenceType(BuiltinTypesCatalogue.NCNameItem, SequenceType.Arity.OneOrZero);
                break;
            case "NCName*":
                st = new SequenceType(BuiltinTypesCatalogue.NCNameItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "NCName+":
                st = new SequenceType(BuiltinTypesCatalogue.NCNameItem, SequenceType.Arity.OneOrMore);
                break;
            case "node()":
                st = new SequenceType(BuiltinTypesCatalogue.nodeItem, SequenceType.Arity.One);
                break;
            case "node()?":
                st = new SequenceType(BuiltinTypesCatalogue.nodeItem, SequenceType.Arity.OneOrZero);
                break;
            case "node()*":
                st = new SequenceType(BuiltinTypesCatalogue.nodeItem, SequenceType.Arity.ZeroOrMore);
                break;
            case "node()+":
                st = new SequenceType(BuiltinTypesCatalogue.nodeItem, SequenceType.Arity.OneOrMore);
                break;
            default:
                throw new OurBadException("Unrecognized type: " + userFriendlyName);
        }
        sequenceTypes.put(userFriendlyName, st);
        return st;
    }
}
