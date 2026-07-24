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
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.items;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.ml.Estimator;
import org.apache.spark.ml.Transformer;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.CannotAtomizeException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.FunctionItemStringValueException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceType;


import sparksoniq.spark.ml.ApplyEstimatorRuntimeIterator;
import sparksoniq.spark.ml.ApplyTransformerRuntimeIterator;
import org.rumbledb.runtime.functions.FunctionCoercionRuntimeIterator;

public class FunctionItem implements Item {

    @Serial
    private static final long serialVersionUID = 1L;
    private FunctionIdentifier identifier;
    private List<Name> parameterNames;

    /**
     * Signature contains type information for all parameters and the return value
     */
    private FunctionSignature signature;

    /**
     * The body iterator is not serialized directly, but through the FunctionBodyIteratorFactory,
     * which allows for creating new instances of the body iterator when needed.
     */
    private FunctionBodyIteratorFactory bodyIteratorFactory;

    private DynamicContext dynamicModuleContext;
    private Map<Name, List<Item>> localVariablesInClosure;
    private Map<Name, JavaRDD<Item>> RDDVariablesInClosure;
    private Map<Name, JSoundDataFrame> dataFrameVariablesInClosure;

    /**
     * When true, this item was created for a builtin named function reference ({@code name#arity}).
     */
    private boolean isBuiltin;

    protected FunctionItem() {
        super();
    }

    /**
     * Creates a new function value for a named-function lookup. The function body factory is immutable: ordinary
     * bodies are created from its serialized snapshot and retained Spark ML bodies are intentionally shared. The
     * closure maps, on the other hand, must be per value because lookup binds the current dynamic context into them.
     * Their captured sequences and items can remain shared because lookup only adds or replaces map entries.
     */
    private FunctionItem(FunctionItem source) {
        this.identifier = source.identifier;
        this.parameterNames = source.parameterNames;
        this.signature = source.signature;
        this.bodyIteratorFactory = source.bodyIteratorFactory;
        this.dynamicModuleContext = source.dynamicModuleContext;
        this.localVariablesInClosure = new HashMap<>(source.localVariablesInClosure);
        this.RDDVariablesInClosure = new HashMap<>(source.RDDVariablesInClosure);
        this.dataFrameVariablesInClosure = new HashMap<>(source.dataFrameVariablesInClosure);
        this.isBuiltin = source.isBuiltin;
    }

    public FunctionItem(
            FunctionIdentifier identifier,
            List<Name> parameterNames,
            FunctionSignature signature,
            DynamicContext dynamicModuleContext,
            RuntimeIterator bodyIterator
    ) {
        this(identifier, parameterNames, signature, dynamicModuleContext, bodyIterator, false);
    }

    public FunctionItem(
            FunctionIdentifier identifier,
            List<Name> parameterNames,
            FunctionSignature signature,
            DynamicContext dynamicModuleContext,
            RuntimeIterator bodyIterator,
            boolean isBuiltin
    ) {
        this.identifier = identifier;
        this.parameterNames = parameterNames;
        this.signature = signature;
        this.bodyIteratorFactory = createBodyIteratorFactory(bodyIterator);
        this.dynamicModuleContext = dynamicModuleContext;
        this.localVariablesInClosure = new HashMap<>();
        this.RDDVariablesInClosure = new HashMap<>();
        this.dataFrameVariablesInClosure = new HashMap<>();
        this.isBuiltin = isBuiltin;
    }

    public FunctionItem(
            FunctionIdentifier identifier,
            List<Name> parameterNames,
            FunctionSignature signature,
            DynamicContext dynamicModuleContext,
            RuntimeIterator bodyIterator,
            Map<Name, List<Item>> localVariablesInClosure,
            Map<Name, JavaRDD<Item>> RDDVariablesInClosure,
            Map<Name, JSoundDataFrame> DFVariablesInClosure
    ) {
        this(
            identifier,
            parameterNames,
            signature,
            dynamicModuleContext,
            bodyIterator,
            localVariablesInClosure,
            RDDVariablesInClosure,
            DFVariablesInClosure,
            false
        );
    }

