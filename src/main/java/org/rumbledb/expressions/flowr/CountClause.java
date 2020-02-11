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
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import sparksoniq.semantics.visitor.AbstractNodeVisitor;

import java.util.Collections;
import java.util.List;


public class CountClause extends FlworClause {
    private VariableReferenceExpression countClauseVar;

    public CountClause(VariableReferenceExpression countClauseVar, ExceptionMetadata metadata) {
        super(FLWOR_CLAUSES.COUNT, metadata);
        this.countClauseVar = countClauseVar;
    }

    @Override
    public List<Node> getDescendants(boolean depthSearch) {
        return getDescendantsFromChildren(Collections.singletonList(this.countClauseVar), depthSearch);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitCountClause(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        return "(countClause count " + this.countClauseVar.serializationString(true) + ")";
    }

    public VariableReferenceExpression getCountVariable() {
        return this.countClauseVar;
    }

}
