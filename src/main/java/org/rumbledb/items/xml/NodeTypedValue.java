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

package org.rumbledb.items.xml;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.OurBadException;

/** Immutable representation of an element or attribute node's XDM typed-value state. */
public final class NodeTypedValue implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final NodeTypedValue UNTYPED_VALUE = new NodeTypedValue(State.UNTYPED, Collections.emptyList());
    private static final NodeTypedValue UNAVAILABLE_VALUE =
            new NodeTypedValue(State.UNAVAILABLE, Collections.emptyList());

    public enum State {
        UNTYPED,
        AVAILABLE,
        UNAVAILABLE
    }

    private final State state;
    private final List<Item> items;

    private NodeTypedValue(State state, List<Item> items) {
        this.state = state;
        this.items = new ArrayList<>(items);
    }

    public static NodeTypedValue untyped() {
        return UNTYPED_VALUE;
    }

    public static NodeTypedValue available(List<Item> items) {
        if (items == null) {
            throw new OurBadException("A node typed value cannot be null.");
        }
        for (Item item : items) {
            if (item == null || !item.isAtomic() || item.isNull()) {
                throw new OurBadException("A node typed value can only contain XDM atomic values.");
            }
        }
        return new NodeTypedValue(State.AVAILABLE, items);
    }

    public static NodeTypedValue unavailable() {
        return UNAVAILABLE_VALUE;
    }

    public State getState() {
        return this.state;
    }

    public List<Item> getItems() {
        if (this.state != State.AVAILABLE) {
            throw new OurBadException("Typed value items were requested while the value is not available.");
        }
        return Collections.unmodifiableList(this.items);
    }
}
