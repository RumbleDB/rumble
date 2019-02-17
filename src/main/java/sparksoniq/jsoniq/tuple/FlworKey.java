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
package sparksoniq.jsoniq.tuple;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ObjectItem;

import java.util.ArrayList;
import java.util.List;

public class FlworKey implements KryoSerializable {

    public static class ResultIndexKeyTuple {
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

    @Override
    public int hashCode() {
        String result = "";
        for (Item key : this.keyItems)
            result += key.serialize();
        return result.hashCode();
    }

    @Override
    public boolean equals(Object otherKey) {
        if (otherKey instanceof FlworKey)
            return this.compareWithFlworKey((FlworKey) otherKey).getResult() == 0;
        else
            return false;
    }


    /**
     *
     *
     * @param flworKey "other" FlworKey to be compared against
     * @return ResultIndexKeyTuple(result, index):
     * result = -1 for smaller, 0 for equal, 1 for bigger (in ascending ordering)
     * index = -1 equal
     */
    public ResultIndexKeyTuple compareWithFlworKey(FlworKey flworKey) {
        int result = 0;
        int sizeOfThisFlworKey = this.keyItems.size();
        int sizeOfOtherFlworKey = flworKey.keyItems.size();

        // iterate over every ordering expression of this flworkey
        int index = 0;
        while (index < sizeOfThisFlworKey) {
            Item currentItem = this.keyItems.get(index);
            Item comparisonItem;

            // if the other Flworkey doesn't have the ordering expression result (it's empty)
            // return 1 (bigger in ascending order) and the index of the current expression
            if (index < sizeOfOtherFlworKey) {
                comparisonItem = flworKey.getKeyItems().get(index);
            } else {
                return new ResultIndexKeyTuple(1, index);
            }

            // check for incorrect ordering inputs
            if (currentItem instanceof ArrayItem || currentItem instanceof ObjectItem ||
                    comparisonItem instanceof ArrayItem || comparisonItem instanceof ObjectItem) {
                throw new SparksoniqRuntimeException("Non atomic key not allowed");
            } else if (!currentItem.getClass().getSimpleName().equals(comparisonItem.getClass().getSimpleName())
                    && (!Item.isNumeric(comparisonItem) || !Item.isNumeric(currentItem))) {
                throw new SparksoniqRuntimeException("Invalid sort key, different Item types");
            } else {
                result = Item.compareItems(currentItem, comparisonItem);
            }

            if (result == 0) {
                index ++;
                continue;
            }
            else {
                // if comparison results in a difference, return the comparison result and ordering expression index
                return new ResultIndexKeyTuple(result, index);
            }
        }
        // execution reaches here when "this" Flworkey (can be empty) has matching ordering results  with the other Flworkey

        // if ordering expressions results have equal size, Flworkeys are fully equal
        if ( sizeOfThisFlworKey == sizeOfOtherFlworKey) {
            return new ResultIndexKeyTuple(0, -1);
        } else if (sizeOfThisFlworKey < sizeOfOtherFlworKey) {
            // if this Flworkey lacks an ordering field, that field is empty
            // the size of "this" Flworkey is the index of the missing field, which is found in "other" Flworkey
            return new ResultIndexKeyTuple(-1, sizeOfThisFlworKey);
        } else {
            // the case where other Flworkey has an empty field is should be handled in the while loop above
            throw new SparksoniqRuntimeException("Unexpected behavior found in comparing Flworkeys.");
        }
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
