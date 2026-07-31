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

package org.rumbledb.runtime.flwor.udfs;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.api.java.UDF1;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn;
import org.rumbledb.runtime.flwor.clauses.OrderByClauseIterator;
import org.rumbledb.runtime.flwor.expression.OrderByClauseAnnotatedChildIterator;
import org.rumbledb.runtime.misc.CollationSupport;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class OrderClauseDetermineTypeUDF implements UDF1<Row, List<String>> {
    @Serial
    private static final long serialVersionUID = 1L;
    private final DataFrameContext dataFrameContext;
    private final List<OrderByClauseAnnotatedChildIterator> expressionsWithIterator;

    private Item nextItem;
    private final List<String> result;

    public OrderClauseDetermineTypeUDF(
            List<OrderByClauseAnnotatedChildIterator> expressionsWithIterator,
            DynamicContext context,
            List<FlworDataFrameColumn> columns
    ) {
        this.dataFrameContext = new DataFrameContext(context, columns);
        this.expressionsWithIterator = expressionsWithIterator;

        this.result = new ArrayList<>();
    }

    @Override
    public List<String> call(Row row) {
        this.dataFrameContext.setFromRow(row);

        this.result.clear();
        for (OrderByClauseAnnotatedChildIterator expressionWithIterator : this.expressionsWithIterator) {
            populateFromExpression(expressionWithIterator);
        }
        return this.result;
    }

    private void populateFromExpression(OrderByClauseAnnotatedChildIterator expressionWithIterator) {
        RuntimePlan<Item> iterator = expressionWithIterator.getIterator();
        try {
            // apply expression in the dynamic context
            this.nextItem = iterator.materializeAtMostOne(this.dataFrameContext.getContext());
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "Can not order by variables with sequences of multiple items.",
                    expressionWithIterator.getIterator().getRuntimeStaticContext().getMetadata()
            );
        }

        if (this.nextItem == null) {
            this.result.add(OrderByClauseIterator.StringFlagForEmptySequence);
            return;
        }
        List<Item> atomized = this.nextItem.atomizedValue();
        if (atomized.size() > 1) {
            throw new UnexpectedTypeException(
                    "Order by variable must atomize to at most one item.",
                    expressionWithIterator.getIterator().getRuntimeStaticContext().getMetadata()
            );
        }
        if (atomized.isEmpty()) {
            this.result.add(OrderByClauseIterator.StringFlagForEmptySequence);
            return;
        }
        this.nextItem = atomized.get(0);
        if (!this.nextItem.isAtomic()) {
            throw new UnexpectedTypeException(
                    "Order by variable must atomize to an atomic value.",
                    expressionWithIterator.getIterator().getRuntimeStaticContext().getMetadata()
            );
        }
        this.nextItem = OrderByClauseIterator.normalizeOrderKeyAtomic(
            this.nextItem,
            CollationSupport.resolveCollation(
                expressionWithIterator.getUri(),
                expressionWithIterator.getIterator().getRuntimeStaticContext()
            ),
            expressionWithIterator.getIterator().getRuntimeStaticContext().getMetadata()
        );
        this.result.add(this.nextItem.getDynamicType().getName().getLocalName());
    }
}
