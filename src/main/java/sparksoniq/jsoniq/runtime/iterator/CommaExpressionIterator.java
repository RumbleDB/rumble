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
 package sparksoniq.jsoniq.runtime.iterator;

import org.omg.SendingContext.RunTime;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.exceptions.IteratorFlowException;

import java.util.List;

public class CommaExpressionIterator extends LocalRuntimeIterator {
    public CommaExpressionIterator(List<RuntimeIterator> childIterators, IteratorMetadata iteratorMetadata) {
        super(childIterators, iteratorMetadata);
    }

    @Override
    public Item next() {
        if(_hasNext == true){
            Item result = _nextResult;  // save the result to be returned
            setNextResult();            // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in Comma expression", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this._childIndex = 0;

        _currentChild = this._children.get(_childIndex);
        _currentChild.open(_currentDynamicContext);

        setNextResult();
    }

    public void setNextResult() {
        _nextResult = null;

        while (_nextResult == null) {
            if (_currentChild.hasNext()) {
                _nextResult = _currentChild.next();
            } else {
                _currentChild.close();
                if (++_childIndex == this._children.size()) {
                    break;
                }
                _currentChild = this._children.get(_childIndex);
                _currentChild.open(_currentDynamicContext);
            }
        }

        if (_nextResult == null) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }

    private RuntimeIterator _currentChild;
    private Item _nextResult;
    private int _childIndex;
}
