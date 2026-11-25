/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.update.primitives;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.lit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.TableIdentifier;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.MapType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import org.rumbledb.api.Item;
import org.rumbledb.spark.SparkSessionManager;

public final class HudiTables {


    public static final String TABLE_LOCATION_PREFIX = "hudi-table:";

    /* Columns that are part of each Hudi record */
    public static final List<String> META_COLUMNS = List.of(
            "_hoodie_commit_time",
            "_hoodie_commit_seqno",
            "_hoodie_record_key",
            "_hoodie_partition_path",
            "_hoodie_file_name");

    /* Hudi stores its schemas in Avro, which only accepts names of this form */
    private static final Pattern AVRO_NAME = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*");

    private HudiTables() {}

    public static void checkNamingConvention(String key) {
        if (!AVRO_NAME.matcher(key).matches()) {
            throw new RuntimeException("Key \"" + key + "\" cannot be stored in a Hudi table. Hudi follows Avro's naming"
                + " rules.");
        }
    }

    public static void checkItemFieldNames(Item item) {
        if (item.isObject()) {
            List<String> keys = item.getStringKeys();
            List<Item> values = item.getItemValues();
            for (int i = 0; i < keys.size(); i++) {
                checkNamingConvention(keys.get(i));
                checkItemFieldNames(values.get(i));
            }
        } else if (item.isArray()) {
            for (Item member : item.getItemMembers()) {
                checkItemFieldNames(member);
            }
        }
    }

    public static void checkSchemaFieldNames(DataType type) {
        if (type instanceof StructType struct) {
            for (StructField field : struct.fields()) {
                checkNamingConvention(field.name());
                checkSchemaFieldNames(field.dataType());
            }
        } else if (type instanceof ArrayType array) {
            checkSchemaFieldNames(array.elementType());
        } else if (type instanceof MapType map) {
            checkSchemaFieldNames(map.valueType());
        }
    }

    public static String encodeTableLocation(String tableName) {
        return TABLE_LOCATION_PREFIX + tableName;
    }

    public static boolean isEncodedTableLocation(String tableLocation) {
        return tableLocation.startsWith(TABLE_LOCATION_PREFIX);
    }

    public static String decodeTableLocation(String tableLocation) {
        return tableLocation.substring(TABLE_LOCATION_PREFIX.length());
    }

    /**
     * Returns the format (provider) the catalog records for an existing table,
     * null if the table does not exist or has no provider
     */
    public static String provider(String tableName) {
        SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();
        if (!session.catalog().tableExists(tableName)) {
            return null;
        }
        // Read the metastore entry directly, tables without a Provider row.
        scala.Option<String> provider;
        try {
            TableIdentifier identifier = session.sessionState().sqlParser().parseTableIdentifier(tableName);
            provider = session.sessionState().catalog().getTableMetadata(identifier).provider();
        } catch (Exception e) {
            // Not a name the session catalog knows 
            return null;
        }
        return provider.isDefined() ? provider.get().toLowerCase() : null;
    }

    public static boolean isHudiTable(String tableName) {
        return "hudi".equals(provider(tableName));
    }

    public static void assertHudiTable(String tableName) {
        String provider = provider(tableName);
        if (provider != null && !provider.equals("hudi")) {
            throw new RuntimeException("Table '"
                + tableName
                + "' is not a Hudi table (its format is "
                + provider
                + "). Use the function that matches its format, such as table(), delta-file() or iceberg-table().");
        }
    }

    public static Dataset<Row> dropMetaColumns(Dataset<Row> dataFrame) {
        return dataFrame.drop(META_COLUMNS.toArray(new String[0]));
    }

    public static StructType dropMetaColumns(StructType schema) {
        return new StructType(Arrays.stream(schema.fields())
                .filter(field -> !META_COLUMNS.contains(field.name()))
                .toArray(StructField[]::new));
    }

    public static void create(String tableName, Dataset<Row> contents) {
        SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();
        String view = "rumble_hudi_" + UUID.randomUUID().toString().replace("-", "");
        contents.createOrReplaceTempView(view);
        try {
            session.sql("CREATE TABLE "
                    + tableName
                    + " USING hudi TBLPROPERTIES (type = 'cow', primaryKey = '"
                    + SparkSessionManager.rowIdColumnName
                    + "') AS SELECT * FROM "
                    + view);
        } finally {
            session.catalog().dropTempView(view);
        }
    }

    public static void append(String tableName, Dataset<Row> contents) {
        SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();
        contents = dropMetaColumns(contents);
        StructType tableSchema = dropMetaColumns(session.table(tableName).schema());

        Set<String> tableColumns =
                Arrays.stream(tableSchema.fieldNames()).collect(Collectors.toSet());
        List<String> newColumns = new ArrayList<>();
        for (StructField field : contents.schema().fields()) {
            if (!tableColumns.contains(field.name())) {
                newColumns.add("`" + field.name() + "` " + field.dataType().sql());
            }
        }
        if (!newColumns.isEmpty()) {
            session.sql("ALTER TABLE " + tableName + " ADD COLUMNS (" + String.join(", ", newColumns) + ")");
            tableSchema = dropMetaColumns(session.table(tableName).schema());
        }

        // insertInto is positional, alignToTable puts the columns in table order
        alignToTable(contents, tableSchema).write().mode("append").insertInto(tableName);
    }

    public static void upsert(String tableName, Dataset<Row> contents) {
        SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();
        StructType tableSchema = dropMetaColumns(session.table(tableName).schema());
        Dataset<Row> aligned = alignToTable(dropMetaColumns(contents), tableSchema);

        String view = "rumble_hudi_" + UUID.randomUUID().toString().replace("-", "");
        aligned.createOrReplaceTempView(view);
        try {
            session.sql("MERGE INTO "
                    + tableName
                    + " AS target USING "
                    + view
                    + " AS source ON target.`"
                    + SparkSessionManager.rowIdColumnName
                    + "` = source.`"
                    + SparkSessionManager.rowIdColumnName
                    + "` WHEN MATCHED THEN UPDATE SET *");
        } finally {
            session.catalog().dropTempView(view);
        }
    }

    private static Dataset<Row> alignToTable(Dataset<Row> contents, StructType tableSchema) {
        Set<String> contentColumns =
                Arrays.stream(contents.schema().fieldNames()).collect(Collectors.toSet());
        StructType contentSchema = contents.schema();
        List<Column> aligned = new ArrayList<>();
        for (StructField field : tableSchema.fields()) {
            Column column;
            if (!contentColumns.contains(field.name())) {
                column = lit(null).cast(field.dataType());
            } else if (contentSchema.apply(field.name()).dataType().equals(field.dataType())) {
                column = col("`" + field.name() + "`");
            } else {
                column = col("`" + field.name() + "`").cast(field.dataType());
            }
            aligned.add(column.as(field.name()));
        }
        return contents.select(aligned.toArray(new Column[0]));
    }
}
