/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
package sparksoniq.spark.closures;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ForClauseDeserializeClosure implements MapFunction<Row, Row> {

    public ForClauseDeserializeClosure() {
    }

    /**
     * @param row
     * @return List<Item>iterator, deserialized from parametrized Row object containing the List<Item>
     */
    @Override
    public Row call(Row row) {
        List<Object> deserializedRow = ClosureUtils.deserializeEntireRow(row);
        List<List<Item>> allColumns = new ArrayList<>();
        for (Object columnObject:deserializedRow) {
            List<Item> column = (List<Item>) columnObject;
            allColumns.add(column);
        }
        return RowFactory.create(allColumns);
    }
}
