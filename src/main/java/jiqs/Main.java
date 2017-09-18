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


import jiqs.spark.SparkContextManager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        String config = args[0];
        String masterConfig = args[1];
        String outputDataFilePath = args[2];
        String queryFilePath = "";
        String logFilePath = "";
        int outputItemLimit = 200;
        if(args.length > 3)
            queryFilePath = args[3];
        if(args.length > 4)
            logFilePath =  args[4];
        if(args.length > 5)
            outputItemLimit =  Integer.valueOf(args[5]);
        initializeApplication(masterConfig);
        if(config.equals("local")) {
            System.out.println("Running in local mode");
            runQueryExecutor(queryFilePath, outputDataFilePath, true, logFilePath, outputItemLimit);
        }
        else{
            System.out.println("Running in remote mode");
            runQueryExecutor(queryFilePath, outputDataFilePath, false, logFilePath, outputItemLimit);
        }
    }

    private static void runQueryExecutor(String queryFile, String outputDataFilePath,
                                         boolean local, String logFilePath, int outputItemLimit) throws IOException {
        JsoniqQueryExecutor translator;
        if(logFilePath.isEmpty())
            translator = new JsoniqQueryExecutor(local, outputItemLimit);
        else
            translator = new JsoniqQueryExecutor(local, 200, logFilePath);
        if(local) {
            String result = translator.runLocal(queryFile);
            List<String> lines = Arrays.asList(result);
            Path file = Paths.get(outputDataFilePath);
            Files.write(file, lines, Charset.forName("UTF-8"));
        } else {
            translator.run(queryFile, outputDataFilePath);
        }
    }

    private static void initializeApplication(String masterConfig) {
        SparkContextManager.getInstance().initializeConfigurationAndContext(masterConfig);
    }

}
