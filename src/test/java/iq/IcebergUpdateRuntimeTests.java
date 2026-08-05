/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package iq;

import iq.base.UpdateRuntimeTestsBase;
import org.apache.spark.SparkConf;

import java.io.File;

public class IcebergUpdateRuntimeTests extends UpdateRuntimeTestsBase {

    public static final File runtimeTestsDirectory = new File(
            System.getProperty("user.dir")
                +
                "/src/test/resources/test_files/runtime-iceberg-updates"
    );

    @Override
    protected File testDirectory() {
        return runtimeTestsDirectory;
    }

    @Override
    protected void configureUpdateStore(SparkConf sparkConfiguration) {
        sparkConfiguration.set(
            "spark.sql.extensions",
            "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions"
        ); // enables iceberg
        sparkConfiguration.set("spark.sql.catalog.spark_catalog", "org.apache.iceberg.spark.SparkSessionCatalog");
        sparkConfiguration.set("spark.sql.catalog.spark_catalog.type", "hadoop");
        sparkConfiguration.set("spark.sql.catalog.spark_catalog.warehouse", "./iceberg-warehouse/spark_catalog");
        sparkConfiguration.set("spark.sql.iceberg.check-ordering", "false");
    }
}
