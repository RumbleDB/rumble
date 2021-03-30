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

    private final Expression tryExpression;
    private final Map<String, Expression> catchExpressions;
    private final Expression catchAllExpression;

    public TryCatchExpression(
            Expression tryExpression,
            Map<String, Expression> catchExpressions,
            Expression catchAllExpression,
            ExceptionMetadata metadataFromContext
    ) {
        super(metadataFromContext);
        this.tryExpression = tryExpression;
        this.catchExpressions = catchExpressions;
        this.catchAllExpression = catchAllExpression;
    }

    public Expression getTryExpression() {
        return this.tryExpression;
    }

    public List<String> getErrorsCaught() {
        return new ArrayList<>(this.catchExpressions.keySet());
    }

    public boolean catches(String error) {
        return this.catchExpressions.containsKey(error);
    }

    public boolean catchesAll() {
        return this.catchAllExpression != null;
    }

    public Expression getExpressionCatching(String error) {
        return this.catchExpressions.get(error);
    }

    public Expression getExpressionCatchingAll() {
        return this.catchAllExpression;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(this.tryExpression);
        result.addAll(this.catchExpressions.values());
        if (this.catchAllExpression != null) {
            result.add(this.catchAllExpression);
        }
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("try {\n");
        this.tryExpression.serializeToJSONiq(sb, indent + 1);

        indentIt(sb, indent);
        sb.append("}\n");

        if (this.catchExpressions != null) {
            for (Map.Entry<String, Expression> entry : this.catchExpressions.entrySet()) {
                indentIt(sb, indent);
                sb.append("catch " + entry.getKey() + " {\n");
                entry.getValue().serializeToJSONiq(sb, indent + 1);
                indentIt(sb, indent);
                sb.append("}\n");
            }
        }

        if (this.catchAllExpression != null) {
            indentIt(sb, indent);
            sb.append("catch * {\n");
            this.catchAllExpression.serializeToJSONiq(sb, indent + 1);
            indentIt(sb, indent);
            sb.append("}\n");
        }
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitTryCatchExpression(this, argument);
    }
}
