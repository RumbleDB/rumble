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

package org.rumbledb.runtime.postfix;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidSelectorException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.primary.ContextExpressionIterator;

import sparksoniq.spark.SparkSessionManager;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

public class ObjectLookupIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private Item lookupKey;
    private boolean contextLookup;
    private Item nextResult;

    public ObjectLookupIterator(
            RuntimeIterator object,
            RuntimeIterator lookupIterator,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Arrays.asList(object, lookupIterator), executionMode, iteratorMetadata);
        this.iterator = object;
    }

    private void initLookupKey() {

        RuntimeIterator lookupIterator = this.children.get(1);

        this.contextLookup = lookupIterator instanceof ContextExpressionIterator;

        if (!this.contextLookup) {

            try {
                this.lookupKey = lookupIterator.materializeExactlyOneItem(this.currentDynamicContextForLocalExecution);
            } catch (NoItemException e) {
                throw new InvalidSelectorException(
                        "Invalid Lookup Key; Object lookup can't be performed with no key.",
                        getMetadata()
                );
            } catch (MoreThanOneItemException e) {
                throw new InvalidSelectorException(
                        "Invalid Lookup Key; Object lookup can't be performed with multiple keys.",
                        getMetadata()
                );
            }

            if (this.lookupKey.isNull() || this.lookupKey.isObject() || this.lookupKey.isArray()) {
                throw new UnexpectedTypeException(
                        "Type error; Object selector can't be converted to a string: "
                            + this.lookupKey.serialize(),
                        getMetadata()
                );
            } else {
                // convert to string
                if (this.lookupKey.isBoolean()) {
                    Boolean value = this.lookupKey.getBooleanValue();
                    this.lookupKey = ItemFactory.getInstance().createStringItem(value.toString());
                } else if (this.lookupKey.isDecimal()) {
                    BigDecimal value = this.lookupKey.getDecimalValue();
                    this.lookupKey = ItemFactory.getInstance().createStringItem(value.toString());
                } else if (this.lookupKey.isDouble()) {
                    Double value = this.lookupKey.getDoubleValue();
                    this.lookupKey = ItemFactory.getInstance().createStringItem(value.toString());
                } else if (this.lookupKey.isInt()) {
                    Integer value = this.lookupKey.getIntValue();
                    this.lookupKey = ItemFactory.getInstance().createStringItem(value.toString());
                } else if (this.lookupKey.isInteger()) {
                    BigInteger value = this.lookupKey.getIntegerValue();
                    this.lookupKey = ItemFactory.getInstance().createStringItem(value.toString());
                } else if (this.lookupKey.isString()) {
                    // do nothing
                }
            }
            if (!this.lookupKey.isString()) {
                throw new UnexpectedTypeException(
                        "Non string object lookup for " + this.lookupKey.serialize(),
                        getMetadata()
                );
            }
        }
    }

    @Override
    public void openLocal() {
        initLookupKey();
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected void resetLocal() {
        this.iterator.reset(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        this.iterator.close();
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in Object Lookup", getMetadata());
    }

    public void setNextResult() {
        this.nextResult = null;

        while (this.iterator.hasNext()) {
            Item item = this.iterator.next();
            if (item.isObject()) {
                if (!this.contextLookup) {
                    Item result = item.getItemByKey(this.lookupKey.getStringValue());
                    if (result != null) {
                        this.nextResult = result;
                        break;
                    }
                } else {
                    Item contextItem = this.currentDynamicContextForLocalExecution.getVariableValues()
                        .getLocalVariableValue(
                            Name.CONTEXT_ITEM,
                            getMetadata()
                        )
                        .get(0);
                    this.nextResult = item.getItemByKey(contextItem.getStringValue());
                }
            }
        }

        if (this.nextResult == null) {
            this.hasNext = false;
            this.iterator.close();
        } else {
            this.hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        JavaRDD<Item> childRDD = this.children.get(0).getRDD(dynamicContext);
        initLookupKey();
        String key;
        if (this.contextLookup) {
            // For now this will always be an error. Later on we will pass the dynamic context from the parent iterator.
            key = dynamicContext.getVariableValues()
                .getLocalVariableValue(
                    Name.CONTEXT_ITEM,
                    getMetadata()
                )
                .get(0)
                .getStringValue();
        } else {
            key = this.lookupKey.getStringValue();
        }
        FlatMapFunction<Item, Item> transformation = new ObjectLookupClosure(key);

        return childRDD.flatMap(transformation);
    }

    @Override
    public boolean implementsDataFrames() {
        return true;
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext newContext = this.iterator.generateNativeQuery(nativeClauseContext);
        if(newContext != NativeClauseContext.NoNativeQuery){
            initLookupKey();
            newContext.setSelectPart(newContext.getSelectPart() + "." + this.lookupKey.getStringValue());
        }
        return newContext;
    }

    public Dataset<Row> getDataFrame(DynamicContext context) {
        Dataset<Row> childDataFrame = this.children.get(0).getDataFrame(context);
        initLookupKey();
        String key;
        if (this.contextLookup) {
            // For now this will always be an error. Later on we will pass the dynamic context from the parent iterator.
            key = context.getVariableValues()
                .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata())
                .get(0)
                .getStringValue();
        } else {
            key = this.lookupKey.getStringValue();
        }
        childDataFrame.createOrReplaceTempView("object");
        StructType schema = childDataFrame.schema();
        String[] fieldNames = schema.fieldNames();
        if (Arrays.asList(fieldNames).contains(key)) {
            int i = schema.fieldIndex(key);
            StructField field = schema.fields()[i];
            DataType type = field.dataType();
            if (type instanceof StructType) {
                return childDataFrame.sparkSession().sql(String.format("SELECT `%s`.* FROM object", key));
            } else {
                return childDataFrame.sparkSession()
                    .sql(
                        String.format(
                            "SELECT `%s` AS `%s` FROM object",
                            key,
                            SparkSessionManager.atomicJSONiqItemColumnName
                        )
                    );
            }
        }
        return childDataFrame.sparkSession().sql("SELECT * FROM object WHERE false");
    }
}
