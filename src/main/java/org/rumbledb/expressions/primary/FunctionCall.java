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

package org.rumbledb.expressions.primary;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.functions.base.BuiltinFunction;
import sparksoniq.jsoniq.runtime.iterator.functions.base.BuiltinFunction.BuiltinFunctionExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionIdentifier;
import sparksoniq.jsoniq.runtime.iterator.functions.base.Functions;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnknownFunctionCallException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.ExpressionOrClause;

public class FunctionCall extends PrimaryExpression {

    private final String _functionName;
    private final List<Expression> _arguments;
    private final boolean _isPartialApplication;

    public FunctionCall(String functionName, List<Expression> arguments, ExceptionMetadata metadata) {
        super(metadata);
        this._functionName = functionName;
        this._arguments = arguments;
        this._isPartialApplication = arguments.stream().anyMatch(arg -> arg instanceof ArgumentPlaceholder);
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
    public final void initHighestExecutionMode() {
        throw new OurBadException("Function call expressions do not use the highestExecutionMode initializer");
    }

    public void initFunctionCallHighestExecutionMode(boolean ignoreMissingFunctionError) {
        FunctionIdentifier identifier = new FunctionIdentifier(this._functionName, this._arguments.size());
        if (Functions.checkBuiltInFunctionExists(identifier)) {
            if (_isPartialApplication) {
                throw new UnsupportedFeatureException(
                        "Partial application on built-in functions are not supported.",
                        this.getMetadata()
                );
            }
            BuiltinFunction builtinFunction = Functions.getBuiltInFunction(identifier);
            this._highestExecutionMode = this.getBuiltInFunctionExecutionMode(builtinFunction);
            return;
        }

        if (Functions.checkUserDefinedFunctionExecutionModeExists(identifier)) {
            if (_isPartialApplication) {
                this._highestExecutionMode = ExecutionMode.LOCAL;
                return;
            }
            this._highestExecutionMode = Functions.getUserDefinedFunctionExecutionMode(identifier, getMetadata());
            return;
        }

        if (!ignoreMissingFunctionError) {
            throw new UnknownFunctionCallException(
                    identifier.getName(),
                    identifier.getArity(),
                    this.getMetadata()
            );
        }
    }

    private ExecutionMode getBuiltInFunctionExecutionMode(BuiltinFunction builtinFunction) {
        BuiltinFunctionExecutionMode functionExecutionMode = builtinFunction.getBuiltinFunctionExecutionMode();
        if (functionExecutionMode == BuiltinFunctionExecutionMode.LOCAL) {
            return ExecutionMode.LOCAL;
        }
        if (functionExecutionMode == BuiltinFunctionExecutionMode.RDD) {
            return ExecutionMode.RDD;
        }
        if (functionExecutionMode == BuiltinFunctionExecutionMode.DATAFRAME) {
            return ExecutionMode.DATAFRAME;
        }
        if (functionExecutionMode == BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT) {
            ExecutionMode firstArgumentExecutionMode = this._arguments.get(0).getHighestExecutionMode();
            if (firstArgumentExecutionMode.isDataFrame()) {
                return ExecutionMode.DATAFRAME;
            }
            if (firstArgumentExecutionMode.isRDD()) {
                return ExecutionMode.RDD;
            }
            return ExecutionMode.LOCAL;
        }
        if (
            functionExecutionMode == BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT_BUT_DATAFRAME_FALLSBACK_TO_LOCAL
        ) {
            ExecutionMode firstArgumentExecutionMode = this._arguments.get(0).getHighestExecutionMode();
            if (
                firstArgumentExecutionMode.isRDD()
                    && !firstArgumentExecutionMode.isDataFrame()
            ) {
                return ExecutionMode.RDD;
            }
            return ExecutionMode.LOCAL;
        }
        throw new OurBadException(
                "Unhandled functionExecutionMode detected while extracting execution mode for built-in function."
        );
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
