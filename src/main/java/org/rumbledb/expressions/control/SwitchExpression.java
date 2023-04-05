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

package org.rumbledb.expressions.control;


import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

public class SwitchExpression extends Expression {

    private final Expression testCondition;
    private final List<SwitchCase> cases;
    private final Expression defaultExpression;

    public SwitchExpression(
            Expression testCondition,
            List<SwitchCase> cases,
            Expression defaultExpression,
            ExceptionMetadata metadataFromContext
    ) {
        super(metadataFromContext);
        this.testCondition = testCondition;
        this.cases = cases;
        this.defaultExpression = defaultExpression;
    }

    public Expression getTestCondition() {
        return this.testCondition;
    }

    public List<SwitchCase> getCases() {
        return this.cases;
    }

    public Expression getDefaultExpression() {
        return this.defaultExpression;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(this.testCondition);
        for (SwitchCase c : this.cases) {
            result.addAll(c.getAllExpressions());
        }
        result.add(this.defaultExpression);
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("switch (");
        this.testCondition.serializeToJSONiq(sb, 0);
        sb.append(")\n");
        for (SwitchCase c : this.cases) {
            indentIt(sb, indent + 1);
            sb.append("case (");
            for (int i = 0; i < c.getAllExpressions().size(); i++) {
                c.getAllExpressions().get(i).serializeToJSONiq(sb, 0);
                if (i == c.getAllExpressions().size() - 1) {
                    sb.append(") ");
                } else {
                    sb.append(", ");
                }
            }
            sb.append("return (");
            c.getReturnExpression().serializeToJSONiq(sb, 0);
            sb.append(")\n");
        }

        if (this.defaultExpression != null) {
            indentIt(sb, indent + 1);
            sb.append("default return (");
            this.defaultExpression.serializeToJSONiq(sb, 0);
            sb.append(")\n");
        }
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitSwitchExpression(this, argument);
    }
}
