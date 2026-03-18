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
    private Collection collection;

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
        String location = this.collection.getPhysicalName();
        long rowID = this.target.getTopLevelID();
        int startOfArrayIndexing = pathIn.indexOf("[");

        if (startOfArrayIndexing == -1) {
            String oldName = this.selector.getStringValue();

            String fullOldPath = pathIn + oldName;
            String fullNewPath = pathIn + this.content.getStringValue();

            if (!fullOldPath.contains(".") && !fullNewPath.contains(".")) {
                String renameColumnQuery = "ALTER TABLE "
                    + location
                    + " RENAME COLUMN "
                    + fullOldPath
                    + " TO "
                    + fullNewPath;
                try {
                    SparkSessionManager.getInstance().getOrCreateSession().sql(renameColumnQuery);
                    return;
                } catch (Exception e) {
                    if (e.getMessage() != null && e.getMessage().contains("columnMapping")) {
                        SparkSessionManager.getInstance()
                            .getOrCreateSession()
                            .sql(
                                "ALTER TABLE "
                                    + location
                                    + " SET TBLPROPERTIES ('delta.columnMapping.mode' = 'name')"
                            );
                        SparkSessionManager.getInstance().getOrCreateSession().sql(renameColumnQuery);
                        return;
                    }
                }
            }

            String type = SparkSessionManager.getInstance()
                .getOrCreateSession()
                .sql("DESC (SELECT " + fullOldPath + " FROM " + location + ")")
                .filter(col("col_name").equalTo(oldName))
                .select("data_type")
                .collectAsList()
                .get(0)
                .getString(0);
            String typedNewValue = "CAST(" + this.target.getItemByKey(oldName).getSparkSQLValue() + " AS " + type + ")";

            String insertNewColumnQuery = "ALTER TABLE "
                + location
                + " ADD COLUMNS ("
                + fullNewPath
                + " "
                + type
                + ");";

            // SKIP INSERTING NEW COL IF COL ALREADY EXISTS
            try {
                SparkSessionManager.getInstance().getOrCreateSession().sql(insertNewColumnQuery);
            } catch (Exception e) {
                if (!(e instanceof AnalysisException)) {
                    throw e;
                }
            }
            this.applySetFieldInCollection(location, rowID, fullOldPath, "CAST(NULL AS " + type + ")");
            this.applySetFieldInCollection(location, rowID, fullNewPath, typedNewValue);
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
        String location = this.collection.getPhysicalName();
        long rowID = this.target.getTopLevelID();

        String selectColQuery = "SELECT "
            + pathIn
            + this.selector.getStringValue()
            + " AS `"
            + SparkSessionManager.nonObjectJSONiqItemColumnName
            + "` FROM "
            + location
            + " WHERE `"
            + SparkSessionManager.rowIdColumnName
            + "` == "
            + rowID;

        Dataset<Row> colDF = SparkSessionManager.getInstance().getOrCreateSession().sql(selectColQuery);

        // Column type is not an object
        ItemType colType = ItemTypeFactory.createItemType(colDF.schema());

        String pathInSchema = pathIn.replaceAll("\\[\\d+]", ".element");
        String fullNewPath = pathInSchema + this.content.getStringValue();

        String insertNewColumnQuery = "ALTER TABLE "
            + location
            + " ADD COLUMNS ("
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
