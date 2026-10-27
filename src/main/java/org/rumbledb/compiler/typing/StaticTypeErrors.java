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

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedStaticTypeException;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.SequenceType;

/**
 * Reports static type errors, which are only raised when static typing is enabled. Types are still inferred
 * otherwise, since the runtime uses them.
 */
public final class StaticTypeErrors {

    private final RumbleConfiguration configuration;

    public StaticTypeErrors(RumbleConfiguration configuration) {
        this.configuration = configuration;
    }

    public void report(String message, ExceptionMetadata metadata) {
        if (this.configuration.analysis().enableStaticTyping()) {
            throw new UnexpectedStaticTypeException(message, metadata);
        }
    }

    public void report(String message, ErrorCode code, ExceptionMetadata metadata) {
        if (this.configuration.analysis().enableStaticTyping()) {
            throw new UnexpectedStaticTypeException(message, code, metadata);
        }
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
        report(message, ErrorCode.StaticallyInferredEmptySequenceNotFromCommaExpression, node.getMetadata());
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
