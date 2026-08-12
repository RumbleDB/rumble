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
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;

import org.rumbledb.spark.SparkSessionManager;

import java.io.Serial;
import java.net.URI;
import java.util.List;

public class RootFileFunctionIterator extends ItemRuntimePlan implements DataFrameRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    public RootFileFunctionIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext context) {
        ItemRuntimePlan urlIterator = this.getChild(0);
        String path = null;
        if (this.getChildren().size() > 1) {
            ItemRuntimePlan pathIterator = this.getChild(1);
            Item pathItem = pathIterator.materializeFirstOrNull(context);
            path = pathItem.getStringValue();
        }
        String url = urlIterator.materializeFirstOrNull(context).getStringValue();
        URI uri = FileSystemUtil.resolveFileSystemURI(this.staticContext.getStaticURI(), url, getMetadata());
        if (!FileSystemUtil.exists(uri, getMetadata())) {
            throw new CannotRetrieveResourceException("File " + uri + " not found.", getMetadata());
        }
        try {
            DataFrameReader reader = SparkSessionManager.getInstance()
                .getOrCreateSession()
                .read()
                .format("root");
            if (path != null) {
                reader.option("tree", path);
            }
            Dataset<Row> dataFrame = reader.load(FileSystemUtil.convertURIToStringForSpark(uri));
            return new HomogeneousItemDataFrame(dataFrame);
        } catch (Exception e) {
            if (e instanceof AnalysisException) {
                throw new CannotRetrieveResourceException("File " + uri + " not found.", getMetadata());
            }
            throw e;
        }
    }
}
