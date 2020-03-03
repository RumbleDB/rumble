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
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

public class GroupByClause extends Clause {

    private final List<GroupByClauseVar> groupVars;

    public GroupByClause(List<GroupByClauseVar> vars, ExceptionMetadata metadata) {
        super(FLWOR_CLAUSES.GROUP_BY, metadata);
        if (vars == null || vars.isEmpty()) {
            throw new SemanticException("Group clause must have at least one variable", metadata);
        }
        this.groupVars = vars;
    }

    public List<GroupByClauseVar> getGroupVariables() {
        return this.groupVars;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        this.groupVars.forEach(e -> {
            if (e != null) {
                result.add(e);
            }
        });
        return result;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitGroupByClause(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(groupByClause group by ";
        for (GroupByClauseVar var : this.groupVars) {
            result += var.serializationString(true)
                + (this.groupVars.indexOf(var) < this.groupVars.size() - 1 ? " , " : "");
        }
        result += ")";
        return result;
    }
}
