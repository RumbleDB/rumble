package org.rumbledb.runtime.update.primitives;

import org.apache.spark.sql.AnalysisException;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.*;
import org.rumbledb.items.ItemFactory;
import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class InsertIntoObjectPrimitive implements UpdatePrimitive {

    private Item target;
    private Item content;
    private Collection collection;


    public InsertIntoObjectPrimitive(Item targetObject, Item contentObject, ExceptionMetadata metadata) {
        for (String key : contentObject.getKeys()) {
            if (targetObject.getItemByKey(key) != null) {
                throw new DuplicateKeyOnUpdateApplyException(
                        "cannot insert a key already present in an object",
                        metadata
                );
            }
        }
        this.target = targetObject;
        this.content = contentObject;
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

        String tempPathIn = this.target.getPathIn() + ".";
        String pathIn = tempPathIn.substring(tempPathIn.indexOf(".") + 1);
        String location = this.collection.getPhysicalName();
        long rowID = this.target.getTopLevelID();
        int startOfArrayIndexing = pathIn.indexOf("[");

        if (startOfArrayIndexing == -1) {
            List<String> columnsClauseList = new ArrayList<>();
            List<String> setClauseList = new ArrayList<>();
            List<String> keys = this.content.getKeys();
            List<Item> values = this.content.getValues();
            for (int i = 0; i < keys.size(); i++) {
                columnsClauseList.add(pathIn + keys.get(i) + " " + values.get(i).getSparkSQLType());
                setClauseList.add(pathIn + keys.get(i) + " = " + values.get(i).getSparkSQLValue());
            }

            String setClauses = String.join(", ", setClauseList);

            List<String> insertColumnQueries = columnsClauseList.stream()
                .map(c -> "ALTER TABLE " + location + " ADD COLUMNS (" + c + ");")
                .collect(Collectors.toList());

            String setFieldQuery = "UPDATE "
                + location
                + " SET "
                + setClauses
                + " WHERE `"
                + SparkSessionManager.rowIdColumnName
                + "` == "
                + rowID;

            SparkSessionManager manager = SparkSessionManager.getInstance();

            // SKIP CREATING NEW COL FOR COL THAT ALREADY EXISTS
            for (String insertColumnQuery : insertColumnQueries) {
                try {
                    manager.getOrCreateSession().sql(insertColumnQuery);
                } catch (Exception e) {
                    if (!(e instanceof AnalysisException)) {
                        throw e;
                    }
                }
            }
            manager.getOrCreateSession().sql(setFieldQuery);
        } else {
            this.arrayIndexingApplyDelta();

            // TODO: Add new column for changed array and do the null trick -- do not forget the nullable etc
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

    @Override
    public boolean updatesSchemaDelta() {
        return true;
    }

    @Override
    public void arrayIndexingUpdateSchemaDelta() {
        String tempPathIn = this.target.getPathIn() + ".";
        String pathIn = tempPathIn.substring(tempPathIn.indexOf(".") + 1);
        String location = this.collection.getPhysicalName();

        String pathInSchema = pathIn.replaceAll("\\[\\d+]", ".element");

        List<String> columnsClauseList = new ArrayList<>();
        List<String> keys = this.content.getKeys();
        List<Item> values = this.content.getValues();
        for (int i = 0; i < keys.size(); i++) {
            columnsClauseList.add(pathInSchema + keys.get(i) + " " + values.get(i).getSparkSQLType());
        }

        List<String> insertColumnQueries = columnsClauseList.stream()
            .map(c -> "ALTER TABLE " + location + " ADD COLUMNS (" + c + ");")
            .collect(Collectors.toList());

        SparkSessionManager manager = SparkSessionManager.getInstance();

        // SKIP CREATING NEW COL FOR COL THAT ALREADY EXISTS
        for (String insertColumnQuery : insertColumnQueries) {
            try {
                manager.getOrCreateSession().sql(insertColumnQuery);
            } catch (Exception e) {
                if (!(e instanceof AnalysisException)) {
                    throw e;
                }
            }
        }
    }

    public static Item mergeSources(Item first, Item second, ExceptionMetadata metadata) {
        Item res;

        List<String> keys = new ArrayList<>(first.getKeys());
        keys.addAll(second.getKeys());

        List<Item> values = new ArrayList<>(first.getValues());
        values.addAll(second.getValues());

        try {
            res = ItemFactory.getInstance().createObjectItem(keys, values, metadata, false);
        } catch (DuplicateObjectKeyException e) {
            throw new DuplicateObjectInsertSourceException(e.getMessage(), metadata);
        }

        return res;
    }
}
