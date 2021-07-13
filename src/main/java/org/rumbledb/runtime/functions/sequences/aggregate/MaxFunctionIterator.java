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

package org.rumbledb.runtime.functions.sequences.aggregate;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.DefaultCollationException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemComparator;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import sparksoniq.spark.SparkSessionManager;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MaxFunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private Item result;

    public MaxFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
        this.iterator = this.children.get(0);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        ItemComparator comparator = new ItemComparator(
                new InvalidArgumentTypeException(
                        "Max expression input error. Input has to be non-null atomics of matching types",
                        getMetadata()
                )
        );
        if (this.children.size() == 2) {
            String collation = this.children.get(1).materializeFirstItemOrNull(context).getStringValue();
            if (!collation.equals("http://www.w3.org/2005/xpath-functions/collation/codepoint")) {
                throw new DefaultCollationException("Wrong collation parameter", getMetadata());
            }
        }

        if (!this.iterator.isRDDOrDataFrame()) {

            List<Item> results = this.iterator.materialize(context);
            if (results.size() == 0) {
                return null;
            }

            try {
                return Collections.max(results, comparator);
            } catch (RumbleException e) {
                RumbleException ex = new InvalidArgumentTypeException(
                        "Max expression input error. Input has to be non-null atomics of matching types.",
                        getMetadata()
                );
                ex.initCause(e);
                throw ex;
            }
        }

        if (this.iterator.isDataFrame()) {
            JSoundDataFrame df = this.iterator.getDataFrame(context);
            if (df.isEmptySequence()) {
                return null;
            }
            df.createOrReplaceTempView("input");
            JSoundDataFrame maxDF = df.evaluateSQL(
                String.format(
                    "SELECT MAX(`%s`) as `%s` FROM input",
                    SparkSessionManager.atomicJSONiqItemColumnName,
                    SparkSessionManager.atomicJSONiqItemColumnName
                ),
                df.getItemType()
            );
            return maxDF.getExactlyOneItem();
        }

        JavaRDD<Item> rdd = this.iterator.getRDD(context);
        if (rdd.isEmpty()) {
            return null;
        }

        this.result = rdd.max(comparator);
        return this.result;

    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        if (this.children.get(0) instanceof VariableReferenceIterator) {
            VariableReferenceIterator expr = (VariableReferenceIterator) this.children.get(0);
            Map<Name, DynamicContext.VariableDependency> result =
                new TreeMap<Name, DynamicContext.VariableDependency>();
            result.put(expr.getVariableName(), DynamicContext.VariableDependency.MAX);
            return result;
        } else {
            return super.getVariableDependencies();
        }
    }
}
