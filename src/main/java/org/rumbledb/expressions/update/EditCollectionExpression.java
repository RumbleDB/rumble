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
public class EditCollectionExpression extends Expression {
    private Expression targetExpression;
    private Expression contentExpression;

    public EditCollectionExpression(
            Expression targetExpression, Expression contentExpression, ExceptionMetadata metadata) {
        super(metadata);
        if (targetExpression == null) {
            throw new OurBadException("Tarrget must be identified for edit.");
        }
        if (contentExpression == null) {
            throw new OurBadException("Content must be specified for edit.");
        }
        this.targetExpression = targetExpression;
        this.contentExpression = contentExpression;
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.contentExpression, this.targetExpression);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitEditCollectionExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        sb.append("edit ");
        this.targetExpression.serializeToJSONiq(sb, 0);
        sb.append(" by ");
        this.contentExpression.serializeToJSONiq(sb, 1);
        sb.append(" from collection\n");
    }
}
