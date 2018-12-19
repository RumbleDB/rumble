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
 package sparksoniq.jsoniq.compiler.translator.expr.primary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

public class FunctionCall extends PrimaryExpression {

    public List<Expression> getParameters() {
        return _parameters;
    }

    public String getFunctionName() {
        return _functionName;
    }


    public FunctionCall(String functionName, List<Expression> parameters, ExpressionMetadata metadata){
        super(metadata);
        this._functionName = functionName;
        this._parameters = parameters;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result =  new ArrayList<>();
        result.addAll(this._parameters);
        return getDescendantsFromChildren(result,depthSearch);
    }

    @Override
    public String serializationString(boolean prefix){
        String result = "(primaryExpr (functionCall " ;
        List<String> names = Arrays.asList(this._functionName.split(":"));
        Collections.reverse(names);
        for(String name : names)
            result += name + (names.indexOf(name) < names.size() -1 ? " : " : " ");
        if(this._parameters.size() > 0)
            result += "(argumentList ( ";
        for(Expression arg: this._parameters)
            result += "(argument (exprSingle " + arg.serializationString(false) +
                    (_parameters.indexOf(arg) < _parameters.size() - 1? ")) , ": ")) ");
        result += "))))";
        return result;

    }

    @Override
    public  <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument){
        return visitor.visitFunctionCall(this, argument);
    }

    private final String _functionName;
    private final List<Expression> _parameters;
}
