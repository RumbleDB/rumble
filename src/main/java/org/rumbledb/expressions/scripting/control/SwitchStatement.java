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
public class SwitchStatement extends Statement {
    private final Expression testCondition;
    private final List<SwitchCaseStatement> cases;
    private final Statement defaultStatement;

    public SwitchStatement(
            Expression testCondition,
            List<SwitchCaseStatement> cases,
            Statement defaultStatement,
            ExceptionMetadata metadata) {
        super(metadata);
        this.testCondition = testCondition;
        this.cases = cases;
        this.defaultStatement = defaultStatement;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitSwitchStatement(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(this.testCondition);
        for (SwitchCaseStatement swc : this.cases) {
            result.addAll(swc.getConditionExpressions());
            result.add(swc.getReturnStatement());
        }
        result.add(this.defaultStatement);
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        sb.append("switch (");
        this.testCondition.serializeToJSONiq(sb, 0);
        sb.append(")\n");
        for (SwitchCaseStatement swc : this.cases) {
            indentIt(sb, indent + 1);
            sb.append("case (");
            for (int i = 0; i < swc.getConditionExpressions().size(); i++) {
                swc.getConditionExpressions().get(i).serializeToJSONiq(sb, 0);
                if (i == swc.getConditionExpressions().size() - 1) {
                    sb.append(") ");
                } else {
                    sb.append(", ");
                }
            }
            sb.append("return (");
            swc.getReturnStatement().serializeToJSONiq(sb, 0);
            sb.append(")\n");
        }

        if (this.defaultStatement != null) {
            indentIt(sb, indent + 1);
            sb.append("default return (");
            this.defaultStatement.serializeToJSONiq(sb, 0);
            sb.append(")\n");
        }
    }
}
