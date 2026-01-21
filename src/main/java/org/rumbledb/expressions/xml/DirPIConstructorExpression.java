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
 * Authors: Matteo Agnoletto (EPMatt)
 *
 */

package org.rumbledb.expressions.xml;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.primary.StringLiteralExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * Expression representing a direct processing instruction constructor.
 *
 * "A direct processing instruction constructor creates a processing instruction node
 * whose target property is PITarget and whose content property is DirPIContents. The base-uri property of the node is
 * empty. The parent property of the node is empty."
 * "The PITarget of a processing instruction must not consist of the characters "XML" in any combination
 * of upper and lower case. The DirPIContents of a processing instruction must not contain the string "?>"."
 *
 * @see <a href="https://www.w3.org/TR/xquery-31/#doc-xquery31-DirPIConstructor">XQuery 3.1, 3.9.1.2: Direct
 *      Processing Instruction Constructors</a>
 */
public class DirPIConstructorExpression extends Expression {
    private final String target;
    private final Expression contentExpression;

    public DirPIConstructorExpression(
            String target,
            Expression contentExpression,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.target = target;
        this.contentExpression = contentExpression;
    }

    public String getTarget() {
        return this.target;
    }

    public Expression getContentExpression() {
        return this.contentExpression;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitDirPIConstructor(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.contentExpression != null) {
            result.add(this.contentExpression);
        }
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("<?");
        sb.append(this.target);
        if (this.contentExpression != null) {
            sb.append(" ");
            if (this.contentExpression instanceof StringLiteralExpression) {
                sb.append(((StringLiteralExpression) this.contentExpression).getValue());
            } else {
                this.contentExpression.serializeToJSONiq(sb, 0);
            }
        }
        sb.append("?>\n");
    }
}

