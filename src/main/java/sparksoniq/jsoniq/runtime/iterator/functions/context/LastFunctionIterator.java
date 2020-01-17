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

package sparksoniq.jsoniq.runtime.iterator.functions.context;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.AbsentPartOfDynamicContextException;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LastFunctionIterator extends LocalFunctionCallIterator {


    private static final long serialVersionUID = 1L;

    public LastFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this._hasNext = true;
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            Item result = _currentDynamicContextForLocalExecution.getLast();
            if (result == null) {
                throw new AbsentPartOfDynamicContextException("Context undefined (last) ", getMetadata());
            }
            return result;
        }
        return null;
    }

    public Map<String, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<String, DynamicContext.VariableDependency> result =
            new TreeMap<String, DynamicContext.VariableDependency>();
        result.put("$last", DynamicContext.VariableDependency.FULL);
        return result;
    }

}
