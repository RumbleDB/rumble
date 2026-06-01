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
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CodepointNotValidException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.xml.XMLUtils;

import java.util.List;

public class CodepointsToStringFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public CodepointsToStringFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override

    public Item materializeFirstItemOrNull(DynamicContext context) {
        String xmlVersion = getConfiguration().getXmlVersion();
        RuntimeIterator argumentIterator = this.children.get(0);

        argumentIterator.open(context);
        try {
            return ItemFactory.getInstance().createStringItem(buildStringFromCodepoints(argumentIterator, xmlVersion));
        } finally {
            argumentIterator.close();
        }
    }

    private String buildStringFromCodepoints(RuntimeIterator argumentIterator, String xmlVersion) {
        StringBuilder sb = new StringBuilder();
        while (argumentIterator.hasNext()) {
            Item item = argumentIterator.next();

            int codepoint = extractCodePoint(item);

            if (!XMLUtils.isValidXmlCharacter(codepoint, xmlVersion)) {
                throw new CodepointNotValidException(
                        "Non-XML-conformant codepoint: " + item.getIntegerValue(),
                        this.children.get(0).getMetadata()
                );
            }
            sb.appendCodePoint(codepoint);
        }
        return sb.toString();
    }

    private int extractCodePoint(Item item) {
        try {
            return item.getIntegerValue().intValueExact();
        } catch (ArithmeticException e) {
            CodepointNotValidException ex = new CodepointNotValidException(
                    "Non-XML-conformant codepoint: " + item.getIntegerValue(),
                    this.children.get(0).getMetadata()
            );
            ex.initCause(e);
            throw ex;
        }
    }
}
