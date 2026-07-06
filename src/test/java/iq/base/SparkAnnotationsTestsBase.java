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
 */

package iq.base;

import org.apache.spark.SparkConf;
import org.junit.jupiter.api.BeforeAll;
import scala.util.Properties;
import org.rumbledb.spark.SparkSessionManager;

public abstract class SparkAnnotationsTestsBase extends AnnotationsTestsBase {

    @BeforeAll
    final void setupSparkSession() {
        SparkSessionManager.getInstance().resetSession();
        System.err.println("Java version: " + System.getProperty("java.version"));
        System.err.println("Scala version: " + Properties.scalaPropOrElse("version.number", () -> "unknown"));

        SparkConf sparkConfiguration = new SparkConf();
        sparkConfiguration.setMaster("local[*]");
        sparkConfiguration.set("spark.submit.deployMode", "client");
        sparkConfiguration.set("spark.executor.extraClassPath", "lib/");
        sparkConfiguration.set("spark.driver.extraClassPath", "lib/");
        sparkConfiguration.set("spark.sql.crossJoin.enabled", "true");
        sparkConfiguration.set("spark.driver.host", "127.0.0.1");
        configureSpark(sparkConfiguration);

        SparkSessionManager.getInstance().initializeConfigurationAndSession(sparkConfiguration, true);
        System.err.println("Spark version: " + SparkSessionManager.getInstance().getJavaSparkContext().version());
    }

    protected void configureSpark(SparkConf sparkConfiguration) {
        // Most annotation suites use the common Spark configuration unchanged.
    }
}
