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

package org.rumbledb.items.parsing;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.types.ItemType;

public class RowToItemMapper implements Function<Row, Item> {

    private static final long serialVersionUID = 1L;
    private final ExceptionMetadata metadata;
    private final ItemType itemType;

    public RowToItemMapper(ExceptionMetadata metadata, ItemType itemType) {
        this.metadata = metadata;
        this.itemType = itemType;
    }

    public RowToItemMapper(ExceptionMetadata metadata) {
        this.metadata = metadata;
        this.itemType = null;
    }

    @Override
    public Item call(Row row) throws Exception {
        Item result = ItemParser.getItemFromRow(row, this.metadata, this.itemType);
        // if (this.itemType != null) {
        // result.setMutabilityLevel(this.itemType.getMutabilityLevel());
        // }
        return result;
    }
}
