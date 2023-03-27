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
import org.rumbledb.expressions.Node;


import java.util.ArrayList;
import java.util.List;

public class GroupByClause extends Clause {

    private final List<GroupByVariableDeclaration> variables;

    public GroupByClause(List<GroupByVariableDeclaration> variables, ExceptionMetadata metadata) {
        super(FLWOR_CLAUSES.GROUP_BY, metadata);
        if (variables == null || variables.isEmpty()) {
            throw new SemanticException("Group clause must have at least one variable", metadata);
        }
        this.variables = variables;
    }

    public List<GroupByVariableDeclaration> getGroupVariables() {
        return this.variables;
    }

    @Override
    public void initHighestExecutionMode(VisitorConfig visitorConfig) {
        this.highestExecutionMode = this.previousClause.getHighestExecutionMode(visitorConfig);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        this.variables.forEach(e -> {
            if (e != null && e.getExpression() != null) {
                result.add(e.getExpression());
            }
        });
    	result.add(this.getPreviousClause());
        return result;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitGroupByClause(this, argument);
    }

    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(" (");
        for (GroupByVariableDeclaration var : this.variables) {
            buffer.append(var.getVariableName());
            buffer.append(", ");
        }
        buffer.append(")");
        buffer.append(" | " + this.highestExecutionMode);
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
        sb.append("group by ");
        int i = 0;
        for (GroupByVariableDeclaration groupby : this.variables) {
            sb.append("$" + groupby.variableName.toString());
            if (groupby.sequenceType != null)
                sb.append(" as " + groupby.sequenceType.toString());
            if (groupby.expression != null) {
                sb.append(" := (");
                groupby.expression.serializeToJSONiq(sb, 0);
                sb.append(")");
            }
            if (i == this.variables.size() - 1) {
                sb.append("\n");
            } else {
                sb.append(", ");
            }
            i++;
        }
    }
}
