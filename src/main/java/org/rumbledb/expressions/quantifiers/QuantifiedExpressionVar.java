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

package org.rumbledb.expressions.quantifiers;

import org.rumbledb.context.Name;
import org.rumbledb.expressions.Expression;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

public class QuantifiedExpressionVar {
    private final Name variableName;
    private final Expression expression;
    private final SequenceType sequenceType;

    public QuantifiedExpressionVar(
            Name variableName,
            Expression varExpression,
            SequenceType sequenceType
    ) {
        this.variableName = variableName;
        this.expression = varExpression;
        this.sequenceType = sequenceType;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public Name getVariableName() {

        return this.variableName;
    }

    // default to item if it is [null]
    public SequenceType getSequenceType() {
        return this.sequenceType == null ? new SequenceType(ItemType.item) : this.sequenceType;
    }

    public SequenceType getActualSequenceType() {
        return this.sequenceType;
    }
}
