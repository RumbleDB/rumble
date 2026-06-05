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
package org.rumbledb.runtime.functions;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates a partially-applied function item by capturing supplied arguments in the closure and
 * exposing each placeholder as a parameter of the returned function item.
 */
public class PartialFunctionCallIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private static final Name PARTIAL_FUNCTION_TARGET_NAME = Name.createVariableInNoNamespace(
        "$4deff1a0-0d7f-43d1-bf96-09934cc6a539"
    );

    private final Item functionItem;
    private final List<RuntimeIterator> functionArguments;
    private final Name functionNameOverride;

    public PartialFunctionCallIterator(
            Item functionItem,
            List<RuntimeIterator> functionArguments,
            RuntimeStaticContext staticContext
    ) {
        this(functionItem, functionArguments, staticContext, null);
    }

    public PartialFunctionCallIterator(
            Item functionItem,
            List<RuntimeIterator> functionArguments,
            RuntimeStaticContext staticContext,
            Name functionNameOverride
    ) {
        super(null, staticContext);
        for (RuntimeIterator arg : functionArguments) {
            if (arg != null) {
                this.children.add(arg);
            }
        }
        this.functionItem = functionItem;
        this.functionArguments = functionArguments;
        this.functionNameOverride = functionNameOverride;

        FunctionCallArgumentCoercion.validateArity(functionItem, this.functionArguments, getMetadata());
        FunctionCallArgumentCoercion.wrapAccordingToSignature(
            functionItem,
            this.functionArguments,
            staticContext
        );
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Map<Name, List<Item>> localArgumentValues = new LinkedHashMap<>(
                this.functionItem.getLocalVariablesInClosure()
        );
        Map<Name, JavaRDD<Item>> rddArgumentValues = new LinkedHashMap<>(
                this.functionItem.getRDDVariablesInClosure()
        );
        Map<Name, JSoundDataFrame> dfArgumentValues = new LinkedHashMap<>(
                this.functionItem.getDFVariablesInClosure()
        );

        List<Name> partialApplicationParamNames = new ArrayList<>();
        List<SequenceType> partialApplicationParamTypes = new ArrayList<>();

        for (int i = 0; i < this.functionArguments.size(); i++) {
            Name parameterName = this.functionItem.getParameterNames().get(i);
            RuntimeIterator argumentIterator = this.functionArguments.get(i);

            if (argumentIterator == null) {
                partialApplicationParamNames.add(parameterName);
                partialApplicationParamTypes.add(this.functionItem.getSignature().getParameterTypes().get(i));
            } else if (argumentIterator.isDataFrame()) {
                dfArgumentValues.put(parameterName, argumentIterator.getDataFrame(context));
            } else if (argumentIterator.isRDDOrDataFrame()) {
                rddArgumentValues.put(parameterName, argumentIterator.getRDD(context));
            } else {
                localArgumentValues.put(parameterName, argumentIterator.materialize(context));
            }
        }

        RuntimeIterator functionBodyIterator = this.functionItem.getBodyIterator();
        boolean isBuiltinFunctionItem = this.functionItem.isBuiltinFunction();
        if (isBuiltinFunctionItem) {
            localArgumentValues.put(PARTIAL_FUNCTION_TARGET_NAME, List.of(this.functionItem));
            functionBodyIterator = createPartialBuiltinBodyIterator();
        }

        return new FunctionItem(
                new FunctionIdentifier(
                        this.functionNameOverride == null
                            ? this.functionItem.getIdentifier().getName()
                            : this.functionNameOverride,
                        partialApplicationParamNames.size()
                ),
                partialApplicationParamNames,
                new FunctionSignature(
                        partialApplicationParamTypes,
                        this.functionItem.getSignature().getReturnType(),
                        this.functionItem.getSignature().isUpdating()
                ),
                this.functionItem.getModuleDynamicContext(),
                functionBodyIterator,
                localArgumentValues,
                rddArgumentValues,
                dfArgumentValues,
                false
        );
    }

    private RuntimeIterator createPartialBuiltinBodyIterator() {
        RuntimeStaticContext targetStaticContext = getRuntimeStaticContext()
            .withStaticType(SequenceType.createSequenceType("function(*)"))
            .withExecutionMode(ExecutionMode.LOCAL);

        RuntimeIterator targetIterator = new VariableReferenceIterator(
                PARTIAL_FUNCTION_TARGET_NAME,
                targetStaticContext
        );

        List<RuntimeIterator> callArguments = new ArrayList<>();
        for (int i = 0; i < this.functionArguments.size(); i++) {
            RuntimeStaticContext argumentStaticContext;
            if (this.functionArguments.get(i) == null) {
                argumentStaticContext = getRuntimeStaticContext()
                    .withStaticType(this.functionItem.getSignature().getParameterTypes().get(i))
                    .withExecutionMode(ExecutionMode.LOCAL);
            } else {
                argumentStaticContext = this.functionArguments.get(i).getRuntimeStaticContext();
            }
            callArguments.add(
                new VariableReferenceIterator(
                        this.functionItem.getParameterNames().get(i),
                        argumentStaticContext
                )
            );
        }

        return new DynamicFunctionCallIterator(
                targetIterator,
                callArguments,
                getRuntimeStaticContext()
                    .withStaticType(this.functionItem.getSignature().getReturnType())
                    .withExecutionMode(ExecutionMode.LOCAL)
        );
    }
}
