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

package org.rumbledb.expressions.xml;


import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

// clone of ObjectLookupExpression but for xquery lookup
public class UnaryLookupExpression extends Expression {

    private Expression lookupExpression;
    // lookupexpression is null if we have a wildcard!!

    public UnaryLookupExpression(Expression lookupExpression, ExceptionMetadata metadata) {
        super(metadata);
        this.lookupExpression = lookupExpression;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.lookupExpression != null)
            result.add(this.lookupExpression);
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("?");
        if (this.lookupExpression != null)
            this.lookupExpression.serializeToJSONiq(sb, 0);
        else
            sb.append("*");
        sb.append("\n");
    }

    public Expression getLookupExpression() {
        // can be null if wildcard
        return this.lookupExpression;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitUnaryLookupExpression(this, argument);
    }
}
