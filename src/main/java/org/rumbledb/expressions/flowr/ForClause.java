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
import org.rumbledb.expressions.Node;
import sparksoniq.semantics.visitor.AbstractNodeVisitor;

import java.util.ArrayList;
import java.util.List;

public class ForClause extends FlworClause {


    private final List<ForClauseVar> forVariables;

    public ForClause(List<ForClauseVar> vars, ExceptionMetadata metadataFromContext) {
        super(FLWOR_CLAUSES.FOR, metadataFromContext);
        if (vars == null || vars.isEmpty())
            throw new SemanticException("For clause must have at least one variable", metadataFromContext);
        this.forVariables = vars;

        // chain forVariables with previousClause relationship
        for (int varIndex = this.forVariables.size() - 1; varIndex > 0; varIndex--) {
            this.forVariables.get(varIndex).setPreviousClause(this.forVariables.get(varIndex - 1));
        }
    }

    public List<ForClauseVar> getForVariables() {
        return this.forVariables;
    }

    @Override
    public void setPreviousClause(FlworClause previousClause) {
        super.setPreviousClause(previousClause);
        // assign the previous clause of the ForClause as the first variable definition's previous
        this.forVariables.get(0).previousClause = this.previousClause;
    }

    @Override
    public void initHighestExecutionMode() {
        // call isDataFrame on the last forVariable
        this._highestExecutionMode =
            this.forVariables.get(this.forVariables.size() - 1).getHighestExecutionMode();
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        this.forVariables.forEach(e -> {
            if (e != null)
                result.add(e);
        });
        return result;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitForClause(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(forClause for ";
        for (ForClauseVar var : this.forVariables)
            result += var.serializationString(true)
                + (this.forVariables.indexOf(var) < this.forVariables.size() - 1 ? " , " : "");
        result += ")";
        return result;
    }
}
