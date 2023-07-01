package org.rumbledb.runtime.update.primitives;

import org.apache.spark.api.java.JavaRDD;
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

public interface UpdatePrimitive {

    void apply();

    void applyItem();

    void applyDelta();

    boolean hasSelector();

    Item getTarget();

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

    default void arrayIndexingApplyDelta() {
        Item target = this.getTarget();

        String pathIn = target.getPathIn().substring(target.getPathIn().indexOf(".") + 1);
        String location = target.getTableLocation();
        long rowID = target.getTopLevelID();
        int startOfArrayIndexing = pathIn.indexOf("[");

        String preIndexingPathIn = pathIn.substring(0, startOfArrayIndexing);
        String postIndexingPathIn = pathIn.substring(startOfArrayIndexing);
        List<String> fields = Arrays.asList(postIndexingPathIn.split("\\."));

        String selectArrayQuery = "SELECT " + preIndexingPathIn + " AS `" + SparkSessionManager.atomicJSONiqItemColumnName + "` FROM delta.`" + location + "` WHERE rowID == " + rowID;

        Dataset<Row> arrayDF = SparkSessionManager.getInstance().getOrCreateSession().sql(selectArrayQuery);

        ItemType arrayType = ItemTypeFactory.createItemType(arrayDF.schema()).getObjectContentFacet().get(SparkSessionManager.atomicJSONiqItemColumnName).getType();

        JavaRDD<Row> rowRDD = arrayDF.javaRDD();
        JavaRDD<Item> itemRDD = rowRDD.map(new RowToItemMapper(ExceptionMetadata.EMPTY_METADATA, arrayType));
        List<Item> collectedItems = itemRDD.take(2);
        Item originalArray = collectedItems.get(0);
        // TODO: errors if 0 items or more than one item

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

        String finalSelector = fields.get(fields.size() - 1);
        this.applyItem();
        if (innerItem.isObject()) {
            innerItem.removeItemByKey(finalSelector);
            innerItem.putItemByKey(finalSelector, this.getTarget());
        } else if (innerItem.isArray()) {
            int finalIndex = Integer.parseInt(finalSelector.substring(finalSelector.indexOf("[") + 1, finalSelector.indexOf("]")));
            innerItem.removeItemAt(finalIndex);
            innerItem.putItemAt(this.getTarget(), finalIndex);
        }

        String setClause = preIndexingPathIn + " = " + originalArray.getSparkSQLValue(arrayType);
        String query = "UPDATE delta.`" + location + "` SET " + setClause + " WHERE rowID == " + rowID;

        // TODO: Perhaps an if here to update schema if required but need to sort out new name of array column -- RenameInObj and InsertIntoObj
//        if (this.isSchemaUpdating()) {
//            String newName = "NEEDS A NEW NAME";
//            String insertColumnsQuery = "ALTER TABLE delta.`" + location + "` ADD COLUMNS (" + arrayType.getSparkSQLType() + ");";
//            SparkSessionManager.getInstance().getOrCreateSession().sql(insertColumnsQuery);
//        }

        SparkSessionManager.getInstance().getOrCreateSession().sql(query);
    }


}
