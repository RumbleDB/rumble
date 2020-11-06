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

import org.rumbledb.context.Name;
import org.rumbledb.expressions.Expression;
import org.rumbledb.types.SequenceType;

public class GroupByVariableDeclaration {

    protected Name variableName;
    protected Expression expression;
    protected SequenceType sequenceType;

    public GroupByVariableDeclaration(
            Name variableName,
            SequenceType sequenceType,
            Expression expression
    ) {
        if (variableName == null) {
            throw new IllegalArgumentException("Flowr var decls cannot be empty");
        }
        this.variableName = variableName;
        this.sequenceType = sequenceType;
        this.expression = expression;
    }

    public Name getVariableName() {
        return this.variableName;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public SequenceType getSequenceType() {
        return this.sequenceType == null ? SequenceType.MOST_GENERAL_SEQUENCE_TYPE : this.sequenceType;
    }

    public SequenceType getActualSequenceType() {
        return this.sequenceType;
    }
}
