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
 * Authors: Stefan Irimescu, Can Berker Cikis, Elwin Stephan
 *
 */

package sparksoniq.jsoniq.tuple;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.expressions.flowr.OrderByClauseSortingKey.EMPTY_ORDER;
import org.rumbledb.runtime.flwor.expression.OrderByClauseAnnotatedChildIterator;
import org.rumbledb.runtime.misc.ComparisonIterator;

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
        for (Item key : this.keyItems) {
            result.append(key.hashCode());
        }
        return result.toString().hashCode();
    }

    @Override
    public boolean equals(Object otherKey) {
        if (otherKey instanceof FlworKey) {
            return this.equalFlworKey((FlworKey) otherKey);
        } else {
            return false;
        }
    }

    /**
     * Invariant - two Flworkeys have the same length
     *
     * @param flworKey "other" FlworKey to be compared against
     * @return true if both items are equal, false otherwise (also when types are different)
     */

    public boolean equalFlworKey(FlworKey flworKey) {
        if (this.keyItems.size() != flworKey.keyItems.size()) {
            throw new OurBadException("Invalid sort key: Key sizes can't be different.");
        }

        // iterate over every ordering expression of this flworkey
        int index = 0;
        while (index < this.keyItems.size()) {
            Item item1 = this.keyItems.get(index);
            Item item2 = flworKey.keyItems.get(index);

            // check for incorrect ordering inputs
            if (
                (item1 != null && !item1.isAtomic())
                    ||
                    (item2 != null && !item2.isAtomic())
            ) {
                throw new RumbleException("Non atomic key not allowed");
            }

            long comparison = ComparisonIterator.compareItems(
                item1,
                item2,
                ComparisonOperator.VC_EQ,
                ExceptionMetadata.EMPTY_METADATA
            );
            if (comparison != 0) {
                return false;
            }

            index++;
        }
        return true;
    }

    /**
     * Invariant - two Flworkeys have the same length
     *
     * @param flworKey "other" FlworKey to be compared against
     * @return comparison value (-1=smaller, 0=equal, 1=larger) * index (of the expression that determines ordering)
     */
    public int compareWithFlworKey(FlworKey flworKey, List<OrderByClauseAnnotatedChildIterator> expressions) {
        if (this.keyItems.size() != flworKey.keyItems.size()) {
            throw new OurBadException("Invalid sort key: Key sizes can't be different.");
        }

        int result = 0;

        // iterate over every ordering expression of this flworkey
        int index = 0;
        long comparison = 0;
        while (index < this.keyItems.size()) {
            Item item1 = this.keyItems.get(index);
            Item item2 = flworKey.keyItems.get(index);

            // check for incorrect ordering inputs
            if (
                (item1 != null && !item1.isAtomic())
                    ||
                    (item2 != null && !item2.isAtomic())
            ) {
                throw new RumbleException("Non atomic key not allowed");
            }

            EMPTY_ORDER emptyOrder = EMPTY_ORDER.LEAST;
            if (expressions != null) {
                emptyOrder = expressions.get(index).getEmptyOrder();
            }
            switch (emptyOrder) {
                case LEAST:
                    if (item1 == null && item2 == null) {
                        result = 0;
                        break;
                    }
                    if (item1 == null) {
                        result = -1;
                        break;
                    }
                    if (item2 == null) {
                        result = 1;
                        break;
                    }
                    if (
                        (item1.isDouble() && Double.isNaN(item1.getDoubleValue()))
                            && item2.isDouble()
                            && Double.isNaN(item2.getDoubleValue())
                    ) {
                        result = 0;
                        break;
                    }
                    if (item1.isDouble() && Double.isNaN(item1.getDoubleValue())) {
                        result = -1;
                        break;
                    }
                    if (item2.isDouble() && Double.isNaN(item2.getDoubleValue())) {
                        result = 1;
                        break;
                    }

                    comparison = ComparisonIterator.compareItems(
                        item1,
                        item2,
                        ComparisonOperator.VC_EQ,
                        ExceptionMetadata.EMPTY_METADATA
                    );
                    if (comparison == Long.MIN_VALUE) {
                        throw new UnexpectedTypeException(
                                " \""
                                    + ComparisonOperator.VC_EQ
                                    + "\": operation not possible with parameters of type \""
                                    + item1.getDynamicType().toString()
                                    + "\" and \""
                                    + item2.getDynamicType().toString()
                                    + "\"",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                    result = (int) comparison;
                    break;
                case GREATEST:
                    if (item1 == null && item2 == null) {
                        result = 0;
                        break;
                    }
                    if (item1 == null) {
                        result = 1;
                        break;
                    }
                    if (item2 == null) {
                        result = -1;
                        break;
                    }
                    if (
                        (item1.isDouble() && Double.isNaN(item1.getDoubleValue()))
                            && item2.isDouble()
                            && Double.isNaN(item2.getDoubleValue())
                    ) {
                        result = 0;
                        break;
                    }
                    if (item1.isDouble() && Double.isNaN(item1.getDoubleValue())) {
                        result = 1;
                        break;
                    }
                    if (item2.isDouble() && Double.isNaN(item2.getDoubleValue())) {
                        result = -1;
                        break;
                    }

                    comparison = ComparisonIterator.compareItems(
                        item1,
                        item2,
                        ComparisonOperator.VC_EQ,
                        ExceptionMetadata.EMPTY_METADATA
                    );
                    if (comparison == Long.MIN_VALUE) {
                        throw new UnexpectedTypeException(
                                " \""
                                    + ComparisonOperator.VC_EQ
                                    + "\": operation not possible with parameters of type \""
                                    + item1.getDynamicType().toString()
                                    + "\" and \""
                                    + item2.getDynamicType().toString()
                                    + "\"",
                                ExceptionMetadata.EMPTY_METADATA
                        );
                    }
                    result = (int) comparison;
                    break;
                case NONE:
                    throw new OurBadException(
                            "Behavior of empty sequence ordering was not resolved",
                            ExceptionMetadata.EMPTY_METADATA
                    );
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
