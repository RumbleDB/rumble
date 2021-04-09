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
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.Collections;
import java.util.List;

public class FlworExpression extends Expression {

    private ReturnClause returnClause;

    public FlworExpression(
            ReturnClause returnClause,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        Clause startClause = returnClause.getFirstClause();
        if (
            startClause.getClauseType() != FLWOR_CLAUSES.FOR
                &&
                startClause.getClauseType() != FLWOR_CLAUSES.LET
        ) {
            throw new SemanticException("FLOWR clause must starts with a FOR or a LET\n", this.getMetadata());
        }

        this.returnClause = returnClause;
    }

    public ReturnClause getReturnClause() {
        return this.returnClause;
    }

    @Override
    public void initHighestExecutionMode(VisitorConfig visitorConfig) {
        this.highestExecutionMode = this.returnClause.getHighestExecutionMode(visitorConfig);
    }

    public List<Node> getChildren() {
        return Collections.singletonList(this.returnClause);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        this.returnClause.serializeToJSONiq(sb, 0);
        sb.append("\n");
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitFlowrExpression(this, argument);
    }
}


