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

import sparksoniq.exceptions.UnknownFunctionCallException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.jsoniq.runtime.iterator.DataFrameRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RDDRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.BuiltinFunction;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionIdentifier;
import sparksoniq.jsoniq.runtime.iterator.functions.base.Functions;
import sparksoniq.jsoniq.runtime.iterator.functions.object.ObjectKeysFunctionIterator;
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
        List<ExpressionOrClause> result = new ArrayList<>(this._arguments);
        return getDescendantsFromChildren(result, depthSearch);
    }

    @Override
    public void initHighestExecutionMode() {
        this._highestExecutionMode = ExecutionMode.LOCAL;
        FunctionIdentifier identifier = new FunctionIdentifier(this._functionName, this._arguments.size());
        if (Functions.checkBuiltInFunctionExists(identifier)) {
            BuiltinFunction builtinFunction = Functions.getBuiltInFunction(identifier);
            Class<? extends RuntimeIterator> functionIteratorClass = builtinFunction.getFunctionIteratorClass();
            // if subclass of RDDRuntimeIterator
            if (RDDRuntimeIterator.class.isAssignableFrom(functionIteratorClass)) {
                if (DataFrameRuntimeIterator.class.isAssignableFrom(functionIteratorClass)) {
                    this._highestExecutionMode = ExecutionMode.DATAFRAME;
                } else {
                    this._highestExecutionMode = ExecutionMode.RDD;
                }
            } else if (HybridRuntimeIterator.class.isAssignableFrom(functionIteratorClass)) {
                if (functionIteratorClass.isInstance(ObjectKeysFunctionIterator.class)) {
                    for (ExpressionOrClause child : this.getDescendants()) {
                        if (child.getHighestExecutionMode().isRDD() && !child.getHighestExecutionMode().isDataFrame()) {
                            this._highestExecutionMode = ExecutionMode.RDD;
                        }
                    }
                } else {
                    for (ExpressionOrClause child : this.getDescendants()) {
                        if (child.getHighestExecutionMode().isRDD()) {
                            this._highestExecutionMode = ExecutionMode.RDD;
                        }
                    }
                }
            } else {
                // Local function call -> isRDD & isDataFrame are false
            }
        } else if (Functions.checkUserDefinedFunctionExecutionModeExists(identifier)) {
            this._highestExecutionMode = Functions.getUserDefinedFunctionExecutionMode(identifier, getMetadata());
        } else {
            throw new UnknownFunctionCallException(
                    identifier.getName(),
                    identifier.getArity(),
                    this.getMetadata()
            );
        }
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
