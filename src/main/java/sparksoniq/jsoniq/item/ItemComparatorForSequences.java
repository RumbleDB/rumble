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

import sparksoniq.exceptions.SparksoniqRuntimeException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;

public class ItemComparatorForSequences implements Comparator<Item>, Serializable {
    /**
     * Comparator used for sequence aggregate functions and their  RDD evaluations
     * It compares 2 atomic items (non-null)
     * Non-atomics and nulls throw an exception
     *
     * @return -1 if v1 < v2; 0 if v1 == v2; 1 if v1 > v2;
     */
    public int compare(Item v1, Item v2) {
        int result;
        if (v1.isNumeric() && v2.isNumeric()) {
            BigDecimal value1 = v1.getNumericValue(BigDecimal.class);
            BigDecimal value2 = v2.getNumericValue(BigDecimal.class);
            result = value1.compareTo(value2);
        } else if (v1.isBoolean() && v2.isBoolean()) {
            Boolean value1 = new Boolean(v1.getBooleanValue());
            Boolean value2 = new Boolean(v2.getBooleanValue());
            result = value1.compareTo(value2);
        } else if (v1.isString() && v2.isString()) {
            String value1 = v1.getStringValue();
            String value2 = v2.getStringValue();
            result = value1.compareTo(value2);
        } else {
            throw new SparksoniqRuntimeException(v1.serialize() + " " + v2.serialize());
        }
        return result;
    }
}
