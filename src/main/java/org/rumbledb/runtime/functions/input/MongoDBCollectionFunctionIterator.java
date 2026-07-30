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

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import sparksoniq.spark.SparkSessionManager;

import java.util.List;

public class MongoDBCollectionFunctionIterator extends DataFrameRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public MongoDBCollectionFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {

        String uri = this.children.get(0).materializeFirstItemOrNull(context).getStringValue();
        String collection = this.children.get(1).materializeFirstItemOrNull(context).getStringValue();

        int partitions = -1;
        if (this.children.size() > 2) {
            partitions = this.children.get(2).materializeFirstItemOrNull(context).getIntValue();
        }

        try {
            Dataset<Row> dataFrame = SparkSessionManager.getInstance()
                .getOrCreateSession()
                .read()
                .format("mongodb")
                .option("spark.mongodb.read.connection.uri", uri)
                .option("collection", collection)
                .load();
            if (partitions != -1) {
                dataFrame = dataFrame.repartition(partitions);
            }
            return new JSoundDataFrame(dataFrame);
        } catch (Exception e) {
            RumbleException ex = new CannotRetrieveResourceException(
                    "Error retrieving MongoDB collection: " + e.getMessage(),
                    getMetadata()
            );
            ex.initCause(e);
            throw ex;
        }
    }
}
