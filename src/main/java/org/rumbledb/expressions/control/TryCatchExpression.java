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


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

public class TryCatchExpression extends Expression {

    private final Expression _tryExpression;
    private final Map<String, Expression> _catchExpressions;
    private final Expression _catchAllExpression;

    public TryCatchExpression(
            Expression tryExpression,
            Map<String, Expression> catchExpressions,
            Expression catchAllExpression,
            ExceptionMetadata metadataFromContext
    ) {
        super(metadataFromContext);
        this._tryExpression = tryExpression;
        this._catchExpressions = catchExpressions;
        this._catchAllExpression = catchAllExpression;
    }

    public Expression getTryExpression() {
        return _tryExpression;
    }

    public List<String> getErrorsCaught() {
        return new ArrayList<>(_catchExpressions.keySet());
    }

    public boolean catches(String error) {
        return _catchExpressions.containsKey(error);
    }

    public boolean catchesAll() {
        return _catchAllExpression != null;
    }

    public Expression getExpressionCatching(String error) {
        return _catchExpressions.get(error);
    }

    public Expression getExpressionCatchingAll() {
        return _catchAllExpression;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(_tryExpression);
        result.addAll(_catchExpressions.values());
        return result;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        System.out.println("Accept!");
        return visitor.visitTryCatchExpression(this, argument);
    }
}
