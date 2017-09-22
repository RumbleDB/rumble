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
import sparksoniq.spark.iterator.flowr.expression.OrderByClauseSparkIteratorExpression;
import sparksoniq.spark.tuple.FlworKey;

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
        if(result.getIndex() != -1){
            //handle empty items
            if(hasEmpty(key1, key2, result)) {
                return handleEmptyItem(key1, key2, result);
            }
            return result.getResult() * getSortOrder(result.getIndex());
        }
        else
            return result.getResult();
    }

    private int handleEmptyItem(FlworKey key1, FlworKey key2, FlworKey.ResultIndexKeyTuple result) {
        int emptyResult;
        if(key1.getKeyItems().get(result.getIndex()) == null)
            emptyResult = -1;
        else
            emptyResult = 1;
        if(_expressions.get(result.getIndex()).getEmptyOrder() == OrderByClauseExpr.EMPTY_ORDER.FIRST)
            emptyResult *= -1;
        return emptyResult;
    }

    private boolean hasEmpty(FlworKey key1, FlworKey key2, FlworKey.ResultIndexKeyTuple result) {
        if(_expressions.get(result.getIndex()).getEmptyOrder() != OrderByClauseExpr.EMPTY_ORDER.NONE)
            if(key1.getKeyItems().get(result.getIndex()) == null || key2.getKeyItems().get(result.getIndex()) == null)
                return true;
        return false;
    }

    private int getSortOrder(int index){
       return  (_expressions.get(index).isAscending() ? 1 : -1);
    }
}
