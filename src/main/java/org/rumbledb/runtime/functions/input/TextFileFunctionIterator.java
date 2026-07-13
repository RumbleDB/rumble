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
 */

package org.rumbledb.runtime.functions.input;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.items.parsing.StringToStringItemMapper;
import org.rumbledb.runtime.RDDRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import sparksoniq.spark.SparkSessionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/** Implements the Rumble-specific jn:text-file() function. */
public class TextFileFunctionIterator extends RDDRuntimeIterator {

    private static final long serialVersionUID = 1L;
    public static final int MIN_PARTITIONS = 10;

    public TextFileFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        RuntimeIterator urlIterator = this.children.get(0);
        Item url = urlIterator.materializeFirstItemOrNull(context);
        if (url == null) {
            return SparkSessionManager.getInstance()
                .getJavaSparkContext()
                .emptyRDD();
        }
        URI uri = FileSystemUtil.resolveURI(this.staticURI, url.getStringValue(), getMetadata());
        int partitions = -1;
        if (this.children.size() > 1) {
            Item partitionsItem = this.children.get(1).materializeFirstItemOrNull(context);
            if (partitionsItem != null) {
                partitions = partitionsItem.getIntValue();
            }
        }

        JavaRDD<String> strings;
        if ("http".equals(uri.getScheme()) || "https".equals(uri.getScheme())) {
            InputStream is = FileSystemUtil.getDataInputStream(
                uri,
                context.getRumbleRuntimeConfiguration(),
                getMetadata()
            );
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            List<String> lines = new ArrayList<>();
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                throw new CannotRetrieveResourceException("Cannot read " + uri, getMetadata());
            }
            if (partitions == -1) {
                strings = SparkSessionManager.getInstance()
                    .getJavaSparkContext()
                    .parallelize(lines);
            } else {
                strings = SparkSessionManager.getInstance()
                    .getJavaSparkContext()
                    .parallelize(lines, partitions);
            }
        } else {
            if (!FileSystemUtil.exists(uri, context.getRumbleRuntimeConfiguration(), getMetadata())) {
                throw new CannotRetrieveResourceException("File " + uri + " not found.", getMetadata());
            }

            int effectivePartitions = partitions == -1 ? MIN_PARTITIONS : partitions;
            strings = SparkSessionManager.getInstance()
                .getJavaSparkContext()
                .textFile(FileSystemUtil.convertURIToStringForSpark(uri), effectivePartitions);
        }
        return strings.mapPartitions(new StringToStringItemMapper());
    }
}
