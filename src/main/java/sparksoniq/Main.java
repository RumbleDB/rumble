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
import sparksoniq.io.shell.JiqsJLineShell;
import sparksoniq.spark.SparkSessionManager;

import java.io.IOException;

public class Main {
    public static JiqsJLineShell terminal = null;

    public static void main(String[] args) throws IOException {
        SparksoniqRuntimeConfiguration sparksoniqConf = null;
        // Parse arguments
        try {
            sparksoniqConf = new SparksoniqRuntimeConfiguration(args);

            if(sparksoniqConf.isShell())
            {
                initializeApplication();
                launchShell(sparksoniqConf);
            } else if(sparksoniqConf.getQueryPath() != null) {
                initializeApplication();
                runQueryExecutor(sparksoniqConf);
            } else {
                System.out.println("************************");
                System.out.println("Sparksoniq version 0.9.7");
                System.out.println("************************");
                System.out.println("Usage:");
                System.out.println("spark-submit <Spark arguments> <path to sparksoniq jar> <Sparksoniq arguments>");
                System.out.println("");
                System.out.println("Example usage:");
                System.out.println("spark-submit sparksoniq-0.9.7.jar --shell yes");
                System.out.println("spark-submit --master local[*] sparksoniq-0.9.7.jar --shell yes");
                System.out.println("spark-submit --master local[2] sparksoniq-0.9.7.jar --shell yes");
                System.out.println("spark-submit --master local[*] --driver-memory 10G sparksoniq-0.9.7.jar --shell yes");
                System.out.println("");
                System.out.println("spark-submit --master yarn sparksoniq-0.9.7.jar --shell yes");
                System.out.println("spark-submit --master yarn --executor-cores 3 --executor-memory 5G sparksoniq-0.9.7.jar --shell yes");
                System.out.println("spark-submit --master local[*] sparksoniq-0.9.7.jar --query-path my-query.jq");
                System.out.println("spark-submit --master local[*] sparksoniq-0.9.7.jar --query-path my-query.jq");
                System.out.println("spark-submit --master yarn --executor-cores 3 --executor-memory 5G sparksoniq-0.9.7.jar --query-path hdfs:///my-query.jq --output-path hdfs:///my-output.json");
                System.out.println("spark-submit --master local[*] sparksoniq-0.9.7.jar --query-path my-query.jq --output-path my-output.json --log-path my-log.txt");
            }
        } catch (Exception ex) {
            throw new CliException(ex.getMessage());
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
        terminal = new JiqsJLineShell(sparksoniqConf);
        terminal.launch();
    }

}
