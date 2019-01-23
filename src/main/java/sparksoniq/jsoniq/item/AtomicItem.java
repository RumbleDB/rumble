/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
 package sparksoniq.jsoniq.item;

import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

import javax.naming.OperationNotSupportedException;
import java.util.Collection;
import java.util.List;

public abstract class AtomicItem extends Item {
    @Override
    public boolean isAtomic()
    {
        return true;
    }

    @Override
    public List<Item> getItems() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Atomic items are not arrays");
    }

    @Override
    public Item getItemAt(int i) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Atomic items are not arrays");
    }

    @Override
    public void putItem(Item value) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Atomic items are not arrays");
    }

    @Override
    public Item getItemByKey(String s) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Atomic items are not objects");
    }

    @Override
    public void putItemByKey(String s, Item value) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Atomic items are not objects");
    }

    @Override
    public List<String> getKeys() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Atomic items are not objects");
    }

    @Override
    public Collection<? extends Item> getValues() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Atomic items are not objects");
    }

    @Override
    public int getSize() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Atomic items are not arrays");
    }

    @Override public boolean isTypeOf(ItemType type) {
        if(type.getType().equals(ItemTypes.AtomicItem) || type.getType().equals(ItemTypes.Item))
            return true;
        return false;
    }

    protected AtomicItem() {
        super();
    }

}
