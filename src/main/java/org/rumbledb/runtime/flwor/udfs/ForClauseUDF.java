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
 * Authors: Stefan Irimescu, Can Berker Cikis, Ghislain Fourny
 *
 */

package org.rumbledb.runtime.flwor.udfs;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.api.java.UDF1;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class ForClauseUDF implements UDF1<Row, List<byte[]>> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final DataFrameContext dataFrameContext;
    private final ItemRuntimePlan expression;

    private List<Item> nextResult;
    private List<byte[]> results;

    public ForClauseUDF(
            ItemRuntimePlan expression,
            DynamicContext context,
            List<FlworDataFrameColumn> columnNames
    ) {
        this.dataFrameContext = new DataFrameContext(context, columnNames);
        this.expression = expression;
        if (this.expression.isSparkJobNeeded()) {
            throw new JobWithinAJobException(
                    "The expression in this clause requires parallel execution, but is itself executed in parallel. Please consider moving it up or unnest it if it is independent on previous FLWOR variables.",
                    this.expression.getRuntimeStaticContext().getMetadata()
            );
        }

        this.nextResult = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    @Override
    public List<byte[]> call(Row row) {
        this.dataFrameContext.setFromRow(row);

        this.results.clear();
        // apply expression in the dynamic context
        for (Item nextItem : this.expression.materialize(this.dataFrameContext.getContext())) {
            this.nextResult.clear();
            this.nextResult.add(nextItem);
            this.results.add(
                FlworDataFrameUtils.serializeItemList(
                    this.nextResult,
                    this.dataFrameContext.getKryo(),
                    this.dataFrameContext.getOutput()
                )
            );
        }

        return this.results;
    }
}
