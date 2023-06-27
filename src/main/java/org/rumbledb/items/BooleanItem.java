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

package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

public class BooleanItem implements Item {


    private static final long serialVersionUID = 1L;
    private boolean value;

    public BooleanItem() {
        super();
    }

    public BooleanItem(boolean value) {
        super();
        this.value = value;
    }

    public boolean getValue() {
        return this.value;
    }

    @Override
    public boolean getBooleanValue() {
        return this.getValue();
    }

    @Override
    public String getStringValue() {
        return String.valueOf(this.value);
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return this.getBooleanValue();
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeBoolean(this.getValue());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = input.readBoolean();
    }

    public boolean equals(Object otherItem) {
        if (!(otherItem instanceof Item)) {
            return false;
        }
        Item o = (Item) otherItem;
        if (!o.isBoolean()) {
            return false;
        }
        return (getBooleanValue() == o.getBooleanValue());
    }

    public int hashCode() {
        return getBooleanValue() ? 1 : 0;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.booleanItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public String getSparkSQLValue() {
        return String.valueOf(this.value);
    }

    @Override
    public String getSparkSQLType() {
        // TODO: Make enum?
        return "BOOLEAN";
    }
}
