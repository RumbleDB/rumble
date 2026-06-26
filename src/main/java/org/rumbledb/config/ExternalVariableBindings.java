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

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExternalVariableBindings {
    /**
     * Stores already-materialized external variable values
     */
    private Map<Name, List<Item>> externalVariableValues;

    /**
     * Stores raw string values passed from CLI/API before they are parsed.
     */
    private Map<Name, String> unparsedExternalVariableValues;

    /**
     * Maps variable names to file paths/URIs.
     */
    private Map<Name, String> externalVariableValuesReadFromFiles;

    /**
     * Stores external variables backed by Spark Dataset<Row>
     */
    private Map<Name, Dataset<Row>> externalVariableValuesReadFromDataFrames;

    /**
     * Set of variable names whose value should be read from stdin.
     */
    private Set<Name> externalVariablesReadFromStandardInput;

    /**
     * Maps each external variable name to the format used to parse its input
     */
    private Map<Name, String> externalVariablesInputFormats;
}
