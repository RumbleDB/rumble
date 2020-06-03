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

package org.rumbledb.runtime.functions.context;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.AbsentPartOfDynamicContextException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.module.FunctionOrVariableName;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.ExecutionMode;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PositionFunctionIterator extends LocalFunctionCallIterator {


    private static final long serialVersionUID = 1L;

    public PositionFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.hasNext = true;
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            Item result = this.currentDynamicContextForLocalExecution.getPosition();
            if (result == null) {
                throw new AbsentPartOfDynamicContextException("Context undefined (position) ", getMetadata());
            }
            return result;
        }
        return null;
    }

    public Map<FunctionOrVariableName, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<FunctionOrVariableName, DynamicContext.VariableDependency> result =
            new TreeMap<FunctionOrVariableName, DynamicContext.VariableDependency>();
        result.put(
            FunctionOrVariableName.createVariableInNoNamespace("$position"),
            DynamicContext.VariableDependency.FULL
        );
        return result;
    }

}
