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

public class InsertExpression extends Expression {

    @Getter
    private final Expression mainExpression;

    @Getter
    private final Expression toInsertExpression;

    private final Expression positionExpression;

    public InsertExpression(
            Expression mainExpression,
            Expression toInsertExpression,
            Expression positionExpression,
            ExceptionMetadata metadata) {
        super(metadata);
        this.mainExpression = mainExpression;
        this.toInsertExpression = toInsertExpression;
        this.positionExpression = positionExpression;
    }

    public boolean hasPositionExpression() {
        return this.positionExpression != null;
    }

    public Expression getPositionExpression() {
        if (this.positionExpression == null) {
            throw new OurBadException("No position expression present in Insert Expression");
        }
        return this.positionExpression;
    }

    @Override
    public List<Node> getChildren() {
        return this.positionExpression == null
                ? Arrays.asList(this.mainExpression, this.toInsertExpression)
                : Arrays.asList(this.mainExpression, this.toInsertExpression, this.positionExpression);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitInsertExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        sb.append("insert json ");
        this.toInsertExpression.serializeToJSONiq(sb, 0);
        sb.append(" into ");
        this.mainExpression.serializeToJSONiq(sb, 0);
        if (this.hasPositionExpression()) {
            sb.append(" at position ");
            this.positionExpression.serializeToJSONiq(sb, 0);
        }
        sb.append("\n");
    }
}
