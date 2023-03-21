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

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.primary.VariableReferenceExpression;

import java.util.Collections;
import java.util.List;


public class CountClause extends Clause {
    private VariableReferenceExpression countClauseVar;

    public CountClause(VariableReferenceExpression countClauseVar, ExceptionMetadata metadata) {
        super(FLWOR_CLAUSES.COUNT, metadata);
        this.countClauseVar = countClauseVar;
    }

    @Override
    public List<Node> getChildren() {
        return Collections.singletonList(this.countClauseVar);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitCountClause(this, argument);
    }

    public VariableReferenceExpression getCountVariable() {
        return this.countClauseVar;
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(" (" + (this.countClauseVar) + ") ");
        buffer.append(" | " + this.highestExecutionMode);
        buffer.append(" | " + this.expressionClassification);
        buffer.append("\n");
        for (Node iterator : getChildren()) {
            iterator.print(buffer, indent + 1);
        }
        if (this.previousClause != null) {
            this.previousClause.print(buffer, indent + 1);
        }
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("count ");
        this.countClauseVar.serializeToJSONiq(sb, 0);
    }

}
