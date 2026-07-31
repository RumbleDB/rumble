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
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.items.parsing.JSONParsingOptions;
import org.rumbledb.items.parsing.JSONSyntaxToItemMapper;
import org.rumbledb.runtime.plan.AbstractItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.ResourceLocalCursor;

import com.google.gson.stream.JsonReader;

import org.rumbledb.runtime.plan.RuntimePlan;
import sparksoniq.spark.SparkSessionManager;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class JsonLinesFunctionIterator extends AbstractItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item> {

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new ResourceLocalCursor<>(
                () -> openJsonLines(context),
                getMetadata()
        );
    }

    private JsonLinesResourceIterator openJsonLines(DynamicContext context) {
        Item path = this.iterator.materializeFirstOrNull(context);
        URI uri = FileSystemUtil.resolveFileSystemURI(
            this.staticContext.getStaticURI(),
            path.getStringValue(),
            getMetadata()
        );
        InputStream input = FileSystemUtil.getDataInputStream(
            uri,
            context.getRumbleRuntimeConfiguration(),
            getMetadata()
        );
        return new JsonLinesResourceIterator(
                new BufferedReader(new InputStreamReader(input)),
                path.getStringValue(),
                getMetadata(),
                this.getRuntimeStaticContext().isQuerySideEffecting()
        );
    }

    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimePlan<Item> iterator;

    public JsonLinesFunctionIterator(
            List<RuntimePlan<Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.iterator = this.getChild(0);
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext context) {
        String url = this.getChild(0).materializeFirstOrNull(context).getStringValue();
        URI uri = FileSystemUtil.resolveFileSystemURI(this.staticContext.getStaticURI(), url, getMetadata());

        int partitions = -1;
        if (this.getChildren().size() > 1) {
            partitions = this.getChild(1).materializeFirstOrNull(context).getIntValue();
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

            String path = FileSystemUtil.convertURIToStringForSpark(uri);

            if (partitions == -1) {
                strings = SparkSessionManager.getInstance()
                    .getJavaSparkContext()
                    .textFile(path);
            } else {
                strings = SparkSessionManager.getInstance()
                    .getJavaSparkContext()
                    .textFile(
                        path,
                        partitions
                    );
            }
        }
        return strings.mapPartitions(
            new JSONSyntaxToItemMapper(getMetadata(), this.getRuntimeStaticContext().isQuerySideEffecting())
        );
    }

    private static final class JsonLinesResourceIterator
            implements
                ResourceLocalCursor.ResourceIterator<Item> {
        private final BufferedReader reader;
        private final String path;
        private final ExceptionMetadata metadata;
        private final boolean querySideEffecting;
        private Item next;

        private JsonLinesResourceIterator(
                BufferedReader reader,
                String path,
                ExceptionMetadata metadata,
                boolean querySideEffecting
        ) {
            this.reader = reader;
            this.path = path;
            this.metadata = metadata;
            this.querySideEffecting = querySideEffecting;
            advance();
        }

        @Override
        public boolean hasNext() {
            return this.next != null;
        }

        @Override
        public Item next() {
            Item result = this.next;
            advance();
            return result;
        }

        private void advance() {
            try {
                String line = this.reader.readLine();
                this.next = line == null
                    ? null
                    : ItemParser.getItemFromObject(
                        new JsonReader(new StringReader(line)),
                        true,
                        JSONParsingOptions.NUMBER_FORMAT_ADAPTIVE,
                        this.metadata,
                        this.querySideEffecting
                    );
            } catch (IOException exception) {
                throw resourceError(exception);
            }
        }

        @Override
        public void close() {
            try {
                this.reader.close();
            } catch (IOException exception) {
                throw resourceError(exception);
            }
        }

        private RumbleException resourceError(IOException exception) {
            RumbleException result = new CannotRetrieveResourceException(
                    "I/O error while accessing file: "
                        + this.path
                        + " Cause: "
                        + exception.getMessage(),
                    this.metadata
            );
            result.initCause(exception);
            return result;
        }
    }
}
