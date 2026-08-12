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

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CodepointNotValidException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.xml.XMLUtils;

import java.io.Serial;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class CodepointsToStringFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public CodepointsToStringFunctionIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        String xmlVersion = getConfiguration().semantics().xmlVersion();
        ItemRuntimePlan argumentIterator = this.getChild(0);

        try (Cursor<Item> cursor = argumentIterator.getCursor(context)) {
            return ItemFactory.getInstance()
                .createStringItem(
                    buildStringFromCodepoints(cursor::hasNext, cursor::next, xmlVersion)
                );
        }
    }

    private String buildStringFromCodepoints(
            BooleanSupplier hasNext,
            Supplier<Item> next,
            String xmlVersion
    ) {
        StringBuilder result = new StringBuilder();
        while (hasNext.getAsBoolean()) {
            Item item = next.get();
            int codepoint = extractCodePoint(item);
            if (!XMLUtils.isValidXmlCharacter(codepoint, xmlVersion)) {
                throw new CodepointNotValidException(
                        "Non-XML-conformant codepoint: " + item.getIntegerValue(),
                        this.getChild(0).getRuntimeStaticContext().getMetadata()
                );
            }
            result.appendCodePoint(codepoint);
        }
        return result.toString();
    }

    private int extractCodePoint(Item item) {
        if (item.isInt()) {
            return item.getIntValue();
        }
        try {
            return item.getIntegerValue().intValueExact();
        } catch (ArithmeticException e) {
            CodepointNotValidException ex = new CodepointNotValidException(
                    "Non-XML-conformant codepoint: " + item.getIntegerValue(),
                    this.getChild(0).getRuntimeStaticContext().getMetadata()
            );
            ex.initCause(e);
            throw ex;
        }
    }
}
