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
package org.rumbledb.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;

/**
 * Resolves {@link ExecutionMode} for a builtin from its catalogue metadata and the first argument mode.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BuiltinFunctionExecutionModes {


    public static ExecutionMode resolve(
            BuiltinFunction builtinFunction,
            ExecutionMode firstArgumentMode,
            RumbleRuntimeConfiguration configuration
    ) {
        ExecutionMode firstMode =
            firstArgumentMode != null ? firstArgumentMode : ExecutionMode.LOCAL;
        BuiltinFunction.BuiltinFunctionExecutionMode functionExecutionMode =
            builtinFunction.getBuiltinFunctionExecutionMode();
        if (functionExecutionMode == BuiltinFunction.BuiltinFunctionExecutionMode.LOCAL) {
            return ExecutionMode.LOCAL;
        }
        if (functionExecutionMode == BuiltinFunction.BuiltinFunctionExecutionMode.RDD) {
            return ExecutionMode.RDD;
        }
        if (functionExecutionMode == BuiltinFunction.BuiltinFunctionExecutionMode.DATAFRAME) {
            return configuration.dataFrameExecution() ? ExecutionMode.DATAFRAME : ExecutionMode.RDD;
        }
        if (functionExecutionMode == BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT) {
            ExecutionMode preferredMode = firstMode.isDataFrame()
                ? configuration.dataFrameExecution() ? ExecutionMode.DATAFRAME : ExecutionMode.RDD
                : firstMode.isRDDOrDataFrame() ? ExecutionMode.RDD : ExecutionMode.LOCAL;
            return selectSupportedExecutionMode(builtinFunction, preferredMode);
        }
        if (
            functionExecutionMode == BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT_BUT_DATAFRAME_FALLSBACK_TO_LOCAL
        ) {
            if (firstMode.isRDDOrDataFrame() && !firstMode.isDataFrame()) {
                return ExecutionMode.RDD;
            }
            return ExecutionMode.LOCAL;
        }
        throw new OurBadException(
                "Unhandled functionExecutionMode detected while extracting execution mode for built-in function."
        );
    }

    private static ExecutionMode selectSupportedExecutionMode(
            BuiltinFunction builtinFunction,
            ExecutionMode preferredMode
    ) {
        Class<?> planClass = builtinFunction.getFunctionIteratorClass();
        boolean supportsLocal = LocalRuntimePlan.class.isAssignableFrom(planClass);
        boolean supportsRDD = RDDRuntimePlan.class.isAssignableFrom(planClass);
        boolean supportsDataFrame = DataFrameRuntimePlan.class.isAssignableFrom(planClass);

        return switch (preferredMode) {
            case LOCAL -> supportsLocal
                ? ExecutionMode.LOCAL
                : supportsRDD ? ExecutionMode.RDD : supportsDataFrame ? ExecutionMode.DATAFRAME : preferredMode;
            case RDD -> supportsRDD
                ? ExecutionMode.RDD
                : supportsDataFrame ? ExecutionMode.DATAFRAME : supportsLocal ? ExecutionMode.LOCAL : preferredMode;
            case DATAFRAME -> supportsDataFrame
                ? ExecutionMode.DATAFRAME
                : supportsRDD ? ExecutionMode.RDD : supportsLocal ? ExecutionMode.LOCAL : preferredMode;
            case UNSET -> preferredMode;
        };
    }
}
