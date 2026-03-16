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
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.exceptions.CodepointNotValidException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
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
        List<Item> codepoints = this.children.get(0).materialize(context);
        String xmlVersion = context.getRumbleRuntimeConfiguration().getXmlVersion();

        StringBuilder stringBuilder = new StringBuilder();
        for (Item item : codepoints) {
            if (!item.isInteger()) {
                throw new UnexpectedTypeException(
                        "Integer item expected",
                        this.children.get(0).getMetadata()
                );
            }
            int codePoint;
            try {
                codePoint = item.getIntegerValue().intValueExact();
            } catch (ArithmeticException e) {
                throw new CodepointNotValidException(
                        "Non-XML-conformant codepoint: " + item.getIntegerValue(),
                        this.children.get(0).getMetadata()
                );
            }
            if (!(isValidCodePoint(codePoint, xmlVersion))) {
                throw new CodepointNotValidException(
                        "Non-XML-conformant codepoint: " + codePoint,
                        this.children.get(0).getMetadata()
                );
            }
            stringBuilder.appendCodePoint(codePoint);
        }

        return ItemFactory.getInstance().createStringItem(stringBuilder.toString());
    }

    private static boolean isPermittedControlCharacter(int codepoint, String xmlVersion) {
        boolean isC0 = (codepoint >= 0 && codepoint <= 31);
        boolean isC1 = (codepoint >= 127 && codepoint <= 159);

        if (!(isC0 || isC1)) {
            return false;
        }

        if (xmlVersion.equals("1.0")) {
            return codepoint == 9 || codepoint == 10 || codepoint == 13;
        } else {
            return codepoint != 0;
        }
    }

    private static boolean isValidCodePoint(int codepoint, String xmlVersion) {
        if (codepoint < 0 || codepoint > 1114111)
            return false;

        boolean isC0 = (codepoint >= 0 && codepoint <= 31);
        boolean isC1 = (codepoint >= 127 && codepoint <= 159);

        if (isC0 || isC1) {
            return isPermittedControlCharacter(codepoint, xmlVersion);
        }

        return (codepoint <= 55295)
            || (57344 <= codepoint && codepoint <= 65533)
            || (65536 <= codepoint);
    }
}
