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
package org.rumbledb.expressions.scripting.statement;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

@Getter
public class StatementsAndExpr extends Expression {
    private final List<Statement> statements;
    private final Expression expression;

    public StatementsAndExpr(List<Statement> statements, Expression expression, ExceptionMetadata metadata) {
        super(metadata);
        // An empty statements list should initialize an empty list for safety.
        if (statements == null) {
            this.statements = new ArrayList<>();
        } else {
            this.statements = statements;
        }
        if (expression == null) {
            throw new OurBadException("Expression cannot be null when statements and expression are needed.");
        }
        this.expression = expression;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitStatementsAndExpr(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (!this.statements.isEmpty()) {
            this.statements.forEach(statement -> {
                if (statement != null) {
                    result.add(statement);
                }
            });
        }
        result.add(this.expression);
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        this.statements.forEach(statement -> {
            statement.serializeToJSONiq(sb, 0);
            sb.append(" ");
        });
        this.expression.serializeToJSONiq(sb, 0);
        sb.append("\n");
    }
}
