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
 package jiqs.spark.tuple;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import jiqs.jsoniq.exceptions.IqRuntimeException;
import jiqs.jsoniq.item.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FlworKey implements KryoSerializable, Comparable<FlworKey> {

    public static class ResultIndexKeyTuple{
        public int getResult() {
            return _result;
        }

        private final int _result;

        public int getIndex() {
            return _index;
        }

        private final int _index;

        public ResultIndexKeyTuple(int result, int index) {
            this._result = result;
            this._index = index;
        }

    }

    public List<Item> getKeyItems() {
        return keyItems;
    }

    public FlworKey(List<Item> contents) {
        this.keyItems = new ArrayList<>();
        this.keyItems.addAll(contents);

    }

    @Override public int hashCode(){
        String result = "";
        for(Item key : this.keyItems)
            result += key.serialize();
        return result.hashCode();
    }

    @Override public boolean equals(Object otherKey){
        if(otherKey instanceof FlworKey)
            return this.compareTo((FlworKey) otherKey) == 0;
        else
            return false;
    }


    @Override
    //TODO handle empty
    public int compareTo(FlworKey flworKey) {
        return this.compareWithFlworKey(flworKey).getResult();
    }

    public ResultIndexKeyTuple compareWithFlworKey(FlworKey flworKey){
        int result = 0;
        for(Item currentItem: this.keyItems){
            int index = this.keyItems.indexOf(currentItem);
            Item comparisonItem = flworKey.getKeyItems().get(index);
            if(currentItem == null || comparisonItem == null)
                return new ResultIndexKeyTuple(1, index);

            //numeric comparison
            if(Item.isNumeric(currentItem) && Item.isNumeric(comparisonItem)){
                BigDecimal value1 = Item.getNumericValue(currentItem, BigDecimal.class);
                BigDecimal value2 = Item.getNumericValue(comparisonItem, BigDecimal.class);
                result = value1.compareTo(value2);
            //Non atomics cannot be compared
            } else if (currentItem instanceof ArrayItem || currentItem instanceof ObjectItem ||
                    comparisonItem instanceof ArrayItem || comparisonItem instanceof ObjectItem)
                throw new IqRuntimeException("Non atomic key not allowed");
            //Boolean comparison
            else if (currentItem instanceof BooleanItem && comparisonItem instanceof BooleanItem) {
                result = new Boolean(((BooleanItem)currentItem).getBooleanValue()).
                        compareTo(((BooleanItem)comparisonItem).getBooleanValue());
            }
            //NULL is smaller than anything
            else if (currentItem instanceof NullItem || comparisonItem instanceof NullItem) {
                if(currentItem instanceof NullItem)
                    result = -1;
                else
                    result = 1;
            }
            else if(currentItem instanceof StringItem && comparisonItem instanceof StringItem)
                result = currentItem.serialize().compareTo(comparisonItem.serialize());
            else if(!currentItem.getClass().getSimpleName().equals(comparisonItem.getClass().getSimpleName()))
                throw new IqRuntimeException("Invalid sort key, different Item types");
            else
                result = currentItem.serialize().compareTo(comparisonItem.serialize());

            if(result != 0)
                return new ResultIndexKeyTuple(result, index);
        }
        return new ResultIndexKeyTuple(0, -1);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, keyItems);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        keyItems = kryo.readObject(input, ArrayList.class);
    }


    private List<Item> keyItems;


}
