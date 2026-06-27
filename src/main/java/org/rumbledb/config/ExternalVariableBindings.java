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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Builder
@Accessors(fluent = true)
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

    public List<Item> getExternalVariableValue(Name name) {
        return this.externalVariableValues.get(name);
    }

    public String getUnparsedExternalVariableValue(Name name) {
        return this.unparsedExternalVariableValues.get(name);
    }

    public String getExternalVariableValueReadFromFile(Name name) {
        return this.externalVariableValuesReadFromFiles.get(name);
    }

    public Dataset<Row> getExternalVariableValueReadFromDataFrame(Name name) {
        return this.externalVariableValuesReadFromDataFrames.get(name);
    }

    public boolean readFromStandardInput(Name variableName) {
        return this.externalVariablesReadFromStandardInput.contains(variableName);
    }

    public String getInputFormat(Name variableName) {
        return this.externalVariablesInputFormats.get(variableName);
    }

    public List<Name> getExternalVariablesReadFromDataFrames() {
        return new ArrayList<>(this.externalVariableValuesReadFromDataFrames.keySet());
    }
}
