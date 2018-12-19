/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
package sparksoniq;

import java.io.IOException;
import java.util.HashMap;

import sparksoniq.config.SparksoniqRuntimeConfiguration;
import sparksoniq.exceptions.CliException;
import sparksoniq.io.shell.JiqsJLineShell;
import sparksoniq.spark.SparkContextManager;

/*
GENERIC LAUNCH COMMAND
spark-submit --class sparksoniq.ShellStart     --master yarn-client     --deploy-mode client
--num-executors 40 --conf spark.yarn.maxAppAttempts=1 --conf spark.ui.port=4051
--conf spark.executor.memory=10g --conf spark.executor.heartbeatInterval=3600s
--conf spark.network.timeout=3600s
jsoniq-spark-app-1.0-jar-with-dependencies.jar --result-size 1000


spark-submit --class sparksoniq.ShellStart  --master local[*]  --deploy-mode client jsoniq-spark-app-1.0-jar-with-dependencies.jar  --result-size 1000

 */

public class ShellStart {
    public static JiqsJLineShell terminal;
    public static void main(String[] args) throws IOException {
        HashMap<String, String> arguments;
        try {
            arguments = SparksoniqRuntimeConfiguration.processCommandLineArgs(args);
        } catch (CliException ex) {
            System.out.println(ex.getMessage());
            return;
        }

        SparkContextManager.getInstance().initializeConfigurationAndContext();
        if (arguments.containsKey("result-size")) {
            int itemLimit = Integer.parseInt(arguments.get("result-size"));
            terminal = new JiqsJLineShell(new SparksoniqRuntimeConfiguration(arguments), itemLimit);
            terminal.launch();
        } else
            terminal = new JiqsJLineShell(new SparksoniqRuntimeConfiguration(arguments));
        terminal.launch();
    }
}
