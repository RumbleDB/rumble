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
 */

package org.rumbledb.config;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExecutionOptions {
    /**
     * Enable native SQL predicates.
     */
    private boolean nativeSQLPredicates;

    /**
     * Whether DataFrame execution mode detection is activated for higher-order functions.
     * If disabled, higher-order functions will be executed locally.
     */
    private boolean dataFrameExecutionModeDetection;

    /**
     * Whether parallel execution (RDD, DataFrames) is enabled.
     */
    private boolean parallelExecution;

    /**
     * Whether DataFrame execution is enabled.
     */
    private boolean dataFrameExecution;

    /**
     * Whether advanced native execution for nested FLWOR queries is enabled.
     */
    private boolean nativeExecution;

    /**
     * Whether tail call optimization is enabled.
     */
    private boolean tailCallOptimization;

    /**
     * Whether function inlining is enabled.
     */
    private boolean functionInlining;

    /**
     * Whether the returned Pending Update List should be applied when executed on the command line.
     */
    private boolean applyUpdates;
}
