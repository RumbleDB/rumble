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
 package sparksoniq.jsoniq.runtime.iterator;

import sparksoniq.exceptions.SparkRuntimeException;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.BooleanItem;
import sparksoniq.jsoniq.item.DecimalItem;
import sparksoniq.jsoniq.item.DoubleItem;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.NullItem;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.validation.Schema;

public abstract class DataFramesRDDOrLocalRuntimeIterator extends HybridRuntimeIterator {

    protected DataFramesRDDOrLocalRuntimeIterator(List<RuntimeIterator> children, IteratorMetadata iteratorMetadata) {
        super(children, iteratorMetadata);
    }

    @Override
    public boolean isDataFrame()
    {
        if(!isDataFrameInitialized )
        {
          _isDataFrame = initIsDataFrame();
          isDataFrameInitialized = true;
        }
        return _isDataFrame;
    }
    
    @Override
    public JavaRDD<Item> getRDD(DynamicContext context)
    {
        if(isDataFrame())
        {
            JavaRDD<Row> rows = getDataFrame(context).javaRDD();
            return rows.map(r -> rowObjectToItem(r));
        }
        else
        {
            return getRDDNoDataFrame(context);
        }
    }
    
    private Item rowObjectToItem(Object attribute) throws SparkRuntimeException
    {
        if(attribute instanceof Row)
        {
            Row row = (Row)attribute;
            ObjectItem object = new ObjectItem(ItemMetadata.fromIteratorMetadata(getMetadata()));
            StructType type = row.schema();
            for(String field : type.fieldNames())
            {
                int i = type.fieldIndex(field);
                Object obj = row.get(i);
                object.putItemByKey(field, rowObjectToItem(obj));
            }
            return object;
        }
        else if(attribute instanceof List)
        {
            List<Object> row = (List<Object>)attribute;
            List<Item> list = new ArrayList<Item>();
            for(Object o : row)
            {
                list.add(rowObjectToItem(o));
            }
            return new ArrayItem(list, ItemMetadata.fromIteratorMetadata(getMetadata()));
        }
        else if(attribute instanceof Boolean)
        {
            Boolean b = (Boolean)attribute;
            return new BooleanItem(b, ItemMetadata.fromIteratorMetadata(getMetadata()));
        }
        else if(attribute instanceof Byte)
        {
            Byte b = (Byte)attribute;
            return new IntegerItem(b, ItemMetadata.fromIteratorMetadata(getMetadata()));
        }
        else if(attribute instanceof Short)
        {
            Short b = (Short)attribute;
            return new IntegerItem(b, ItemMetadata.fromIteratorMetadata(getMetadata()));
        }
        else if(attribute instanceof Integer)
        {
            Integer b = (Integer)attribute;
            return new IntegerItem(b, ItemMetadata.fromIteratorMetadata(getMetadata()));
        }
        else if(attribute instanceof Float)
        {
            Float b = (Float)attribute;
            return new DoubleItem(b, ItemMetadata.fromIteratorMetadata(getMetadata()));
        }
        else if(attribute instanceof Double)
        {
            Double b = (Double)attribute;
            return new DoubleItem(b, ItemMetadata.fromIteratorMetadata(getMetadata()));
        }
        else if(attribute instanceof String)
        {
            String b = (String)attribute;
            return new StringItem(b, ItemMetadata.fromIteratorMetadata(getMetadata()));
        }
        else if(attribute instanceof BigDecimal)
        {
            BigDecimal b = (BigDecimal)attribute;
            return new DecimalItem(b, ItemMetadata.fromIteratorMetadata(getMetadata()));
        }
        else if(attribute == null)
        {
            return new NullItem(ItemMetadata.fromIteratorMetadata(getMetadata()));
        }
        throw new SparkRuntimeException("Conversion from Data Frames to Collection of Items failed " + attribute.getClass(), getMetadata());
    }
    
    protected abstract boolean initIsDataFrame();
    protected abstract JavaRDD<Item> getRDDNoDataFrame(DynamicContext context);

    protected boolean isDataFrameInitialized = false;
    protected boolean _isDataFrame;
}
