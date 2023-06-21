package org.rumbledb.runtime.update.primitives;

import io.delta.tables.DeltaTable;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotResolveUpdateSelectorException;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.*;

public class DeleteFromObjectPrimitive implements UpdatePrimitive {

    private Item target;
    private List<Item> content;

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
        for (
                String str : this.content.stream().map(Item::getStringValue).collect(Collectors.toList())
        ) {
            this.target.removeItemByKey(str);
        }
    }

    public void applyDF(DeltaTable deltaTable, Dataset<Row> dataframe, String targetName) {
//        String pathIn = this.target.getPathIn();
//        List<String> fieldsToDel = this.content.stream().map(i -> pathIn + "." + i.getStringValue()).collect(Collectors.toList());
//        Map<String, Column> colMap = new HashMap<>();
//        Map<String, String> matchMap = new HashMap<>();
//        for (String field : fieldsToDel) {
//            colMap.put(targetName, when(col("rowID").equalTo(this.target.getTopLevelID()), col(targetName).withField(field, lit(null))).otherwise(col(targetName)));
//            matchMap.put(targetName + "." + field, "updates." + "." + field);
//        }
//
//        dataframe = dataframe.withColumns(colMap);
//
//        deltaTable.as("original").merge(
//                dataframe.as("updates"),
//                ("original.rowID = updates.rowID")
//        ).whenMatched().updateExpr(matchMap).execute();
    }

    @Override
    public void applyDelta() {
        String pathIn = this.target.getPathIn();
        String location = this.target.getTableLocation();
        long rowID = this.target.getTopLevelID();

        List<String> setFieldsToNulls = this.content.stream().map(i -> pathIn + "." + i.getStringValue() + " = NULL").collect(Collectors.toList());
        String concatSetNulls = String.join(", ", setFieldsToNulls);

        String query = "UPDATE delta.`" + location + "` SET " + concatSetNulls + " WHERE rowID == " + rowID;

        SparkSessionManager.getInstance().getOrCreateSession().sql(query);
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

    public static List<Item> mergeSources(List<Item> first, List<Item> second) {
        List<Item> merged = new ArrayList<>(first);
        merged.addAll(second);
        merged = merged.stream().distinct().collect(Collectors.toList());
        return merged;
    }
}
