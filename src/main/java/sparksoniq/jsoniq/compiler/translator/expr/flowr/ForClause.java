/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
 package sparksoniq.jsoniq.compiler.translator.expr.flowr;

import java.util.ArrayList;
import java.util.List;

import sparksoniq.exceptions.SemanticException;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

public class ForClause extends FlworClause {


    public List<ForClauseVar> getForVariables() {
        return forVariables;
    }

    private final List<ForClauseVar> forVariables;

    public ForClause(List<ForClauseVar> vars, ExpressionMetadata metadataFromContext) {
        super(FLWOR_CLAUSES.FOR, metadataFromContext);
        if(vars == null || vars.isEmpty())
            throw new SemanticException("For clause must have at least one variable", metadataFromContext);
        this.forVariables = vars;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result =  new ArrayList<>();
        forVariables.forEach(e -> {
            if (e != null)
                result.add(e);
        });
        return getDescendantsFromChildren(result,depthSearch);
    }

    @Override
    public  <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument){
        return visitor.visitForClause(this, argument);
    }

    @Override
    public String serializationString(boolean prefix){
        String result = "(forClause for ";
        for(ForClauseVar var: forVariables)
            result += var.serializationString(true)
                    + (forVariables.indexOf(var) < forVariables.size() -1 ? " , " : "");
        result += ")";
        return result;
    }
}
