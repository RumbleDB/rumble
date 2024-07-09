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

import org.apache.spark.sql.api.java.UDF1;
import org.rumbledb.api.Item;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import scala.collection.mutable.ArraySeq;

import java.util.ArrayList;
import java.util.List;

public class GroupClauseSerializeAggregateResultsUDF implements UDF1<ArraySeq<byte[]>, byte[]> {


    private static final long serialVersionUID = 1L;
    private List<Item> nextResult;
    private List<List<Item>> deserializedParams;
    private DataFrameContext dataFrameContext;

    public GroupClauseSerializeAggregateResultsUDF() {
        this.nextResult = new ArrayList<>();
        this.deserializedParams = new ArrayList<>();
        this.dataFrameContext = new DataFrameContext();
    }

    @Override
    public byte[] call(ArraySeq<byte[]> wrappedParameters) {
        this.nextResult.clear();
        this.deserializedParams.clear();
        FlworDataFrameUtils.deserializeWrappedParameters(
            wrappedParameters,
            this.deserializedParams,
            this.dataFrameContext.getKryo(),
            this.dataFrameContext.getInput()
        );

        for (List<Item> deserializedParam : this.deserializedParams) {
            this.nextResult.addAll(deserializedParam);
        }
        return FlworDataFrameUtils.serializeItemList(
            this.nextResult,
            this.dataFrameContext.getKryo(),
            this.dataFrameContext.getOutput()
        );
    }
}
