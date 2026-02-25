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
import org.rumbledb.config.RumbleRuntimeConfiguration.XMLVersion;
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
        XMLVersion xmlVersion = context.getXmlVersion();
        RuntimeIterator argIter = this.children.get(0);

        /*
         * For debugging purposes to see if the tests have correctly configured the Rumble Runtime - Prints the XML Version
         *
        System.err.println("xmlVersion in context = " + context.getXmlVersion());
        System.err.println("conf object = " + System.identityHashCode(context.getRumbleRuntimeConfiguration()));
         */

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
                            "Non-XML-conformant codepoint: " + cp + " (for XML " + (xmlVersion == XMLVersion.XML10 ? "1.0" : "1.1") + ")",
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

    /**
     * There are some tests that explicitly list a dependency on either XML 1.0 or XML 1.1.
     * The XML version decides the handling of certain control characters.
     *  XML 1.0 and XML 1.1 differ in how they handle C0 and C1 control characters:
     *  <ul>
     *    <li>XML 1.0 allows only TAB (0x09), LF (0x0A), and CR (0x0D).</li>
     *     <li>XML 1.1 allows all control characters except NUL (0x00).</li>
     *  </ul>
     * @param codepoint  the code point to check
     * @param xmlVersion the XML version that signals the correct handling
     * @return true if the code point is a permitted control character
     *         according to the specified XML version
     */
    private static boolean isPermittedControlCharacter(int codepoint, XMLVersion xmlVersion) {
        boolean isC0 = (codepoint >= 0x0000 && codepoint <= 0x001F);
        boolean isC1 = (codepoint >= 0x007F && codepoint <= 0x009F);

        if (!(isC0 || isC1)) {
            return false;
        }

        if (xmlVersion == XMLVersion.XML10) {
            return codepoint == 0x09 || codepoint == 0x0A || codepoint == 0x0D;
        } else {
            return codepoint != 0x00;
        }
    }

    /**
     * Checks whether a code point is valid by
     * <ol>
     *     <li>checking if it is within the allowed Unicode range</li>
     *     <li>checking if it is a control point in the C0 or C1 range — if so, verify it</li>
     *     <li>checking if it lies in one of the XML-permitted character ranges</li>
     * </ol>
     *
     * @param codepoint  the code point to check
     * @param xmlVersion the XML version used for control character validation
     * @return true iff the code point is valid
     */
    private static boolean isValidCodePoint(int codepoint, XMLVersion xmlVersion) {
        if (codepoint < 0 || codepoint > 0x10FFFF) return false;

        boolean isC0 = (codepoint >= 0x0000 && codepoint <= 0x001F);
        boolean isC1 = (codepoint >= 0x007F && codepoint <= 0x009F);

        if (isC0 || isC1) {
            return isPermittedControlCharacter(codepoint, xmlVersion);
        }

        return (codepoint <= 0xD7FF)
                || (0xE000 <= codepoint && codepoint <= 0xFFFD)
                || (0x10000 <= codepoint);
    }
}
