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
 * Authors: Marco Schöb
 *
 */

package org.rumbledb.runtime.functions.input;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.items.parsing.XmlSyntaxToItemMapper;
import org.rumbledb.runtime.RDDRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import scala.Tuple2;
import sparksoniq.spark.SparkSessionManager;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XmlFilesFunctionIterator extends RDDRuntimeIterator {

    private static final long serialVersionUID = 1L;
    RuntimeIterator iterator;
    BufferedReader reader;
    Item path;
    Item nextItem;

    public XmlFilesFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.iterator = this.children.get(0);
        this.reader = null;
        this.nextItem = null;
        this.path = null;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        String url = this.children.get(0).materializeFirstItemOrNull(context).getStringValue();
        URI uri = FileSystemUtil.resolveURI(this.staticURI, url, getMetadata());

        int partitions = 32;
        if (this.children.size() > 1) {
            partitions = this.children.get(1).materializeFirstItemOrNull(context).getIntValue();
        }

        JavaPairRDD<String, String> strings;
        if (uri.getScheme().equals("http") || uri.getScheme().equals("https")) {
            InputStream is = FileSystemUtil.getDataInputStream(
                uri,
                context.getRumbleRuntimeConfiguration(),
                getMetadata()
            );
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            List<String> lines = new ArrayList<>();
            String line = null;
            try {
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                throw new CannotRetrieveResourceException("Cannot read " + uri, getMetadata());
            }
            String fileContent = String.join("", lines);
            strings = SparkSessionManager.getInstance()
                .getJavaSparkContext()
                .parallelizePairs(
                    Collections.singletonList(
                        new Tuple2<>(FileSystemUtil.convertURIToStringForSpark(uri), fileContent)
                    ),
                    partitions
                );
        } else {
            if (!FileSystemUtil.exists(uri, context.getRumbleRuntimeConfiguration(), getMetadata())) {
                throw new CannotRetrieveResourceException("File " + uri + " not found.", getMetadata());
            }

            String path = FileSystemUtil.convertURIToStringForSpark(uri);
            strings = SparkSessionManager.getInstance()
                .getJavaSparkContext()
                .wholeTextFiles(
                    path,
                    partitions
                );
        }
        return strings.mapPartitions(
            new XmlSyntaxToItemMapper(getMetadata(), context.getRumbleRuntimeConfiguration().optimizeParentPointers())
        );
    }
}