    public FunctionItem(
            FunctionIdentifier identifier,
            List<Name> parameterNames,
            FunctionSignature signature,
            DynamicContext dynamicModuleContext,
            RuntimeIterator bodyIterator,
            Map<Name, List<Item>> localVariablesInClosure,
            Map<Name, JavaRDD<Item>> RDDVariablesInClosure,
            Map<Name, JSoundDataFrame> DFVariablesInClosure,
            boolean isBuiltin
    ) {
        this.identifier = identifier;
        this.parameterNames = parameterNames;
        this.signature = signature;
        this.bodyIteratorFactory = createBodyIteratorFactory(bodyIterator);
        this.dynamicModuleContext = dynamicModuleContext;
        this.localVariablesInClosure = localVariablesInClosure;
        this.RDDVariablesInClosure = RDDVariablesInClosure;
        this.dataFrameVariablesInClosure = DFVariablesInClosure;
        this.isBuiltin = isBuiltin;
    }

    public FunctionItem(
            Name name,
            Map<Name, SequenceType> paramNameToSequenceTypes,
            SequenceType returnType,
            DynamicContext dynamicModuleContext,
            RuntimeIterator bodyIterator,
            boolean isUpdating
    ) {
        this(name, paramNameToSequenceTypes, returnType, dynamicModuleContext, bodyIterator, isUpdating, false);
    }

    public FunctionItem(
            Name name,
            Map<Name, SequenceType> paramNameToSequenceTypes,
            SequenceType returnType,
            DynamicContext dynamicModuleContext,
            RuntimeIterator bodyIterator,
            boolean isUpdating,
            boolean isBuiltin
    ) {
        List<Name> paramNames = new ArrayList<>();
        List<SequenceType> parameters = new ArrayList<>();
        for (Map.Entry<Name, SequenceType> paramEntry : paramNameToSequenceTypes.entrySet()) {
            paramNames.add(paramEntry.getKey());
            parameters.add(paramEntry.getValue());
        }

        this.identifier = new FunctionIdentifier(name, paramNames.size());
        this.parameterNames = paramNames;
        this.signature = new FunctionSignature(parameters, returnType, isUpdating);
        this.bodyIteratorFactory = createBodyIteratorFactory(bodyIterator);
        this.dynamicModuleContext = dynamicModuleContext;
        this.localVariablesInClosure = new HashMap<>();
        this.RDDVariablesInClosure = new HashMap<>();
        this.dataFrameVariablesInClosure = new HashMap<>();
        this.isBuiltin = isBuiltin;
    }

    @Override
    public FunctionItem copy(boolean mutable) {
        return this.deepCopy();
    }

    @Override
    public FunctionIdentifier getIdentifier() {
        return this.identifier;
    }

    @Override
    public List<Name> getParameterNames() {
        return this.parameterNames;
    }

    @Override
    public FunctionSignature getSignature() {
        return this.signature;
    }

    @Override
    public DynamicContext getModuleDynamicContext() {
        return this.dynamicModuleContext;
    }

    @Override
    public RuntimeIterator getBodyIterator() {
        return this.bodyIteratorFactory.getPrototype();
    }

    public RuntimeIterator createBodyIterator() {
        return this.bodyIteratorFactory.createExecutionInstance();
    }

    /**
     * Returns an independent function value without serializing its iterator tree.
     *
     * This is suitable for named-function lookup, which only extends the closure of the returned value. The closure
     * maps are copied while immutable function metadata and the body factory are shared.
     */
    public FunctionItem copyForLookup() {
        return new FunctionItem(this);
    }

    private static FunctionBodyIteratorFactory createBodyIteratorFactory(RuntimeIterator bodyIterator) {
        boolean retainBody = bodyIterator instanceof ApplyEstimatorRuntimeIterator
            || bodyIterator instanceof ApplyTransformerRuntimeIterator;
        return new FunctionBodyIteratorFactory(bodyIterator, retainBody);
    }

    @Override
    public Map<Name, List<Item>> getLocalVariablesInClosure() {
        return this.localVariablesInClosure;
    }

    @Override
    public Map<Name, JavaRDD<Item>> getRDDVariablesInClosure() {
        return this.RDDVariablesInClosure;
    }

    @Override
    public Map<Name, JSoundDataFrame> getDFVariablesInClosure() {
        return this.dataFrameVariablesInClosure;
    }

    @Override
    public boolean equals(Object other) {
        // functions can not be compared
        return false;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return false;
    }

    @Override
    public boolean isAtomic() {
        return false;
    }

    @Override
    public boolean isFunction() {
        return true;
    }

