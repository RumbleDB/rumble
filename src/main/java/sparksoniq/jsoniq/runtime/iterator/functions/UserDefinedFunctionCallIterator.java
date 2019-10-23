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
import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.object.ObjectKeysClosure;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.visitor.RuntimeIteratorVisitor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserDefinedFunctionCallIterator extends HybridRuntimeIterator {

	private static final long serialVersionUID = 1L;
	private String _fnName;
	private Expression _fnBody;
	private RuntimeIterator _fnBodyIterator;
	private List<RuntimeIterator> _fnArguments;
    private List<String> _fnArgumentNames;
    private Item _nextLocalResult;

    public UserDefinedFunctionCallIterator(
            String fnName,
            Expression fnBody,
            List<RuntimeIterator> arguments,
            List<String> argumentNames,
            IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
        _fnName = fnName;
        _fnBody = fnBody;
        _fnArguments = arguments;
        _fnArgumentNames = argumentNames;

    }

    @Override
    public void openLocal() {
        DynamicContext dc = new DynamicContext(_currentDynamicContext);
        putArgumentValuesInDynamicContext(dc);
        _currentDynamicContext = dc;
        _fnBodyIterator.open(_currentDynamicContext);
        setNextLocalResult();
    }

    private void initializeFunctionBodyIterator() {
        _fnBodyIterator = new RuntimeIteratorVisitor().visit(_fnBody, null);
    }

    private void putArgumentValuesInDynamicContext(DynamicContext context) {
        RuntimeIterator arg;
        String argName;
        List<Item> argValue;
        for (int i = 0; i < _fnArguments.size(); i++) {
            arg = _fnArguments.get(i);
            argName = _fnArgumentNames.get(i);

            argValue = getItemsFromIteratorWithCurrentContext(arg);
            context.addVariableValue("$" + argName, argValue);
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
        _fnBodyIterator.reset(_currentDynamicContext);
        setNextLocalResult();
    }

    @Override
    protected void closeLocal() {
        // ensure that recursive function calls terminate gracefully
        // the function call in the body of the deepest recursion call is never visited, never opened and never closed
        if (this.isOpen()) {
            _fnBodyIterator.close();
        }
    }

    public void setNextLocalResult() {
        _nextLocalResult = null;
        while (_fnBodyIterator.hasNext()) {
            _nextLocalResult = _fnBodyIterator.next();
        }

        if (_nextLocalResult == null) {
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
        initializeFunctionBodyIterator();
        return _fnBodyIterator.isRDD();
    }
}
