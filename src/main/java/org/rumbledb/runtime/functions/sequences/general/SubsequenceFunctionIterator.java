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

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;

import sparksoniq.spark.SparkSessionManager;

import java.util.List;

public class SubsequenceFunctionIterator extends HybridRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator sequenceIterator;
    private RuntimeIterator positionIterator;
    private RuntimeIterator lengthIterator;
    private Item nextResult;
    private int startPosition;
    private int currentLength;
    private int length;

    public SubsequenceFunctionIterator(
            List<RuntimeIterator> parameters,
            RuntimeStaticContext staticContext
    ) {
        super(parameters, staticContext);
        this.sequenceIterator = this.children.get(0);
        this.positionIterator = this.children.get(1);
        if (this.children.size() == 3) {
            this.lengthIterator = this.children.get(2);
        }
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> childRDD = this.sequenceIterator.getRDD(context);
        setInstanceVariables(context);

        if (!childRDD.isEmpty() || this.length == 0) {
            JavaPairRDD<Item, Long> zippedRDD = childRDD.zipWithIndex();
            JavaPairRDD<Item, Long> filteredRDD;
            if (this.length < 0) {
                filteredRDD = zippedRDD.filter((input) -> input._2() >= this.startPosition - 1);
            } else {
                filteredRDD = zippedRDD.filter(
                    (input) -> input._2() >= this.startPosition - 1 && input._2() < this.startPosition - 1 + this.length
                );
            }
            return filteredRDD.map(x -> x._1);
        }
        return SparkSessionManager.getInstance().getJavaSparkContext().emptyRDD();
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        JSoundDataFrame df = this.sequenceIterator.getDataFrame(dynamicContext);
        setInstanceVariables(dynamicContext);

        List<FlworDataFrameColumn> allColumns = df.getColumns();

        String selectSQL = FlworDataFrameUtils.getSQLColumnProjection(allColumns, false);

        String input = FlworDataFrameUtils.createTempView(df.getDataFrame());
        if (this.length != -1) {
            df = df.evaluateSQL(
                String.format(
                    "SELECT * FROM %s LIMIT %s",
                    input,
                    Integer.toString(this.startPosition + this.length - 1)
                ),
                df.getItemType()
            );
        }

        Dataset<Row> ds = FlworDataFrameUtils.zipWithIndex(
            df.getDataFrame(),
            1L,
            SparkSessionManager.temporaryColumnName
        );

        String inputds = FlworDataFrameUtils.createTempView(ds);
        ds = ds.sparkSession()
            .sql(
                String.format(
                    "SELECT %s FROM (SELECT * FROM %s WHERE `%s` >= %s)",
                    selectSQL,
                    inputds,
                    SparkSessionManager.temporaryColumnName,
                    Integer.toString(this.startPosition)
                )
            );
        return new JSoundDataFrame(ds, df.getItemType());
    }

    @Override
    protected void openLocal() {
        setInstanceVariables(this.currentDynamicContextForLocalExecution);
        initializeLocal();
    }

    @Override
    protected void closeLocal() {
        this.sequenceIterator.close();
    }

    @Override
    protected void resetLocal() {
        initializeLocal();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (this.hasNext()) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "subsequence function", getMetadata());
    }

    private void initializeLocal() {
        int currentPosition = 1; // JSONiq indices start from 1

        this.currentLength = this.length;
        // if length is 0, just return empty sequence
        if (this.currentLength == 0) {
            this.hasNext = false;
            return;
        } else {
            if (this.sequenceIterator.isOpen()) {
                this.sequenceIterator.reset(this.currentDynamicContextForLocalExecution);
            } else {
                this.sequenceIterator.open(this.currentDynamicContextForLocalExecution);
            }

            // find the start of the subsequence
            while (this.sequenceIterator.hasNext()) {
                if (currentPosition < this.startPosition) {
                    this.sequenceIterator.next(); // skip item
                } else {
                    this.nextResult = this.sequenceIterator.next();
                    // if length is specified, decrement it
                    if (this.currentLength != -1) {
                        this.currentLength--;
                    }
                    break;
                }
                currentPosition++;
            }
        }

        // if startPosition overshoots, return empty sequence
        if (this.nextResult == null) {
            this.hasNext = false;
        } else {
            this.hasNext = true;
        }
    }

    private void setInstanceVariables(DynamicContext context) {
        Item positionItem = this.positionIterator
            .materializeFirstItemOrNull(context);
        this.startPosition = (int) Math.round(positionItem.getDoubleValue());

        this.length = -1;
        if (this.children.size() == 3) {
            Item lengthItem = this.lengthIterator
                .materializeFirstItemOrNull(context);
            this.length = (int) Math.round(lengthItem.getDoubleValue());
        }
    }

    private void setNextResult() {
        this.nextResult = null;

        if (this.currentLength != 0) {
            if (this.sequenceIterator.hasNext()) {
                if (this.currentLength > 0) { // take length many items -> decrement the value for each item until 0
                    this.nextResult = this.sequenceIterator.next();
                    this.currentLength--;
                } else if (this.currentLength == -1) { // length not specified -> take all items until the end
                    this.nextResult = this.sequenceIterator.next();
                } else {
                    throw new OurBadException(
                            "Unexpected length value found."
                    );
                }
            }
        }

        if (this.nextResult == null) {
            this.hasNext = false;
        } else {
            this.hasNext = true;
        }
    }
}
