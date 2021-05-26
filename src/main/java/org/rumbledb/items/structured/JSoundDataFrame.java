package org.rumbledb.items.structured;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.types.ItemType;

public class JSoundDataFrame {
    private Dataset<Row> dataFrame;
    private ItemType itemType;
    
    public JSoundDataFrame(Dataset<Row> dataFrame, ItemType itemType)
    {
        this.dataFrame = dataFrame;
        this.itemType = itemType;
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
}
