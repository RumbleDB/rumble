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

package org.rumbledb.runtime.flwor.expression;


import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.io.Serializable;

public class GroupByClauseSparkIteratorExpression implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;
    private final Name variableName;
    private final RuntimeIterator expression;
    private final ExceptionMetadata iteratorMetadata;
    private final String collationURI;
    private final SequenceType sequenceType;

    public GroupByClauseSparkIteratorExpression(
            RuntimeIterator expression,
            Name variableName,
            ExceptionMetadata iteratorMetadata,
            String collationURI,
            SequenceType sequenceType
    ) {
        this.expression = expression;
        this.variableName = variableName;
        this.iteratorMetadata = iteratorMetadata;
        this.collationURI = collationURI;
        this.sequenceType = sequenceType;
    }

    public Name getVariableName() {
        return this.variableName;
    }

    public RuntimeIterator getExpression() {
        return this.expression;
    }

    public String getCollationURI() {
        return this.collationURI;
    }

    public SequenceType getSequenceType() {
        return this.sequenceType;
    }
}
