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

package org.rumbledb.runtime.functions.sequences.general;

import org.apache.log4j.LogManager;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;

import scala.Tuple2;
import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.List;

public class ReverseFunctionIterator extends HybridRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator sequenceIterator;
    private List<Item> results;
    private int currentIndex = 0;

    public ReverseFunctionIterator(
            List<RuntimeIterator> parameters,
            RuntimeStaticContext staticContext
    ) {
        super(parameters, staticContext);
        this.sequenceIterator = this.children.get(0);
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> childRDD = this.sequenceIterator.getRDD(context);
        JavaPairRDD<Long, Item> zippedRDD = childRDD.zipWithIndex().mapToPair(Tuple2::swap);
        return zippedRDD.sortByKey(false).map(item -> item._2);
    }

    @Override
    public boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        JSoundDataFrame childDataFrame = this.children.get(0).getDataFrame(context);
        String viewName = FlworDataFrameUtils.createTempView(childDataFrame.getDataFrame());
        String selectSQL = childDataFrame.getSQLColumnProjection(false);
        LogManager.getLogger("ReverseFunctioniterator")
            .info(
                String.format(
                    "SELECT %s FROM (SELECT %s, monotonically_increasing_id() as `%s` FROM %s ORDER BY `%s` DESC)",
                    selectSQL,
                    selectSQL,
                    "foo",
                    viewName,
                    "foo"
                )
            );
        String tempName = SparkSessionManager.temporaryColumnName;
        JSoundDataFrame result = childDataFrame.evaluateSQL(
            String.format(
                "SELECT %s FROM (SELECT %s, monotonically_increasing_id() as `%s` FROM %s ORDER BY `%s` DESC)",
                selectSQL,
                selectSQL,
                tempName,
                viewName,
                tempName
            ),
            childDataFrame.getItemType()
        );
        return result;
    }

    @Override
    protected void openLocal() {
        this.results = new ArrayList<>();
        this.currentIndex = 0;

        List<Item> items = this.sequenceIterator.materialize(this.currentDynamicContextForLocalExecution);

        for (int i = items.size() - 1; i >= 0; i--) {
            this.results.add(items.get(i));
        }

        this.hasNext = this.results.size() != 0;
    }

    @Override
    protected void closeLocal() {
    }

    @Override
    protected void resetLocal() {
        this.results = new ArrayList<>();
        this.currentIndex = 0;

        List<Item> items = this.sequenceIterator.materialize(this.currentDynamicContextForLocalExecution);

        for (int i = items.size() - 1; i >= 0; i--) {
            this.results.add(items.get(i));
        }

        this.hasNext = this.results.size() != 0;
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (this.hasNext()) {
            return getResult();
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "reverse function", getMetadata());
    }

    public Item getResult() {
        if (this.results == null || this.results.size() == 0) {
            throw new IteratorFlowException("getResult called on an empty list of results", getMetadata());
        }
        if (this.currentIndex == this.results.size() - 1) {
            this.hasNext = false;
        }
        return this.results.get(this.currentIndex++);
    }
}
