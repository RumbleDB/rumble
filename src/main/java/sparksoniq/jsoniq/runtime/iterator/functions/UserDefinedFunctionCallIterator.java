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

package sparksoniq.jsoniq.runtime.iterator.functions;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.FunctionItem;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionIdentifier;
import sparksoniq.jsoniq.runtime.iterator.functions.base.Functions;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class UserDefinedFunctionCallIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    // parametrized fields
    private FunctionIdentifier _fnIdentifier;
    private List<RuntimeIterator> _fnArguments;

    // calculated fields
    private FunctionItem _fnItem;
    private RuntimeIterator _fnBodyIterator;
    private Item _nextResult;

    public UserDefinedFunctionCallIterator(
            FunctionIdentifier fnIdentifier,
            List<RuntimeIterator> arguments,
            IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
        _fnIdentifier = fnIdentifier;
        _fnArguments = arguments;
    }

    @Override
    public void openLocal() {
        DynamicContext dc = new DynamicContext(_currentDynamicContext);
        putArgumentValuesInDynamicContext(dc);
        _currentDynamicContext = dc;
        _fnItem.getBodyIterator().open(_currentDynamicContext);
        setNextResult();
    }

    private void putArgumentValuesInDynamicContext(DynamicContext context) {
        RuntimeIterator arg;
        String argName;
        List<Item> argValue;
        for (int i = 0; i < _fnArguments.size(); i++) {
            arg = _fnArguments.get(i);
            argName = _fnItem.getParameterNames().get(i);

            argValue = getItemsFromIteratorWithCurrentContext(arg);
            context.addVariableValue("$" + argName, argValue);
        }
    }

    @Override
    public Item nextLocal() {
        if (this._hasNext) {
            Item result = _nextResult;
            setNextResult();
            return result;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in " + _fnIdentifier.getName() + "  function",
                getMetadata()
        );
    }

    @Override
    protected boolean hasNextLocal() {
        return _hasNext;
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        _fnBodyIterator.reset(_currentDynamicContext);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        // ensure that recursive function calls terminate gracefully
        // the function call in the body of the deepest recursion call is never visited, never opened and never closed
        if (this.isOpen()) {
            _fnBodyIterator.close();
        }
    }

    public void setNextResult() {
        _nextResult = null;
        if (_fnBodyIterator.hasNext()) {
            _nextResult = _fnBodyIterator.next();
        }

        if (_nextResult == null) {
            this._hasNext = false;
            _fnBodyIterator.close();
        } else {
            this._hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDD(DynamicContext dynamicContext) {
        DynamicContext dc = new DynamicContext(_currentDynamicContext);
        putArgumentValuesInDynamicContext(dc);
        _currentDynamicContext = dc;
        return _fnBodyIterator.getRDD(_currentDynamicContext);
    }

    @Override
    public boolean initIsRDD() {
        initializeFunctionItem();
        return _fnBodyIterator.isRDD();
    }

    private void initializeFunctionItem() {
        _fnItem = Functions.getUserDefinedFunction(_fnIdentifier, getMetadata());
        _fnBodyIterator = _fnItem.getBodyIterator();
    }
}
