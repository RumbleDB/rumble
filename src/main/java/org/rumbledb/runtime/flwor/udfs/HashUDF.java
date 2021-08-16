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
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.RuntimeIterator;
import java.util.List;

public class HashUDF implements UDF1<Row, Long> {

    private static final long serialVersionUID = 1L;

    private DataFrameContext dataFrameContext;
    private RuntimeIterator expression;

    public HashUDF(
            RuntimeIterator expression,
            DynamicContext context,
            StructType schema,
            List<String> columnNames
    ) {
        this.dataFrameContext = new DataFrameContext(context, schema, columnNames);
        this.expression = expression;
    }

    @Override
    public Long call(Row row) {
        this.dataFrameContext.setFromRow(row);

        Item item = null;
        try {
            item = this.expression.materializeAtMostOneItemOrNull(this.dataFrameContext.getContext());
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "Invalid args. Value comparison can't be performed on sequences with more than 1 items",
                    this.expression.getMetadata()
            );
        }
        long hashCode = 0;
        if (item != null) {
            hashCode = item.hashCode();
        }
        return Long.valueOf(hashCode);
    }
}
