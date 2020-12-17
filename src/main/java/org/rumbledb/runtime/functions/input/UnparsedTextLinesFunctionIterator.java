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

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.parsing.StringToStringItemMapper;
import org.rumbledb.runtime.RDDRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import sparksoniq.spark.SparkSessionManager;

import java.net.URI;
import java.util.List;

public class UnparsedTextLinesFunctionIterator extends RDDRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public UnparsedTextLinesFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<String> strings;
        RuntimeIterator urlIterator = this.children.get(0);
        Item url = urlIterator.materializeFirstItemOrNull(context);
        if (url == null) {
            return SparkSessionManager.getInstance()
                .getJavaSparkContext()
                .emptyRDD();
        }
        URI uri = FileSystemUtil.resolveURI(this.staticURI, url.getStringValue(), getMetadata());
        if (!FileSystemUtil.exists(uri, context.getRumbleRuntimeConfiguration(), getMetadata())) {
            throw new CannotRetrieveResourceException("File " + uri + " not found.", getMetadata());
        }

        if (this.children.size() == 1) {
            strings = SparkSessionManager.getInstance()
                .getJavaSparkContext()
                .textFile(uri.toString());
        } else {
            RuntimeIterator partitionsIterator = this.children.get(1);
            partitionsIterator.open(this.currentDynamicContextForLocalExecution);
            strings = SparkSessionManager.getInstance()
                .getJavaSparkContext()
                .textFile(
                    uri.toString(),
                    partitionsIterator.next().getIntValue()
                );
            partitionsIterator.close();
        }
        return strings.mapPartitions(new StringToStringItemMapper());
    }
}
