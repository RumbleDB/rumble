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

import org.rumbledb.compiler.VisitorConfig;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

public class ReturnClause extends Clause {

    private static final long serialVersionUID = 1L;

    private final Expression returnExpr;

    public ReturnClause(Expression expr, ExceptionMetadata metadata) {
        super(FLWOR_CLAUSES.RETURN, metadata);
        this.returnExpr = expr;
    }

    public Expression getReturnExpr() {
        return this.returnExpr;
    }

    @Override
    public void initHighestExecutionMode(VisitorConfig visitorConfig) {
        if (this.previousClause.getHighestExecutionMode(visitorConfig).isDataFrame()) {
            this.highestExecutionMode = ExecutionMode.RDD;
            return;
        }
        if (this.returnExpr.getHighestExecutionMode(visitorConfig).isRDD()) {
            this.highestExecutionMode = ExecutionMode.RDD;
            return;
        }
        if (this.returnExpr.getHighestExecutionMode(visitorConfig).isDataFrame()) {
            this.highestExecutionMode = ExecutionMode.DATAFRAME;
            return;
        }
        this.highestExecutionMode = ExecutionMode.LOCAL;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.returnExpr != null) {
            result.add(this.returnExpr);
        }
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("return ");
        this.returnExpr.serializeToJSONiq(sb, 0);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitReturnClause(this, argument);
    }
}
