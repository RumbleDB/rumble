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
 * Author: Marco Schöb
 *
 */

package org.rumbledb.runtime.functions.input;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.parsing.XmlSyntaxToItemMapper;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import sparksoniq.spark.SparkSessionManager;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class XmlFilesFunctionIterator extends HybridRuntimeIterator {

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
        url = url.replaceAll(" ", "%20");
        URI uri = FileSystemUtil.resolveURI(this.staticURI, url, getMetadata());

        int partitions = -1;
        if (this.children.size() > 1) {
            partitions = this.children.get(1).materializeFirstItemOrNull(context).getIntValue();
        }

        JavaRDD<String> strings;
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
            if (partitions == -1) {
                strings = SparkSessionManager.getInstance()
                    .getJavaSparkContext()
                    .parallelize(lines);
            } else {
                strings = SparkSessionManager.getInstance()
                    .getJavaSparkContext()
                    .parallelize(
                        lines,
                        partitions
                    );
            }
        } else {
            if (!FileSystemUtil.exists(uri, context.getRumbleRuntimeConfiguration(), getMetadata())) {
                throw new CannotRetrieveResourceException("File " + uri + " not found.", getMetadata());
            }

            String path = uri.toString();
            if (uri.getScheme().contentEquals("file")) {
                path = path.replaceAll("%20", " ");
            }

            if (partitions == -1) {
                strings = SparkSessionManager.getInstance()
                    .getJavaSparkContext()
                    .wholeTextFiles(path)
                    .values();
            } else {
                strings = SparkSessionManager.getInstance()
                    .getJavaSparkContext()
                    .wholeTextFiles(
                        path,
                        partitions
                    )
                    .values();
            }
        }
        return strings.mapPartitions(new XmlSyntaxToItemMapper(getMetadata()));
    }

    protected void init() {
        try {
            URI uri = FileSystemUtil.resolveURI(
                this.staticURI,
                this.path.getStringValue(),
                getMetadata()
            );
            InputStream is = FileSystemUtil.getDataInputStream(
                uri,
                this.currentDynamicContextForLocalExecution.getRumbleRuntimeConfiguration(),
                getMetadata()
            );
            this.reader = new BufferedReader(new InputStreamReader(is));
            fetchNext();
        } catch (IteratorFlowException e) {
            throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
        }
    }

    @Override
    protected void openLocal() {
        this.path = this.iterator.materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
        init();
    }

    @Override
    protected void closeLocal() {
        try {
            this.reader.close();
        } catch (IOException e) {
            handleException(e);
        }
        this.reader = null;
        this.nextItem = null;
    }

    @Override
    protected void resetLocal() {
        try {
            this.reader.close();
        } catch (IOException e) {
            handleException(e);
        }
        this.path = this.iterator.materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
        init();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        Item result = this.nextItem;
        fetchNext();
        return result;
    }

    public void fetchNext() {
        throw new OurBadException("TODO");
        // try {
        // String line = this.reader.readLine();
        // this.hasNext = (line != null);
        // if (this.hasNext) {
        // JsonReader object = new JsonReader(new StringReader(line));
        // this.nextItem = ItemParser.getItemFromObject(object, getMetadata());
        // }
        // } catch (IOException e) {
        // handleException(e);
        // }
    }

    public void handleException(IOException e) {
        RumbleException rumbleException = new CannotRetrieveResourceException(
                "I/O error while accessing file: "
                    + this.path.getStringValue()
                    + " Cause: "
                    + e.getMessage(),
                getMetadata()
        );
        rumbleException.initCause(e);
        throw rumbleException;
    }
}
