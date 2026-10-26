/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.functions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaRDD;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlanBindings;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceType;

/**
 * A call of a function item, a map or an array: the plan that the call evaluates, and the context in which it
 * evaluates it. For a user-defined function, the plan is the callee plan of the function item
 * ({@link FunctionItem#getCalleePlan}), which is built once, and the context binds the arguments to the parameters.
 */
public record FunctionCall(ItemRuntimePlan plan, DynamicContext context) {

    /**
     * Prepares a call of a function item. A null argument is a placeholder of a partial application, whose result is
     * the partially applied function item.
     *
     * @param functionItem the function item to call
     * @param arguments the arguments, evaluated in {@code context}
     * @param callerContext the static context of the caller, for the argument conversions and error locations
     * @param context the dynamic context of the caller
     */
    public static FunctionCall of(
            Item functionItem,
            List<? extends ItemRuntimePlan> arguments,
            RuntimeStaticContext callerContext,
            DynamicContext context) {
        List<ItemRuntimePlan> convertedArguments = new ArrayList<>(arguments);
        FunctionCallArgumentConversion.validateArity(functionItem, convertedArguments, callerContext.getMetadata());
        boolean isPartialApplication = convertedArguments.contains(null);
        FunctionCallArgumentConversion.wrapAccordingToSignature(
                functionItem.getIdentifier().getName(), functionItem.getSignature(), convertedArguments, callerContext);
        if (isPartialApplication) {
            FunctionItem result = partiallyApply(functionItem, convertedArguments, false, context);
            RuntimeStaticContext resultContext = callerContext.toBuilder()
                    .staticType(new SequenceType(ItemTypeFactory.createFunctionItemType(result.getSignature())))
                    .executionMode(ExecutionMode.LOCAL)
                    .build();
            return new FunctionCall(new ConstantRuntimeIterator(result, resultContext), context);
        }
        DynamicContext callContext = createCallContext(functionItem, convertedArguments, context);
        if (functionItem.getBodyIterator() instanceof BuiltinNamedFunctionReferenceMarkerIterator builtin) {
            return new FunctionCall(builtin.buildCallPlan(callContext), callContext);
        }
        FunctionItem function = (FunctionItem) functionItem;
        return new FunctionCall(function.getCalleePlan(), callContext);
    }

    /**
     * The execution mode in which a call of a function item evaluates.
     */
    public static ExecutionMode calleeExecutionMode(
            Item functionItem,
            List<? extends ItemRuntimePlan> arguments,
            boolean isPartialApplication,
            RuntimeStaticContext callerContext) {
        if (isPartialApplication) {
            return ExecutionMode.LOCAL;
        }
        FunctionCallArgumentConversion.validateArity(
                functionItem, new ArrayList<>(arguments), callerContext.getMetadata());
        if (functionItem.getBodyIterator() instanceof BuiltinNamedFunctionReferenceMarkerIterator builtin) {
            Name firstParameter = builtin.getFirstParameterName();
            int argumentIndex = functionItem.getParameterNames().indexOf(firstParameter);
            ExecutionMode firstMode;
            if (argumentIndex >= 0) {
                firstMode =
                        arguments.get(argumentIndex).getRuntimeStaticContext().getExecutionMode();
            } else if (functionItem.getDFVariablesInClosure().containsKey(firstParameter)) {
                firstMode = ExecutionMode.DATAFRAME;
            } else if (functionItem.getRDDVariablesInClosure().containsKey(firstParameter)) {
                firstMode = ExecutionMode.RDD;
            } else {
                firstMode = ExecutionMode.LOCAL;
            }
            return builtin.executionMode(firstMode);
        }
        if (functionItem.getBodyIterator() instanceof FunctionCoercionRuntimeIterator coercionRuntimeIterator) {
            return coercionRuntimeIterator.getWrappedCallableExecutionMode();
        }
        return functionItem.getBodyIterator().getRuntimeStaticContext().getExecutionMode();
    }

