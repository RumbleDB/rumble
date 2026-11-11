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
package org.rumbledb.expressions;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.rumbledb.exceptions.ExceptionMetadata;

@Getter
public class CommaExpression extends Expression {

    private final List<Expression> expressions;

    public CommaExpression(List<Expression> expressions, ExceptionMetadata metadata) {
        super(metadata);
        this.expressions = expressions;
    }

    // Empty sequence
    public CommaExpression(ExceptionMetadata metadata) {
        super(metadata);
        this.expressions = new ArrayList<>();
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.expressions != null) {
            this.expressions.forEach(e -> {
                if (e != null) {
                    result.add(e);
                }
            });
        }
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        for (int i = 0; i < this.expressions.size(); i++) {
            this.expressions.get(i).serializeToJSONiq(sb, 0);
            if (i == this.expressions.size() - 1) {
                sb.append("\n");
            } else {
                sb.append(", ");
            }
        }
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitCommaExpression(this, argument);
    }
}
