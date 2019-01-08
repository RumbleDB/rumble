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
 package sparksoniq.jsoniq.runtime.iterator.operational;

import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.item.AtomicItem;
import sparksoniq.jsoniq.item.BooleanItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.base.UnaryOperationBaseIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

public class NotOperationIterator extends UnaryOperationBaseIterator {

    public NotOperationIterator(RuntimeIterator child, IteratorMetadata iteratorMetadata) {
        super(child, OperationalExpressionBase.Operator.NOT, iteratorMetadata);
    }

    @Override
    public AtomicItem next() {
        _child.open(_currentDynamicContext);
        Item child = _child.next();
        _child.close();
        this._hasNext = false;
        return new BooleanItem(!Item.getEffectiveBooleanValue(child), ItemMetadata.fromIteratorMetadata(getMetadata()));
    }
}
