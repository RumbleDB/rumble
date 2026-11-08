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

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.StaticTypeErrorsException;
import org.rumbledb.exceptions.UnexpectedStaticTypeException;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.ItemItemType;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

/**
 * Collects static type errors, which are only raised when static typing is enabled. Types are still inferred
 * otherwise, since the runtime uses them.
 *
 * <p>
 * Typing goes on after an error, so that every error of a module is found. An expression with an error, or whose
 * error is not reported because an operand failed, gets the {@link #isUnknown unknown} type, and checks of an operand
 * with that type report nothing: they would only repeat the error that the operand already had. A variable bound to
 * such an expression has the unknown type too, so its uses report nothing either, but a variable with a declared type
 * keeps it. Each node has at most one error.
 */
public final class StaticTypeErrors {

    /** Behaves as item, of which it is a copy, but is told apart by identity. */
    private static final ItemType UNKNOWN_ITEM = new ItemItemType(Name.createVariableInDefaultTypeNamespace("item"));

    /** The type of an expression that could not be typed because of an error. */
    public static final SequenceType UNKNOWN = new SequenceType(UNKNOWN_ITEM, SequenceType.Arity.ZeroOrMore);

    private final RumbleConfiguration configuration;

    private final List<RumbleException> errors = new ArrayList<>();

    /** The nodes with an error, reported or not, which are typed as unknown. */
    private final Set<Node> failedNodes = Collections.newSetFromMap(new IdentityHashMap<>());

    public StaticTypeErrors(RumbleConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Whether the type only stands for an expression that could not be typed because of an error. Cardinality changes
     * keep it, as for the variable of a for clause over such an expression.
     */
    public static boolean isUnknown(SequenceType type) {
        return type != null && !type.isEmptySequence() && type.getItemType() == UNKNOWN_ITEM;
    }

    public void report(String message, Node node) {
        report(message, ErrorCode.UnexpectedTypeErrorCode, node);
    }

    public void report(String message, ErrorCode code, Node node) {
        if (!this.configuration.analysis().enableStaticTyping() || !this.failedNodes.add(node)) {
            return;
        }
        if (!hasUnknownOperand(node)) {
            this.errors.add(new UnexpectedStaticTypeException(message, code, node.getMetadata()));
        }
    }

    /** Whether an error was reported. */
    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    /** Whether the node had an error, reported or not, so that its type is unknown. */
    public boolean hasFailed(Node node) {
        return this.failedNodes.contains(node);
    }

    /** Whether an operand of the node is unknown, so that an error of the node would only repeat the operand's. */
    public static boolean hasUnknownOperand(Node node) {
        return node.getChildren().stream()
                .anyMatch(child -> child instanceof Expression operand && isUnknown(operand.getStaticSequenceType()));
    }

    /** Static typing only allows a comma expression to be inferred as the empty sequence. */
    public void rejectInferredEmpty(SequenceType type, Node node) {
        if (type.isEmptySequence()) {
            reportInferredEmpty(node);
        }
    }

    public void reportInferredEmpty(Node node) {
        reportInferredEmpty(
                "The inferred type of a "
                        + node.getClass().getSimpleName()
                        + " is the empty sequence, which static typing only allows for a comma expression",
                node);
    }

    public void reportInferredEmpty(String message, Node node) {
        report(message, ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression, node);
    }

    /**
     * Throws the errors collected while typing a module, if any: a single error as it is, and several as a
     * {@link StaticTypeErrorsException} whose code, message and location are those of the first one. An exception that
     * stopped the typing is thrown after them, as it may follow from them.
     */
    public void throwCollected(RumbleException stoppedBy) {
        List<RumbleException> all = new ArrayList<>(this.errors);
        if (stoppedBy != null) {
            all.add(stoppedBy);
        }
        if (all.size() == 1) {
            throw all.get(0);
        }
        if (all.size() > 1) {
            throw new StaticTypeErrorsException(all);
        }
    }

    /** Returns the type inferred for a child of the node, which every visited child has. */
    public static SequenceType requireInferredType(SequenceType type, Node node) {
        if (type == null) {
            throw new OurBadException(
                    "A child of a " + node.getClass().getSimpleName() + " has no inferred type", node.getMetadata());
        }
        return type;
    }
}