    @Override
    public boolean isBuiltinFunction() {
        return this.isBuiltin;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Function\n");
        sb.append("Identifier:" + this.identifier + "\n");
        sb.append("Parameters: ");
        for (Name param : this.parameterNames) {
            sb.append(param + " ");
        }
        sb.append("Signature: " + this.signature + "\n");
        sb.append("Body:\n" + getBodyIterator() + "\n");
        sb.append("Closure:\n");
        sb.append("  Local:\n");
        for (Name name : this.localVariablesInClosure.keySet()) {
            sb.append("    " + name + " (" + this.localVariablesInClosure.get(name).size() + " items)\n");
            if (this.localVariablesInClosure.get(name).size() == 1) {
                sb.append("      " + this.localVariablesInClosure.get(name).get(0).serialize() + "\n");
            }
        }
        sb.append("  RDD:\n");
        for (Name name : this.RDDVariablesInClosure.keySet()) {
            sb.append("    " + name + " (" + this.RDDVariablesInClosure.get(name).count() + " items)\n");
        }
        sb.append("  Data Frames:\n");
        for (Name name : this.dataFrameVariablesInClosure.keySet()) {
            sb.append("    " + name + " (" + this.dataFrameVariablesInClosure.get(name).count() + " items)\n");
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return this.identifier.hashCode()
            + String.join("", this.parameterNames.toString()).hashCode()
            + this.signature.hashCode();
    }



    @Override
    public ItemType getDynamicType() {
        return ItemTypeFactory.createFunctionItemType(this.signature);
    }

    public FunctionItem deepCopy() {
        return new FunctionItem(this);
    }

    public void populateClosureFromDynamicContext(DynamicContext dynamicContext, ExceptionMetadata metadata) {
        for (Name variable : dynamicContext.getVariableValues().getLocalVariableNames()) {
            this.localVariablesInClosure.put(
                variable,
                dynamicContext.getVariableValues().getLocalVariableValue(variable, metadata)
            );
        }
        for (Name variable : dynamicContext.getVariableValues().getRDDVariableNames()) {
            this.RDDVariablesInClosure.put(
                variable,
                dynamicContext.getVariableValues().getRDDVariableValue(variable, metadata)
            );
        }
        for (Name variable : dynamicContext.getVariableValues().getDataFrameVariableNames()) {
            this.dataFrameVariablesInClosure.put(
                variable,
                dynamicContext.getVariableValues().getDataFrameVariableValue(variable, metadata)
            );
        }
    }

    @Override
    public boolean isEstimator() {
        var bodyIterator = getBodyIterator();
        if (bodyIterator instanceof ApplyEstimatorRuntimeIterator) {
            return true;
        }
        if (bodyIterator instanceof FunctionCoercionRuntimeIterator coercionRuntimeIterator) {
            return coercionRuntimeIterator.getCallableItem().isEstimator();
        }
        return false;
    }

    @Override
    public Estimator<?> getEstimator() {
        var bodyIterator = getBodyIterator();
        if (bodyIterator instanceof ApplyEstimatorRuntimeIterator estimatorRuntimeIterator) {
            return estimatorRuntimeIterator.getEstimator();
        }
        if (bodyIterator instanceof FunctionCoercionRuntimeIterator coercionRuntimeIterator) {
            return coercionRuntimeIterator.getCallableItem().getEstimator();
        }
        throw new OurBadException("This is not an estimator.", ExceptionMetadata.EMPTY_METADATA);
    }

    @Override
    public boolean isTransformer() {
        var bodyIterator = getBodyIterator();
        if (bodyIterator instanceof ApplyTransformerRuntimeIterator) {
            return true;
        }
        if (bodyIterator instanceof FunctionCoercionRuntimeIterator coercionRuntimeIterator) {
            return coercionRuntimeIterator.getCallableItem().isTransformer();
        }
        return false;
    }

    @Override
    public Transformer getTransformer() {
        var bodyIterator = getBodyIterator();
        if (bodyIterator instanceof ApplyTransformerRuntimeIterator transformerRuntimeIterator) {
            return transformerRuntimeIterator.getTransformer();
        }
        if (bodyIterator instanceof FunctionCoercionRuntimeIterator coercionRuntimeIterator) {
            return coercionRuntimeIterator.getCallableItem().getTransformer();
        }
        throw new OurBadException("This is not a transformer.", ExceptionMetadata.EMPTY_METADATA);
    }


    public void setModuleDynamicContext(DynamicContext dynamicModuleContext) {
        this.dynamicModuleContext = dynamicModuleContext;
    }

    @Override
    public List<Item> atomizedValue() {
        throw new CannotAtomizeException("tried to atomize Function", ExceptionMetadata.EMPTY_METADATA);
    }

    @Override
    public String getStringValue() {
        throw new FunctionItemStringValueException(
                FunctionItemStringValueException.DEFAULT_MESSAGE,
                ExceptionMetadata.EMPTY_METADATA
        );
    }
}
