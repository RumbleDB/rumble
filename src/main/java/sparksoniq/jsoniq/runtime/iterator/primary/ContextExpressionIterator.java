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

package sparksoniq.jsoniq.runtime.iterator.primary;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.semantics.DynamicContext;

import java.util.Map;
import java.util.TreeMap;

public class ContextExpressionIterator extends LocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public ContextExpressionIterator(ExecutionMode executionMode, ExceptionMetadata iteratorMetadata) {
        super(null, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (hasNext()) {
            this.hasNext = false;
            return this.currentDynamicContextForLocalExecution.getLocalVariableValue("$$", getMetadata()).get(0);
        }
        throw new IteratorFlowException("Invalid next() call in Context Expression!", getMetadata());
    }

    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<String, DynamicContext.VariableDependency> result = new TreeMap<>();
        result.put("$", DynamicContext.VariableDependency.FULL);
        return result;
    }
}
