package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotResolveUpdateSelectorException;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.spark.SparkSessionManager;

import static org.apache.spark.sql.functions.col;


public class ReplaceInObjectPrimitive implements UpdatePrimitive {

    private Item target;
    private Item selector;
    private Item content;
    private Collection collection;

    public ReplaceInObjectPrimitive(
            Item targetObject,
            Item targetName,
            Item replacementItem,
            ExceptionMetadata metadata
    ) {

        if (targetObject.getItemByKey(targetName.getStringValue()) == null) {
            throw new CannotResolveUpdateSelectorException(
                    "Cannot replace key that does not exist in target object",
                    metadata
            );
        }

        this.target = targetObject;
        this.selector = targetName;
        this.content = replacementItem;
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
        String name = this.getSelector().getStringValue();
        if (this.getTarget().getKeys().contains(name)) {
            this.getTarget().removeItemByKey(name);
            this.getTarget().putItemByKey(name, this.getContent());
        }
    }

    @Override
    public void applyDelta() {
        // TODO: Sort out diff types of content Item
        // PERHAPS CHANGE OF TYPE SHOULD CREATE NEW COLUMN?

        String tempPathIn = this.target.getPathIn() + ".";
        String pathIn = tempPathIn.substring(tempPathIn.indexOf(".") + 1);
        String location = this.collection.getPhysicalName();
        long rowID = this.target.getTopLevelID();
        int startOfArrayIndexing = pathIn.indexOf("[");

        if (startOfArrayIndexing == -1) {
            String fullFieldPath = pathIn + this.selector.getStringValue();
            if (this.collection.getMode() == Mode.ICEBERG || this.collection.getMode() == Mode.DELTA) {
                if (!fullFieldPath.contains(".")) {
                    String fieldName = this.selector.getStringValue();
                    String[] fieldNames = SparkSessionManager.getInstance()
                        .getOrCreateSession()
                        .table(location)
                        .schema()
                        .fieldNames();
                    int fieldIndex = -1;
                    for (int i = 0; i < fieldNames.length; i++) {
                        if (fieldNames[i].equals(fieldName)) {
                            fieldIndex = i;
                            break;
                        }
                    }
                    String previousFieldName = fieldIndex > 0 ? fieldNames[fieldIndex - 1] : null;
                    String currentType = SparkSessionManager.getInstance()
                        .getOrCreateSession()
                        .sql("DESC (SELECT " + fullFieldPath + " FROM " + location + ")")
                        .filter(col("col_name").equalTo(fieldName))
                        .select("data_type")
                        .collectAsList()
                        .get(0)
                        .getString(0);
                    String replacementType = this.content.getSparkSQLType();
                    String normalizedCurrentType = currentType.replaceAll("\\s+", "");
                    String normalizedReplacementType = replacementType.replaceAll("\\s+", "");
                    String normalizedReplacementTypeUpper = normalizedReplacementType.toUpperCase();
                    boolean isTypeChange = !normalizedCurrentType.equalsIgnoreCase(normalizedReplacementType);
                    boolean deltaNeedsComplexEvolution = this.collection.getMode() == Mode.DELTA
                        && (normalizedReplacementTypeUpper.startsWith("STRUCT<")
                            || normalizedReplacementTypeUpper.startsWith("ARRAY<"));
                    boolean needsSchemaEvolution = (this.collection.getMode() == Mode.ICEBERG && isTypeChange)
                        || (isTypeChange && deltaNeedsComplexEvolution);
                    if (needsSchemaEvolution) {
                        if (this.collection.getMode() == Mode.DELTA) {
                            SparkSessionManager.getInstance()
                                .getOrCreateSession()
                                .sql(
                                    "ALTER TABLE "
                                        + location
                                        + " SET TBLPROPERTIES ('delta.columnMapping.mode' = 'name')"
                                );
                        }
                        SparkSessionManager.getInstance()
                            .getOrCreateSession()
                            .sql("ALTER TABLE " + location + " DROP COLUMN " + fullFieldPath);
                        SparkSessionManager.getInstance()
                            .getOrCreateSession()
                            .sql(
                                "ALTER TABLE "
                                    + location
                                    + " ADD COLUMNS ("
                                    + fullFieldPath
                                    + " "
                                    + replacementType
                                    + ")"
                            );
                        if (fieldIndex == 0) {
                            SparkSessionManager.getInstance()
                                .getOrCreateSession()
                                .sql("ALTER TABLE " + location + " ALTER COLUMN " + fullFieldPath + " FIRST");
                        } else if (previousFieldName != null) {
                            SparkSessionManager.getInstance()
                                .getOrCreateSession()
                                .sql(
                                    "ALTER TABLE "
                                        + location
                                        + " ALTER COLUMN "
                                        + fullFieldPath
                                        + " AFTER "
                                        + previousFieldName
                                );
                        }
                    }
                }
            }
            this.applySetFieldInCollection(location, rowID, fullFieldPath, this.content.getSparkSQLValue());
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
    public boolean isReplaceObject() {
        return true;
    }
}
