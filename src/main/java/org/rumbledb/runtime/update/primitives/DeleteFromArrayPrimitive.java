package org.rumbledb.runtime.update.primitives;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotResolveUpdateSelectorException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import sparksoniq.spark.SparkSessionManager;


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
            String setClause = "SET " + pathIn + " = ";
            this.applyItem();
            setClause = setClause + this.target.getSparkSQLValue(arrayType);

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
