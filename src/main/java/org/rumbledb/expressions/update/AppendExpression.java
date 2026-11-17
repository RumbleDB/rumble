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
package org.rumbledb.expressions.update;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

@Getter
public class AppendExpression extends Expression {

    private Expression arrayExpression;
    private Expression toAppendExpression;

    public AppendExpression(Expression arrayExpression, Expression toAppendExpression, ExceptionMetadata metadata) {
        super(metadata);
        if (arrayExpression == null) {
            throw new OurBadException("Array expression cannot be null in a append expression.");
        }
        if (toAppendExpression == null) {
            throw new OurBadException("Expression to append cannot be null in a append expression.");
        }
        this.arrayExpression = arrayExpression;
        this.toAppendExpression = toAppendExpression;
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.arrayExpression, this.toAppendExpression);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitAppendExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        sb.append("append json ");
        this.toAppendExpression.serializeToJSONiq(sb, 0);
        sb.append(" into ");
        this.arrayExpression.serializeToJSONiq(sb, 0);
        sb.append("\n");
    }
}
