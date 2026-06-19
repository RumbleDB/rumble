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
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;
import java.util.HashMap;

public class TranslateFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private static final int DELETE_CODEPOINT = -1;

    public TranslateFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item inputItem = this.children.get(0)
            .materializeFirstItemOrNull(context);
        Item mapStringItem = this.children.get(1)
            .materializeFirstItemOrNull(context);
        Item transStringItem = this.children.get(2)
            .materializeFirstItemOrNull(context);

        if (inputItem == null) {
            return ItemFactory.getInstance().createStringItem("");
        }

        String input = inputItem.getStringValue();
        String mapString = mapStringItem.getStringValue();
        String transString = transStringItem.getStringValue();

        int[] mapCodepoints = mapString.codePoints().toArray();
        int[] translationCodepoints = transString.codePoints().toArray();
        HashMap<Integer, Integer> translationMap = new HashMap<>();
        for (int i = 0; i < mapCodepoints.length; i++) {
            int replacement = i < translationCodepoints.length ? translationCodepoints[i] : DELETE_CODEPOINT;
            translationMap.putIfAbsent(mapCodepoints[i], replacement);
        }

        StringBuilder output = new StringBuilder(input.length());
        for (int codepoint : input.codePoints().toArray()) {
            int replacement = translationMap.getOrDefault(codepoint, codepoint);
            if (replacement != DELETE_CODEPOINT) {
                output.appendCodePoint(replacement);
            }
        }

        return ItemFactory.getInstance().createStringItem(output.toString());
    }

}
