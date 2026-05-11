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

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;

/**
 * Resolves {@link ExecutionMode} for a builtin from its catalogue metadata and the first argument mode.
 */
public final class BuiltinFunctionExecutionModes {

    private BuiltinFunctionExecutionModes() {
    }

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
            return dataFrameIfConfigurationAllows(configuration);
        }
        if (functionExecutionMode == BuiltinFunction.BuiltinFunctionExecutionMode.INHERIT_FROM_FIRST_ARGUMENT) {
            if (firstMode.isDataFrame()) {
                return dataFrameIfConfigurationAllows(configuration);
            }
            if (firstMode.isRDDOrDataFrame()) {
                return ExecutionMode.RDD;
            }
            return ExecutionMode.LOCAL;
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

    public static ExecutionMode dataFrameIfConfigurationAllows(RumbleRuntimeConfiguration configuration) {
        if (configuration.dataFrameExecution()) {
            return ExecutionMode.DATAFRAME;
        }
        return ExecutionMode.RDD;
    }
}
