/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package sparksoniq.jsoniq.runtime.iterator;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.SparkRuntimeException;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public abstract class LocalRuntimeIterator extends RuntimeIterator {

    private static final long serialVersionUID = 1L;

    protected LocalRuntimeIterator(List<RuntimeIterator> children, IteratorMetadata iteratorMetadata) {
        super(children, iteratorMetadata);
    }

    @Override
    public JavaRDD<Item> getRDD(DynamicContext dynamicContext) {
        throw new SparkRuntimeException("Iterator has no RDDs", getMetadata());
    }

    @Override
    public boolean isRDD() {
        return false;
    }

    @Override
    public boolean isDataFrame() {
        return false;
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext dynamicContext) {
        throw new SparkRuntimeException("Iterator has no DataFrames", getMetadata());
    }
}
