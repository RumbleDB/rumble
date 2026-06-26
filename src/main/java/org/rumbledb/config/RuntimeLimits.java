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
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class RuntimeLimits {
    public static final int DEFAULT_RESULTS_SIZE_CAP = 10;
    public static final int DEFAULT_MATERIALIZATION_CAP = 100000;

    /**
     * Number of Items that should be collected as the overall result of a query.
     */
    @Default
    private int resultsSizeCap = DEFAULT_RESULTS_SIZE_CAP;

    /**
     * Number of Items that should be collected in case of a forced materialization. This applies in
     * particular to a local use of the ItemIterator.
     */
    @Default
    private int materializationCap = DEFAULT_MATERIALIZATION_CAP;
}
