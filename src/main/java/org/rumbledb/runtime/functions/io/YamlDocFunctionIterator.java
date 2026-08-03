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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.parsing.ItemParser;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;

import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serial;
import java.net.URI;
import java.util.List;

public class YamlDocFunctionIterator extends LocalFunctionCallIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public YamlDocFunctionIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        Item path = this.getChild(0).materializeFirstOrNull(context);
        try {
            URI uri = FileSystemUtil.resolveURI(
                this.staticContext.getStaticURI(),
                path.getStringValue(),
                getMetadata()
            );
            InputStream input = FileSystemUtil.getDataInputStream(
                uri,
                context.getRumbleRuntimeConfiguration(),
                getMetadata()
            );
            YAMLParser yamlParser = new YAMLFactory().createParser(new InputStreamReader(input));
            return new YamlResourceCursor(yamlParser, getMetadata());
        } catch (IOException e) {
            throw new ParsingException(e.getMessage(), getMetadata());
        }
    }

    private static final class YamlResourceCursor
            extends
                AbstractLocalCursor<Item> {

        private final YAMLParser parser;
        private final ExceptionMetadata metadata;
        private Item next;

        private YamlResourceCursor(
                YAMLParser parser,
                ExceptionMetadata metadata
        ) {
            super(metadata);
            this.parser = parser;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            this.advance();
        }

        @Override
        public boolean hasNextLocal() {
            return this.next != null;
        }

        @Override
        public Item nextLocal() {
            Item result = this.next;
            this.advance();
            return result;
        }

        private void advance() {
            try {
                this.next = ItemParser.getItemFromYAML(
                    this.parser,
                    this.parser.nextToken(),
                    this.metadata
                );
            } catch (IOException e) {
                RumbleException exception = new ParsingException(
                        "An error happened while parsing YAML. YAML is not well-formed!",
                        this.metadata
                );
                exception.initCause(e);
                throw exception;
            }
        }

        @Override
        public void closeLocal() {
            try {
                this.parser.close();
            } catch (IOException e) {
                RumbleException exception = new ParsingException(e.getMessage(), this.metadata);
                exception.initCause(e);
                throw exception;
            }
        }
    }

}
