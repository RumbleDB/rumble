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
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.ItemType;

import java.util.ArrayList;
import java.util.List;

public class PredicateUDF implements UDF1<Row, Boolean> {
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator expression;
    private final DynamicContext dynamicContext;
    private final ExceptionMetadata metadata;
    private final ItemType itemType;
    List<Item> currentItems = new ArrayList<>();

    public PredicateUDF(
            RuntimeIterator expression,
            DynamicContext context,
            ExceptionMetadata metadata,
            ItemType itemType
    ) {
        this.expression = expression;
        this.dynamicContext = new DynamicContext(context);
        this.metadata = metadata;
        this.itemType = itemType;
    }

    @Override
    public Boolean call(Row row) {
        this.dynamicContext.getVariableValues().removeAllVariables();
        this.currentItems.clear();
        Item item = ItemParser.getItemFromRow(row, this.metadata, this.itemType);
        this.currentItems.add(item);
        this.dynamicContext.getVariableValues().addVariableValue(Name.CONTEXT_ITEM, this.currentItems);

        boolean result = this.expression.getEffectiveBooleanValue(this.dynamicContext);
        return result;
    }
}
