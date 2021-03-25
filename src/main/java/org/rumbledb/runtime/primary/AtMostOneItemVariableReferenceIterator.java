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

package org.rumbledb.runtime.primary;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.types.SequenceType;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AtMostOneItemVariableReferenceIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private SequenceType sequence;
    private Name variableName;

    public AtMostOneItemVariableReferenceIterator(
            Name variableName,
            SequenceType seq,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        this.variableName = variableName;
        this.sequence = seq;
    }

    public SequenceType getSequence() {
        return this.sequence;
    }

    public Name getVariableName() {
        return this.variableName;
    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>();
        result.put(this.variableName, DynamicContext.VariableDependency.FULL);
        return result;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        List<Item> items = context.getVariableValues()
            .getLocalVariableValue(
                this.variableName,
                getMetadata()
            );
        if (items.isEmpty()) {
            return null;
        }
        return items.get(0);
    }
}
