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

import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import java.io.*;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

public class LocalTextFileFunctionIterator extends LocalFunctionCallIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public LocalTextFileFunctionIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        Item path = this.getChild(0).materializeFirstOrNull(context);
        if (path == null) {
            throw new IteratorFlowException(
                    IteratorFlowException.FLOW_EXCEPTION_MESSAGE + " local-text-file function",
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
            getMetadata()
        );
        return new TextLineCursor(input, getMetadata());
    }

    private static final class TextLineCursor extends AbstractLocalCursor<Item> {
        private final InputStream input;
        private Iterator<String> lines;
        private final ExceptionMetadata metadata;

        private TextLineCursor(
                InputStream input,
                ExceptionMetadata metadata
        ) {
            super(metadata);
            this.input = input;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            this.lines = new BufferedReader(new InputStreamReader(this.input)).lines().iterator();
        }

        @Override
        protected boolean hasNextLocal() {
            return this.lines.hasNext();
        }

        @Override
        protected Item nextLocal() {
            return ItemFactory.getInstance().createStringItem(this.lines.next());
        }

        @Override
        protected void closeLocal() {
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
