package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotResolveUpdateSelectorException;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.spark.SparkSessionManager;

import java.util.*;
import java.util.stream.Collectors;

public class DeleteFromObjectPrimitive implements UpdatePrimitive {
    private Item target;
    private List<Item> content;
    private Collection collection;

    public DeleteFromObjectPrimitive(Item targetObject, List<Item> namesToRemove, ExceptionMetadata metadata) {

        for (Item item : namesToRemove) {
            if (targetObject.getItemByKey(item.getStringValue()) == null) {
                throw new CannotResolveUpdateSelectorException(
                        "Cannot delete key that does not exist in target object",
                        metadata
                );
            }
        }

        this.target = targetObject;
        this.content = namesToRemove;
        this.collection = targetObject.getCollection();
    }

    @Override
    public void apply() {
        if (this.collection == null) {
            this.applyItem();
        } else {
            this.applyDelta();
        }
    }

    @Override
    public void applyItem() {
        for (
            String str : this.content.stream().map(Item::getStringValue).collect(Collectors.toList())
        ) {
            this.target.removeItemByKey(str);
        }
    }

    @Override
    public void applyDelta() {
        String tempPathIn = this.target.getPathIn() + ".";
        String pathIn = tempPathIn.substring(tempPathIn.indexOf(".") + 1);
        String location = this.collection.getPhysicalName();
        long rowID = this.target.getTopLevelID();
        int startOfArrayIndexing = pathIn.indexOf("[");

        if (startOfArrayIndexing == -1) {
            List<String> setFieldsToNulls = this.content.stream()
                .map(i -> pathIn + i.getStringValue() + " = NULL")
                .collect(Collectors.toList());
            String concatSetNulls = String.join(", ", setFieldsToNulls);

            String query = "UPDATE "
                + location
                + " SET "
                + concatSetNulls
                + " WHERE `"
                + SparkSessionManager.rowIdColumnName
                + "` == "
                + rowID;

            SparkSessionManager.getInstance().getOrCreateSession().sql(query);
        } else {
            this.arrayIndexingApplyDelta();
        }
    }

    @Override
    public boolean hasSelector() {
        return false;
    }

    @Override
    public Item getTarget() {
        return this.target;
    }

    @Override
    public List<Item> getContentList() {
        return this.content;
    }

    @Override
    public boolean isDeleteObject() {
        return true;
    }

}
