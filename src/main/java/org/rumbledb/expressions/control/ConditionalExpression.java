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

public class ConditionalExpression extends Expression {

    private final Expression conditionExpression;
    private final Expression thenExpression;
    private final Expression elseExpression;

    public ConditionalExpression(
            Expression condition,
            Expression branch,
            Expression elseBranch,
            ExceptionMetadata metadataFromContext
    ) {
        super(metadataFromContext);
        this.conditionExpression = condition;
        this.thenExpression = branch;
        this.elseExpression = elseBranch;

    }

    public Expression getElseBranch() {
        return this.elseExpression;
    }

    public Expression getCondition() {
        return this.conditionExpression;
    }

    public Expression getBranch() {
        return this.thenExpression;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(this.conditionExpression);
        result.add(this.thenExpression);
        result.add(this.elseExpression);
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("if (");
        this.conditionExpression.serializeToJSONiq(sb, 0);
        sb.append(")\n");

        indentIt(sb, indent + 1);
        sb.append("then (");

        this.thenExpression.serializeToJSONiq(sb, 0);
        sb.append(")\n");

        indentIt(sb, indent + 1);
        sb.append("else (");

        this.elseExpression.serializeToJSONiq(sb, indent + 1);
        sb.append(")\n");
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitConditionalExpression(this, argument);
    }

}
