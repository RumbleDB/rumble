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
 package jiqs;

import jiqs.config.RuntimeConfiguration;
import jiqs.io.shell.JiqsJLineShell;
import jiqs.spark.SparkContextManager;

import java.io.IOException;

/*
GENERIC LAUNCH COMMAND
spark-submit --class jiqs.ShellStart     --master yarn-client     --deploy-mode client --num-executors 50 --conf spark.yarn.maxAppAttempts=1 --conf spark.ui.port=4050 jsoniq-spark-app-1.0-jar-with-dependencies.jar yarn-client

 */
public class ShellStart {
    public static void main(String[] args) throws IOException {
        String masterConfig = "local[*]";
        if(args.length >= 1)
            masterConfig = args[0];
        SparkContextManager.getInstance().initializeConfigurationAndContext(masterConfig);
        if(args.length >= 2) {
            int itemLimit = Integer.valueOf(args[1]);
            new JiqsJLineShell(new RuntimeConfiguration(null), itemLimit).launch();
        } else
            new JiqsJLineShell(new RuntimeConfiguration(null)).launch();
    }
}
