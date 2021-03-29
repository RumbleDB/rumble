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

package org.rumbledb.items;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.runtime.misc.ComparisonIterator;

import java.io.Serializable;
import java.util.Comparator;

public class ItemComparator implements Comparator<Item>, Serializable {

    private static final long serialVersionUID = 1L;

    private RumbleException exception;

    public ItemComparator(RumbleException exception) {
        this.exception = exception;
    }

    /**
     * Comparator used for sequence aggregate functions and their RDD evaluations
     * It compares 2 atomic items (non-null)
     * Non-atomics and nulls throw an exception
     *
     * @return -1 if v1 &lt; v2; 0 if v1 == v2; 1 if v1 &gt; v2;
     */
    public int compare(Item v1, Item v2) {
        try {
            long comparison = ComparisonIterator.compareItems(
                v1,
                v2,
                ComparisonExpression.ComparisonOperator.VC_LT,
                ExceptionMetadata.EMPTY_METADATA
            );
            if (comparison == Long.MIN_VALUE) {

                throw new UnexpectedTypeException(
                        " \""
                            + ComparisonExpression.ComparisonOperator.VC_LT
                            + "\": operation not possible with parameters of type \""
                            + v1.getDynamicType().toString()
                            + "\" and \""
                            + v2.getDynamicType().toString()
                            + "\"",
                        ExceptionMetadata.EMPTY_METADATA
                );
            }
            return (int) comparison;
        } catch (RumbleException e) {
            this.exception.initCause(e);
            throw this.exception;
        }
    }
}
