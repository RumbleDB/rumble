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
package org.rumbledb.expressions.scripting.mutation;

import java.util.Collections;
import java.util.List;

import lombok.Getter;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.statement.Statement;

@Getter
public class ApplyStatement extends Statement {
    private final Expression applyExpression;

    public ApplyStatement(Expression applyExpression, ExceptionMetadata metadata) {
        super(metadata);
        this.applyExpression = applyExpression;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitApplyStatement(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return Collections.singletonList(this.applyExpression);
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        this.applyExpression.serializeToJSONiq(sb, 0);
    }
}
