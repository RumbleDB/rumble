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

package org.rumbledb.runtime.flwor.closures;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn;
import org.rumbledb.runtime.flwor.udfs.DataFrameContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReturnFlatMapClosure implements FlatMapFunction<Row, Item> {

    private static final long serialVersionUID = 1L;
    private DataFrameContext dataFrameContext;
    private RuntimeIterator expression;

    List<Item> results;

    public ReturnFlatMapClosure(
            RuntimeIterator expression,
            DynamicContext context,
            List<FlworDataFrameColumn> columns
    ) {
        this.dataFrameContext = new DataFrameContext(context, columns);
        this.expression = expression;
        if (this.expression.isSparkJobNeeded()) {
            throw new JobWithinAJobException(
                    "The expression in this clause requires parallel execution, but is itself executed in parallel. Please consider moving it up or unnest it if it is independent on previous FLWOR variables.",
                    this.expression.getMetadata()
            );
        }
        this.results = new ArrayList<>();
    }

    @Override
    public Iterator<Item> call(Row row) {
        this.dataFrameContext.setFromRow(row);
        this.expression.materialize(this.dataFrameContext.getContext(), this.results);
        return this.results.iterator();
    }
}
