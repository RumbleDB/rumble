package org.rumbledb.items.structured;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;

import sparksoniq.spark.DataFrameUtils;

public class JSoundDataFrame {
    private Dataset<Row> dataFrame;
    private ItemType itemType;
    
    public JSoundDataFrame(Dataset<Row> dataFrame, ItemType itemType)
    {
        this.dataFrame = dataFrame;
        this.itemType = itemType;
    }
    
    public JSoundDataFrame(Dataset<Row> dataFrame)
    {
        this.dataFrame = dataFrame;
        this.itemType = ItemTypeFactory.createItemTypeFromSparkStructType(null, this.dataFrame.schema());
    }
    
    public Dataset<Row> getDataFrame()
    {
        return this.dataFrame;
    }
    
    public JavaRDD<Row> javaRDD()
    {
        return this.dataFrame.javaRDD();
    }
    
    public long count()
    {
        return this.dataFrame.count();
    }
    
    public ItemType getItemType()
    {
        return this.itemType;
    }
    
    public List<String> getKeys()
    {
        if (!isSequenceOfObjects()) {
            throw new OurBadException("Cannot get the keys if the sequence is not a sequence of objects.");
        }
        return Arrays.asList(dataFrame.schema().fieldNames());
    }
    
    public void createOrReplaceTempView(String name)
    {
        this.dataFrame.createOrReplaceTempView(name);
    }
    
    public boolean isSequenceOfObjects()
    {
        return DataFrameUtils.isSequenceOfObjects(this.dataFrame);
    }
    
    public JSoundDataFrame evaluateSQL(String sqlQuery, ItemType outputType)
    {
        return new JSoundDataFrame(this.dataFrame.sparkSession().sql(sqlQuery), outputType);
    }
}
