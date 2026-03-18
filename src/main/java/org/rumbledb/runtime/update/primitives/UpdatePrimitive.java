package org.rumbledb.runtime.update.primitives;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.parsing.RowToItemMapper;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import sparksoniq.spark.SparkSessionManager;

import java.util.Arrays;
import java.util.List;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.expr;

public interface UpdatePrimitive {

    void apply();

    void applyItem();

    void applyDelta();

    boolean hasSelector();

    default Item getTarget() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    default Dataset<Row> getTargetDataFrame() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    default Item getSelector() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    default int getIntSelector() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    default Item getContent() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    default List<Item> getContentList() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    default Dataset<Row> getContentDataFrame() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    default boolean isDeleteObject() {
        return false;
    }

    default boolean isDeleteArray() {
        return false;
    }

    default boolean isInsertObject() {
        return false;
    }

    default boolean isInsertArray() {
        return false;
    }

    default boolean isReplaceObject() {
        return false;
    }

    default boolean isReplaceArray() {
        return false;
    }

    default boolean isRenameObject() {
        return false;
    }

    default boolean updatesSchemaDelta() {
        return false;
    }

    default boolean isCreateCollection() {
        return false;
    }

    default boolean isDeleteTuple() {
        return false;
    }

    default boolean isEditTuple() {
        return false;
    }

    default boolean isInsertTuple() {
        return false;
    }

    default boolean isInsertAfterIntoCollection() {
        return false;
    }

    default boolean isInsertBeforeIntoCollection() {
        return false;
    }

    default boolean isInsertFirstIntoCollection() {
        return false;
    }

    default boolean isInsertLastIntoCollection() {
        return false;
    }

    default boolean isTruncateCollection() {
        return false;
    }

    default void arrayIndexingUpdateSchemaDelta() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    default String getCollectionPath() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    default String getCollectionName() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    default double getRowOrder() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    default double getRowOrderRangeBase() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    default double getRowOrderRangeMax() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    default void applySetFieldInCollection(String location, long rowID, String fieldPath, String fieldValueSQL) {
        if (this.getTarget().getCollection().getMode() != Mode.ICEBERG) {
            String updateQuery = "UPDATE "
                + location
                + " SET "
                + fieldPath
                + " = "
                + fieldValueSQL
                + " WHERE `"
                + SparkSessionManager.rowIdColumnName
                + "` == "
                + rowID;
            SparkSessionManager.getInstance().getOrCreateSession().sql(updateQuery);
            return;
        }

        // Iceberg UPDATE is not supported in this runtime; rewrite the target row instead
        // Future optimization: batch row rewrites (delete all + insert all) for multi-row updates

        Dataset<Row> updatedRows = SparkSessionManager.getInstance()
            .getOrCreateSession()
            .table(location)
            .where(col(SparkSessionManager.rowIdColumnName).equalTo(rowID));
        Column newValue = expr(fieldValueSQL);

        if (fieldPath.contains(".")) {
            String[] parts = fieldPath.split("\\.");
            String root = parts[0];
            Column updatedRoot = this.setNestedStructField(col(root), parts, 1, newValue);
            updatedRows = updatedRows.withColumn(root, updatedRoot);
        } else {
            updatedRows = updatedRows.withColumn(fieldPath, newValue);
        }

        List<Row> rewrittenRows = updatedRows.collectAsList();
        if (rewrittenRows.size() != 1) {
            throw new IllegalStateException(
                    "Expected exactly one row in update rewrite for row ID "
                        + rowID
                        + " but found "
                        + rewrittenRows.size()
            );
        }
        Dataset<Row> frozenRows = SparkSessionManager.getInstance()
            .getOrCreateSession()
            .createDataFrame(rewrittenRows, updatedRows.schema());

        String deleteQuery = "DELETE FROM "
            + location
            + " WHERE `"
            + SparkSessionManager.rowIdColumnName
            + "` = "
            + rowID;
        SparkSessionManager.getInstance().getOrCreateSession().sql(deleteQuery);

        this.getTarget().getCollection().insertUnordered(frozenRows);
    }

    default Column setNestedStructField(Column structColumn, String[] parts, int index, Column newValue) {
        if (index == parts.length - 1) {
            return structColumn.withField(parts[index], newValue);
        }
        Column nested = structColumn.getField(parts[index]);
        Column updatedNested = setNestedStructField(nested, parts, index + 1, newValue);
        return structColumn.withField(parts[index], updatedNested);
    }

    default void arrayIndexingApplyDelta() {
        Item target = this.getTarget();

        String pathIn = target.getPathIn().substring(target.getPathIn().indexOf(".") + 1);
        String location = target.getCollection().getPhysicalName();
        long rowID = target.getTopLevelID();
        int startOfArrayIndexing = pathIn.indexOf("[");

        String preIndexingPathIn = pathIn.substring(0, startOfArrayIndexing);
        String postIndexingPathIn = pathIn.substring(startOfArrayIndexing);
        List<String> fields = Arrays.asList(postIndexingPathIn.split("\\."));

        // TODO: Perhaps an if here to update schema if required but need to sort out new name of array column --
        if (this.updatesSchemaDelta()) {
            this.arrayIndexingUpdateSchemaDelta();
        }

        String selectArrayQuery = "SELECT "
            + preIndexingPathIn
            + " AS `"
            + SparkSessionManager.nonObjectJSONiqItemColumnName
            + "` FROM "
            + location
            + " WHERE `"
            + SparkSessionManager.rowIdColumnName
            + "` == "
            + rowID;

        Dataset<Row> arrayDF = SparkSessionManager.getInstance().getOrCreateSession().sql(selectArrayQuery);

        ItemType arrayType = ItemTypeFactory.createItemType(arrayDF.schema())
            .getObjectContentFacet()
            .get(SparkSessionManager.nonObjectJSONiqItemColumnName)
            .getType();

        JavaRDD<Row> rowRDD = arrayDF.javaRDD();
        JavaRDD<Item> itemRDD = rowRDD.map(new RowToItemMapper(ExceptionMetadata.EMPTY_METADATA, arrayType));
        List<Item> collectedItems = itemRDD.take(2);
        Item originalArray = collectedItems.get(0);

        // TODO: errors if 0 items or more than one item

        // Navigate to the inner item
        Item innerItem = originalArray;
        for (int i = 0; i < fields.size() - 1; i++) {
            String field = fields.get(i);
            if (innerItem.isObject()) {
                innerItem = innerItem.getItemByKey(field);
            } else if (innerItem.isArray()) {
                int index = Integer.parseInt(field.substring(field.indexOf("[") + 1, field.indexOf("]")));
                innerItem = innerItem.getItemAt(index);
            }
        }

        // Apply the update to the inner item
        this.applyItem();

        // Put the modified inner item back into the original array
        String finalSelector = fields.get(fields.size() - 1);
        if (innerItem.isObject()) {
            innerItem.removeItemByKey(finalSelector);
            innerItem.putItemByKey(finalSelector, this.getTarget());
        } else if (innerItem.isArray()) {
            int finalIndex = Integer.parseInt(
                finalSelector.substring(finalSelector.indexOf("[") + 1, finalSelector.indexOf("]"))
            );
            innerItem.removeItemAt(finalIndex);
            innerItem.putItemAt(this.getTarget(), finalIndex);
        }

        this.applySetFieldInCollection(location, rowID, preIndexingPathIn, originalArray.getSparkSQLValue(arrayType));
    }

}
