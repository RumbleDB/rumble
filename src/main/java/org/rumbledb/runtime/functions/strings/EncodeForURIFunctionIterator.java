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

package org.rumbledb.runtime.functions.strings;

import org.rumbledb.api.Item;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class EncodeForURIFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    @SuppressWarnings("unused")
    private static final HashSet<Integer> exclusionCharacters = new HashSet<Integer>(
            Arrays.asList(

            )
    );

    public EncodeForURIFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            Item inputItem = this.children.get(0)
                .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);

            if (inputItem == null) {
                return ItemFactory.getInstance().createStringItem("");
            }

            String encodedURI;
            try {
                encodedURI = URLEncoder.encode(inputItem.getStringValue(), "UTF-8")
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
            } catch (UnsupportedEncodingException e) {
                throw new OurBadException(e.getMessage(), getMetadata()); // Will only get here if "UTF-8" is changed or
                                                                          // method deprecates
            }

            return ItemFactory.getInstance().createStringItem(encodedURI);
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " translate function",
                    getMetadata()
            );
    }
}
