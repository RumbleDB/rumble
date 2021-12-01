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

package org.rumbledb.expressions.postfix;


import org.rumbledb.compiler.VisitorConfig;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.primary.IntegerLiteralExpression;
import org.rumbledb.items.ItemFactory;

import java.util.ArrayList;
import java.util.List;


public class FilterExpression extends Expression {

    private Expression mainExpression;
    private Expression predicateExpression;

    public FilterExpression(Expression mainExpression, Expression predicateExpression, ExceptionMetadata metadata) {
        super(metadata);
        if (mainExpression == null) {
            throw new OurBadException("Main expression cannot be null in a postfix expression.");
        }
        this.mainExpression = mainExpression;
        this.predicateExpression = predicateExpression;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(this.mainExpression);
        result.add(this.predicateExpression);
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        this.mainExpression.serializeToJSONiq(sb, 0);
        sb.append("[");
        this.predicateExpression.serializeToJSONiq(sb, 0);
        sb.append("]\n");
    }

    public Expression getPredicateExpression() {
        return this.predicateExpression;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitFilterExpression(this, argument);
    }

    public Expression getMainExpression() {
        return this.mainExpression;
    }

    @Override
    public void initHighestExecutionMode(VisitorConfig visitorConfig) {
        if (this.predicateExpression instanceof IntegerLiteralExpression) {
            String lexicalValue = ((IntegerLiteralExpression) this.predicateExpression).getLexicalValue();
            if (ItemFactory.getInstance().createIntegerItem(lexicalValue).isInt()) {
                if (
                    ItemFactory.getInstance().createIntegerItem(lexicalValue).getIntValue() <= this.staticContext
                        .getRumbleCOnfiguration()
                        .getResultSizeCap()
                ) {
                    this.highestExecutionMode = ExecutionMode.LOCAL;
                    return;
                }
            }
        }
        this.highestExecutionMode = this.mainExpression.getHighestExecutionMode(visitorConfig);
    }
}
