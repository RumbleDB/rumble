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

package org.rumbledb.runtime.functions.input;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.SparkSessionManager;

import java.util.List;

public class CSVFileFunctionIterator extends DataFrameRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public CSVFileFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        Item stringItem = this.children.get(0)
                .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
        String url = stringItem.getStringValue();
        try {
            DataFrameReader dfr = SparkSessionManager.getInstance().getOrCreateSession().read();
            if (this.children.size() > 1) {
                Item headerBooleanItem = this.children.get(1)
                        .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
                boolean header = headerBooleanItem.getBooleanValue();
                dfr = dfr.option("header", header);
            }
            if (this.children.size() > 2) {
                Item inferSchemaBooleanItem = this.children.get(2)
                        .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
                boolean inferSchema = inferSchemaBooleanItem.getBooleanValue();
                dfr = dfr.option("inferSchema", inferSchema);
            }
            return dfr.csv(url);
        } catch (Exception e) {
            if (e instanceof AnalysisException) {
                throw new CannotRetrieveResourceException("File " + url + " not found.", getMetadata());
            }
            throw e;
        }
    }
}
