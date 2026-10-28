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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NonNull;
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
        this(itemType, cardinalityOf(itemType, arity));
    }

    public SequenceType(ItemType itemType, SequenceCardinality cardinality) {
        this.cardinality = cardinality;
        this.itemType = cardinality == SequenceCardinality.EMPTY ? BuiltinTypesCatalogue.item : itemType;
        if (this.itemType == null) {
            log.warn("Missing item type in incomplete sequence type "
                    + getArity()
                    + ", defaulting to item. Please let us know as we would like to look into this!");
            this.itemType = BuiltinTypesCatalogue.item;
        }
    }

    public SequenceType(@NonNull ItemType itemType) {
        this.itemType = itemType;
        this.cardinality = SequenceCardinality.ONE;
    }

    private static SequenceCardinality cardinalityOf(ItemType itemType, Arity arity) {
        if (arity == null) {
            log.warn("Missing arity in incomplete sequence type "
                    + itemType
                    + ", defaulting to *. Please let us know as we would like to look into this!");
            return SequenceCardinality.ANY;
        }
        return SequenceCardinality.fromArity(arity);
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
            return this.itemType.allMemberTypesMatch(
                    member -> new SequenceType(member, this.cardinality).isSubtypeOfOrCanBePromotedTo(superType));
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
        }
        return this.itemType.allMemberTypesMatch(this::memberHasEffectiveBooleanValue);
    }

    private boolean memberHasEffectiveBooleanValue(ItemType member) {
        if (member.isSubtypeOf(BuiltinTypesCatalogue.nodeItem) || member.isSubtypeOf(BuiltinTypesCatalogue.JSONItem)) {
            return true;
        }
        return !this.cardinality.allowsMany()
                && (member.isNumeric()
                        || member.isSubtypeOf(BuiltinTypesCatalogue.stringItem)
                        || member.isSubtypeOf(BuiltinTypesCatalogue.anyURIItem)
                        || member.isSubtypeOf(BuiltinTypesCatalogue.untypedAtomicItem)
                        || member.equals(BuiltinTypesCatalogue.nullItem)
                        || member.equals(BuiltinTypesCatalogue.booleanItem));
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

    public SequenceType concatenateWith(SequenceType other) {
        if (isEmptySequence()) {
            return other;
        }
        if (other.isEmptySequence()) {
            return this;
        }
        ItemType contentType = joinItemTypes(other.itemType);
        return new SequenceType(contentType, this.cardinality.concatenate(other.cardinality));
    }

    /**
     * Joins the item types of two sequences that are concatenated or that are alternatives, e.g. the branches of a
     * conditional. Atomic types, and types of different kinds such as xs:integer and an object, become an inferred
     * union that keeps its members in operand order. Two types of the same structured kind keep their existing join,
     * which navigation and native execution rely on.
     */
    private ItemType joinItemTypes(ItemType other) {
        if (this.itemType.equals(other)) {
            return this.itemType;
        }
        if (other.isSubtypeOf(this.itemType)) {
            return this.itemType;
        }
        if (this.itemType.isObjectItemType() && other.isObjectItemType()) {
            boolean anonymous = !this.itemType.hasName()
                    && !other.hasName()
                    && this.itemType.getBaseType().equals(BuiltinTypesCatalogue.objectItem)
                    && other.getBaseType().equals(BuiltinTypesCatalogue.objectItem);
            // A sequence of anonymous objects keeps the union of their fields, which are optional unless every
            // object has them. This is the schema DataFrame conversion infers from the items themselves.
            if (anonymous) {
                ItemType laxJoin = this.itemType.findLeastCommonSuperTypeLax(other);
                // In a DataFrame, SQL NULL in an optional field means the field is absent, so it cannot also stand
                // for JSON null. Such a join would let DataFrames turn {"a": null} into {}.
                if (!hasOptionalNullableField(laxJoin)) {
                    return laxJoin;
                }
            }
            return this.itemType.findLeastCommonSuperTypeWith(other);
        }
        if (haveSameStructuredKind(this.itemType, other)) {
            return this.itemType.findLeastCommonSuperTypeWith(other);
        }
        return ItemTypeFactory.createInferredUnionType(List.of(this.itemType, other));
    }

    private static boolean hasOptionalNullableField(ItemType type) {
        if (type.isArrayItemType()) {
            return hasOptionalNullableField(type.getArrayContentFacet());
        }
        if (!type.isObjectItemType()) {
            return false;
        }
        for (FieldDescriptor field : type.getObjectContentFacet()) {
            if (!field.isRequired() && BuiltinTypesCatalogue.nullItem.isSubtypeOf(field.getType())) {
                return true;
            }
            if (hasOptionalNullableField(field.getType())) {
                return true;
            }
        }
        return false;
    }

    private static boolean haveSameStructuredKind(ItemType left, ItemType right) {
        return (left.isArrayItemType() && right.isArrayItemType())
                || (left.isMapItemType() && right.isMapItemType())
                || (left.isFunctionItemType() && right.isFunctionItemType())
                || (left.isNodeItemType() && right.isNodeItemType());
    }

    /** The type of evaluating an expression of this type once for each of times items. */
    public SequenceType repeated(SequenceCardinality times) {
        return isEmptySequence() ? this : new SequenceType(this.itemType, this.cardinality.repeated(times));
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

    /**
     * Returns the sequence type of a notation such as "integer?", "item*", "node()+" or "function(item*) as boolean",
     * whose item types are builtin types named without a prefix. Builtin function signatures are written this way.
     */
    public static SequenceType createSequenceType(String notation) {
        // Not computeIfAbsent: parsing a function notation creates the sequence types of its parameters.
        SequenceType sequenceType = sequenceTypes.get(notation);
        if (sequenceType == null) {
            sequenceType = parseSequenceType(notation);
            sequenceTypes.put(notation, sequenceType);
        }
        return sequenceType;
    }

    private static SequenceType parseSequenceType(String notation) {
        if (notation.equals("()")) {
            return new SequenceType();
        }
        // The occurrence indicator of function(item*) as item* belongs to its return type.
        if (notation.startsWith("function(") && !notation.startsWith("function(*)")) {
            return new SequenceType(parseFunctionItemType(notation));
        }
        Arity arity =
                switch (notation.charAt(notation.length() - 1)) {
                    case '?' -> Arity.OneOrZero;
                    case '*' -> Arity.ZeroOrMore;
                    case '+' -> Arity.OneOrMore;
                    default -> Arity.One;
                };
        String itemType = arity == Arity.One ? notation : notation.substring(0, notation.length() - 1);
        return new SequenceType(parseItemType(itemType), arity);
    }

    /** Resolves a builtin type by its local name, as in node() or integer, except for arrays and functions. */
    private static ItemType parseItemType(String name) {
        return switch (name) {
                // Both arrays are named array: in a notation it is array(*), and item-array is the JSON array.
            case "array" -> BuiltinTypesCatalogue.xqueryArrayItem;
            case "item-array" -> BuiltinTypesCatalogue.arrayItem;
            case "function", "function(*)" -> BuiltinTypesCatalogue.anyFunctionItem;
            default -> {
                String localName = name.endsWith("()") ? name.substring(0, name.length() - "()".length()) : name;
                yield BuiltinTypesCatalogue.getItemTypeByName(Name.createVariableInDefaultTypeNamespace(localName));
            }
        };
    }

    /** Parses function(T1, ..., Tn) as R, where each type may itself be a function notation. */
    private static ItemType parseFunctionItemType(String notation) {
        List<String> parameters = new ArrayList<>();
        int depth = 0;
        int start = "function(".length();
        int index = start;
        for (; depth >= 0; index++) {
            char character = notation.charAt(index);
            if (character == '(') {
                depth++;
            } else if (character == ')') {
                depth--;
            }
            if ((character == ',' && depth == 0) || depth < 0) {
                String parameter = notation.substring(start, index).strip();
                if (!parameter.isEmpty()) {
                    parameters.add(parameter);
                }
                start = index + 1;
            }
        }
        String returnType = notation.substring(index).strip();
        if (!returnType.startsWith("as ")) {
            throw new OurBadException("Unrecognized type: " + notation);
        }
        return ItemTypeFactory.createFunctionItemType(new FunctionSignature(
                parameters.stream().map(SequenceType::createSequenceType).toList(),
                createSequenceType(returnType.substring("as ".length()).strip())));
    }
}
