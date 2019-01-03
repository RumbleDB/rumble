/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
package sparksoniq.jsoniq.runtime.iterator.functions.sequences.aggregate;

import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.List;

public abstract class AggregateFunctionIterator extends LocalFunctionCallIterator {

    protected AggregateFunctionIterator(List<RuntimeIterator> arguments,
                                        AggregateFunctionOperator operator, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
        AggregateFunctionOperator _operator = operator;
    }

    public enum AggregateFunctionOperator {
        MIN,
        MAX,
        AVG,
        SUM,
        COUNT
    }

}
