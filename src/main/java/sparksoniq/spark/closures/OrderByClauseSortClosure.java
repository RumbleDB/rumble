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
package sparksoniq.spark.closures;

import sparksoniq.jsoniq.compiler.translator.expr.flowr.OrderByClauseExpr;
import sparksoniq.jsoniq.tuple.FlworKey;
import sparksoniq.spark.iterator.flowr.expression.OrderByClauseSparkIteratorExpression;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class OrderByClauseSortClosure implements Comparator<FlworKey>, Serializable {
    private final boolean _isStable;
    private final List<OrderByClauseSparkIteratorExpression> _expressions;

    public OrderByClauseSortClosure(List<OrderByClauseSparkIteratorExpression> expressions, boolean isStable) {
        this._isStable = isStable;
        this._expressions = expressions;
    }

    @Override
    public int compare(FlworKey key1, FlworKey key2) {

        FlworKey.ResultIndexKeyTuple result = key1.compareWithFlworKey(key2);
        // if index is -1, two keys are fully equal (result is 0)
        if (result.getIndex() == -1) {
            return result.getResult();
        } else {
            int resultValue = result.getResult();

            //handle empty items
            if (hasEmpty(key1, key2, result)) {
                resultValue = handleEmptyItem(key1, result);
            }
            return resultValue * getSortOrder(result.getIndex());
        }
    }

    private boolean hasEmpty(FlworKey key1, FlworKey key2, FlworKey.ResultIndexKeyTuple result) {
        int expressionIndex = result.getIndex();

        // handle empty items specially, only if an empty item ordering is specified
        if (_expressions.get(expressionIndex).getEmptyOrder() != OrderByClauseExpr.EMPTY_ORDER.NONE) {

            // if expressionIndex is equal to or larger than the size, an empty ordering expression result is found
            if ((expressionIndex >= key1.getKeyItems().size()) || expressionIndex >= key2.getKeyItems().size()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Default value for empty field is smaller (least, -1, EMPTY_ORDER.FIRST). Can be specified for EMPTY_ORDER parameters
     * @param key1   First FlworKey in comparison
     * @param result contains (result, index) tuple.
     * @return
     */
    private int handleEmptyItem(FlworKey key1, FlworKey.ResultIndexKeyTuple result) {
        int emptyResult;

        int expressionIndex = result.getIndex();
        int expressionCount = key1.getKeyItems().size();
        // an empty item exists in the 1st FlworKey (index of the expression exceeds the size of key1 -> hence, empty item)
        if (expressionIndex >= expressionCount) {
            emptyResult = -1;
        }
        else {  // empty item exists in the 2nd FlworKey
            emptyResult = 1;
        }

        // if empty_items are specified to have the greater value (greatest, 1, EMPTY_ORDER.LAST).
        if (_expressions.get(expressionIndex).getEmptyOrder() == OrderByClauseExpr.EMPTY_ORDER.LAST) {
            emptyResult *= -1;
        }
        return emptyResult;
    }



    private int getSortOrder(int index) {
        return (_expressions.get(index).isAscending() ? 1 : -1);
    }
}
