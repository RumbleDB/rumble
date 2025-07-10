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
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import sparksoniq.spark.SparkSessionManager;

import java.net.URI;
import java.util.List;

public class ParquetFileFunctionIterator extends DataFrameRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public ParquetFileFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {

        String url = this.children.get(0).materializeFirstItemOrNull(context).getStringValue();

        URI uri = FileSystemUtil.resolveURI(this.staticURI, url, getMetadata());
        if (!FileSystemUtil.exists(uri, context.getRumbleRuntimeConfiguration(), getMetadata())) {
            throw new CannotRetrieveResourceException("File " + uri + " not found.", getMetadata());
        }
        int partitions = -1;
        if (this.children.size() > 1) {
            partitions = this.children.get(1).materializeFirstItemOrNull(context).getIntValue();
        }
        try {
            Dataset<Row> dataFrame = SparkSessionManager.getInstance()
                .getOrCreateSession()
                .read()
                .parquet(uri.toString());
            if (partitions != -1) {
                dataFrame = dataFrame.repartition(partitions);
            }
            return new JSoundDataFrame(dataFrame);
        } catch (Exception e) {
            if (e instanceof AnalysisException) {
                throw new CannotRetrieveResourceException("File " + uri + " not found.", getMetadata());
            }
            throw e;
        }
    }
}
