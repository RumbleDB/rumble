package org.rumbledb.runtime.update.primitives;

import org.apache.spark.sql.AnalysisException;
import org.rumbledb.api.Item;
import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InsertIntoArrayPrimitive implements UpdatePrimitive {

    private Item target;
    private Item selector;
    private List<Item> content;

    public InsertIntoArrayPrimitive(Item targetArray, Item positionInt, List<Item> sourceSequence) {
        if (!targetArray.isArray() || !positionInt.isNumeric()) {
            // TODO ERROR
        }
        if (positionInt.getIntValue() < 0 || positionInt.getIntValue() >= targetArray.getSize()) {
            // TODO throw error or do nothing?
        }

        this.target = targetArray;
        this.selector = positionInt;
        this.content = sourceSequence;

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
        this.target.putItemsAt(this.content, this.selector.getIntValue() - 1);
    }

    @Override
    public void applyDelta() {
        // TODO: Properly discern ItemType to SQLType
        // TODO: Sort out types for value item
        // ASSUMES ArrayType ONLY CONTAINS 1 TYPE AND INSERTION OF A DIFF TYPE IS INVALID
        // TODO: perhaps check for homogenous typing of array w/o relying on SQL error

        String pathIn = this.target.getPathIn().substring(this.target.getPathIn().indexOf(".") + 1);
        String location = this.target.getTableLocation();
        long rowID = this.target.getTopLevelID();
        int startOfArrayIndexing = pathIn.indexOf("[");

        if (startOfArrayIndexing == -1) {
            String setClause = pathIn + " = ";
            this.applyItem();
            setClause = setClause + this.target.getSparkSQLValue();

            String setFieldQuery = "UPDATE delta.`" + location + "` SET " + setClause + " WHERE rowID == " + rowID;

            SparkSessionManager.getInstance().getOrCreateSession().sql(setFieldQuery);
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
    public List<Item> getContentList() {
        return this.content;
    }

    @Override
    public boolean isInsertArray() {
        return true;
    }

    public static List<Item> mergeSources(List<Item> first, List<Item> second) {
        List<Item> merged = new ArrayList<>(first);
        merged.addAll(second);
        return merged;
    }
}
