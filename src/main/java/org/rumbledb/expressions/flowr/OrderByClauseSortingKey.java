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

package org.rumbledb.expressions.flowr;

import org.rumbledb.expressions.Expression;

public class OrderByClauseSortingKey {
    private final Expression expression;
    private final boolean ascending;
    private final EMPTY_ORDER emptyOrder;
    private final String collationURI;

    public OrderByClauseSortingKey(
            Expression expression,
            boolean ascending,
            String collationURI,
            EMPTY_ORDER empty_order
    ) {
        super();
        this.expression = expression;
        this.ascending = ascending;
        this.collationURI = collationURI;
        this.emptyOrder = empty_order;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public boolean isAscending() {
        return this.ascending;
    }

    public EMPTY_ORDER getEmptyOrder() {
        return this.emptyOrder;
    }

    public String getUri() {
        return this.collationURI;
    }

    public enum EMPTY_ORDER {
        LEAST,
        GREATEST,
        NONE
    }
}
