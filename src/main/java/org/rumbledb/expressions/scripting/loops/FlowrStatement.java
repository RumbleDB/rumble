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
package org.rumbledb.expressions.scripting.loops;

import java.util.Collections;
import java.util.List;

import lombok.Getter;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.expressions.scripting.statement.Statement;

@Getter
public class FlowrStatement extends Statement {
    private final ReturnStatementClause returnStatementClause;

    public FlowrStatement(ReturnStatementClause returnStatementClause, ExceptionMetadata metadata) {
        super(metadata);
        Clause startClause = returnStatementClause.getFirstClause();
        if (startClause.getClauseType() != FLWOR_CLAUSES.FOR && startClause.getClauseType() != FLWOR_CLAUSES.LET) {
            throw new SemanticException("FLOWR clause must starts with a FOR or a LET\n", this.getMetadata());
        }
        this.returnStatementClause = returnStatementClause;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitFlowrStatement(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return Collections.singletonList(this.returnStatementClause);
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        this.returnStatementClause.serializeToJSONiq(sb, 0);
        sb.append("\n");
    }
}
