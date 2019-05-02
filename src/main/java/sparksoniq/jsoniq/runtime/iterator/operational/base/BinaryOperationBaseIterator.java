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

package sparksoniq.jsoniq.runtime.iterator.operational.base;

import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.ArrayList;

public abstract class BinaryOperationBaseIterator extends LocalRuntimeIterator {

    protected final RuntimeIterator _leftIterator;
    protected final RuntimeIterator _rightIterator;
    protected final OperationalExpressionBase.Operator _operator;

    protected BinaryOperationBaseIterator(RuntimeIterator left, RuntimeIterator right,
                                          OperationalExpressionBase.Operator operator, IteratorMetadata iteratorMetadata) {
        super(new ArrayList<>(), iteratorMetadata);
        this._children.add(left);
        this._children.add(right);
        this._leftIterator = left;
        this._rightIterator = right;
        this._operator = operator;
    }

}
