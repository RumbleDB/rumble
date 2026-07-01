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

package org.rumbledb.config.model;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
@Accessors(fluent = true)
public class DebugConfig {
    /**
     * Whether verbose error info should be shown in case an error is returned.
     */
    @Default
    private boolean showErrorInfo = false;

    /**
     * Whether verbose information on query plans should be displayed.
     */
    @Default
    private boolean printIteratorTree = false;

    /**
     * Whether debug output is enabled.
     * Note: this was meant to replace debug() of RumbleRuntimeConfiguration
     * But it's only used in one single place, so it might be worth merge this with showErrorInfo
     */
    @Default
    private boolean logging = false;
}
