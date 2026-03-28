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
 */

package org.rumbledb.items;

import java.util.Objects;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

/**
 * Atomic item representing an {@code xs:QName} value as an expanded name ({@link Name}).
 */
public class QNameItem implements Item {

    private static final long serialVersionUID = 1L;

    private Name name;

    public QNameItem() {
        super();
    }

    public QNameItem(Name name) {
        super();
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Item)) {
            return false;
        }
        Item otherItem = (Item) other;
        if (!otherItem.isQName()) {
            return false;
        }
        return this.name.equals(otherItem.getQNameValue());
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public String getStringValue() {
        return this.name.toString();
    }

    @Override
    public Object getVariantValue() {
        return this.name;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.name);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.name = kryo.readObject(input, Name.class);
    }

    @Override
    public boolean isQName() {
        return true;
    }

    @Override
    public Name getQNameValue() {
        return this.name;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.QNameItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }
}
