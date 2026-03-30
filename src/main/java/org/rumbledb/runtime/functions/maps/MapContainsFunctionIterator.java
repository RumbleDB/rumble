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
 */

package org.rumbledb.runtime.functions.maps;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayList;
import java.util.List;

/**
 * XPath/XQuery map:contains($map, $key) implementation.
 */
public class MapContainsFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public MapContainsFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        List<Item> mapArguments = new ArrayList<>();
        this.children.get(0).materialize(context, mapArguments);
        if (mapArguments.size() != 1 || !mapArguments.get(0).isMap()) {
            throw new UnexpectedTypeException(
                    "The first argument of map:contains must be a single map item [err:XPTY0004].",
                    getMetadata()
            );
        }

        List<Item> rawKey = new ArrayList<>();
        this.children.get(1).materialize(context, rawKey);
        List<Item> atomized = new ArrayList<>();
        for (Item item : rawKey) {
            atomized.addAll(item.atomizedValue());
        }
        if (atomized.size() != 1 || !atomized.get(0).isAtomic()) {
            throw new UnexpectedTypeException(
                    "The second argument of map:contains must atomize to a single atomic value [err:XPTY0004].",
                    getMetadata()
            );
        }

        Item map = mapArguments.get(0);
        Item key = atomized.get(0);
        boolean contains = map.getSequenceByKey(key) != null;
        return ItemFactory.getInstance().createBooleanItem(contains);
    }
}
