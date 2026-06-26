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

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class ExternalVariableBindings {
    /**
     * Stores already-materialized external variable values
     */
    @Default
    private Map<Name, List<Item>> externalVariableValues = new HashMap<>();

    /**
     * Stores raw string values passed from CLI/API before they are parsed.
     */
    @Default
    private Map<Name, String> unparsedExternalVariableValues = new HashMap<>();

    /**
     * Maps variable names to file paths/URIs.
     */
    @Default
    private Map<Name, String> externalVariableValuesReadFromFiles = new HashMap<>();

    /**
     * Stores external variables backed by Spark Dataset<Row>
     */
    @Default
    private Map<Name, Dataset<Row>> externalVariableValuesReadFromDataFrames = new HashMap<>();

    /**
     * Set of variable names whose value should be read from stdin.
     */
    @Default
    private Set<Name> externalVariablesReadFromStandardInput = new HashSet<>();

    /**
     * Maps each external variable name to the format used to parse its input
     */
    @Default
    private Map<Name, String> externalVariablesInputFormats = new HashMap<>();
}
