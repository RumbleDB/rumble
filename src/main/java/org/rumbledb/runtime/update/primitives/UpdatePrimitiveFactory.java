package org.rumbledb.runtime.update.primitives;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;

import java.util.List;

public class UpdatePrimitiveFactory {

    private static UpdatePrimitiveFactory instance;

    public static UpdatePrimitiveFactory getInstance() {
        if (instance == null) {
            instance = new UpdatePrimitiveFactory();
        }
        return instance;
    }

    public UpdatePrimitive createDeleteFromArrayPrimitive(
            Item targetArray,
            Item selectorInt,
            ExceptionMetadata metadata
    ) {
        return new DeleteFromArrayPrimitive(targetArray, selectorInt, metadata);
    }

    public UpdatePrimitive createDeleteFromObjectPrimitive(
            Item targetObject,
            List<Item> selectorStrs,
            ExceptionMetadata metadata
    ) {
        return new DeleteFromObjectPrimitive(targetObject, selectorStrs, metadata);
    }

    public UpdatePrimitive createInsertIntoArrayPrimitive(
            Item targetArray,
            Item selectorInt,
            List<Item> contents,
            ExceptionMetadata metadata
    ) {
        return new InsertIntoArrayPrimitive(targetArray, selectorInt, contents, metadata);
    }

    public UpdatePrimitive createInsertIntoObjectPrimitive(
            Item targetObject,
            Item contentsObject,
            ExceptionMetadata metadata
    ) {
        return new InsertIntoObjectPrimitive(targetObject, contentsObject, metadata);
    }

    public UpdatePrimitive createReplaceInArrayPrimitive(
            Item targetArray,
            Item selectorInt,
            Item content,
            ExceptionMetadata metadata
    ) {
        return new ReplaceInArrayPrimitive(targetArray, selectorInt, content, metadata);
    }

    public UpdatePrimitive createReplaceInObjectPrimitive(
            Item targetObject,
            Item selectorStr,
            Item content,
            ExceptionMetadata metadata
    ) {
        return new ReplaceInObjectPrimitive(targetObject, selectorStr, content, metadata);
    }

    public UpdatePrimitive createRenameInObjectPrimitive(
            Item targetObject,
            Item selectorStr,
            Item content,
            ExceptionMetadata metadata
    ) {
        return new RenameInObjectPrimitive(targetObject, selectorStr, content, metadata);
    }

    public UpdatePrimitive createCreateCollectionPrimitive(
            String collectionName,
            Dataset<Row> contents,
            boolean isTable,
            ExceptionMetadata metadata
    ) {
        return new CreateCollectionPrimitive(collectionName, contents, isTable, metadata);
    }

    public UpdatePrimitive createDeleteTupleFromCollectionPrimitive(
            String collectionName,
            double rowOrder,
            ExceptionMetadata metadata
    ) {
        return new DeleteTupleFromCollectionPrimitive(collectionName, rowOrder, metadata);
    }

    public UpdatePrimitive createEditTuplePrimitive(
            Item target,
            Dataset<Row> contents,
            ExceptionMetadata metadata
    ) {
        return new EditTuplePrimitive(target, contents, metadata);
    }

    public UpdatePrimitive createInsertTuplePrimitive(
            String collection,
            Dataset<Row> contents,
            double rowOrderBase,
            double rowOrderMax,
            ExceptionMetadata metadata
    ) {
        return new InsertTuplePrimitive(collection, contents, rowOrderBase, rowOrderMax, metadata);
    }

    public UpdatePrimitive createTruncateCollectionPrimitive(
            String collectionName,
            ExceptionMetadata metadata
    ) {
        return new TruncateCollectionPrimitive(collectionName, metadata);
    }

}
