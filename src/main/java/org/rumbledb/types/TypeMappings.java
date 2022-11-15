package org.rumbledb.types;

import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.DecimalType;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.parsing.ItemParser;

public class TypeMappings {

    public static DataType getDataFrameDataTypeFromItemType(ItemType itemType) {
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.booleanItem)) {
            return DataTypes.BooleanType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.doubleItem)) {
            return DataTypes.DoubleType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.floatItem)) {
            return DataTypes.FloatType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.byteItem)) {
            return DataTypes.ByteType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.shortItem)) {
            return DataTypes.ShortType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.intItem)) {
            return DataTypes.IntegerType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.longItem)) {
            return DataTypes.LongType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.integerItem)) {
            return ItemParser.integerType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.decimalItem)) {
            return ItemParser.decimalType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.stringItem)) {
            return DataTypes.StringType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.anyURIItem)) {
            return DataTypes.StringType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.nullItem)) {
            return DataTypes.NullType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.dateItem)) {
            return DataTypes.DateType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.dateTimeStampItem)) {
            return DataTypes.TimestampType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.hexBinaryItem)) {
            return DataTypes.BinaryType;
        }
        throw new IllegalArgumentException(
                "Unexpected item type found: '" + itemType + "' in namespace " + itemType.getName().getNamespace() + "."
        );
    }

    public static ItemType getItemTypeFromDataFrameDataType(DataType dataType) {
        if (DataTypes.BooleanType.equals(dataType)) {
            return BuiltinTypesCatalogue.booleanItem;
        }
        if (DataTypes.LongType.equals(dataType)) {
            return BuiltinTypesCatalogue.decimalItem;
        }
        if (DataTypes.IntegerType.equals(dataType)) {
            return BuiltinTypesCatalogue.integerItem;
        }
        if (DataTypes.ShortType.equals(dataType)) {
            return BuiltinTypesCatalogue.shortItem;
        }
        if (DataTypes.ByteType.equals(dataType)) {
            return BuiltinTypesCatalogue.byteItem;
        }
        if (DataTypes.DoubleType.equals(dataType)) {
            return BuiltinTypesCatalogue.doubleItem;
        }
        if (DataTypes.FloatType.equals(dataType)) {
            return BuiltinTypesCatalogue.floatItem;
        }
        if (dataType instanceof DecimalType && ((DecimalType) dataType).scale() == 0) {
            return BuiltinTypesCatalogue.integerItem;
        }
        if (dataType instanceof DecimalType) {
            return BuiltinTypesCatalogue.decimalItem;
        }
        if (DataTypes.StringType.equals(dataType)) {
            return BuiltinTypesCatalogue.stringItem;
        }
        if (DataTypes.NullType.equals(dataType)) {
            return BuiltinTypesCatalogue.nullItem;
        }
        if (DataTypes.DateType.equals(dataType)) {
            return BuiltinTypesCatalogue.dateItem;
        }
        if (DataTypes.TimestampType.equals(dataType)) {
            return BuiltinTypesCatalogue.dateTimeItem;
        }
        if (DataTypes.BinaryType.equals(dataType)) {
            return BuiltinTypesCatalogue.hexBinaryItem;
        }
        if (ItemParser.vectorType.equals(dataType)) {
            return BuiltinTypesCatalogue.objectItem;
        }
        throw new OurBadException("Unexpected DataFrame data type found: '" + dataType.toString() + "'.");
    }
}
