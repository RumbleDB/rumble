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

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;

/**
 * W3C XPath/XQuery {@code map:keys}:
 * <ul>
 * <li>requires exactly one map argument</li>
 * <li>returns the atomic keys present in the map</li>
 * </ul>
 *
 * This built-in is local execution only (consistent with map/array accessors).
 */
public class MapKeysFunctionIterator extends ItemRuntimePlan implements LocalRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan mapIterator;

    public MapKeysFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
        if (arguments.size() != 1) {
            throw new OurBadException("map:keys must have exactly one argument.");
        }
        this.mapIterator = arguments.get(0);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(
                () -> getKeys(this.mapIterator.materialize(context)).iterator(), getMetadata());
    }

    private List<Item> getKeys(List<Item> maps) {
        if (maps.size() != 1) {
            throw new UnexpectedTypeException("map:keys expects exactly one map argument.", getMetadata());
        }
        Item mapItem = maps.get(0);
        if (mapItem == null || !mapItem.isMap()) {
            throw new UnexpectedTypeException("Type error; argument to map:keys must be a map.", getMetadata());
        }

        // MapItem already enforces distinct atomic keys (via op:same-key) during construction/merge.
        return mapItem.getItemKeys();
    }
}
