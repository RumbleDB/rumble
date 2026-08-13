/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
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

package org.rumbledb.items;

import java.io.Serial;
import java.util.Objects;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

/** Atomic item representing an {@code xs:NOTATION} value as an expanded name. */
public final class NotationItem extends AbstractAtomicItem {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Name name;

    public NotationItem(Name name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public Item copy(boolean mutable) {
        return new NotationItem(this.name);
    }

    @Override
    public String getStringValue() {
        String prefix = this.name.getPrefix();
        return prefix == null || prefix.isEmpty()
            ? this.name.getLocalName()
            : prefix + ":" + this.name.getLocalName();
    }

    @Override
    public Object getVariantValue() {
        return this.name;
    }

    @Override
    public boolean isNotation() {
        return true;
    }

    @Override
    public Name getNotationValue() {
        return this.name;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.NOTATIONItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }
}
