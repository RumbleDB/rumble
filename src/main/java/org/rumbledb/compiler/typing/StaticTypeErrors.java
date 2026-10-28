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
import java.util.HashSet;
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
import org.rumbledb.expressions.Node;
import org.rumbledb.types.SequenceType;

/**
 * Collects static type errors, which are only raised when static typing is enabled. Types are still inferred
 * otherwise, since the runtime uses them.
 *
 * <p>
 * Only the first error of each top-level unit is kept: a function declaration, a global variable declaration, a
 * statement of the main program or its final expression. After an error, the types inferred are fallbacks, so later
 * errors in the same unit, or in a unit that uses a variable whose declaration had an error, are usually consequences
 * of the first one.
 *
 * <p>
 * An error is certain when every value of the checked expression fails, and possible when only some do: an item*
 * argument for an xs:integer parameter fails unless it is one integer. Static typing rejects both. A possible error
 * does not make the rest of its unit unreliable, since the types inferred after it are those of the values that
 * pass. Each node has at most one error.
 */
public final class StaticTypeErrors {

    private final RumbleConfiguration configuration;

    private final List<RumbleException> errors = new ArrayList<>();

    /** The variables whose declaration had an error, so that their type is not reliable. */
    private final Set<Name> unreliableVariables = new HashSet<>();

    private final Set<Node> nodesWithErrors = Collections.newSetFromMap(new IdentityHashMap<>());

    /** Whether the unit being typed had a certain error, or uses an unreliable variable. */
    private boolean unitUnreliable;

    public StaticTypeErrors(RumbleConfiguration configuration) {
        this.configuration = configuration;
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

    /** Reports that the actual type is not a subtype of the expected one, which is possible if they may overlap. */
    public void reportMismatch(String message, SequenceType actual, SequenceType expected, Node node) {
        report(message, node, actual.mayMatch(expected));
    }

    private void record(String message, ErrorCode code, Node node, boolean certain) {
        if (!this.configuration.analysis().enableStaticTyping()
                || this.unitUnreliable
                || !this.nodesWithErrors.add(node)) {
            return;
        }
        if (certain) {
            this.unitUnreliable = true;
        }
        this.errors.add(new UnexpectedStaticTypeException(message, code, node.getMetadata(), certain));
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

    /** Types a top-level unit, and returns whether it had a certain error. */
    public boolean typeUnit(Runnable typing) {
        boolean outerUnreliable = this.unitUnreliable;
        this.unitUnreliable = false;
        try {
            typing.run();
            return this.unitUnreliable;
        } finally {
            this.unitUnreliable = outerUnreliable;
        }
    }

    /** Records that the declaration of a variable had an error. */
    public void markUnreliable(Name variable) {
        this.unreliableVariables.add(variable);
    }

    /** Stops reporting errors in the current unit if it uses a variable whose declaration had an error. */
    public void useVariable(Name variable) {
        if (this.unreliableVariables.contains(variable)) {
            this.unitUnreliable = true;
        }
    }

    /**
     * Throws the errors collected while typing a module, if any. An exception that stopped the typing is thrown
     * after them, as it may follow from them.
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
