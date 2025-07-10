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
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExpressionEvaluationUDF implements UDF1<Row, List<byte[]>> {

    private static final long serialVersionUID = 1L;

    private DataFrameContext dataFrameContext;
    private RuntimeIterator expression;

    private transient List<byte[]> results;

    public ExpressionEvaluationUDF(
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
    public List<byte[]> call(Row row) {
        this.dataFrameContext.setFromRow(row);

        this.results.clear();
        // apply expression in the dynamic context
        this.expression.open(this.dataFrameContext.getContext());
        while (this.expression.hasNext()) {
            Item nextItem = this.expression.next();
            this.results.add(
                FlworDataFrameUtils.serializeItem(
                    nextItem,
                    this.dataFrameContext.getKryo(),
                    this.dataFrameContext.getOutput()
                )
            );
        }
        this.expression.close();

        return this.results;
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException,
                ClassNotFoundException {
        in.defaultReadObject();
        this.results = new ArrayList<>();
    }
}
