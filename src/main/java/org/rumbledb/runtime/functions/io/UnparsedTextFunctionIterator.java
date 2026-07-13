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
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnavailableResourceException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.URIUtils;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class UnparsedTextFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;

    private transient Item path;

    public UnparsedTextFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.iterator = this.children.get(0);
        this.path = this.iterator.materializeFirstItemOrNull(context);
        this.hasNext = this.path != null;
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        this.iterator = this.children.get(0);
        this.path = this.iterator.materializeFirstItemOrNull(context);
        this.hasNext = this.path != null;
    }

    @Override
    public void close() {
        super.close();
        this.path = null;
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            try {
                URI uri = resolveURI(this.path.getStringValue());
                String result = FileSystemUtil.readContent(
                    uri,
                    this.currentDynamicContextForLocalExecution.getRumbleRuntimeConfiguration(),
                    getMetadata()
                );
                this.hasNext = false;
                return ItemFactory.getInstance().createStringItem(result);
            } catch (UnavailableResourceException e) {
                throw e;
            } catch (RumbleException e) {
                UnavailableResourceException ex = new UnavailableResourceException(e.getMessage(), getMetadata());
                ex.initCause(e);
                throw ex;
            }
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " unparsed-text function",
                getMetadata()
        );
    }

    private URI resolveURI(String href) {
        try {
            URI uri = URIUtils.resolveURIReference(this.staticURI, href);
            if (uri.getFragment() != null) {
                throw new URISyntaxException(href, "Fragment identifiers are not allowed");
            }
            return uri;
        } catch (URISyntaxException e) {
            UnavailableResourceException ex = new UnavailableResourceException(
                    "Invalid URI supplied to fn:unparsed-text(): " + href,
                    getMetadata()
            );
            ex.initCause(e);
            throw ex;
        }
    }

}
