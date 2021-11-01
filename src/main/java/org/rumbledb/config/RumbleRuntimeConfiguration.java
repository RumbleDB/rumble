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

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.CliException;
import org.rumbledb.serialization.Serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import sparksoniq.spark.SparkSessionManager;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RumbleRuntimeConfiguration implements Serializable, KryoSerializable {

    private static final long serialVersionUID = 1L;
    private static final String ARGUMENT_PREFIX = "--";
    private static final String ARGUMENT_FORMAT_ERROR_MESSAGE =
        "Invalid argument format. Required format: --property value";
    private HashMap<String, String> arguments;

    List<String> allowedPrefixes;
    private int resultsSizeCap;
    private String outputFormat;
    private Map<String, String> outputFormatOptions;
    private int numberOfOutputPartitions;
    private Map<Name, List<Item>> externalVariableValues;
    private Map<Name, String> unparsedExternalVariableValues;
    private boolean checkReturnTypeOfBuiltinFunctions;
    private String queryPath;
    private String outputPath;
    private String logPath;
    private String query;
    private String shell;


    private static final RumbleRuntimeConfiguration defaultConfiguration = new RumbleRuntimeConfiguration();

    public RumbleRuntimeConfiguration() {
        this.arguments = new HashMap<>();
    }

    public RumbleRuntimeConfiguration(String[] args) {
        this.arguments = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            if (!args[i].startsWith(ARGUMENT_PREFIX)) {
                throw new CliException(ARGUMENT_FORMAT_ERROR_MESSAGE);
            }
            String argumentName = args[i].trim().replace(ARGUMENT_PREFIX, "");
            if (i + 1 >= args.length || args[i + 1].startsWith(ARGUMENT_PREFIX)) {
                throw new CliException("Missing argument value for a provided argument: " + argumentName + ".");
            }
            String argumentValue = args[i + 1];
            this.arguments.put(argumentName, argumentValue);
        }
        init();
    }

    public static RumbleRuntimeConfiguration getDefaultConfiguration() {
        return RumbleRuntimeConfiguration.defaultConfiguration;
    }

    public String getConfigurationArgument(String key) {
        if (this.arguments.containsKey(key)) {
            return this.arguments.get(key);
        } else {
            return null;
        }
    }

    public int getPort() {
        if (this.arguments.containsKey("port")) {
            return Integer.parseInt(this.arguments.get("port"));
        } else {
            return 8001;
        }
    }

    public String getHost() {
        if (this.arguments.containsKey("host")) {
            return this.arguments.get("host");
        } else {
            return "localhost";
        }
    }

    public List<String> getAllowedURIPrefixes() {
        return this.allowedPrefixes;
    }

    public void setAllowedURIPrefixes(List<String> newValue) {
        this.allowedPrefixes = newValue;
    }

    public String getOutputFormat() {
        return this.outputFormat;
    }

    public void setOutputFormat(String newValue) {
        this.outputFormat = newValue;
    }

    public int getNumberOfOutputPartitions() {
        return this.numberOfOutputPartitions;
    }

    public RumbleRuntimeConfiguration setNumberOfOutputPartitions(int newValue) {
        this.numberOfOutputPartitions = newValue;
        return this;
    }

    public Map<String, String> getOutputFormatOptions() {
        return this.outputFormatOptions;
    }

    public RumbleRuntimeConfiguration setOutputFormatOption(String key, String value) {
        this.outputFormatOptions.put(key, value);
        return this;
    }

    public boolean isCheckReturnTypeOfBuiltinFunctions() {
        return this.checkReturnTypeOfBuiltinFunctions;
    }

    public RumbleRuntimeConfiguration setCheckReturnTypeOfBuiltinFunctions(boolean checkReturnTypeOfBuiltinFunctions) {
        this.checkReturnTypeOfBuiltinFunctions = checkReturnTypeOfBuiltinFunctions;
        return this;
    }

    public void init() {
        if (this.arguments.containsKey("allowed-uri-prefixes")) {
            this.allowedPrefixes = Arrays.asList(this.arguments.get("allowed-uri-prefixes").split(";"));
        } else {
            this.allowedPrefixes = Arrays.asList();
        }
        if (this.arguments.containsKey("output-format")) {
            this.outputFormat = this.arguments.get("output-format").toLowerCase();
        } else {
            this.outputFormat = "json";
        }
        if (this.arguments.containsKey("number-of-output-partitions")) {
            this.numberOfOutputPartitions = Integer.valueOf(this.arguments.get("number-of-output-partitions"));
        } else {
            this.numberOfOutputPartitions = -1;
        }
        this.outputFormatOptions = new HashMap<>();
        for (String s : this.arguments.keySet()) {
            if (s.startsWith("output-format-option:")) {
                String key = s.substring(21);
                String value = this.arguments.get(s);
                this.outputFormatOptions.put(key, value);
            }
        }
        if (this.arguments.containsKey("materialization-cap")) {
            this.resultsSizeCap = Integer.parseInt(this.arguments.get("materialization-cap"));
        } else {
            if (this.arguments.containsKey("result-size")) {
                System.err.println("[WARNING] --result-size is obsolete. Please use --materialization-cap instead.");
                this.resultsSizeCap = Integer.parseInt(this.arguments.get("result-size"));
            } else {
                this.resultsSizeCap = 200;
            }
        }
        this.externalVariableValues = new HashMap<>();
        this.unparsedExternalVariableValues = new HashMap<>();
        for (String s : this.arguments.keySet()) {
            if (s.startsWith("variable:")) {
                String variableLocalName = s.substring(9);
                Name name = Name.createVariableInNoNamespace(variableLocalName);
                this.unparsedExternalVariableValues.put(name, this.arguments.get(s));
            }
        }
        if (this.arguments.containsKey("check-return-types-of-builtin-functions")) {
            this.checkReturnTypeOfBuiltinFunctions = this.arguments.get("check-return-types-of-builtin-functions")
                .equals("yes");
        } else {
            this.checkReturnTypeOfBuiltinFunctions = false;
        }

        if (this.arguments.containsKey("query-path")) {
            this.queryPath = this.arguments.get("query-path");
        } else {
            this.queryPath = null;
        }

        if (this.arguments.containsKey("log-path")) {
            this.logPath = this.arguments.get("log-path");
        } else {
            this.logPath = null;
        }

        if (this.arguments.containsKey("output-path")) {
            this.outputPath = this.arguments.get("output-path");
        } else {
            this.outputPath = null;
        }

        if (this.arguments.containsKey("query")) {
            this.query = this.arguments.get("query");
        } else {
            this.query = null;
        }

        if (this.arguments.containsKey("shell-filter")) {
            this.shell = this.arguments.get("shell-filter");
        } else {
            this.shell = null;
        }
    }

    public boolean getOverwrite() {
        if (this.arguments.containsKey("overwrite")) {
            return this.arguments.get("overwrite").equals("yes");
        } else {
            return false;
        }
    }

    public boolean getShowErrorInfo() {
        if (this.arguments.containsKey("show-error-info")) {
            return this.arguments.get("show-error-info").equals("yes");
        } else {
            return false;
        }
    }

    public String getLogPath() {
        return this.logPath;
    }

    public String getQueryPath() {
        return this.queryPath;
    }

    public String getOutputPath() {
        return this.outputPath;
    }

    public String getQuery() {
        return this.query;
    }

    public String getShellFilter() {
        return this.shell;
    }

    public void setLogPath(String path) {
        this.logPath = path;
    }

    public void setQueryPath(String path) {
        this.queryPath = path;
    }

    public void setOutputPath(String path) {
        this.outputPath = path;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setShellFilter(String shell) {
        this.shell = shell;
    }

    /**
     * Gets the configured number of Items that should be collected in case of a forced materialization. This applies in
     * particular to a local use of the ItemIterator.
     *
     * @return the current number of Items to collect.
     */
    public int getResultSizeCap() {
        return this.resultsSizeCap;
    }

    /**
     * Sets the number of Items that should be collected in case of a forced materialization. This applies in particular
     * to a local use of the ItemIterator.
     *
     * @param i the maximum number of Items to collect.
     */
    public RumbleRuntimeConfiguration setResultSizeCap(int i) {
        this.resultsSizeCap = i;
        return this;
    }

    public List<Item> getExternalVariableValue(Name name) {
        if (this.externalVariableValues.containsKey(name)) {
            return this.externalVariableValues.get(name);
        }
        return null;
    }

    public String getUnparsedExternalVariableValue(Name name) {
        if (this.unparsedExternalVariableValues.containsKey(name)) {
            return this.unparsedExternalVariableValues.get(name);
        }
        return null;
    }

    public RumbleRuntimeConfiguration setExternalVariableValue(Name name, List<Item> items) {
        this.externalVariableValues.put(name, items);
        return this;
    }

    public boolean isShell() {
        if (this.arguments.containsKey("shell")) {
            return this.arguments.get("shell").equals("yes");
        } else {
            return false;
        }
    }

    public boolean isServer() {
        if (this.arguments.containsKey("server")) {
            return this.arguments.get("server").equals("yes");
        } else {
            return false;
        }
    }

    public boolean isPrintIteratorTree() {
        if (this.arguments.containsKey("print-iterator-tree")) {
            return this.arguments.get("print-iterator-tree").equals("yes");
        } else {
            return false;
        }
    }

    public boolean doStaticAnalysis() {
        return this.arguments.containsKey("static-typing") && this.arguments.get("static-typing").equals("yes");
    }

    public boolean printInferredTypes() {
        return this.arguments.containsKey("print-inferred-types")
            && this.arguments.get("print-inferred-types").equals("yes");
    }

    public boolean escapeBackticks() {
        return this.arguments.containsKey("escape-backticks")
            && this.arguments.get("escape-backticks").equals("yes");
    }

    public boolean isLocal() {
        String masterConfig = SparkSessionManager.getInstance().getJavaSparkContext().getConf().get("spark.master");
        return masterConfig.contains("local");
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(
            "App name: "
                + SparkSessionManager.getInstance().getJavaSparkContext().getConf().get("spark.app.name", "(not set)")
                + "\n"
        );
        sb.append(
            "Master: "
                + SparkSessionManager.getInstance().getJavaSparkContext().getConf().get("spark.master", "(not set)")
                + "\n"
        );
        sb.append(
            "Driver's memory: "
                + SparkSessionManager.getInstance()
                    .getJavaSparkContext()
                    .getConf()
                    .get("spark.driver.memory", "(not set)")
                + "\n"
        );
        sb.append(
            "Number of executors (only applies if running on a cluster): "
                + SparkSessionManager.getInstance()
                    .getJavaSparkContext()
                    .getConf()
                    .get("spark.executor.instances", "(not set)")
                + "\n"
        );
        sb.append(
            "Cores per executor (only applies if running on a cluster): "
                + SparkSessionManager.getInstance()
                    .getJavaSparkContext()
                    .getConf()
                    .get("spark.executor.cores", "(not set)")
                + "\n"
        );
        sb.append(
            "Memory per executor (only applies if running on a cluster): "
                + SparkSessionManager.getInstance()
                    .getJavaSparkContext()
                    .getConf()
                    .get("spark.executor.memory", "(not set)")
                + "\n"
        );
        sb.append(
            "Dynamic allocation: "
                + SparkSessionManager.getInstance()
                    .getJavaSparkContext()
                    .getConf()
                    .get("spark.dynamicAllocation.enabled", "(not set)")
                + "\n"
        );
        sb.append(
            "Item Display Limit: "
                + getResultSizeCap()
                + "\n"
        );
        sb.append(
            "Output Path: "
                + (this.arguments.getOrDefault("output-path", "-"))
                + "\n"
        );
        sb.append(
            "Log Path: "
                + (this.arguments.getOrDefault("log-path", "-"))
                + "\n"
        );
        sb.append(
            "Query Path : "
                + (this.arguments.getOrDefault("query-path", "-"))
                + "\n"
        );
        return sb.toString();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.arguments);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.arguments = kryo.readObject(input, HashMap.class);
    }

    public Serializer getSerializer() {
        Serializer.Method method = Serializer.Method.XML_JSON_HYBRID;
        if (this.getOutputFormat().equals("tyson")) {
            method = Serializer.Method.TYSON;
        }
        if (this.getOutputFormat().equals("json")) {
            method = Serializer.Method.JSON;
        }
        boolean indent = false;
        Map<String, String> options = this.getOutputFormatOptions();
        if (options.containsKey("indent")) {
            if (options.get("indent").equals("yes")) {
                indent = true;
            }
        }
        String itemSeparator = "\n";
        if (options.containsKey("item-separator")) {
            itemSeparator = options.get("item-separator");
        }
        String encoding = "UTF-8";
        if (options.containsKey("encoding")) {
            itemSeparator = options.get("encoding");
        }
        return new Serializer(
                encoding,
                method,
                indent,
                itemSeparator
        );
    }
}
