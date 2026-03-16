/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.expressions.primary;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;


public class ArrayConstructorExpression extends Expression {

    private Expression expression;
    private List<Expression> memberExpressions;
    private boolean isSquareConstructor;

    /**
     * Curly array constructor: {@code array { expr }}.
     * Each item produced by the expression becomes a singleton member.
     */
    public ArrayConstructorExpression(Expression expression, ExceptionMetadata metadata) {
        super(metadata);
        this.expression = expression;
        this.memberExpressions = null;
        this.isSquareConstructor = false;
    }

    /**
     * Empty array constructor (curly or square with no members).
     */
    public ArrayConstructorExpression(ExceptionMetadata metadata) {
        super(metadata);
        this.expression = null;
        this.memberExpressions = null;
        this.isSquareConstructor = false;
    }

    /**
     * Square array constructor: {@code [ E1, E2, ... ]}.
     * Each expression becomes a separate member whose result (possibly a sequence) is preserved.
     */
    public ArrayConstructorExpression(
            List<Expression> memberExpressions,
            boolean isSquareConstructor,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.expression = null;
        this.memberExpressions = memberExpressions;
        this.isSquareConstructor = isSquareConstructor;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public List<Expression> getMemberExpressions() {
        return this.memberExpressions;
    }

    public boolean isSquareConstructor() {
        return this.isSquareConstructor;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.isSquareConstructor && this.memberExpressions != null) {
            result.addAll(this.memberExpressions);
        } else if (this.expression != null) {
            result.add(this.expression);
        }
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        if (this.isSquareConstructor) {
            sb.append("[");
            if (this.memberExpressions != null) {
                for (int i = 0; i < this.memberExpressions.size(); i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    this.memberExpressions.get(i).serializeToJSONiq(sb, 0);
                }
            }
            sb.append("]\n");
        } else {
            sb.append("array {");
            if (this.expression != null) {
                this.expression.serializeToJSONiq(sb, 0);
            }
            sb.append("}\n");
        }
    }


    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitArrayConstructor(this, argument);
    }

}
