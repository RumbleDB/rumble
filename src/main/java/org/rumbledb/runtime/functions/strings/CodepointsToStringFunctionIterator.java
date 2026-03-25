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
import java.math.BigInteger;
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
        RuntimeIterator argIter = this.children.get(0);

        StringBuilder sb = new StringBuilder();
        argIter.open(context);
        try {
            while (argIter.hasNext()) {
                Item item = argIter.next();

                if (!item.isInteger()) {
                    throw new UnexpectedTypeException("Integer item expected", argIter.getMetadata());
                }

                BigInteger bi = item.getIntegerValue();

                if (bi.signum() < 0 || bi.compareTo(BigInteger.valueOf(0x10FFFFL)) > 0) {
                    throw new CodepointNotValidException(
                            "Non-Unicode codepoint: " + bi + " (must be 0..0x10FFFF)",
                            argIter.getMetadata()
                    );
                }

                int cp = bi.intValue();

                if (!isValidCodePoint(cp, xmlVersion)) {
                    throw new CodepointNotValidException(
                            "Non-XML-conformant codepoint: "
                                + cp
                                + " (for XML "
                                + (xmlVersion.equals("1.0") ? "1.0" : "1.1")
                                + ")",
                            argIter.getMetadata()
                    );
                }

                sb.appendCodePoint(cp);
            }
        } finally {
            argIter.close();
        }

        return ItemFactory.getInstance().createStringItem(sb.toString());
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
