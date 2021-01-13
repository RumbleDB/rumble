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

import org.rumbledb.runtime.flwor.expression.OrderByClauseAnnotatedChildIterator;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class FlworKeyComparator implements Comparator<FlworKey>, Serializable {

    private static final long serialVersionUID = 1L;
    private final List<OrderByClauseAnnotatedChildIterator> expressions;

    public FlworKeyComparator(List<OrderByClauseAnnotatedChildIterator> expressions) {
        this.expressions = expressions;
    }

    @Override
    public int compare(FlworKey key1, FlworKey key2) {
        int result = key1.compareWithFlworKey(key2, this.expressions);

        if (result == 0) {
            return 0;
        }

        // extract the index from result
        // subtract 1 to offset the effect of preventing multiplication w/ 0 in "compareWithFlworKey" method
        int expressionIndex = Math.abs(result) - 1;
        result = (int) Math.signum(result); // sign of the result gives comparison result (1 / -1)

        // Account for descending order
        if (!this.expressions.get(expressionIndex).isAscending()) {
            result *= -1;
        }

        return result;
    }

}
