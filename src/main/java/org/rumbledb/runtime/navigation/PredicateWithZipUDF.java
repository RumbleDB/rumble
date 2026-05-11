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

package org.rumbledb.runtime.navigation;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.api.java.UDF1;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.ItemType;

import scala.Option;
import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.List;

public class PredicateWithZipUDF implements UDF1<Row, Boolean> {
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator expression;
    private final DynamicContext dynamicContext;
    private final ExceptionMetadata metadata;
    private final ItemType itemType;
    private final long contextSize;
    List<Item> currentItems = new ArrayList<>();

    public PredicateWithZipUDF(
            RuntimeIterator expression,
            DynamicContext context,
            ExceptionMetadata metadata,
            ItemType itemType,
            long contextSize
    ) {
        this.expression = expression;
        this.dynamicContext = new DynamicContext(context);
        this.metadata = metadata;
        this.itemType = itemType;
        this.contextSize = contextSize;
    }

    @Override
    public Boolean call(Row row) {
        this.dynamicContext.getVariableValues().removeAllVariables();
        this.currentItems.clear();
        Item item = ItemParser.getItemFromRow(row, this.metadata, this.itemType);
        this.currentItems.add(item);
        this.dynamicContext.getVariableValues().addVariableValue(Name.CONTEXT_ITEM, this.currentItems);
        Option<Object> opt = row.schema().getFieldIndex(SparkSessionManager.countColumnName);
        if (opt.isEmpty()) {
            throw new OurBadException("No count column for a zipped predicate on a dataframe.");
        }
        this.dynamicContext.getVariableValues().setPosition(row.getLong((int) opt.get()));
        this.dynamicContext.getVariableValues().setLast(this.contextSize);

        boolean result = this.expression.getEffectiveBooleanValueOrCheckPosition(
            this.dynamicContext,
            this.dynamicContext.getVariableValues().getPosition()
        );

        return result;
    }
}
