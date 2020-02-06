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

package sparksoniq.spark.iterator.flowr.expression;

import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;

import java.io.Serializable;

import org.rumbledb.expressions.flowr.OrderByClauseExpr;

public class OrderByClauseAnnotatedChildIterator implements Serializable {

    private static final long serialVersionUID = 1L;
    private final RuntimeIterator _iterator;
    private final boolean _ascending;
    private final String _uri;
    private final OrderByClauseExpr.EMPTY_ORDER _emptyOrder;


    public OrderByClauseAnnotatedChildIterator(
            RuntimeIterator iterator,
            boolean ascending,
            String uri,
            OrderByClauseExpr.EMPTY_ORDER empty_order
    ) {
        this._iterator = iterator;
        this._ascending = ascending;
        this._uri = uri;
        this._emptyOrder = empty_order;
    }

    public RuntimeIterator getIterator() {
        return _iterator;
    }

    public boolean isAscending() {
        return _ascending;
    }

    public String getUri() {
        return _uri;
    }

    public OrderByClauseExpr.EMPTY_ORDER getEmptyOrder() {
        return _emptyOrder;
    }

}
