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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

import javax.naming.OperationNotSupportedException;
import java.math.BigDecimal;

public class IntegerItem extends AtomicItem {

    public int getValue() {
        return _value;
    }

    public IntegerItem(int value){
        super();
        this._value =value;
    }

    @Override
    public String getStringValue() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Integer value exception");
    }

    @Override
    public boolean getBooleanValue() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Integer value exception");
    }

    @Override
    public double getDoubleValue() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Integer value exception");
    }

    @Override
    public int getIntegerValue()  {
        return _value;
    }

    @Override
    public BigDecimal getDecimalValue() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Integer value exception");
    }

    @Override
    public  boolean isInteger(){ return true; }

    @Override public boolean isTypeOf(ItemType type) {
        if(type.getType().equals(ItemTypes.IntegerItem) ||type.getType().equals(ItemTypes.DecimalItem)
                || super.isTypeOf(type))
            return true;
        return false;
    }

    @Override
    public String serialize() {
        return String.valueOf(_value);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeInt(this._value);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._value = input.readInt();
    }

    private int _value;
}
