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
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.SingleType;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

import java.math.BigDecimal;

public class BooleanItem extends AtomicItem {


	private static final long serialVersionUID = 1L;
	private boolean _value;

    public BooleanItem() {
        super();
    }

    public BooleanItem(boolean value) {
        super();
        this._value = value;
    }

    public boolean getValue() {
        return _value;
    }

    @Override
    public boolean getBooleanValue() {
        return _value;
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
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.BooleanItem) || super.isTypeOf(type);
    }

    @Override
    public boolean isCastableAs(SingleType type) {
        return type.getType() != AtomicTypes.AtomicItem &&
                type.getType() != AtomicTypes.NullItem;
    }

    @Override
    public String serialize() {
        return String.valueOf(_value);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeBoolean(this._value);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._value = input.readBoolean();
    }
    
    public boolean equals(Object otherItem)
    {
        if(!(otherItem instanceof Item))
        {
            return false;
        }
        Item o = (Item)otherItem;
        if(!o.isBoolean())
        {
            return false;
        }
        return (getBooleanValue() == o.getBooleanValue());
    }
    
    public int hashCode()
    {
        return getBooleanValue()?1:0;
    }

    @Override
    public int compareTo(Item other) {
        return other.isNull() ? 1 : Boolean.compare(this.getBooleanValue(), other.getBooleanValue());
    }

    @Override
    public Item compareItem(Item other, OperationalExpressionBase.Operator operator, IteratorMetadata metadata) {
        if (!other.isBoolean() && !other.isNull()) {
            throw new UnexpectedTypeException("Invalid args for boolean comparison " + this.serialize() +
                    ", " + other.serialize(), metadata);
        }
        return operator.apply(this, other);
    }
}
