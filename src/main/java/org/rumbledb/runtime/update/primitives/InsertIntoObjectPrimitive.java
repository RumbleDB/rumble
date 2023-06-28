package org.rumbledb.runtime.update.primitives;

import com.amazonaws.services.dynamodbv2.xspec.S;
import org.apache.spark.sql.AnalysisException;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.*;
import org.rumbledb.items.ItemFactory;
import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.col;

public class InsertIntoObjectPrimitive implements UpdatePrimitive {

    private Item target;
    private Item content;


    public InsertIntoObjectPrimitive(Item targetObject, Item contentObject) {
        if (!targetObject.isObject() || !contentObject.isObject()) {
            // TODO: ERROR
        }
        this.target = targetObject;
        this.content = contentObject;
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
        try {
            for (String key : this.content.getKeys()) {
                this.target.putItemByKey(key, this.content.getItemByKey(key));
            }
        } catch (DuplicateObjectKeyException e) {
            throw new DuplicateKeyOnUpdateApplyException(e.getMessage(), ExceptionMetadata.EMPTY_METADATA);
        }
    }

    @Override
    public void applyDelta() {
        // TODO: Properly discern ItemType to SQLType
        // TODO: Sort out types for value item

        String pathIn = this.target.getPathIn();
        String location = this.target.getTableLocation();
        long rowID = this.target.getTopLevelID();

        List<String> columnsClauseList = new ArrayList<>();
        List<String> setClauseList = new ArrayList<>();
        List<String> keys = this.content.getKeys();
        List<Item> values = this.content.getValues();
        for (int i = 0; i < keys.size(); i++) {
            columnsClauseList.add(pathIn + keys.get(i) + " " + values.get(i).getSparkSQLType());
            setClauseList.add(pathIn + keys.get(i) + " = " + values.get(i).getSparkSQLValue());
        }

//        String columnsClause = String.join(", ", columnsClauseList);
        String setClauses = String.join(", ", setClauseList);

        // Perhaps try to insert each -- check if exists and ignore if it does
        List<String> insertColumnQueries = columnsClauseList.stream().map(c -> "ALTER TABLE delta.`" + location + "` ADD COLUMNS (" + c + ");").collect(Collectors.toList());

//        String insertColumnQuery = "ALTER TABLE delta.`" + location + "` ADD COLUMNS (" + columnsClause + ");";
        String setFieldQuery = "UPDATE delta.`" + location + "` SET " + setClauses + " WHERE rowID == " + rowID;

        SparkSessionManager manager = SparkSessionManager.getInstance();

        // SKIP CREATING NEW COL FOR COL THAT ALREADY EXISTS
        for (String insertColumnQueryAlt : insertColumnQueries) {
            try {
                manager.getOrCreateSession().sql(insertColumnQueryAlt);
            } catch (Exception e) {
                if (!(e instanceof AnalysisException)) {
                    throw e;
                }
            }
        }
//        manager.getOrCreateSession().sql(insertColumnQuery);
        manager.getOrCreateSession().sql(setFieldQuery);
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
    public Item getSelector() {
        throw new OurBadException("INVALID SELECTOR GET IN INSERTINTOOBJECT PRIMITIVE");
    }

    @Override
    public Item getContent() {
        return this.content;
    }

    @Override
    public boolean isInsertObject() {
        return true;
    }

    public static Item mergeSources(Item first, Item second, ExceptionMetadata metadata) {
        Item res;

        List<String> keys = new ArrayList<>(first.getKeys());
        keys.addAll(second.getKeys());

        List<Item> values = new ArrayList<>(first.getValues());
        values.addAll(second.getValues());

        try {
            res = ItemFactory.getInstance().createObjectItem(keys, values, metadata);
        } catch (DuplicateObjectKeyException e) {
            throw new DuplicateObjectInsertSourceException(e.getMessage(), metadata);
        }

        return res;
    }
}
