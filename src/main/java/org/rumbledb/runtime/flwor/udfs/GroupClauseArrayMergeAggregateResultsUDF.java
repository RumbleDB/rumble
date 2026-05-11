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

import scala.collection.immutable.ArraySeq;
import scala.collection.Iterator;

import java.util.ArrayList;
// import java.util.Iterator;
import java.util.List;

public class GroupClauseArrayMergeAggregateResultsUDF implements UDF1<ArraySeq<Object>, Object[]> {


    private static final long serialVersionUID = 1L;
    private List<Item> nextResult;
    private List<List<Item>> deserializedParams;

    public GroupClauseArrayMergeAggregateResultsUDF() {
        this.nextResult = new ArrayList<>();
        this.deserializedParams = new ArrayList<>();
    }

    @Override
    public Object[] call(ArraySeq<Object> wrappedParameters) {
        this.nextResult.clear();
        this.deserializedParams.clear();
        List<Object> result = new ArrayList<Object>();
        Iterator<Object> iterator = wrappedParameters.iterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o instanceof Row) {
                Row row = (Row) o;
                result.add(row);
            }
            if (o instanceof ArraySeq) {
                @SuppressWarnings("unchecked")
                ArraySeq<Object> arraySeq = (ArraySeq<Object>) o;
                Iterator<Object> iterator2 = arraySeq.iterator();
                while (iterator2.hasNext())
                    result.add(iterator2.next());
            } else {
                throw new OurBadException("We cannot process " + o.getClass().getCanonicalName());
            }
        }
        Object[] results = new Object[result.size()];
        result.toArray(results);
        return results;
    }
}
