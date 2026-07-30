package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotResolveUpdateSelectorException;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.spark.SparkSessionManager;


public class ReplaceInObjectPrimitive implements UpdatePrimitive {

    private Item target;
    private Item selector;
    private Item content;
    private Collection collection;

    public ReplaceInObjectPrimitive(
            Item targetObject,
            Item targetName,
            Item replacementItem,
            ExceptionMetadata metadata
    ) {

        if (targetObject.getItemByKey(targetName.getStringValue()) == null) {
            throw new CannotResolveUpdateSelectorException(
                    "Cannot replace key that does not exist in target object",
                    metadata
            );
        }

        this.target = targetObject;
        this.selector = targetName;
        this.content = replacementItem;
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
        String name = this.getSelector().getStringValue();
        if (this.getTarget().getKeys().contains(name)) {
            this.getTarget().removeItemByKey(name);
            this.getTarget().putItemByKey(name, this.getContent());
        }
    }

    @Override
    public void applyDelta() {
        // TODO: Sort out diff types of content Item
        // PERHAPS CHANGE OF TYPE SHOULD CREATE NEW COLUMN?

        String tempPathIn = this.target.getPathIn() + ".";
        String pathIn = tempPathIn.substring(tempPathIn.indexOf(".") + 1);
        String location = this.collection.getPhysicalName();
        long rowID = this.target.getTopLevelID();
        int startOfArrayIndexing = pathIn.indexOf("[");

        if (startOfArrayIndexing == -1) {

            String setField = pathIn + this.selector.getStringValue() + " = " + this.content.getSparkSQLValue();

            String query = "UPDATE "
                + location
                + " SET "
                + setField
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
        return true;
    }

    @Override
    public Item getTarget() {
        return this.target;
    }

    @Override
    public Item getSelector() {
        return this.selector;
    }

    @Override
    public Item getContent() {
        return this.content;
    }

    @Override
    public boolean isReplaceObject() {
        return true;
    }
}
