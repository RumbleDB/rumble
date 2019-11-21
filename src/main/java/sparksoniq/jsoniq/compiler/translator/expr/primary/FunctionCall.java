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

package sparksoniq.jsoniq.compiler.translator.expr.primary;

import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FunctionCall extends PrimaryExpression {

    private final String _functionName;
    private final List<Expression> _arguments;


    public FunctionCall(String functionName, List<Expression> arguments, ExpressionMetadata metadata) {
        super(metadata);
        this._functionName = functionName;
        this._arguments = arguments;
    }

    public List<Expression> getArguments() {
        return _arguments;
    }

    public String getFunctionName() {
        return _functionName;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result = new ArrayList<>();
        result.addAll(this._arguments);
        return getDescendantsFromChildren(result, depthSearch);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(primaryExpr (functionCall ";
        List<String> names = Arrays.asList(this._functionName.split(":"));
        Collections.reverse(names);
        for (String name : names)
            result += name + (names.indexOf(name) < names.size() - 1 ? " : " : " ");
        result += "(argumentList ( ";
        for (Expression arg : this._arguments)
            result += "(argument (exprSingle "
                + arg.serializationString(false)
                +
                (_arguments.indexOf(arg) < _arguments.size() - 1 ? ")) , " : ")) ");
        result += "))";
        result += "))";
        return result;

    }

    @Override
    public <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument) {
        return visitor.visitFunctionCall(this, argument);
    }
}
