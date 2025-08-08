package org.rumbledb.runtime.update.primitives;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotResolveUpdateSelectorException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import sparksoniq.spark.SparkSessionManager;

import static org.apache.spark.sql.functions.col;

public class RenameInObjectPrimitive implements UpdatePrimitive {

    private Item target;
    private Item selector;
    private Item content;

    public RenameInObjectPrimitive(
            Item targetObject,
            Item targetName,
            Item replacementName,
            ExceptionMetadata metadata
    ) {

        if (targetObject.getItemByKey(targetName.getStringValue()) == null) {
            throw new CannotResolveUpdateSelectorException(
                    "Cannot rename key that does not exist in target object",
                    metadata
            );
        }


        this.target = targetObject;
        this.selector = targetName;
        this.content = replacementName;
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
        String name = this.selector.getStringValue();
        if (this.target.getKeys().contains(name)) {
            Item item = this.target.getItemByKey(name);
            this.target.removeItemByKey(name);
            this.target.putItemByKey(this.content.getStringValue(), item);
        }
        // TODO: implement replace and rename methods for Array & Object to avoid deletion and append
    }

    @Override
    public void applyDelta() {
        String tempPathIn = this.target.getPathIn() + ".";
        String pathIn = tempPathIn.substring(tempPathIn.indexOf(".") + 1);
        String location = this.target.getTableLocation();
        long rowID = this.target.getTopLevelID();
        int startOfArrayIndexing = pathIn.indexOf("[");

        if (startOfArrayIndexing == -1) {
            String oldName = this.selector.getStringValue();

            String fullOldPath = pathIn + oldName;
            String fullNewPath = pathIn + this.content.getStringValue();

            String setOldFieldClause = fullOldPath + " = NULL";
            String setNewFieldClause = fullNewPath + " = " + this.target.getItemByKey(oldName).getSparkSQLValue();
            String type = SparkSessionManager.getInstance()
                .getOrCreateSession()
                .sql("DESC (SELECT " + fullOldPath + " FROM delta.`" + location + "`)")
                .filter(col("col_name").equalTo(oldName))
                .select("data_type")
                .collectAsList()
                .get(0)
                .getString(0);

            String insertNewColumnQuery = "ALTER TABLE delta.`"
                + location
                + "` ADD COLUMNS ("
                + fullNewPath
                + " "
                + type
                + ");";
            String setFieldsQuery = "UPDATE delta.`"
                + location
                + "` SET "
                + setOldFieldClause
                + ", "
                + setNewFieldClause
                + " WHERE `"
                + SparkSessionManager.rowIdColumnName
                + "` == "
                + rowID;

            // SKIP INSERTING NEW COL IF COL ALREADY EXISTS
            try {
                SparkSessionManager.getInstance().getOrCreateSession().sql(insertNewColumnQuery);
            } catch (Exception e) {
                if (!(e instanceof AnalysisException)) {
                    throw e;
                }
            }
            SparkSessionManager.getInstance().getOrCreateSession().sql(setFieldsQuery);
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
    public boolean isRenameObject() {
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
        String location = this.target.getTableLocation();
        long rowID = this.target.getTopLevelID();

        String selectColQuery = "SELECT "
            + pathIn
            + this.selector.getStringValue()
            + " AS `"
            + SparkSessionManager.atomicJSONiqItemColumnName
            + "` FROM delta.`"
            + location
            + "` WHERE `"
            + SparkSessionManager.rowIdColumnName
            + "` == "
            + rowID;

        Dataset<Row> colDF = SparkSessionManager.getInstance().getOrCreateSession().sql(selectColQuery);

        ItemType colType = ItemTypeFactory.createItemType(colDF.schema())
            .getObjectContentFacet()
            .get(SparkSessionManager.atomicJSONiqItemColumnName)
            .getType();

        String pathInSchema = pathIn.replaceAll("\\[\\d+]", ".element");
        String fullNewPath = pathInSchema + this.content.getStringValue();

        String insertNewColumnQuery = "ALTER TABLE delta.`"
            + location
            + "` ADD COLUMNS ("
            + fullNewPath
            + " "
            + colType.getSparkSQLType()
            + ");";

        // SKIP INSERTING NEW COL IF COL ALREADY EXISTS
        try {
            SparkSessionManager.getInstance().getOrCreateSession().sql(insertNewColumnQuery);
        } catch (Exception e) {
            if (!(e instanceof AnalysisException)) {
                throw e;
            }
        }
    }
}
