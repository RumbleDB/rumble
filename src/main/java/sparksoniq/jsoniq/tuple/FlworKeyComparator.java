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

import org.rumbledb.expressions.flowr.OrderByClauseSortingKey;
import org.rumbledb.runtime.flwor.expression.OrderByClauseAnnotatedChildIterator;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class FlworKeyComparator implements Comparator<FlworKey>, Serializable {

    private static final long serialVersionUID = 1L;
    // private final boolean isStable;
    private final List<OrderByClauseAnnotatedChildIterator> expressions;

    public FlworKeyComparator(List<OrderByClauseAnnotatedChildIterator> expressions, boolean isStable) {
        // this._isStable = isStable;
        this.expressions = expressions;
    }

    @Override
    public int compare(FlworKey key1, FlworKey key2) {

        int result = key1.compareWithFlworKey(key2);
        if (result == 0) {
            return 0;
        } else {
            // extract the index from result
            // subtract 1 to offset the effect of preventing multiplication w/ 0 in "compareWithFlworKey" method
            int expressionIndex = Math.abs(result) - 1;
            result = (int) Math.signum(result); // sign of the result gives comparison result (1 / -1)

            // Java null shows that the ordering expression is empty
            if (key1.getKeyItems().get(expressionIndex) == null || key2.getKeyItems().get(expressionIndex) == null) {
                // Default behavior(NONE) for empty ordering expressions is equal to FIRST(empty least)
                // if LAST is given, empty ordering expressions are the greatest (reverse the comparison)
                if (this.expressions.get(expressionIndex).getEmptyOrder() == OrderByClauseSortingKey.EMPTY_ORDER.LAST) {
                    result *= -1;
                }
            }

            // account for sorting order
            result *= getSortOrder(expressionIndex);

            return result;
        }
    }

    private int getSortOrder(int index) {
        return (this.expressions.get(index).isAscending() ? 1 : -1);
    }
}
