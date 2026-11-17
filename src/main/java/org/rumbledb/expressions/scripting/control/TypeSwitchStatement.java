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
package org.rumbledb.expressions.scripting.control;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.statement.Statement;

@Getter
public class TypeSwitchStatement extends Statement {
    private final Expression testCondition;
    private final List<TypeSwitchStatementCase> cases;
    private final TypeSwitchStatementCase defaultCase;

    public TypeSwitchStatement(
            Expression testCondition,
            List<TypeSwitchStatementCase> cases,
            TypeSwitchStatementCase defaultCase,
            ExceptionMetadata metadata) {
        super(metadata);
        this.testCondition = testCondition;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitTypeSwitchStatement(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(this.testCondition);
        for (TypeSwitchStatementCase tswc : this.cases) {
            result.add(tswc.getReturnStatement());
        }
        result.add(this.defaultCase.getReturnStatement());
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        sb.append("typeswitch (");
        this.testCondition.serializeToJSONiq(sb, 0);
        sb.append(")\n");
        for (TypeSwitchStatementCase c : this.cases) {
            indentIt(sb, indent + 1);
            sb.append("case ($");
            sb.append(c.getVariableName().toString() + " as ");
            for (int i = 0; i < c.getUnion().size(); i++) {
                c.getUnion().get(i).toString();
                if (i == c.getUnion().size() - 1) {
                    sb.append(") ");
                } else {
                    sb.append(" | ");
                }
            }
            sb.append("return (");
            c.getReturnStatement().serializeToJSONiq(sb, 0);
            sb.append(")\n");
        }

        if (this.defaultCase != null) {
            indentIt(sb, indent + 1);
            // TODO seems somehow wrong, should've been Statement not the case
            sb.append("default ($");
            sb.append(this.defaultCase.getVariableName().toString() + " as ");
            for (int i = 0; i < this.defaultCase.getUnion().size(); i++) {
                this.defaultCase.getUnion().get(i).toString();
                if (i == this.defaultCase.getUnion().size() - 1) {
                    sb.append(") ");
                } else {
                    sb.append(" | ");
                }
            }
            sb.append(")\n");

            sb.append("return (");
            this.defaultCase.getReturnStatement().serializeToJSONiq(sb, 0);
            sb.append(")\n");
        }
    }
}
