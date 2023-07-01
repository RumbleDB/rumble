package org.rumbledb.runtime.update.primitives;

import io.delta.tables.DeltaTable;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotResolveUpdateSelectorException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.parsing.RowToItemMapper;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import sparksoniq.spark.SparkSessionManager;

import java.util.Arrays;
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
        if (this.target.getTableLocation() == null || this.target.getTableLocation().equals("null")) {
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
        String pathIn = this.target.getPathIn().substring(this.target.getPathIn().indexOf(".") + 1);
        String location = this.target.getTableLocation();
        long rowID = this.target.getTopLevelID();
        int startOfArrayIndexing = pathIn.indexOf("[");

        if (startOfArrayIndexing == -1) {
            String setClause = "SET " + pathIn + " = ";
            this.applyItem();
            setClause = setClause + this.target.getSparkSQLValue();

            String query = "UPDATE delta.`" + location + "` " + setClause + " WHERE rowID == " + rowID;

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
        return null;
    }

    @Override
    public boolean isDeleteArray() {
        return true;
    }
}
