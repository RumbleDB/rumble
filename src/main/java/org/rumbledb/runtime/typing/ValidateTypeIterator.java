package org.rumbledb.runtime.typing;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MLInvalidDataFrameSchemaException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.ItemType;

import sparksoniq.spark.DataFrameUtils;
import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ValidateTypeIterator extends DataFrameRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private ItemType itemType;

    public ValidateTypeIterator(
            RuntimeIterator instance,
            ItemType itemType,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Collections.singletonList(instance), executionMode, iteratorMetadata);
        this.itemType = itemType;
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        RuntimeIterator inputDataIterator = this.children.get(0);

        try {

            if (inputDataIterator.isDataFrame()) {
                Dataset<Row> inputDataAsDataFrame = inputDataIterator.getDataFrame(context);
                validateItemTypeAgainstDataFrame(
                    itemType,
                    inputDataAsDataFrame.schema()
                );
                return inputDataAsDataFrame;
            }

            if (inputDataIterator.isRDDOrDataFrame()) {
                JavaRDD<Item> rdd = inputDataIterator.getRDD(context);
                return convertRDDToValidDataFrame(rdd, itemType);
            }

            List<Item> items = inputDataIterator.materialize(context);
            return convertLocalItemsToValidDataFrame(items, itemType);
        } catch (MLInvalidDataFrameSchemaException ex) {
            MLInvalidDataFrameSchemaException e = new MLInvalidDataFrameSchemaException(
                    "Schema error in annotate(); " + ex.getJSONiqErrorMessage(),
                    getMetadata()
            );
            e.initCause(ex);
            throw e;
        }
    }

    private static Dataset<Row> convertRDDToValidDataFrame(
            JavaRDD<Item> itemRDD,
            ItemType itemType
    ) {
        Item firstDataItem = (Item) itemRDD.take(1).get(0);
        checkForValidity(itemType, firstDataItem);
        StructType schema = convertToDataFrameSchema(itemType);
        JavaRDD<Row> rowRDD = itemRDD.map(
            new Function<Item, Row>() {
                private static final long serialVersionUID = 1L;

                @Override
                public Row call(Item item) {
                    return ItemParser.getRowFromItemUsingSchema(item, schema);
                }
            }
        );
        return SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rowRDD, schema);
    }

    private static void checkForValidity(
            ItemType itemType,
            Item dataItem
    ) {
        for (String schemaColumn : itemType.getObjectContentFacet().keySet()) {
            if (!dataItem.getKeys().contains(schemaColumn)) {
                throw new MLInvalidDataFrameSchemaException(
                        "Fields defined in schema must fully match the fields of input data: "
                            + "redundant type information for non-existent field '"
                            + schemaColumn
                            + "' column."
                );
            }
        }

        for (String dataColumn : dataItem.getKeys()) {
            if (!itemType.getObjectContentFacet().keySet().contains(dataColumn)) {
                throw new MLInvalidDataFrameSchemaException(
                        "Fields defined in schema must fully match the fields of input data: "
                            + "missing type information for '"
                            + dataColumn
                            + "' field."
                );
            }
        }
    }

    private static StructType convertToDataFrameSchema(ItemType itemType) {
        if (!itemType.isObjectItemType()) {
            throw new MLInvalidDataFrameSchemaException(
                    "Error while checking against the DataFrame schema: it is not an object type: " + itemType
            );

        }
        List<StructField> fields = new ArrayList<>();
        try {
            for (String columnName : itemType.getObjectContentFacet().keySet()) {
                StructField field = createStructField(
                    columnName,
                    itemType.getObjectContentFacet().get(columnName).getType()
                );
                fields.add(field);
            }
        } catch (IllegalArgumentException ex) {
            MLInvalidDataFrameSchemaException e = new MLInvalidDataFrameSchemaException(
                    "Error while applying the schema; " + ex.getMessage()
            );
            e.initCause(ex);
            throw e;
        }
        return DataTypes.createStructType(fields);
    }

    private static StructField createStructField(String columnName, ItemType item) {
        DataType type = convertToDataType(item);
        return DataTypes.createStructField(columnName, type, true);
    }

    private static DataType convertToDataType(ItemType itemType) {
        if (itemType.isArrayItemType()) {
            ItemType arrayContentsTypeItemType = itemType.getArrayContentFacet().getType();
            DataType arrayContentsType = convertToDataType(arrayContentsTypeItemType);
            return DataTypes.createArrayType(arrayContentsType);
        }

        if (itemType.isObjectItemType()) {
            return convertToDataFrameSchema(itemType);
        }

        return ItemParser.getDataFrameDataTypeFromItemType(itemType);
    }

    private static Dataset<Row> convertLocalItemsToValidDataFrame(
            List<Item> items,
            ItemType itemType
    ) {
        if (items.size() == 0) {
            return SparkSessionManager.getInstance().getOrCreateSession().emptyDataFrame();
        }
        ObjectItem firstDataItem = (ObjectItem) items.get(0);
        checkForValidity(itemType, firstDataItem);
        StructType schema = convertToDataFrameSchema(itemType);
        List<Row> rows = ItemParser.getRowsFromItemsUsingSchema(items, schema);
        return SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(rows, schema);
    }


    private static void validateItemTypeAgainstDataFrame(
            ItemType itemType,
            StructType dataFrameSchema
    ) {
        StructType generatedSchema = convertToDataFrameSchema(itemType);
        for (StructField column : dataFrameSchema.fields()) {
            final String columnName = column.name();
            final DataType columnDataType = column.dataType();

            boolean columnMatched = Arrays.stream(generatedSchema.fields()).anyMatch(structField -> {
                String generatedColumnName = structField.name();
                if (!generatedColumnName.equals(columnName)) {
                    return false;
                }

                DataType generatedDataType = structField.dataType();
                if (DataFrameUtils.isUserTypeApplicable(generatedDataType, columnDataType)) {
                    return true;
                }

                throw new MLInvalidDataFrameSchemaException(
                        "Fields defined in schema must fully match the fields of input data: "
                            + "expected '"
                            + ItemParser.getItemTypeNameFromDataFrameDataType(columnDataType)
                            + "' type for field '"
                            + columnName
                            + "', but found '"
                            + ItemParser.getItemTypeNameFromDataFrameDataType(generatedDataType)
                            + "'"
                );
            });

            if (!columnMatched) {
                throw new MLInvalidDataFrameSchemaException(
                        "Fields defined in schema must fully match the fields of input data: "
                            + "missing type information for '"
                            + columnName
                            + "' field."
                );
            }
        }

        for (String generatedSchemaColumnName : generatedSchema.fieldNames()) {
            boolean userColumnMatched = Arrays.asList(dataFrameSchema.fieldNames()).contains(generatedSchemaColumnName);

            if (!userColumnMatched) {
                throw new MLInvalidDataFrameSchemaException(
                        "Fields defined in schema must fully match the fields of input data: "
                            + "redundant type information for non-existent field '"
                            + generatedSchemaColumnName
                            + "'."
                );
            }
        }
    }

}
