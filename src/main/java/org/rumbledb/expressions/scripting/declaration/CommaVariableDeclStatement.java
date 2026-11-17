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
package org.rumbledb.expressions.scripting.declaration;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.statement.Statement;

@Getter
public class CommaVariableDeclStatement extends Statement {
    private final List<VariableDeclStatement> variables;

    public CommaVariableDeclStatement(List<VariableDeclStatement> variables, ExceptionMetadata metadata) {
        super(metadata);
        this.variables = variables;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitCommaVariableDeclStatement(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>(this.variables);
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        for (VariableDeclStatement variableDeclStatement : this.variables) {
            variableDeclStatement.serializeToJSONiq(sb, 0);
        }
    }
}
