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
package org.rumbledb.cli;


import org.apache.spark.SparkException;
import org.rumbledb.config.SparksoniqRuntimeConfiguration;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SparksoniqRuntimeException;
import org.rumbledb.shell.RumbleJLineShell;
import sparksoniq.spark.SparkSessionManager;

import java.io.IOException;

public class Main {
    public static RumbleJLineShell terminal = null;

    public static void main(String[] args) throws IOException {
        SparksoniqRuntimeConfiguration sparksoniqConf = null;
        // Parse arguments
        try {
            sparksoniqConf = new SparksoniqRuntimeConfiguration(args);

            if (sparksoniqConf.isShell()) {
                initializeApplication();
                launchShell(sparksoniqConf);
            } else if (sparksoniqConf.getQueryPath() != null) {
                initializeApplication();
                runQueryExecutor(sparksoniqConf);
            } else {
                System.out.println("    ____                  __    __   ");
                System.out.println("   / __ \\__  ______ ___  / /_  / /__ ");
                System.out.println("  / /_/ / / / / __ `__ \\/ __ \\/ / _ \\");
                System.out.println(" / _, _/ /_/ / / / / / / /_/ / /  __/");
                System.out.println("/_/ |_|\\__,_/_/ /_/ /_/_.___/_/\\___/ ");
                System.out.println("Usage:");
                System.out.println("spark-submit <Spark arguments> <path to rumble jar> <Rumble arguments>");
                System.out.println("");
                System.out.println("Example usage:");
                System.out.println("spark-submit spark-rumble-1.0.jar --shell yes");
                System.out.println("spark-submit --master local[*] spark-rumble-1.0.jar --shell yes");
                System.out.println("spark-submit --master local[2] spark-rumble-1.0.jar --shell yes");
                System.out.println(
                    "spark-submit --master local[*] --driver-memory 10G spark-rumble-1.0.jar --shell yes"
                );
                System.out.println("");
                System.out.println("spark-submit --master yarn sparksoniq-0.9.7.jar --shell yes");
                System.out.println(
                    "spark-submit --master yarn --executor-cores 3 --executor-memory 5G spark-rumble-1.0.jar --shell yes"
                );
                System.out.println("spark-submit --master local[*] spark-rumble-1.0.jar --query-path my-query.jq");
                System.out.println("spark-submit --master local[*] spark-rumble-1.0.jar --query-path my-query.jq");
                System.out.println(
                    "spark-submit --master yarn --executor-cores 3 --executor-memory 5G spark-rumble-1.0.jar --query-path hdfs:///my-query.jq --output-path hdfs:///my-output.json"
                );
                System.out.println(
                    "spark-submit --master local[*] spark-rumble-1.0.jar --query-path my-query.jq --output-path my-output.json --log-path my-log.txt"
                );
            }
        } catch (Exception ex) {
            handleException(ex, sparksoniqConf.getShowErrorInfo());
        }
    }

    private static void handleException(Throwable ex, boolean showErrorInfo) {
        if (ex != null) {
            if (ex instanceof SparkException) {
                Throwable sparkExceptionCause = ex.getCause();
                handleException(sparkExceptionCause, showErrorInfo);
            } else if (ex instanceof SparksoniqRuntimeException && !(ex instanceof OurBadException)) {
                System.err.println("⚠️  ️" + ex.getMessage());
                if (showErrorInfo) {
                    ex.printStackTrace();
                }
            } else {
                System.out.println("An error has occured: " + ex.getMessage());
                System.out.println(
                    "We should investigate this 🙈. Please contact us or file an issue on GitHub with your query."
                );
                System.out.println("Link: https://github.com/RumbleDB/rumble/issues");
                if (showErrorInfo) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static void runQueryExecutor(SparksoniqRuntimeConfiguration sparksoniqConf) throws IOException {

        JsoniqQueryExecutor translator;
        translator = new JsoniqQueryExecutor(sparksoniqConf.isLocal(), sparksoniqConf);
        if (sparksoniqConf.isLocal()) {
            System.out.println("Running in local mode");
            translator.runLocal(sparksoniqConf.getQueryPath(), sparksoniqConf.getOutputPath());
        } else {
            System.out.println("Running in remote mode");
            translator.run(sparksoniqConf.getQueryPath(), sparksoniqConf.getOutputPath());
        }
    }

    private static void initializeApplication() {
        SparkSessionManager.getInstance().initializeConfigurationAndSession();
    }

    private static void launchShell(SparksoniqRuntimeConfiguration sparksoniqConf) throws IOException {
        terminal = new RumbleJLineShell(sparksoniqConf);
        terminal.launch();
    }

    public static void printMessageToLog(String message) {
        if (Main.terminal == null) {
            System.out.println(message);
        } else {
            Main.terminal.output(message);
        }
    }

}
