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

import org.rumbledb.types.SequenceCardinality;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.TypeAtomization;

/** Static types of operands after atomization, shared by the operators, casts and FLWOR keys. */
public final class AtomizedOperands {

    private AtomizedOperands() {}

    /**
     * Returns the type of an operand after atomization: one node can yield zero values (a nilled element), one value,
     * or several (a schema list). Returns null when the typed value is only known at runtime.
     */
    public static SequenceType knownAtomizedType(SequenceType operand) {
        if (!TypeAtomization.containsNode(operand.getItemType())) {
            return operand;
        }
        return TypeAtomization.hasUnknownTypedValue(operand.getItemType()) ? null : TypeAtomization.inferType(operand);
    }

    /** Describes an operand together with its typed value, when atomization changes it and it is known. */
    public static String describeAtomizedType(SequenceType operand, SequenceType atomized) {
        return atomized == null || operand.equals(atomized)
                ? operand.toString()
                : operand + " with typed value " + atomized;
    }

    // Singleton operators constrain atomized values. Unknown node typed values are
    // checked at runtime; known multi-valued atomic operands remain static errors.
    public static SequenceType.Arity atomizedSingletonArity(SequenceType source) {
        SequenceType atomized =
                TypeAtomization.isAtomicOrNode(source.getItemType()) ? TypeAtomization.inferType(source) : source;
        if (atomized.getCardinality().allowsMany() && !TypeAtomization.hasUnknownTypedValue(source.getItemType())) {
            return null;
        }
        return atomized.getCardinality().atMostOne().toArity();
    }

    /** Whether some value of the operand atomizes to at most one value, as singleton operators require. */
    public static boolean mayBeSingleton(SequenceType source) {
        SequenceType atomized =
                TypeAtomization.isAtomicOrNode(source.getItemType()) ? TypeAtomization.inferType(source) : source;
        return !atomized.getCardinality().equals(SequenceCardinality.MANY)
                || TypeAtomization.hasUnknownTypedValue(source.getItemType());
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
