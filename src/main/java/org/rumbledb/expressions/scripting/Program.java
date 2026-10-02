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
package org.rumbledb.expressions.scripting;

import java.util.Collections;
import java.util.List;

import lombok.Getter;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;

/**
 * A program is the first-class citizen in JSONiq scripting syntax. A program
 * corresponds to the body of a block expression.
 *
 * The final expression may be omitted. In this case, the final expression is
 * considered the empty expression.
 *
 * The result of a program is the result of executing, if present, an
 * expression.
 *
 * A program forms a tree of statements and possibly an expression.
 */
@Getter
public class Program extends Node {
    private final StatementsAndOptionalExpr statementsAndOptionalExpr;

    public Program(StatementsAndOptionalExpr statementsAndOptionalExpr, ExceptionMetadata metadata) {
        super(metadata);
        this.statementsAndOptionalExpr = statementsAndOptionalExpr;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitProgram(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return Collections.singletonList(this.statementsAndOptionalExpr);
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        this.statementsAndOptionalExpr.serializeToJSONiq(sb, indent);
    }

    public boolean isSequential() {
        return this.statementsAndOptionalExpr.isSequential();
    }

    public boolean isUpdating() {
        return this.statementsAndOptionalExpr.isUpdating();
    }
}
