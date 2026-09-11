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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.expressions.*;

@Getter
public class TransformExpression extends Expression {

    private List<CopyDeclaration> copyDeclarations;
    private Expression modifyExpression;
    private Expression returnExpression;

    @Setter
    private int mutabilityLevel;

    public TransformExpression(
            List<CopyDeclaration> copyDeclarations,
            Expression modifyExpression,
            Expression returnExpression,
            ExceptionMetadata metadata) {
        super(metadata);
        if (copyDeclarations == null || copyDeclarations.isEmpty()) {
            throw new SemanticException("Transform expression must have at least one variable", metadata);
        }
        this.copyDeclarations = copyDeclarations;
        this.modifyExpression = modifyExpression;
        this.returnExpression = returnExpression;
        this.mutabilityLevel = 0;
    }

    public List<Expression> getCopySourceExpressions() {
        return this.copyDeclarations.stream()
                .filter(Objects::nonNull)
                .map(CopyDeclaration::getSourceExpression)
                .collect(Collectors.toList());
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = this.copyDeclarations.stream()
                .filter(Objects::nonNull)
                .map(CopyDeclaration::getSourceExpression)
                .collect(Collectors.toList());
        result.add(this.modifyExpression);
        result.add(this.returnExpression);
        return result;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitTransformExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        for (CopyDeclaration copyDecl : this.copyDeclarations) {
            sb.append("copy $").append(copyDecl.getVariableName().toString());
            sb.append(" := (");
            copyDecl.getSourceExpression().serializeToJSONiq(sb, 0);
            sb.append(")\n");
        }
        sb.append("\n modify ");
        this.modifyExpression.serializeToJSONiq(sb, 0);
        sb.append("\n return ");
        this.returnExpression.serializeToJSONiq(sb, 0);
        sb.append("\n");
    }
}
