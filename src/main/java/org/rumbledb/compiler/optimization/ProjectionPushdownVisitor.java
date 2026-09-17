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
package org.rumbledb.compiler.optimization;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.compiler.rewriting.CloneVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.FlworExpression;
import org.rumbledb.expressions.flowr.LetClause;
import org.rumbledb.expressions.flowr.ReturnClause;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.primary.ObjectConstructorExpression;
import org.rumbledb.expressions.scripting.Program;

public class ProjectionPushdownVisitor extends CloneVisitor {

    private final ProjectionPushdownDetectionVisitor projectionPushdownDetectionVisitor;

    public ProjectionPushdownVisitor() {
        this.projectionPushdownDetectionVisitor = new ProjectionPushdownDetectionVisitor();
    }

    @Override
    protected Node defaultAction(Node node, Node argument) {
        return node;
    }

    @Override
    public Node visitMainModule(MainModule mainModule, Node argument) {
        this.projectionPushdownDetectionVisitor.visit(mainModule, null);
        MainModule result = new MainModule(
                mainModule.getProlog(),
                (Program) visit(mainModule.getProgram(), mainModule.getProlog()),
                mainModule.getMetadata());
        result.setStaticContext(mainModule.getStaticContext());
        return result;
    }

    // region flwor
    @Override
    public Node visitFlowrExpression(FlworExpression expression, Node argument) {
        Clause clause = expression.getReturnClause().getFirstClause();
        Clause result = null;
        while (clause != null) {
            Clause temp = (Clause) this.visit(clause, argument);
            if (temp != null) {
                if (result != null) {
                    result.chainWith(temp);
                }
                result = temp;
            }
            clause = clause.getNextClause();
        }
        return new FlworExpression((ReturnClause) result, expression.getMetadata());
    }

    @Override
    public Node visitLetClause(LetClause clause, Node argument) {
        // return new LetClause(
        // clause.getVariableName(),
        // clause.getActualSequenceType(),
        // (Expression) visit(clause.getExpression(), argument),
        // clause.getMetadata()
        // );

        // code below from dominik
        // problem is that it removes the last let clause (returns null for that)
        if (clause.getReferenced()) {
            return new LetClause(
                    clause.getVariableName(),
                    clause.getActualSequenceType(),
                    (Expression) visit(clause.getExpression(), argument),
                    clause.getMetadata());
        }
        return null;
    }

    @Override
    public Node visitObjectConstructor(ObjectConstructorExpression expression, Node argument) {
        if (expression.isMergedConstructor()) {
            return new ObjectConstructorExpression(
                    (Expression) visit(expression.getChildren().get(0), argument), expression.getMetadata());
        } else {
            List<Expression> keys = new ArrayList<>();
            List<Expression> values = new ArrayList<>();
            for (int i = 0; i < expression.getKeys().size(); i++) {
                if (expression.getReferenced(i)) {
                    keys.add((Expression) visit(expression.getKeys().get(i), argument));
                    values.add((Expression) visit(expression.getValues().get(i), argument));
                }
            }
            return new ObjectConstructorExpression(keys, values, expression.getMetadata());
        }
    }
}
