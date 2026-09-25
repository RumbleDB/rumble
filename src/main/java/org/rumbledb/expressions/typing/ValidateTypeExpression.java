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
package org.rumbledb.expressions.typing;

import java.util.Collections;
import java.util.List;

import lombok.Getter;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.SequenceType;

@Getter
public class ValidateTypeExpression extends Expression {

    private Expression mainExpression;
    private SequenceType sequenceType;
    private boolean isValidate;

    public ValidateTypeExpression(
            Expression mainExpression, boolean isValidate, SequenceType sequenceType, ExceptionMetadata metadata) {
        super(metadata);
        if (mainExpression == null) {
            throw new OurBadException("Expression cannot be null.");
        }
        this.mainExpression = mainExpression;
        this.isValidate = isValidate;
        this.sequenceType = sequenceType;
        if (sequenceType.isEmptySequence()) {
            throw new OurBadException(
                    "It is not possible to validate against the empty sequence type. Please use empty() instead to check for emptiness.");
        }
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitValidateTypeExpression(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return Collections.singletonList(this.mainExpression);
    }

    @Override
    public void print(StringBuilder buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(" ("
                + (this.sequenceType.toString())
                + (this.getSequenceType().isResolved() ? " (resolved)" : " (unresolved)")
                + ") ");
        buffer.append(" | " + this.highestExecutionMode);
        buffer.append(" | " + this.expressionClassification);
        buffer.append(" | "
                + (this.staticSequenceType == null
                        ? "not set"
                        : this.staticSequenceType
                                + (this.staticSequenceType.isResolved() ? " (resolved)" : " (unresolved)")));
        buffer.append("\n");
        for (Node iterator : getChildren()) {
            iterator.print(buffer, indent + 1);
        }
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        sb.append(" validate type " + this.sequenceType.toString() + "\n");
        this.mainExpression.serializeToJSONiq(sb, 0);
        sb.append(" as {" + this.sequenceType.toString() + "\n}\n");
    }
}
