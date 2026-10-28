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

import java.util.List;

import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceCardinality;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeAtomization;

/** Static types of operands after atomization, shared by the operators, casts and FLWOR keys. */
public final class AtomizedOperands {

    private AtomizedOperands() {}

    /**
     * The error for an operand that may not be atomizable: FOTY0013, the error of atomization, when it can never be
     * atomized, such as an object, and XPTY0004 when it only may not be.
     */
    public static ErrorCode nonAtomizableErrorCode(ItemType type) {
        return TypeAtomization.isNeverAtomizable(type)
                ? ErrorCode.FunctionAtomizationErrorCode
                : ErrorCode.UnexpectedTypeErrorCode;
    }

    /** Checks that the operands of an operator that atomizes them, such as + or eq, can be atomized. */
    public static void checkAtomizableOperands(
            StaticTypeErrors errors, String operator, SequenceType left, SequenceType right, Node node) {
        ItemType reported = null;
        for (SequenceType operand : List.of(left, right)) {
            ItemType type = operand.isEmptySequence() ? null : operand.getItemType();
            // an operand that can never be atomized is reported first, since atomizing it always fails
            if (type != null
                    && !TypeAtomization.isAtomizable(type)
                    && (reported == null || TypeAtomization.isNeverAtomizable(type))) {
                reported = type;
            }
        }
        if (reported != null) {
            errors.report(
                    "The operands of '" + operator + "' must be atomizable, found " + reported,
                    nonAtomizableErrorCode(reported),
                    node,
                    !TypeAtomization.isNeverAtomizable(reported));
        }
    }

    /**
     * Returns the type of an operand after atomization: one node can yield zero values (a nilled element), one value,
     * or several (a schema list). Returns null when the typed value, or the number of values, is only known at
     * runtime, as for an array.
     */
    public static SequenceType knownAtomizedType(SequenceType operand) {
        if (!TypeAtomization.containsNodeOrArray(operand.getItemType())) {
            return operand;
        }
        return TypeAtomization.hasUnknownAtomizedCount(operand.getItemType())
                ? null
                : TypeAtomization.inferType(operand);
    }

    /** Describes an operand together with its typed value, when atomization changes it and it is known. */
    public static String describeAtomizedType(SequenceType operand, SequenceType atomized) {
        return atomized == null || operand.equals(atomized)
                ? operand.toString()
                : operand + " with typed value " + atomized;
    }

    /**
     * The arity of the atomized value of an operand of a singleton operator, or null when it may have several values.
     * Unknown node typed values and the members of arrays are checked at runtime; known multi-valued atomic operands
     * remain static errors.
     */
    public static SequenceType.Arity atomizedSingletonArity(SequenceType source) {
        SequenceType atomized =
                TypeAtomization.isAtomizable(source.getItemType()) ? TypeAtomization.inferType(source) : source;
        if (atomized.getCardinality().allowsMany() && !TypeAtomization.hasUnknownAtomizedCount(source.getItemType())) {
            return null;
        }
        return atomized.getCardinality().atMostOne().toArity();
    }

    /** Whether some value of the operand atomizes to at most one value, as singleton operators require. */
    public static boolean mayBeSingleton(SequenceType source) {
        SequenceType atomized =
                TypeAtomization.isAtomizable(source.getItemType()) ? TypeAtomization.inferType(source) : source;
        return !atomized.getCardinality().equals(SequenceCardinality.MANY)
                || TypeAtomization.hasUnknownAtomizedCount(source.getItemType());
    }

    public static SequenceType.Arity resolveArities(SequenceType leftType, SequenceType rightType) {
        SequenceType.Arity left = atomizedSingletonArity(leftType);
        SequenceType.Arity right = atomizedSingletonArity(rightType);
        if (left == null || right == null) return null;
        return (left == SequenceType.Arity.OneOrZero || right == SequenceType.Arity.OneOrZero)
                ? SequenceType.Arity.OneOrZero
                : SequenceType.Arity.One;
    }
}
