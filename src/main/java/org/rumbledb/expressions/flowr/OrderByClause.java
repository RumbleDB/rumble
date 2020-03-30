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


import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

public class OrderByClause extends Clause {


    private final List<OrderByClauseSortingKey> sortingKeys;
    private final boolean isStable;

    public OrderByClause(List<OrderByClauseSortingKey> exprs, boolean stable, ExceptionMetadata metadata) {
        super(FLWOR_CLAUSES.ORDER_BY, metadata);
        if (exprs == null || exprs.isEmpty()) {
            throw new SemanticException("Group clause must have at least one variable", metadata);
        }
        this.sortingKeys = exprs;
        this.isStable = stable;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        this.sortingKeys.forEach(e -> {
            if (e != null) {
                result.add(e.getExpression());
            }
        });
        return result;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitOrderByClause(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(orderByClause order by ";
        for (OrderByClauseSortingKey var : this.sortingKeys) {
            result += var.getExpression().serializationString(true)
                + (this.sortingKeys.indexOf(var) < this.sortingKeys.size() - 1 ? " , " : "");
        }
        result += ")";
        return result;
    }

    public boolean isStable() {
        return this.isStable;
    }

    public List<OrderByClauseSortingKey> getSortingKeys() {
        return this.sortingKeys;
    }
}
