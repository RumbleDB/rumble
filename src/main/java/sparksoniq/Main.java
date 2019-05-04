/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package sparksoniq;


import sparksoniq.config.SparksoniqRuntimeConfiguration;
import sparksoniq.exceptions.CliException;
import sparksoniq.spark.SparkSessionManager;

import java.io.IOException;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException {
        HashMap<String, String> arguments;
        String outputDataFilePath, queryFilePath = "", logFilePath = "";
        int outputItemLimit = 200;
        try {
            arguments = SparksoniqRuntimeConfiguration.processCommandLineArgs(args);
            outputDataFilePath = arguments.get("output-path");
            initializeApplication();
            if (arguments.containsKey("query-path"))
                queryFilePath = arguments.get("query-path");
            if (arguments.containsKey("log-path"))
                logFilePath = arguments.get("log-path");
            if (arguments.containsKey("result-size"))
                outputItemLimit = Integer.valueOf(arguments.get("result-size"));

        } catch (Exception ex) {
            throw new CliException(ex.getMessage());
        }

        String masterConfig = SparkSessionManager.getInstance().getJavaSparkContext().getConf().get("spark.master");

        if (masterConfig.contains("local")) {
            System.out.println("Running in local mode");
            runQueryExecutor(queryFilePath, outputDataFilePath, true, logFilePath, outputItemLimit);
        } else {
            System.out.println("Running in remote mode");
            runQueryExecutor(queryFilePath, outputDataFilePath, false, logFilePath, outputItemLimit);
        }
    }

    private static void runQueryExecutor(String queryFile, String outputDataFilePath,
                                         boolean local, String logFilePath, int outputItemLimit) throws IOException {
        JsoniqQueryExecutor translator;
        if (logFilePath.isEmpty())
            translator = new JsoniqQueryExecutor(local, outputItemLimit);
        else
            translator = new JsoniqQueryExecutor(local, outputItemLimit, logFilePath);
        if (local) {
            translator.runLocal(queryFile, outputDataFilePath);
        } else {
            translator.run(queryFile, outputDataFilePath);
        }
    }

    private static void initializeApplication() {
        SparkSessionManager.getInstance().initializeConfigurationAndSession();
    }

}
