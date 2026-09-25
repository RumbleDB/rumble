/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.update.primitives;

import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;

public class UpdatePrimitiveFactory {

    private static UpdatePrimitiveFactory instance;

    public static UpdatePrimitiveFactory getInstance() {
        if (instance == null) {
            instance = new UpdatePrimitiveFactory();
        }
        return instance;
    }

    public UpdatePrimitive createDeleteFromArrayPrimitive(
            Item targetArray, Item selectorInt, ExceptionMetadata metadata) {
        return new DeleteFromArrayPrimitive(targetArray, selectorInt, metadata);
    }

    public UpdatePrimitive createDeleteFromObjectPrimitive(
            Item targetObject, List<Item> selectorStrs, ExceptionMetadata metadata) {
        return new DeleteFromObjectPrimitive(targetObject, selectorStrs, metadata);
    }

    public UpdatePrimitive createInsertIntoArrayPrimitive(
            Item targetArray, Item selectorInt, List<Item> contents, ExceptionMetadata metadata) {
        return new InsertIntoArrayPrimitive(targetArray, selectorInt, contents, metadata);
    }

    public UpdatePrimitive createInsertIntoObjectPrimitive(
            Item targetObject, Item contentsObject, ExceptionMetadata metadata) {
        return new InsertIntoObjectPrimitive(targetObject, contentsObject, metadata);
    }

    public UpdatePrimitive createReplaceInArrayPrimitive(
            Item targetArray, Item selectorInt, Item content, ExceptionMetadata metadata) {
        return new ReplaceInArrayPrimitive(targetArray, selectorInt, content, metadata);
    }

    public UpdatePrimitive createReplaceInObjectPrimitive(
            Item targetObject, Item selectorStr, Item content, ExceptionMetadata metadata) {
        return new ReplaceInObjectPrimitive(targetObject, selectorStr, content, metadata);
    }

    public UpdatePrimitive createRenameInObjectPrimitive(
            Item targetObject, Item selectorStr, Item content, ExceptionMetadata metadata) {
        return new RenameInObjectPrimitive(targetObject, selectorStr, content, metadata);
    }

    public UpdatePrimitive createCreateCollectionPrimitive(
            Collection collection, Dataset<Row> contents, ExceptionMetadata metadata) {
        return new CreateCollectionPrimitive(collection, contents, metadata);
    }

    public UpdatePrimitive createDeleteTupleFromCollectionPrimitive(
            Collection collection, double rowOrder, ExceptionMetadata metadata) {
        return new DeleteTupleFromCollectionPrimitive(collection, rowOrder, metadata);
    }

    public UpdatePrimitive createEditTuplePrimitive(Item target, Dataset<Row> contents, ExceptionMetadata metadata) {
        return new EditTuplePrimitive(target, contents, metadata);
    }

    public UpdatePrimitive createInsertAfterIntoCollectionPrimitive(
            Item target, Dataset<Row> contents, ExceptionMetadata metadata) {
        return new InsertAfterIntoCollectionPrimitive(target, contents, metadata);
    }

    public UpdatePrimitive createInsertBeforeIntoCollectionPrimitive(
            Item target, Dataset<Row> contents, ExceptionMetadata metadata) {
        return new InsertBeforeIntoCollectionPrimitive(target, contents, metadata);
    }

    public UpdatePrimitive createInsertFirstIntoCollectionPrimitive(
            Collection collection, Dataset<Row> contents, ExceptionMetadata metadata) {
        return new InsertFirstIntoCollectionPrimitive(collection, contents, metadata);
    }

    public UpdatePrimitive createInsertLastIntoCollectionPrimitive(
            Collection collection, Dataset<Row> contents, ExceptionMetadata metadata) {
        return new InsertLastIntoCollectionPrimitive(collection, contents, metadata);
    }

    public UpdatePrimitive createTruncateCollectionPrimitive(Collection collection, ExceptionMetadata metadata) {
        return new TruncateCollectionPrimitive(collection, metadata);
    }
}
