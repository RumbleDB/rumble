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

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.PartiallyAppliedFunctionItem;
import org.rumbledb.items.PartiallyAppliedFunctionItem.ArgumentBinding;
import org.rumbledb.items.PartiallyAppliedFunctionItem.DataFrameBinding;
import org.rumbledb.items.PartiallyAppliedFunctionItem.LocalBinding;
import org.rumbledb.items.PartiallyAppliedFunctionItem.PlaceholderBinding;
import org.rumbledb.items.PartiallyAppliedFunctionItem.RddBinding;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a partially-applied function item by capturing supplied arguments in the closure and
 * exposing each placeholder as a parameter of the returned function item.
 */
public class PartialFunctionCallIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
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
        List<Name> partialApplicationParamNames = new ArrayList<>();
        List<SequenceType> partialApplicationParamTypes = new ArrayList<>();
        List<ArgumentBinding> argumentBindings = new ArrayList<>();

        for (int i = 0; i < this.functionArguments.size(); i++) {
            Name parameterName = this.functionItem.getParameterNames().get(i);
            RuntimeIterator argumentIterator = this.functionArguments.get(i);
            SequenceType parameterType = this.functionItem.getSignature().getParameterTypes().get(i);

            if (argumentIterator == null) {
                partialApplicationParamNames.add(parameterName);
                partialApplicationParamTypes.add(parameterType);
                argumentBindings.add(new PlaceholderBinding(parameterType));
            } else if (argumentIterator.isDataFrame()) {
                argumentBindings.add(
                    new DataFrameBinding(parameterType, argumentIterator.getDataFrame(context))
                );
            } else if (argumentIterator.isRDDOrDataFrame()) {
                argumentBindings.add(new RddBinding(parameterType, argumentIterator.getRDD(context)));
            } else {
                argumentBindings.add(new LocalBinding(parameterType, argumentIterator.materialize(context)));
            }
        }

        return new PartiallyAppliedFunctionItem(
                new FunctionIdentifier(
                        this.functionNameOverride,
                        partialApplicationParamNames.size()
                ),
                partialApplicationParamNames,
                new FunctionSignature(
                        partialApplicationParamTypes,
                        this.functionItem.getSignature().getReturnType(),
                        this.functionItem.getSignature().isUpdating()
                ),
                this.functionItem.getModuleDynamicContext(),
                this.functionItem,
                argumentBindings
        );
    }
}
