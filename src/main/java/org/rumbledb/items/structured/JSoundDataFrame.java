package org.rumbledb.items.structured;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;

import sparksoniq.spark.DataFrameUtils;
import sparksoniq.spark.SparkSessionManager;

public class JSoundDataFrame {
    private Dataset<Row> dataFrame;
    private ItemType itemType;

    public JSoundDataFrame(Dataset<Row> dataFrame, ItemType itemType) {
        this.dataFrame = dataFrame;
        this.itemType = itemType;
    }

    public JSoundDataFrame(Dataset<Row> dataFrame) {
        this.dataFrame = dataFrame;
        this.itemType = ItemTypeFactory.createItemTypeFromSparkStructType(null, this.dataFrame.schema());
    }

    public static JSoundDataFrame emptyDataFrame() {
        return new JSoundDataFrame(
                SparkSessionManager.getInstance().getOrCreateSession().emptyDataFrame(),
                BuiltinTypesCatalogue.item
        );
    }

    public Dataset<Row> getDataFrame() {
        return this.dataFrame;
    }

    public JavaRDD<Row> javaRDD() {
        return this.dataFrame.javaRDD();
    }

    public long count() {
        return this.dataFrame.count();
    }

    public ItemType getItemType() {
        return this.itemType;
    }

    public List<String> getKeys() {
        if (!isSequenceOfObjects()) {
            throw new OurBadException("Cannot get the keys if the sequence is not a sequence of objects.");
        }
        return Arrays.asList(dataFrame.schema().fieldNames());
    }

    public void createOrReplaceTempView(String name) {
        this.dataFrame.createOrReplaceTempView(name);
    }

    public boolean isSequenceOfObjects() {
        return DataFrameUtils.isSequenceOfObjects(this.dataFrame);
    }

    public boolean isSequenceOfArrays() {
        StructType schema = this.dataFrame.schema();
        String[] fieldNames = schema.fieldNames();
        if (
            fieldNames.length == 1 && Arrays.asList(fieldNames).contains(SparkSessionManager.atomicJSONiqItemColumnName)
        ) {
            int i = schema.fieldIndex(SparkSessionManager.atomicJSONiqItemColumnName);
            StructField field = schema.fields()[i];
            DataType type = field.dataType();
            if (type instanceof ArrayType) {
                if (!this.itemType.isSubtypeOf(BuiltinTypesCatalogue.arrayItem)) {
                    throw new OurBadException(
                            "Inconsistency in internal representation: " + this.itemType + " is not an array type."
                    );
                }
                return true;
            }
            if (type instanceof StructType) {
                throw new OurBadException("Inconsistency in internal representation: expecting atomic or array.");
            }
            if (!this.itemType.isSubtypeOf(BuiltinTypesCatalogue.atomicItem)) {
                throw new OurBadException(
                        "Inconsistency in internal representation: " + this.itemType + " is not an atomic type."
                );
            }
            return false;
        }
        if (this.itemType.isSubtypeOf(BuiltinTypesCatalogue.arrayItem)) {
            throw new OurBadException(
                    "Inconsistency in internal representation: " + this.itemType + " is an array type."
            );
        }
        return false;
    }

    public boolean isSequenceOfAtomics() {
        StructType schema = this.dataFrame.schema();
        String[] fieldNames = schema.fieldNames();
        if (
            fieldNames.length == 1 && Arrays.asList(fieldNames).contains(SparkSessionManager.atomicJSONiqItemColumnName)
        ) {
            int i = schema.fieldIndex(SparkSessionManager.atomicJSONiqItemColumnName);
            StructField field = schema.fields()[i];
            DataType type = field.dataType();
            if (type instanceof ArrayType) {
                return false;
            }
            if (type instanceof StructType) {
                throw new OurBadException("Inconsistency in internal representation: expecting atomic or array.");
            }
            if (!this.itemType.isSubtypeOf(BuiltinTypesCatalogue.atomicItem)) {
                throw new OurBadException(
                        "Inconsistency in internal representation: " + this.itemType + " is not an atomic type."
                );
            }
            return true;
        }
        if (this.itemType.isSubtypeOf(BuiltinTypesCatalogue.atomicItem)) {
            throw new OurBadException(
                    "Inconsistency in internal representation: " + this.itemType + " is an atomic type."
            );
        }
        return false;
    }

    public JSoundDataFrame evaluateSQL(String sqlQuery, ItemType outputType) {
        return new JSoundDataFrame(this.dataFrame.sparkSession().sql(sqlQuery), outputType);
    }

    public boolean isEmptySequence() {
        return this.dataFrame.isEmpty();
    }

    public Item getExactlyOneItem() {
        List<Row> result = this.dataFrame.takeAsList(1);
        DataType fieldType = this.dataFrame.schema().fields()[0].dataType();
        return ItemParser.convertValueToItem(result.get(0).get(0), fieldType, ExceptionMetadata.EMPTY_METADATA);
    }

    public JSoundDataFrame distinct() {
        return new JSoundDataFrame(this.dataFrame.distinct(), this.itemType);
    }

    public JSoundDataFrame union(JSoundDataFrame other) {
        if (!this.itemType.equals(other.itemType)) {
            throw new OurBadException(
                    "Cannot union two dataframes with types " + this.itemType + " and " + other.itemType
            );
        }
        return new JSoundDataFrame(this.getDataFrame().union(other.getDataFrame()), this.itemType);
    }

    public boolean hasKey(String key) {
        if (!this.itemType.isObjectItemType()) {
            throw new OurBadException("Cannot test for existing of a key of non-object.");
        }
        StructType schema = this.dataFrame.schema();
        String[] fieldNames = schema.fieldNames();
        return Arrays.asList(fieldNames).contains(key);
    }
}
