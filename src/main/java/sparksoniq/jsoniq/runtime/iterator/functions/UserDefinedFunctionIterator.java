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
import org.apache.spark.api.java.function.FlatMapFunction;
import org.rumbledb.api.Item;
import scala.Dynamic;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.object.ObjectKeysClosure;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserDefinedFunctionIterator extends HybridRuntimeIterator {

	private static final long serialVersionUID = 1L;
	private String _fnName;
	private RuntimeIterator _fnBody;
	private List<RuntimeIterator> _fnArguments;
    private List<String> _fnArgumentNames;
    private Item _nextLocalResult;

    public UserDefinedFunctionIterator(
            String fnName,
            List<RuntimeIterator> argumentsAndBody,
            List<String> argumentNames,
            IteratorMetadata iteratorMetadata) {
        super(argumentsAndBody, iteratorMetadata);
        _fnName = fnName;
        _fnBody = argumentsAndBody.remove(argumentsAndBody.size()-1);
        _fnArguments = argumentsAndBody;
        _fnArgumentNames = argumentNames;

    }

    @Override
    public void openLocal() {
        DynamicContext dc = new DynamicContext(_currentDynamicContext);
        putArgumentValuesInDynamicContext(dc);
        _currentDynamicContext = dc;
        _fnBody.open(this._currentDynamicContext);
        setNextLocalResult();
    }

    private void putArgumentValuesInDynamicContext(DynamicContext context) {
        RuntimeIterator arg;
        String argName;
        List<Item> argValue;
        for (int i = 0; i < _fnArguments.size(); i++) {
            arg = _fnArguments.get(i);
            argName = _fnArgumentNames.get(i);

            argValue = getItemsFromIteratorWithCurrentContext(arg);
            context.addVariableValue(argName, argValue);
        }
    }


    @Override
    public Item nextLocal() {
        if (this._hasNext) {
            Item result = _nextLocalResult;
            setNextLocalResult();
            return result;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in "+ _fnName + "  function",
                getMetadata());
    }

    @Override
    protected boolean hasNextLocal() {
        return _hasNext;
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        _fnBody.reset(_currentDynamicContext);
        setNextLocalResult();
    }

    @Override
    protected void closeLocal() {
        _fnBody.close();
    }

    public void setNextLocalResult() {
        _nextLocalResult = null;
        while (_fnBody.hasNext()) {
            _nextLocalResult = _fnBody.next();
        }

        if (_nextLocalResult == null) {
            this._hasNext = false;
            _fnBody.close();
        } else {
            this._hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDD(DynamicContext dynamicContext) {
        DynamicContext dc = new DynamicContext(_currentDynamicContext);
        putArgumentValuesInDynamicContext(dc);
        _currentDynamicContext = dc;
        return _fnBody.getRDD(_currentDynamicContext);
    }

    @Override
    public boolean initIsRDD() {
        return _fnBody.isRDD();
    }
}
