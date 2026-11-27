/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.flwor.udfs;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.api.java.UDF1;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn;
import org.rumbledb.runtime.flwor.clauses.OrderByClauseIterator;
import org.rumbledb.runtime.flwor.expression.OrderByClauseAnnotatedChildIterator;

public class OrderClauseDetermineTypeUDF implements UDF1<Row, List<String>> {
    @Serial
    private static final long serialVersionUID = 1L;

    private final DataFrameContext dataFrameContext;
    private final List<OrderByClauseAnnotatedChildIterator> expressionsWithIterator;

    private final List<String> result;

    public OrderClauseDetermineTypeUDF(
            List<OrderByClauseAnnotatedChildIterator> expressionsWithIterator,
            DynamicContext context,
            List<FlworDataFrameColumn> columns) {
        this.dataFrameContext = new DataFrameContext(context, columns);
        this.expressionsWithIterator = expressionsWithIterator;

        this.result = new ArrayList<>();
    }

    @Override
    public List<String> call(Row row) {
        this.dataFrameContext.setFromRow(row);

        this.result.clear();
        for (OrderByClauseAnnotatedChildIterator expression : this.expressionsWithIterator) {
            Item key = OrderByClauseIterator.evaluateOrderKey(expression, this.dataFrameContext.getContext());
            this.result.add(key == null
                    ? OrderByClauseIterator.StringFlagForEmptySequence
                    : key.getDynamicType().getName().getLocalName());
        }
        return this.result;
    }
}
