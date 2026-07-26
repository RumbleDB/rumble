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
 */

package org.rumbledb.runtime.functions.maps;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.CommaExpressionIterator;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * FO 3.1 map:for-each($map as map(*), $action as function(xs:anyAtomicType, item()*) as item()*)
 * as item()*.
 */
public class MapForEachFunctionIterator extends HybridRuntimeIterator {

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new MapForEachLocalCursor(this, context);
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator mapIterator;
    private final RuntimeIterator actionIterator;

    public MapForEachFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2) {
            throw new OurBadException("map:for-each must have exactly two arguments.");
        }
        this.mapIterator = arguments.get(0);
        this.actionIterator = arguments.get(1);
    }

    private Invocation resolveInvocation(DynamicContext context) {
        List<Item> mapArguments = LocalCursorUtils.materialize(this.mapIterator, context);
        if (mapArguments.size() != 1 || !mapArguments.get(0).isMap()) {
            throw new UnexpectedTypeException(
                    "The first argument of map:for-each must be a single map item [err:XPTY0004].",
                    getMetadata()
            );
        }
        Item mapItem = mapArguments.get(0);

        List<Item> functionArguments = LocalCursorUtils.materialize(this.actionIterator, context);
        if (functionArguments.size() != 1 || !functionArguments.get(0).isFunction()) {
            throw new UnexpectedTypeException(
                    "The second argument of map:for-each must be a single function item [err:XPTY0004].",
                    getMetadata()
            );
        }
        Item actionFunction = functionArguments.get(0);
        if (actionFunction.getIdentifier().getArity() != 2) {
            throw new UnexpectedTypeException(
                    "The function passed to map:for-each must accept exactly two arguments [err:XPTY0004].",
                    getMetadata()
            );
        }

        RuntimeStaticContext keyArgumentContext = RuntimeStaticContext.builder()
            .configuration(getConfiguration())
            .staticType(SequenceType.createSequenceType("anyAtomicType"))
            .executionMode(ExecutionMode.LOCAL)
            .metadata(getMetadata())
            .build();
        RuntimeStaticContext valueArgumentContext = RuntimeStaticContext.builder()
            .configuration(getConfiguration())
            .staticType(SequenceType.createSequenceType("item*"))
            .executionMode(ExecutionMode.LOCAL)
            .metadata(getMetadata())
            .build();
        return new Invocation(mapItem, actionFunction, keyArgumentContext, valueArgumentContext);
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("map:for-each is currently supported only in local execution mode.");
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("map:for-each is currently supported only in local execution mode.");
    }

    private RuntimeIterator buildCallback(Invocation invocation, Item key) {
        List<RuntimeIterator> valueChildren = new ArrayList<>();
        List<Item> values = invocation.map.getSequenceByKey(key);
        if (values != null) {
            for (Item value : values) {
                valueChildren.add(new ConstantRuntimeIterator(value, invocation.valueContext));
            }
        }
        return NamedFunctions.buildFunctionItemCallIterator(
            invocation.action,
            this.staticContext,
            ExecutionMode.LOCAL,
            List.of(
                new ConstantRuntimeIterator(key, invocation.keyContext),
                new CommaExpressionIterator(valueChildren, invocation.valueContext)
            ),
            false
        );
    }

    private static final class Invocation {
        private final Item map;
        private final Item action;
        private final RuntimeStaticContext keyContext;
        private final RuntimeStaticContext valueContext;

        private Invocation(
                Item map,
                Item action,
                RuntimeStaticContext keyContext,
                RuntimeStaticContext valueContext
        ) {
            this.map = map;
            this.action = action;
            this.keyContext = keyContext;
            this.valueContext = valueContext;
        }
    }

    private static final class MapForEachLocalCursor extends AbstractLocalCursor<Item> {

        private final MapForEachFunctionIterator plan;
        private final DynamicContext context;
        private Invocation invocation;
        private java.util.Iterator<Item> keys;
        private LocalCursor<Item> callbackCursor;

        private MapForEachLocalCursor(MapForEachFunctionIterator plan, DynamicContext context) {
            super(plan.getMetadata());
            this.plan = plan;
            this.context = context;
        }

        @Override
        protected void openLocal() {
            this.invocation = this.plan.resolveInvocation(this.context);
            this.keys = this.invocation.map.getItemKeys().iterator();
        }

        @Override
        protected boolean hasNextLocal() {
            while (this.callbackCursor == null || !this.callbackCursor.hasNext()) {
                closeCallback();
                if (!this.keys.hasNext()) {
                    return false;
                }
                RuntimeIterator callback = this.plan.buildCallback(
                    this.invocation,
                    this.keys.next()
                );
                this.callbackCursor = callback.createLocalCursor(this.context);
                this.callbackCursor.open();
            }
            return true;
        }

        @Override
        protected Item nextLocal() {
            if (!hasNextLocal()) {
                throw invalidState("No more map:for-each results are available.");
            }
            return this.callbackCursor.next();
        }

        private void closeCallback() {
            if (this.callbackCursor != null) {
                this.callbackCursor.close();
                this.callbackCursor = null;
            }
        }

        @Override
        protected void closeLocal() {
            closeCallback();
            this.invocation = null;
            this.keys = null;
        }
    }
}
