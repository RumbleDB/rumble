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
package org.rumbledb.expressions.primary;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.Getter;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

@Getter
public class StringConstructorExpression extends Expression {

    private final List<Expression> parts;
    private final List<Boolean> isInterpolated;

    public StringConstructorExpression(
            List<Expression> parts, List<Boolean> isInterpolated, ExceptionMetadata metadata) {
        super(metadata);
        this.parts = parts == null ? Collections.emptyList() : parts;
        this.isInterpolated = isInterpolated == null ? Collections.emptyList() : isInterpolated;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitStringConstructor(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return this.parts.stream().filter(Objects::nonNull).map(e -> (Node) e).collect(Collectors.toList());
    }

    @Override
    public void print(StringBuilder buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(" | " + this.highestExecutionMode);
        buffer.append(" | " + this.expressionClassification);
        buffer.append(" | " + (this.staticSequenceType == null ? "not set" : this.staticSequenceType));
        buffer.append("\n");
        for (Node iterator : getChildren()) {
            iterator.print(buffer, indent + 1);
        }
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        sb.append("``[");
        for (int i = 0; i < this.parts.size(); i++) {
            Expression part = this.parts.get(i);
            if (part == null) {
                continue;
            }
            if (this.isInterpolated.get(i)) {
                sb.append("`{");
                part.serializeToJSONiq(sb, 0);
                sb.append("}`");
            } else if (part instanceof StringLiteralExpression) {
                sb.append(((StringLiteralExpression) part).getValue());
            } else {
                part.serializeToJSONiq(sb, 0);
            }
        }
        sb.append("]``\n");
    }
}
