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


import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworVarSequenceType;
import org.rumbledb.exceptions.ExceptionMetadata;;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionIdentifier;
import sparksoniq.jsoniq.runtime.iterator.functions.base.Functions;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FunctionDeclaration extends PrimaryExpression {

    private final String _name;
    private final Map<String, FlworVarSequenceType> _params;
    private final FlworVarSequenceType _returnType;
    private final Expression _body;

    public FunctionDeclaration(
            String name,
            Map<String, FlworVarSequenceType> params,
            FlworVarSequenceType returnType,
            Expression body,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this._name = name;
        this._params = params;
        this._returnType = returnType;
        this._body = body;
    }

    public String get_name() {
        return _name;
    }

    public Map<String, FlworVarSequenceType> get_params() {
        return _params;
    }

    public FlworVarSequenceType get_returnType() {
        return _returnType;
    }

    public Expression get_body() {
        return _body;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result = new ArrayList<>();
        return getDescendantsFromChildren(result, depthSearch);
    }

    public void registerUserDefinedFunctionExecutionMode(
            boolean ignoreDuplicateUserDefinedFunctionError,
            boolean ignoreUnsetExecutionModeAccessDuringFunctionDeclaration
    ) {
        FunctionIdentifier identifier = new FunctionIdentifier(this._name, this._params.size());
        // if named(static) function declaration
        if (!this._name.equals("")) {
            Functions.addUserDefinedFunctionExecutionMode(
                identifier,
                _body.getHighestExecutionMode(ignoreUnsetExecutionModeAccessDuringFunctionDeclaration),
                ignoreDuplicateUserDefinedFunctionError,
                this.getMetadata()
            );
        }
    }

    @Override
    public <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument) {
        return visitor.visitFunctionDeclaration(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(functionDeclaration ";
        result += _name;
        result += " (paramList (";
        for (Map.Entry<String, FlworVarSequenceType> entry : _params.entrySet()) {
            result += "param (";
            result += "NCName "
                + entry.getKey()
                + " sequenceType "
                + entry.getValue().serializationString(false)
                + ") , ";
        }
        result = result.substring(0, result.length() - 1); // remove last comma
        result += "))";

        result += " (sequenceType ( " + _returnType.serializationString(false) + "))";

        result += " (expr (" + _body.serializationString(false) + "))";

        result += ")";
        return result;
    }
}

