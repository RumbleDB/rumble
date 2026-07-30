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
 * Authors: Ghislain Fourny
 *
 */


package org.rumbledb.runtime.functions.io;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.ResourceLocalCursor;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import java.io.*;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

public class LocalTextFileFunctionIterator extends LocalFunctionCallIterator {

    @Serial
    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;

    private transient InputStream is;
    private transient Iterator<String> stream;

    public LocalTextFileFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new ResourceLocalCursor<>(
                () -> {
                    Item path = this.getChild(0).materializeFirstOrNull(context);
                    if (path == null) {
                        throw new IteratorFlowException(
                                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " local-text-file function",
                                getMetadata()
                        );
                    }
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
                    return new TextLineResourceIterator(input, getMetadata());
                },
                getMetadata()
        );
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.iterator = this.getChild(0);
        Item path = this.iterator.materializeFirstItemOrNull(context);
        if (path == null) {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " local-text-file function",
                    getMetadata()
            );
        }
        URI uri = FileSystemUtil.resolveFileSystemURI(
            this.staticContext.getStaticURI(),
            path.getStringValue(),
            getMetadata()
        );
        this.is = FileSystemUtil.getDataInputStream(
            uri,
            this.currentDynamicContextForLocalExecution.getRumbleRuntimeConfiguration(),
            getMetadata()
        );
        InputStreamReader r = new InputStreamReader(this.is);
        BufferedReader br = new BufferedReader(r);
        this.stream = br.lines().iterator();
        this.hasNext = this.stream.hasNext();
    }

    @Override
    public void close() {
        super.close();
        this.iterator = null;
        try {
            this.is.close();
        } catch (IOException e) {
            CannotRetrieveResourceException ex = new CannotRetrieveResourceException("I/O exception", getMetadata());
            ex.initCause(e);
            throw ex;
        }
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            String line = this.stream.next();
            this.hasNext = this.stream.hasNext();
            return ItemFactory.getInstance().createStringItem(line);
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " local-text-file function",
                getMetadata()
        );
    }

    private static final class TextLineResourceIterator
            implements
                ResourceLocalCursor.ResourceIterator<Item> {

        private final InputStream input;
        private final Iterator<String> lines;
        private final org.rumbledb.exceptions.ExceptionMetadata metadata;

        private TextLineResourceIterator(
                InputStream input,
                org.rumbledb.exceptions.ExceptionMetadata metadata
        ) {
            this.input = input;
            this.lines = new BufferedReader(new InputStreamReader(input)).lines().iterator();
            this.metadata = metadata;
        }

        @Override
        public boolean hasNext() {
            return this.lines.hasNext();
        }

        @Override
        public Item next() {
            return ItemFactory.getInstance().createStringItem(this.lines.next());
        }

        @Override
        public void close() {
            try {
                this.input.close();
            } catch (IOException e) {
                CannotRetrieveResourceException exception = new CannotRetrieveResourceException(
                        "I/O exception",
                        this.metadata
                );
                exception.initCause(e);
                throw exception;
            }
        }
    }

}
