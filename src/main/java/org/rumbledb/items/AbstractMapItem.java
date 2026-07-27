/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
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
import java.util.List;

import org.rumbledb.api.Item;

/**
 * Order-independent structural Java equality for every materialized and lazy map representation.
 */
public abstract class AbstractMapItem implements Item {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Item otherItem) || !otherItem.isMap() || this.getSize() != otherItem.getSize()) {
            return false;
        }
        for (Item key : this.getItemKeys()) {
            List<Item> value = this.getSequenceByKey(key);
            List<Item> otherValue = otherItem.getSequenceByKey(key);
            if (!value.equals(otherValue)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final int hashCode() {
        int result = this.getSize();
        for (Item key : this.getItemKeys()) {
            result += 31 * key.hashCode() + this.getSequenceByKey(key).hashCode();
        }
        return result;
    }
}
