package org.rumbledb.runtime.update.primitives;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotResolveUpdateSelectorException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import sparksoniq.spark.SparkSessionManager;


public class ReplaceInArrayPrimitive implements UpdatePrimitive {

    private Item target;
    private Item selector;
    private Item content;

    public ReplaceInArrayPrimitive(
            Item targetArray,
            Item positionInt,
            Item replacementItem,
            ExceptionMetadata metadata
    ) {
        if (positionInt.getIntValue() <= 0 || positionInt.getIntValue() > targetArray.getSize()) {
            throw new CannotResolveUpdateSelectorException(
                    "Cannot replace item at index out of range of target array",
                    metadata
            );
        }

        this.target = targetArray;
        this.selector = positionInt;
        this.content = replacementItem;
    }

    @Override
    public void apply() {
        if (this.target.getTableLocation() == null || this.target.getTableLocation().equals("null")) {
            this.applyItem();
        } else {
            this.applyDelta();
        }
    }

    @Override
    public void applyItem() {
        int index = this.selector.getIntValue() - 1;
        if (index >= 0 || index < this.target.getSize()) {
            this.target.removeItemAt(index);
            if (index == this.target.getSize()) {
                this.target.append(this.content);
            } else {
                this.target.putItemAt(this.content, index);
            }
        }
    }

    @Override
    public void applyDelta() {
        // TODO: Sort out diff types of content Item
        // TODO: Find out name of array column
        // ASSUME pathIn CONTAINS ARRAYFIELD NAME
        // PERHAPS CASE OF REPLACING ARRAY WITH 1 ITEM SHOULD CREATE NEW ARRAYCOL WITH CORRECTED TYPE IF TYPE CHANGES

        String pathIn = this.target.getPathIn().substring(this.target.getPathIn().indexOf(".") + 1);
        String location = this.target.getTableLocation();
        long rowID = this.target.getTopLevelID();
        int startOfArrayIndexing = pathIn.indexOf("[");

        if (startOfArrayIndexing == -1) {
            String selectArrayQuery = "SELECT "
                + pathIn
                + " AS `"
                + SparkSessionManager.atomicJSONiqItemColumnName
                + "` FROM delta.`"
                + location
                + "` WHERE rowID == "
                + rowID;

            Dataset<Row> arrayDF = SparkSessionManager.getInstance().getOrCreateSession().sql(selectArrayQuery);

            ItemType arrayType = ItemTypeFactory.createItemType(arrayDF.schema())
                .getObjectContentFacet()
                .get(SparkSessionManager.atomicJSONiqItemColumnName)
                .getType();

            String setField = pathIn + " = ";
            this.applyItem();
            setField = setField + this.target.getSparkSQLValue(arrayType);

            String query = "UPDATE delta.`" + location + "` SET " + setField + " WHERE rowID == " + rowID;

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
    public int getIntSelector() {
        return this.selector.getIntValue();
    }

    @Override
    public Item getContent() {
        return this.content;
    }

    @Override
    public boolean isReplaceArray() {
        return true;
    }
}
