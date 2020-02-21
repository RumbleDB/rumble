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

package org.rumbledb.expressions.operational;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.operational.base.UnaryExpressionBase;

import sparksoniq.semantics.types.SequenceType;


public class InstanceOfExpression extends UnaryExpressionBase {

    private SequenceType sequenceType;

    public InstanceOfExpression(
            Expression mainExpression,
            SequenceType sequenceType,
            ExceptionMetadata metadata
    ) {
        super(mainExpression, Operator.INSTANCE_OF, metadata);
        this.sequenceType = sequenceType;
    }

    public SequenceType getsequenceType() {
        return this.sequenceType;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitInstanceOfExpression(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(instanceOfExpr ";
        result += this.mainExpression.serializationString(true);
        result += this.sequenceType != null ? "instance of " + this.sequenceType.toString() : "";
        result += ")";
        return result;
    }

}
