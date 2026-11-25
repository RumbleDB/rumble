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
package iq;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.FileUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.rumbledb.spark.SparkSessionManager;

import iq.base.UpdateRuntimeTestsBase;

public class HudiUpdateRuntimeTests extends UpdateRuntimeTestsBase {

    private static final String NON_HUDI_TABLE = "nothudi";

    public static final File runtimeTestsDirectory =
            new File(System.getProperty("user.dir") + "/src/test/resources/test_files/runtime-hudi-updates");

    @Override
    protected File testDirectory() {
        return runtimeTestsDirectory;
    }

    @Override
    protected void configureUpdateStore(SparkConf sparkConfiguration) {
        sparkConfiguration.set(
                "spark.sql.extensions", "org.apache.spark.sql.hudi.HoodieSparkSessionExtension"); // enables hudi
        sparkConfiguration.set("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.hudi.catalog.HoodieCatalog");
        sparkConfiguration.set("spark.sql.warehouse.dir", "./hudi-warehouse");
        // Hudi needs its own Kryo registrator. Rumble only sets its registrator when spark.serializer is unset,
        // so both are listed here.
        sparkConfiguration.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        sparkConfiguration.set(
                "spark.kryo.registrator",
                "org.rumbledb.spark.RumbleKryoRegistrator,org.apache.spark.HoodieSparkKryoRegistrar");
        // Needed for DROP COLUMN and RENAME COLUMN in nested updates.
        sparkConfiguration.set("hoodie.schema.on.read.enable", "true");
        // Hudi validates that a write keeps all existing columns, and treats RENAME COLUMN as dropping the old one.
        sparkConfiguration.set("hoodie.datasource.write.schema.allow.auto.evolution.column.drop", "true");
        // The Hive metastore behind spark_catalog rejects column type changes by default, e.g. a number replaced by
        // an object, or a new field inside a nested object.
        sparkConfiguration.set("spark.hadoop.hive.metastore.disallow.incompatible.col.type.changes", "false");
        // Markers through the embedded timeline server fail intermittently in local tests ("Failed to check marker
        // directory"), sometimes after a long hang. Direct markers on the file system avoid the server.
        sparkConfiguration.set("hoodie.write.markers.type", "DIRECT");
        sparkConfiguration.set("hoodie.embed.timeline.server", "false");
    }

    /**
     * A table in another format, for the wrong-format tests. Rumble cannot create it itself: table() writes Delta,
     * whose catalog is not configured in a Hudi session.
     */
    @BeforeAll
    void createNonHudiTable() throws Exception {
        SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();
        dropNonHudiTable(session);
        session.sql("CREATE TABLE " + NON_HUDI_TABLE + " (id BIGINT) USING parquet");
        session.sql("INSERT INTO " + NON_HUDI_TABLE + " VALUES (1)");
    }

    @AfterAll
    void dropNonHudiTable() throws Exception {
        dropNonHudiTable(SparkSessionManager.getInstance().getOrCreateSession());
    }

    /**
     * Drops the table and deletes its directory. DROP TABLE can leave the directory behind, for example after an
     * interrupted run, and CREATE TABLE then fails with LOCATION_ALREADY_EXISTS. The directory is looked up from the
     * default database, whose location the persistent metastore keeps.
     */
    private static void dropNonHudiTable(SparkSession session) throws AnalysisException, IOException {
        session.sql("DROP TABLE IF EXISTS " + NON_HUDI_TABLE);
        String warehouse = session.catalog().getDatabase("default").locationUri();
        FileUtils.deleteDirectory(new File(new File(URI.create(warehouse)), NON_HUDI_TABLE));
    }
}
