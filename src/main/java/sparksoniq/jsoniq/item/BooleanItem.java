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

public class BooleanItem extends AtomicItem {

    public boolean getValue() {
        return _value;
    }

    protected BooleanItem() {
        super();
    }

    public BooleanItem(boolean value){
        super();
        this._value = value;
    }

    @Override
    public String getStringValue() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Boolean value exception");
    }

    @Override
    public boolean getBooleanValue() {
        return _value;
    }

    @Override
    public double getDoubleValue() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Boolean value exception");
    }

    @Override
    public int getIntegerValue() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Boolean value exception");
    }

    @Override
    public BigDecimal getDecimalValue() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Boolean value exception");
    }

    @Override
    public  boolean isBoolean(){ return true; }

    @Override public boolean isTypeOf(ItemType type) {
        if(type.getType().equals(ItemTypes.BooleanItem) || super.isTypeOf(type))
            return true;
        return false;
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

    private boolean _value;
}
