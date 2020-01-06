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

package org.rumbledb.config;

import sparksoniq.exceptions.CliException;
import sparksoniq.spark.SparkSessionManager;

import java.util.HashMap;

public class SparksoniqRuntimeConfiguration {

    public static final String ARGUMENT_PREFIX = "--";
    private static final String ARGUMENT_FORMAT_ERROR_MESSAGE =
        "Invalid argument format. Required format: --property value";
    private HashMap<String, String> _arguments;

    public SparksoniqRuntimeConfiguration(String[] args) {
        _arguments = new HashMap<>();
        for (int i = 0; i < args.length; i += 2)
            if (args[i].startsWith(ARGUMENT_PREFIX))
                _arguments.put(args[i].trim().replace(ARGUMENT_PREFIX, ""), args[i + 1]);
            else
                throw new CliException(ARGUMENT_FORMAT_ERROR_MESSAGE);
    }

    public String getConfigurationArgument(String key) {
        if (this._arguments.containsKey(key))
            return this._arguments.get(key);
        else
            return null;
    }

    public String getOutputPath() {
        if (this._arguments.containsKey("output-path"))
            return this._arguments.get("output-path");
        else
            return null;
    }

    public boolean getOverwrite() {
        if (this._arguments.containsKey("overwrite"))
            return this._arguments.get("overwrite").equals("yes");
        else
            return false;
    }

    public boolean getErrorInfo() {
        if (this._arguments.containsKey("error-info"))
            return this._arguments.get("error-info").equals("yes");
        else
            return false;
    }

    public String getLogPath() {
        if (this._arguments.containsKey("log-path"))
            return this._arguments.get("log-path");
        else
            return null;
    }

    public String getQueryPath() {
        if (this._arguments.containsKey("query-path"))
            return this._arguments.get("query-path");
        else
            return null;
    }

    public int getResultSizeCap() {
        if (this._arguments.containsKey("result-size"))
            return Integer.parseInt(this._arguments.get("result-size"));
        else
            return 200;
    }

    public boolean isShell() {
        if (this._arguments.containsKey("shell"))
            return _arguments.get("shell").equals("yes");
        else
            return false;
    }

    public boolean isPrintIteratorTree() {
        if (this._arguments.containsKey("print-iterator-tree"))
            return _arguments.get("print-iterator-tree").equals("yes");
        else
            return false;
    }

    public boolean isLocal() {
        String masterConfig = SparkSessionManager.getInstance().getJavaSparkContext().getConf().get("spark.master");
        return masterConfig.contains("local");
    }

    @Override
    public String toString() {
        String result = "";
        result += "Master: "
            + SparkSessionManager.getInstance().getJavaSparkContext().getConf().get("spark.master")
            + "\n"
            +
            "Item Display Limit: "
            + (_arguments.getOrDefault("result-size", "-"))
            + "\n"
            +
            "Output Path: "
            + (_arguments.getOrDefault("output-path", "-"))
            + "\n"
            +
            "Log Path: "
            + (_arguments.getOrDefault("log-path", "-"))
            + "\n"
            +
            "Query Path : "
            + (_arguments.getOrDefault("query-path", "-"))
            + "\n";
        return result;
    }
}
