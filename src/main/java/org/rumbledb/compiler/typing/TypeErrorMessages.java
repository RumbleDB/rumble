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

import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

/**
 * The static type errors that several checks share, each with its message and error code, so that they read alike. A
 * message names what expects a value, such as "'+'" or "A cast", then the operand that does not provide it, such as
 * "its left operand", with its type. Messages are full sentences and do not restate the rule of their error code.
 */
public final class TypeErrorMessages {

    /** The message of a static type error and its code. */
    public record Message(String text, ErrorCode code) {}

    private TypeErrorMessages() {}

    /** An operand that may not be atomizable, such as a map, where atomic values are expected. */
    public static Message notAtomizable(String expecter, String operand, SequenceType type) {
        return new Message(
                expecter + " expects atomizable values, but " + operand + " is " + TypeNotation.shortened(type) + ".",
                AtomizedOperands.nonAtomizableErrorCode(type.getItemType()));
    }

    /** An operand that may atomize to several values, where at most one is expected. */
    public static Message severalValues(String expecter, String operand, SequenceType type) {
        return new Message(
                expecter
                        + " expects at most one value, but "
                        + operand
                        + " is "
                        + AtomizedOperands.describe(type)
                        + ".",
                ErrorCode.UnexpectedTypeErrorCode);
    }

    /** An operand that has no effective boolean value, or only may have one, where a condition is expected. */
    public static Message noEffectiveBooleanValue(String operand, SequenceType type) {
        return new Message(
                operand
                        + " is "
                        + TypeNotation.shortened(type)
                        + (type.mayHaveEffectiveBooleanValue() ? ", which may have" : ", which has")
                        + " no effective boolean value.",
                ErrorCode.UnexpectedTypeErrorCode);
    }

    /** A context item that may be a map, array or function item, where its string value is taken. */
    public static Message noStringValue(Name function, SequenceType contextType) {
        return new Message(
                function
                        + " takes the string value of the context item, but it is "
                        + TypeNotation.shortened(contextType)
                        + ", and a map, array or function item has none.",
                ErrorCode.FunctionItemStringValueErrorCode);
    }

    /** An argument that does not match the parameter type of the function it is passed to. */
    public static Message argumentMismatch(Name function, String argument, SequenceType expected, SequenceType actual) {
        return new Message(
                function
                        + " expects "
                        + TypeNotation.shortened(expected)
                        + " for "
                        + argument
                        + ", but it is "
                        + TypeNotation.shortened(actual)
                        + ".",
                conversionErrorCode(actual, expected));
    }

    /** A function result that does not match the declared return type. */
    public static Message returnMismatch(Name function, SequenceType declared, SequenceType actual) {
        return new Message(
                function
                        + " declares the return type "
                        + TypeNotation.shortened(declared)
                        + ", but returns "
                        + TypeNotation.shortened(actual)
                        + ".",
                conversionErrorCode(actual, declared));
    }

    /** A value that does not match the declared type of the variable it is bound or assigned to. */
    public static Message declaredMismatch(Name variable, SequenceType declared, SequenceType actual) {
        return new Message(
                "$"
                        + variable
                        + " is declared as "
                        + TypeNotation.shortened(declared)
                        + ", but its value is "
                        + TypeNotation.shortened(actual)
                        + ".",
                ErrorCode.UnexpectedTypeErrorCode);
    }

    /** An expression that always returns the empty sequence, for a reason such as "its left operand does". */
    public static Message inferredEmpty(String reason) {
        return new Message(
                "This expression always returns the empty sequence, because " + reason + ".",
                ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression);
    }

    // Function conversion atomizes arguments and results for atomic types, which fails for objects, maps and functions.
    private static ErrorCode conversionErrorCode(SequenceType actual, SequenceType expected) {
        return !actual.isEmptySequence() && expected.getItemType().isSubtypeOf(BuiltinTypesCatalogue.atomicItem)
                ? AtomizedOperands.nonAtomizableErrorCode(actual.getItemType())
                : ErrorCode.UnexpectedTypeErrorCode;
    }
}
