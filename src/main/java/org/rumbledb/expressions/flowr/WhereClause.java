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

package org.rumbledb.expressions.flowr;


<<<<<<< HEAD
=======

import sparksoniq.semantics.visitor.AbstractNodeVisitor;

import java.util.ArrayList;
import java.util.List;

>>>>>>> c94fc8ddae10d0d8652a240536e13bdcdb7fce0d
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import sparksoniq.semantics.visitor.AbstractNodeVisitor;

import java.util.List;

public class WhereClause extends FlworClause {

    private final Expression whereExpression;

    public WhereClause(Expression expr, ExceptionMetadata metadata) {
        super(FLWOR_CLAUSES.WHERE, metadata);
        this.whereExpression = expr;
    }

    public Expression getWhereExpression() {
        return this.whereExpression;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitWhereClause(this, argument);
    }

    @Override
<<<<<<< HEAD
    public List<Node> getDescendants(boolean depthSearch) {
        List<Node> result = super.getDescendants(depthSearch);
        if (this.whereExpression != null)
            result.add(this.whereExpression);
        return getDescendantsFromChildren(result, depthSearch);
=======
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (whereExpression != null)
            result.add(whereExpression);
        return result;
>>>>>>> c94fc8ddae10d0d8652a240536e13bdcdb7fce0d
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(whereClause where ";
        result += this.whereExpression.serializationString(true);
        result += "))";
        return result;
    }
}
