/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package sparksoniq.jsoniq.item;

import org.rumbledb.api.Item;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.SingleType;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

public class NullItem extends AtomicItem {


	private static final long serialVersionUID = 1L;

	public NullItem() {
        super();
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.NullItem) || super.isTypeOf(type);
    }

    @Override
    public boolean isCastableAs(SingleType type) {
        return type.getType() == AtomicTypes.NullItem ||
                type.getType() == AtomicTypes.StringItem;
    }

    @Override
    public AtomicItem castAs(AtomicItem atomicItem) {
        return atomicItem.createFromNull(this);
    }

    @Override
    public AtomicItem createFromString(StringItem stringItem) {
        return ItemFactory.getInstance().createNullItem();
    }

    @Override
    public AtomicItem createFromNull(NullItem nullItem) {
        return this;
    }

    @Override
    public String serialize() {
        return "null";
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return false;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObjectOrNull(output, null, Item.class);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        kryo.readObjectOrNull(input, Item.class);

    }
    
    public boolean equals(Object otherItem)
    {
        if(!(otherItem instanceof Item))
        {
            return false;
        }
        Item o = (Item)otherItem;
        return o.isNull();
    }
    
    public int hashCode()
    {
        return 0;
    }
}
