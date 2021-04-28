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
import org.rumbledb.exceptions.OurBadException;

import scala.collection.mutable.WrappedArray;

import java.util.ArrayList;
import java.util.List;

public class GroupClauseArrayMergeAggregateResultsUDF implements UDF1<WrappedArray<Object>, Object[]> {


    private static final long serialVersionUID = 1L;
    private List<Item> nextResult;
    private List<List<Item>> deserializedParams;

    public GroupClauseArrayMergeAggregateResultsUDF() {
        this.nextResult = new ArrayList<>();
        this.deserializedParams = new ArrayList<>();
    }

    @Override
    public Object[] call(WrappedArray<Object> wrappedParameters) {
        this.nextResult.clear();
        this.deserializedParams.clear();
        List<Object> result = new ArrayList<Object>();
        Object[] insideArrays = (Object[]) wrappedParameters.array();
        for (Object o : insideArrays) {
            if (o instanceof Row) {
                Row row = (Row) o;
                result.add(row);
            }
            if (o instanceof WrappedArray) {
                @SuppressWarnings("rawtypes")
                WrappedArray wrappedArray = (WrappedArray) o;
                Object[] insideArrays2 = (Object[]) wrappedArray.array();
                for (Object p : insideArrays2)
                    result.add(p);
            } else {
                throw new OurBadException("We cannot process " + o.getClass().getCanonicalName());
            }
        }
        Object[] results = new Object[result.size()];
        result.toArray(results);
        return results;
    }
}
