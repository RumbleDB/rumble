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
package org.rumbledb.compiler.typing;

import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceCardinality;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeAtomization;
import org.rumbledb.xml.schema.XmlSchemaCatalog;

/** Typing rules of cast and castable expressions, to atomic and XML Schema simple types. */
public final class CastTyping {

    private final StaticTypeErrors errors;

    public CastTyping(StaticTypeErrors errors) {
        this.errors = errors;
    }

    public SequenceType inferCastableType(
            SequenceType operandType, SequenceType targetType, XmlSchemaCatalog schemaCatalog, Node node) {
        if (isSchemaCastTarget(targetType, schemaCatalog)) {
            checkCastOperand(operandType, node);
            return new SequenceType(BuiltinTypesCatalogue.booleanItem);
        }
        ItemType itemType = targetType.getItemType();
        if (itemType.equals(BuiltinTypesCatalogue.atomicItem)) {
            this.errors.report(
                    "atomic item type is not allowed in castable expression",
                    ErrorCode.CastableErrorCode,
                    node.getMetadata());
        }
        checkCastOperand(operandType, node);
        return new SequenceType(BuiltinTypesCatalogue.booleanItem);
    }

    public SequenceType inferCastType(
            SequenceType operandType, SequenceType targetType, XmlSchemaCatalog schemaCatalog, Node node) {
        if (isSchemaCastTarget(targetType, schemaCatalog)) {
            checkCastOperand(operandType, node);

            if (operandType.isEmptySequence()) {
                if (targetType.getArity() != SequenceType.Arity.OneOrZero) {
                    this.errors.report(
                            "Empty sequence cannot be cast to a non-optional XML Schema simple type.",
                            node.getMetadata());
                }
                return new SequenceType(BuiltinTypesCatalogue.item, SequenceType.Arity.Zero);
            }

            SequenceType atomizedOperand = AtomizedOperands.knownAtomizedType(operandType);
            if (atomizedOperand != null && !atomizedOperand.isAritySubtypeOf(targetType.getArity())) {
                this.errors.report(
                        castCardinalityMessage(operandType, atomizedOperand, targetType), node.getMetadata());
            }

            // Check the type of result will casting to this schema type produce
            SequenceType resultType = schemaCatalog.getSimpleTypeCastResultType(
                    targetType.getItemType().getName());

            if (resultType.getArity() == SequenceType.Arity.One
                    && targetType.getArity() == SequenceType.Arity.OneOrZero
                    && (atomizedOperand == null || atomizedOperand.getArity() != SequenceType.Arity.One)) {
                // Because getSimpleTypeCastResultType does not take into account the arity of the cast expression,
                // this if-statement is needed to ensure that the result type is correctly set to OneOrZero when the
                // cast expression has an optional arity.
                resultType = new SequenceType(resultType.getItemType(), SequenceType.Arity.OneOrZero);
            }
            return resultType;
        }

        // check at static time for casting errors (note cast only allows for normal or ? arity)
        SequenceType castedSequenceType = targetType;

        if (castedSequenceType.getItemType().equals(BuiltinTypesCatalogue.atomicItem)) {
            this.errors.report(
                    "atomic item type is not allowed in cast expression",
                    ErrorCode.CastableErrorCode,
                    node.getMetadata());
        }

        // Empty sequence case
        if (operandType.isEmptySequence()) {
            if (castedSequenceType.getArity() != SequenceType.Arity.OneOrZero) {
                this.errors.report(
                        "Empty sequence cannot be cast to type with quantifier different from '?'", node.getMetadata());
            } else {
                // no additional check is needed
                return castedSequenceType;
            }
        }

        SequenceType atomizedOperand = AtomizedOperands.knownAtomizedType(operandType);
        if (atomizedOperand != null && !atomizedOperand.isAritySubtypeOf(castedSequenceType.getArity())) {
            this.errors.report(
                    castCardinalityMessage(operandType, atomizedOperand, castedSequenceType), node.getMetadata());
        }

        checkCastOperand(operandType, node);
        // Non-atomic operands were reported above; with static typing disabled, leave them to runtime.
        if (TypeAtomization.isAtomicOrNode(operandType.getItemType())
                && !castedSequenceType.getItemType().equals(BuiltinTypesCatalogue.errorItem)
                && !isCastOperandTypeCompatible(operandType.getItemType(), castedSequenceType.getItemType())) {
            this.errors.report(
                    "It is never possible to cast a " + operandType + " as " + castedSequenceType,
                    ErrorCode.UnexpectedTypeErrorCode,
                    node.getMetadata());
        }
        if (TypeAtomization.inferType(operandType).getCardinality() == SequenceCardinality.ONE) {
            castedSequenceType = new SequenceType(castedSequenceType.getItemType(), SequenceType.Arity.One);
        }
        return castedSequenceType;
    }

    private static String castCardinalityMessage(SequenceType operand, SequenceType atomized, SequenceType target) {
        return "with static type feature it is not possible to cast a "
                + AtomizedOperands.describeAtomizedType(operand, atomized)
                + " as "
                + target;
    }

    private boolean isSchemaCastTarget(SequenceType sequenceType, XmlSchemaCatalog schemaCatalog) {
        ItemType itemType = sequenceType.getItemType();
        return itemType.hasName() && schemaCatalog.isSchemaCastTarget(itemType.getName());
    }

    /**
     * Accepts atomic operands and nodes whose typed values are atomized at runtime.
     * A node is not itself atomic, but its typed value can supply atomic cast operands.
     * The static node types used here do not say whether atomization succeeds or how many
     * atomic values it produces; the runtime checks those properties after atomization.
     */
    private void checkCastOperand(SequenceType operandType, Node node) {
        StaticTypeErrors.requireInferredType(operandType, node);
        if (!operandType.isEmptySequence() && !TypeAtomization.isAtomicOrNode(operandType.getItemType())) {
            this.errors.report(
                    "A cast operand must be atomic after atomization, found " + operandType,
                    operandType.getItemType().isSubtypeOf(BuiltinTypesCatalogue.JSONItem)
                            ? ErrorCode.NonAtomicElementErrorCode
                            : ErrorCode.AtomizationError,
                    node.getMetadata());
        }
    }

    private boolean isCastOperandTypeCompatible(ItemType source, ItemType target) {
        // A node is cast through its typed value: an unknown one is checked at runtime, a known one statically.
        return source.allMemberTypesMatch(member -> TypeAtomization.hasUnknownTypedValue(member)
                || TypeAtomization.atomizedItemType(member)
                        .allMemberTypesMatch(atomized -> atomized.isStaticallyCastableAs(target)));
    }
}
