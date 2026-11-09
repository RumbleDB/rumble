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
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IsStaticallyUnexpectedTypeException;
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
 * keeps it. Each node, with its copies, has at most one error.
 *
 * <p>
 * An error is certain when every value of the checked expression fails, and possible when only some do: an item*
 * argument for an xs:integer parameter fails unless it is one integer. Static typing rejects both. A possible error
 * does not make its expression unknown, since the types inferred after it are those of the values that pass.
 */
public final class StaticTypeErrors {

    /** Behaves as item, of which it is a copy, but is told apart by identity. */
    private static final ItemType UNKNOWN_ITEM = new ItemItemType(Name.createVariableInDefaultTypeNamespace("item"));

    /** The type of an expression that could not be typed because of an error. */
    public static final SequenceType UNKNOWN = new SequenceType(UNKNOWN_ITEM, SequenceType.Arity.ZeroOrMore);

    private final RumbleConfiguration configuration;

    private final List<RumbleException> errors = new ArrayList<>();

    /**
     * The index in {@link #errors} of the error at each {@link #site}. A later certain error replaces a possible one in
     * place, so that errors stay in the order they were found.
     */
    private final Map<Object, Integer> errorIndices = new HashMap<>();

    /** The nodes with a certain error, or with an error that is not reported, which are typed as unknown. */
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

    /** Reports a certain error. */
    public void report(String message, Node node) {
        report(message, ErrorCode.UnexpectedTypeErrorCode, node);
    }

    /** Reports a certain error. */
    public void report(String message, ErrorCode code, Node node) {
        record(message, code, node, true);
    }

    /** Reports an error, which is possible when some values may still pass the check, and otherwise certain. */
    public void report(String message, Node node, boolean mayPass) {
        report(message, ErrorCode.UnexpectedTypeErrorCode, node, mayPass);
    }

    /** Reports an error, which is possible when some values may still pass the check, and otherwise certain. */
    public void report(String message, ErrorCode code, Node node, boolean mayPass) {
        record(message, code, node, !mayPass);
    }

    /** Reports a shared error, which is possible when some values may still pass the check, and otherwise certain. */
    public void report(TypeErrorMessages.Message message, Node node, boolean mayPass) {
        report(message.text(), message.code(), node, mayPass);
    }

    /** Reports that the actual type is not a subtype of the expected one, which is possible if they may overlap. */
    public void reportMismatch(String message, SequenceType actual, SequenceType expected, Node node) {
        report(message, node, actual.mayMatch(expected));
    }

    /** Reports that the actual type is not a subtype of the expected one, which is possible if they may overlap. */
    public void reportMismatch(
            TypeErrorMessages.Message message, SequenceType actual, SequenceType expected, Node node) {
        report(message, node, actual.mayMatch(expected));
    }

    private void record(String message, ErrorCode code, Node node, boolean certain) {
        if (!this.configuration.analysis().enableStaticTyping()) {
            return;
        }
        // An error of a node with an unknown operand only repeats the operand's, so the node is unknown too.
        if (hasUnknownOperand(node)) {
            this.failedNodes.add(node);
            return;
        }
        if (certain) {
            this.failedNodes.add(node);
        }
        Integer previousIndex = this.errorIndices.get(site(node));
        UnexpectedStaticTypeException error =
                new UnexpectedStaticTypeException(message, code, node.getMetadata(), certain);
        if (previousIndex == null) {
            this.errorIndices.put(site(node), this.errors.size());
            this.errors.add(error);
        } else if (certain
                && this.errors.get(previousIndex) instanceof UnexpectedStaticTypeException previous
                && !previous.isCertain()) {
            this.errors.set(previousIndex, error);
        }
    }

    /**
     * Explicit static assertions apply even when optional static typing is disabled. One whose operand is unknown is
     * not reported, since its type is not known either.
     */
    public void reportAssertion(IsStaticallyUnexpectedTypeException error, Node node) {
        recordStaticError(error, node, hasUnknownOperand(node));
    }

    /**
     * Reports an error that the query has whatever its operand types, and even when optional static typing is
     * disabled, such as a call of an undeclared function. The node is typed as unknown, and typing goes on.
     */
    public void reportStaticError(RumbleException error, Node node) {
        recordStaticError(error, node, false);
    }

    private void recordStaticError(RumbleException error, Node node, boolean suppressed) {
        this.failedNodes.add(node);
        if (!suppressed && !this.errorIndices.containsKey(site(node))) {
            this.errorIndices.put(site(node), this.errors.size());
            this.errors.add(error);
        }
    }

    /**
     * Where an error of the node is shown: its source range, so that the copies of an inlined function body, which
     * are typed at each call, report an error of the body once. A node without a source range is its own site.
     */
    private static Object site(Node node) {
        ExceptionMetadata metadata = node.getMetadata();
        return metadata == null || metadata == ExceptionMetadata.EMPTY_METADATA ? node : metadata.toString();
    }

    /** The errors reported so far, in the order they were found. */
    public List<RumbleException> getErrors() {
        return Collections.unmodifiableList(this.errors);
    }

    /** Whether an error was reported. */
    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    /** Whether the node had a certain error, or an error that is not reported, so that its type is unknown. */
    public boolean hasFailed(Node node) {
        return this.failedNodes.contains(node);
    }

    /** Whether an operand of the node is unknown, so that an error of the node would only repeat the operand's. */
    public static boolean hasUnknownOperand(Node node) {
        return node.getChildren().stream()
                .anyMatch(child -> child instanceof Expression operand && isUnknown(operand.getStaticSequenceType()));
    }

    /**
     * Reports the node if the type, of its result or of an operand, is the empty sequence. The reason says why the node
     * always returns the empty sequence, as in "its left operand does".
     */
    public void rejectInferredEmpty(SequenceType type, Node node, String reason) {
        if (type.isEmptySequence()) {
            reportInferredEmpty(node, reason);
        }
    }

    /** Reports the node if one of its two operands is the empty sequence, which makes its result empty too. */
    public void rejectEmptyOperands(SequenceType left, SequenceType right, Node node) {
        if (left.isEmptySequence()) {
            reportInferredEmpty(node, right.isEmptySequence() ? "both its operands do" : "its left operand does");
        } else if (right.isEmptySequence()) {
            reportInferredEmpty(node, "its right operand does");
        }
    }

    /**
     * Reports an expression that always returns the empty sequence (XPST0005), which static typing only allows for (),
     * for a reason such as "its left operand does".
     */
    public void reportInferredEmpty(Node node, String reason) {
        report(TypeErrorMessages.inferredEmpty(reason), node, false);
    }

    /** Reports a node that makes its expression always return the empty sequence, which the message explains. */
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
