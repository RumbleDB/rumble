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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;

public class TranslateFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public TranslateFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
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

        HashMap<Character, Character> mp = new HashMap<>();
        for (int i = 0; i < mapString.length(); i++) {
            char c = mapString.charAt(i);
            if (!(mp.containsKey(c))) {
                mp.put(c, i < transString.length() ? transString.charAt(i) : '\0');
            }
        }

        String output = input
            .codePoints()
            .mapToObj(c -> (char) c)
            .filter(s -> !(mp.containsKey(s) && mp.get(s) == '\0'))
            .map(s -> {
                if (mp.containsKey(s)) {
                    return mp.get(s);
                }
                return s;
            })
            .map(String::valueOf)
            .collect(Collectors.joining());

        return ItemFactory.getInstance().createStringItem(output);
    }

}
