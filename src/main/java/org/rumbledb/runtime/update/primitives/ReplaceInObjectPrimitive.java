package org.rumbledb.runtime.update.primitives;

import io.delta.tables.DeltaTable;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotResolveUpdateSelectorException;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.spark.SparkSessionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.*;

public class ReplaceInObjectPrimitive implements UpdatePrimitive {

    private Item target;
    private Item selector;
    private Item content;

    public ReplaceInObjectPrimitive(
            Item targetObject,
            Item targetName,
            Item replacementItem,
            ExceptionMetadata metadata
    ) {

        if (targetObject.getItemByKey(targetName.getStringValue()) == null) {
            throw new CannotResolveUpdateSelectorException(
                    "Cannot delete key that does not exist in target object",
                    metadata
            );
        }

        this.target = targetObject;
        this.selector = targetName;
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
        String name = this.getSelector().getStringValue();
        if (this.getTarget().getKeys().contains(name)) {
            this.getTarget().removeItemByKey(name);
            this.getTarget().putItemByKey(name, this.getContent());
        }
    }

    public void applyDF(DeltaTable deltaTable, Dataset<Row> dataframe, String targetName) {
//        String pathIn = this.target.getPathIn();
//        String fieldToRep = pathIn + "." + this.selector.getStringValue();
//
//        dataframe = dataframe.withColumn(targetName, when(col("rowID").equalTo(this.target.getTopLevelID()), col(targetName).withField(fieldToRep, lit(this.content))).otherwise(col(targetName)));
//
//        deltaTable.as("original").merge(
//                dataframe.as("updates"),
//                ("original.rowID = updates.rowID")
//        ).whenMatched().updateExpr(
//                new HashMap<String, String>() {{
//                    put(targetName + "." + fieldToRep, "updates." + fieldToRep);
//                }}
//        ).execute();
    }

    @Override
    public void applyDelta() {
        // TODO: Sort out diff types of content Item
        // PERHAPS CHANGE OF TYPE SHOULD CREATE NEW COLUMN?

        String pathIn = this.target.getPathIn();
        String location = this.target.getTableLocation();
        long rowID = this.target.getTopLevelID();

        String setField = pathIn + this.selector.getStringValue() + " = " + this.content.getSparkSQLValue();

        String query = "UPDATE delta.`" + location + "` SET " + setField + " WHERE rowID == " + rowID;

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
    public Item getContent() {
        return this.content;
    }

    @Override
    public boolean isReplaceObject() {
        return true;
    }
}
