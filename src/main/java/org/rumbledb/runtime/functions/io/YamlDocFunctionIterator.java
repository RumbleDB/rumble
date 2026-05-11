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
import org.rumbledb.items.parsing.ItemParser;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;

import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

public class YamlDocFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private YAMLParser parser;
    private Item nextResult;

    public YamlDocFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.iterator = this.children.get(0);
        Item path = this.iterator.materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
        try {
            URI uri = FileSystemUtil.resolveURI(
                this.staticURI,
                path.getStringValue(),
                getMetadata()
            );
            InputStream is = FileSystemUtil.getDataInputStream(
                uri,
                this.currentDynamicContextForLocalExecution.getRumbleRuntimeConfiguration(),
                getMetadata()
            );
            YAMLFactory factory = new YAMLFactory();
            this.parser = factory.createParser(new InputStreamReader(is));
            getNextResult();
        } catch (IOException e) {
            throw new ParsingException(e.getMessage(), getMetadata());
        } catch (IteratorFlowException e) {
            throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
        }
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            Item result = this.nextResult;
            getNextResult();
            return result;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " yaml-doc function", getMetadata());
    }

    public void getNextResult() {
        com.fasterxml.jackson.core.JsonToken nt = null;
        try {
            nt = this.parser.nextToken();
        } catch (IOException e) {
            RumbleException r = new ParsingException(
                    "An error happened while parsing YAML. YAML is not well-formed!",
                    this.getMetadata()
            );
            r.initCause(e);
            throw r;
        }
        this.nextResult = ItemParser.getItemFromYAML(this.parser, nt, getMetadata());
        if (this.nextResult == null) {
            this.hasNext = false;
        } else {
            this.hasNext = true;
        }
    }


}