    /**
     * Creates the context of a call: the module context of the function, its closure, and the arguments bound to the
     * parameters. A call context belongs to one call; reusing it would retain parameters and function-local
     * variables.
     *
     * @param arguments the arguments, already converted to the parameter types, evaluated in {@code context}
     */
    public static DynamicContext createCallContext(
            Item functionItem, List<ItemRuntimePlan> arguments, DynamicContext context) {
        DynamicContext callContext = new DynamicContext(
                functionItem.getModuleDynamicContext(),
                new LinkedHashMap<>(functionItem.getLocalVariablesInClosure()),
                new LinkedHashMap<>(functionItem.getRDDVariablesInClosure()),
                new LinkedHashMap<>(functionItem.getDFVariablesInClosure()));
        if (functionItem.isBuiltinFunction()) {
            // The global context item must not replace the focus captured at the reference site.
            // DynamicContext removes global bindings from closure copies, so restore the captured focus.
            for (Name name : List.of(Name.CONTEXT_ITEM, Name.CONTEXT_POSITION, Name.CONTEXT_COUNT)) {
                List<Item> value = functionItem.getLocalVariablesInClosure().get(name);
                if (value != null) {
                    callContext.getVariableValues().addVariableValue(name, value);
                }
            }
        }
        for (int i = 0; i < arguments.size(); i++) {
            // Parameters shadow captured bindings, including bindings in another representation.
            callContext
                    .getVariableValues()
                    .removeVariable(functionItem.getParameterNames().get(i));
            RuntimePlanBindings.bind(
                    arguments.get(i),
                    callContext,
                    functionItem.getParameterNames().get(i),
                    context);
        }
        return callContext;
    }

    /**
     * Partial application generates a new function:
     *
     * <ul>
     * <li>Supplied parameters are set as NonLocalVariables</li>
     * <li>Argument placeholders form the parameters</li>
     * </ul>
     *
     * For a tail call, all arguments are supplied, and the body of the function then evaluates the returned function
     * item (see {@link FunctionBodyIterator}).
     *
     * @param arguments the arguments, already converted to the parameter types, with null for the placeholders
     * @return the partially applied function item
     */
    public static FunctionItem partiallyApply(
            Item functionItem, List<ItemRuntimePlan> arguments, boolean isTailCall, DynamicContext context) {
        Map<Name, List<Item>> localArgumentValues = new LinkedHashMap<>(functionItem.getLocalVariablesInClosure());
        Map<Name, JavaRDD<Item>> RDDArgumentValues = new LinkedHashMap<>(functionItem.getRDDVariablesInClosure());
        Map<Name, HomogeneousItemDataFrame> DFArgumentValues =
                new LinkedHashMap<>(functionItem.getDFVariablesInClosure());

        List<Name> partialApplicationParamNames = new ArrayList<>();
        List<SequenceType> partialApplicationParamTypes = new ArrayList<>();

        for (int i = 0; i < arguments.size(); i++) {
            Name argName = functionItem.getParameterNames().get(i);
            ItemRuntimePlan argIterator = arguments.get(i);

            if (argIterator == null) { // == ArgumentPlaceholder
                partialApplicationParamNames.add(argName);
                partialApplicationParamTypes.add(
                        functionItem.getSignature().getParameterTypes().get(i));
            } else {
                localArgumentValues.remove(argName);
                RDDArgumentValues.remove(argName);
                DFArgumentValues.remove(argName);
                if (argIterator.getRuntimeStaticContext().getExecutionMode().isDataFrame()) {
                    DFArgumentValues.put(argName, ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(argIterator, context));
                } else if (argIterator
                        .getRuntimeStaticContext()
                        .getExecutionMode()
                        .isRDDOrDataFrame()) {
                    RDDArgumentValues.put(argName, argIterator.getRDD(context));
                } else {
                    localArgumentValues.put(argName, argIterator.materialize(context));
                }
            }
        }

        Name functionItemName = functionItem.getIdentifier().getName();
        if (isTailCall) {
            functionItemName = Name.TAIL_CALL_OPTIMIZATION;
        } else {
            // Partial applications are anonymous, including those of user-defined functions.
            functionItemName = null;
        }
        FunctionItem partiallyAppliedFunction = new FunctionItem(
                new FunctionIdentifier(functionItemName, partialApplicationParamNames.size()),
                partialApplicationParamNames,
                new FunctionSignature(
                        partialApplicationParamTypes,
                        functionItem.getSignature().getReturnType(),
                        functionItem.getSignature().isUpdating()),
                functionItem.getModuleDynamicContext(),
                functionItem.getBodyIterator(),
                localArgumentValues,
                RDDArgumentValues,
                DFArgumentValues,
                functionItem.isBuiltinFunction());
        if (functionItem instanceof FunctionItem function
                && !(functionItem.getBodyIterator() instanceof BuiltinNamedFunctionReferenceMarkerIterator)) {
            partiallyAppliedFunction.shareCalleePlan(function);
        }
        return partiallyAppliedFunction;
    }

    public Cursor<Item> getCursor() {
        return this.plan.getCursor(this.context);
    }

    public List<Item> materialize() {
        return this.plan.materialize(this.context);
    }

    public JavaRDD<Item> getRDD() {
        return this.plan.getRDD(this.context);
    }

    public HomogeneousItemDataFrame getDataFrame() {
        return ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(this.plan, this.context);
    }
}
