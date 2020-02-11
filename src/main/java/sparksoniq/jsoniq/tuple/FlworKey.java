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

package sparksoniq.jsoniq.tuple;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SparksoniqRuntimeException;
import sparksoniq.semantics.types.ItemTypes;

import java.util.ArrayList;
import java.util.List;

public class FlworKey implements KryoSerializable {

    private List<Item> keyItems;

    public FlworKey(List<Item> contents) {
        this.keyItems = new ArrayList<>();
        this.keyItems.addAll(contents);

    }

    List<Item> getKeyItems() {
        return this.keyItems;
    }

    @Override
    public int hashCode() {
        StringBuilder result = new StringBuilder();
        for (Item key : this.keyItems)
            result.append(key.hashCode());
        return result.toString().hashCode();
    }

    @Override
    public boolean equals(Object otherKey) {
        if (otherKey instanceof FlworKey)
            return this.compareWithFlworKey((FlworKey) otherKey) == 0;
        else
            return false;
    }

    /**
     * Invariant - two Flworkeys have the same length
     *
     * @param flworKey "other" FlworKey to be compared against
     * @return comparison value (-1=smaller, 0=equal, 1=larger) * index (of the expression that determines ordering)
     */
    public int compareWithFlworKey(FlworKey flworKey) {
        if (this.keyItems.size() != flworKey.keyItems.size()) {
            throw new OurBadException("Invalid sort key: Key sizes can't be different.");
        }

        int result = 0;

        // iterate over every ordering expression of this flworkey
        int index = 0;
        while (index < this.keyItems.size()) {
            Item currentItem = this.keyItems.get(index);
            Item comparisonItem = flworKey.keyItems.get(index);

            // check for incorrect ordering inputs
            if (
                (currentItem != null && currentItem.isArray())
                    || (currentItem != null && currentItem.isObject())
                    ||
                    (comparisonItem != null && comparisonItem.isArray())
                    || (comparisonItem != null && comparisonItem.isObject())
            ) {
                throw new SparksoniqRuntimeException("Non atomic key not allowed");
            }

            // handle the Java null placeholder used in orderByClauseSparkIterator
            if (currentItem == null || comparisonItem == null) {
                // null equals null
                if (currentItem == null && comparisonItem == null) {
                    result = 0;
                } else if (currentItem == null) {
                    result = -1;
                } else {
                    result = 1;
                }
            } else {
                try {
                    result = currentItem.compareTo(comparisonItem);
                } catch (RuntimeException e) {
                    throw new SparksoniqRuntimeException(
                            "Invalid sort key: cannot compare item of type "
                                + ItemTypes.getItemTypeName(comparisonItem.getClass().getSimpleName())
                                + " with item of type "
                                + ItemTypes.getItemTypeName(currentItem.getClass().getSimpleName())
                                + "."
                    );
                }
            }

            // Simplify comparison result to -1/0/1
            result = (int) Math.signum(result);

            // if comparison result is not an equality, return it multiplied with the index of the expression compared
            if (result != 0) {
                return result * (index + 1); // use index+1 to prevent multiplication w/ 0 for the first index
            }
            index++;
        }
        // if keys are fully equal, return 0
        return result;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.keyItems);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.keyItems = kryo.readObject(input, ArrayList.class);
    }


}
