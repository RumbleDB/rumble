package org.rumbledb.runtime.update.primitives;

import io.delta.tables.DeltaTable;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotResolveUpdateSelectorException;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.spark.SparkSessionManager;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteFromArrayPrimitive implements UpdatePrimitive {


    private Item target;
    private Item selector;

    public DeleteFromArrayPrimitive(Item targetArray, Item positionInt, ExceptionMetadata metadata) {
        if (positionInt.getIntValue() <= 0 || positionInt.getIntValue() > targetArray.getSize()) {
            throw new CannotResolveUpdateSelectorException(
                    "Cannot delete item at index out of range of target array",
                    metadata
            );
        }
        this.target = targetArray;
        this.selector = positionInt;
    }

    @Override
    public void apply() {
        if (this.target.getTableLocation() == null) {
            this.applyItem();
        } else {
            this.applyDelta();
        }
    }

    @Override
    public void applyItem() {
        this.target.removeItemAt(this.selector.getIntValue() - 1);
    }

    @Override
    public void applyDelta() {
        // TODO: Find out name of array column
        // ASSUME pathIn CONTAINS ARRAYFIELD NAME
        String pathIn = this.target.getPathIn();
        String location = this.target.getTableLocation();
        long rowID = this.target.getTopLevelID();

        String setClause = "SET " + pathIn + " = ";
        this.applyItem();
        setClause = setClause + this.target.getSparkSQLValue();

        String query = "UPDATE delta.`" + location + "` " + setClause + " WHERE rowID == " + rowID;

        SparkSessionManager.getInstance().getOrCreateSession().sql(query);
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
        return null;
    }

    @Override
    public boolean isDeleteArray() {
        return true;
    }
}
