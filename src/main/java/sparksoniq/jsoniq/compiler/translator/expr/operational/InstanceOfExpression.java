/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package sparksoniq.jsoniq.compiler.translator.expr.operational;

import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworVarSequenceType;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.UnaryExpressionBase;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;


public class InstanceOfExpression extends UnaryExpressionBase {

    private FlworVarSequenceType _sequenceType;

    public InstanceOfExpression(Expression _mainExpression, ExpressionMetadata metadata) {
        super(_mainExpression, metadata);
        this._isActive = false;
    }

    public InstanceOfExpression(Expression _mainExpression, FlworVarSequenceType sequenceType,
                                ExpressionMetadata metadata) {
        super(_mainExpression, Operator.INSTANCE_OF, true, metadata);
        this._sequenceType = sequenceType;
    }

    public FlworVarSequenceType getsequenceType() {
        return _sequenceType;
    }

    @Override
    public boolean isActive() {
        return this._isActive;
    }

    @Override
    public <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument) {
        return visitor.visitInstanceOfExpression(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(instanceOfExpr ";
        result += _mainExpression.serializationString(true);
        result += _sequenceType != null ? "instance of " + _sequenceType.serializationString(prefix) : "";
        result += ")";
        return result;
    }

}
