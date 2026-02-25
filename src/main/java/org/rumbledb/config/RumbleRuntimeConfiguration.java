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

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RumbleRuntimeConfiguration implements Serializable, KryoSerializable {

    private static final long serialVersionUID = 1L;
    private static final String SHORTCUT_PREFIX = "-";
    private static final String ARGUMENT_PREFIX = "--";
    private HashMap<String, String> arguments;

    List<String> allowedPrefixes;
    private int resultsSizeCap;
    private int materializationCap;
    private String inputFormat;
    private String outputFormat;
    private Map<String, String> outputFormatOptions;
    private int numberOfOutputPartitions;
    private Map<Name, List<Item>> externalVariableValues;
    private Map<Name, String> unparsedExternalVariableValues;
    private Map<Name, String> externalVariableValuesReadFromFiles;
    private Map<Name, Dataset<Row>> externalVariableValuesReadFromDataFrames;
    private Set<Name> externalVariablesReadFromStandardInput;
    private Map<Name, String> externalVariablesInputFormats;
    private boolean checkReturnTypeOfBuiltinFunctions;
    private String queryPath;
    private String outputPath;
    private String logPath;
    private String query;
    private String shell;
    private boolean showErrorInfo;
    private boolean printIteratorTree;
    private boolean nativeSQLPredicates;
    private boolean dataFrameExecutionModeDetection;
    private boolean datesWithTimeZone;
    private boolean laxJSONNullValidation;
    private boolean optimizeGeneralComparisonToValueComparison;
    private boolean parallelExecution;
    private boolean dataFrameExecution;
    private boolean nativeExecution;
    private boolean functionInlining;
    private boolean applyUpdates;
    private String queryLanguage;
    private String staticBaseUri;
    private boolean optimizeSteps; // do optimized version in SlashExpr that may violate stability condition of document
                                   // order
    private boolean optimizeStepsExperimental; // experimentally optimize steps even more, correctness not yet verified
    private boolean optimizeParentPointers; // true if no steps in query require the parent pointer, allows removal of
                                            // parent pointer from node items

    private Map<String, String> shortcutMap;
    private Set<String> yesNoShortcuts;


    private static final RumbleRuntimeConfiguration defaultConfiguration = new RumbleRuntimeConfiguration();

    public RumbleRuntimeConfiguration() {
        this.arguments = new HashMap<>();
        initShortcuts();
        init();
    }

    private void initShortcuts() {
        this.shortcutMap = new HashMap<>();
        this.shortcutMap.put("q", "query");
        this.shortcutMap.put("o", "output-path");
        this.shortcutMap.put("f", "output-format");
        this.shortcutMap.put("O", "overwrite");
        this.shortcutMap.put("c", "materialization-cap");
        this.shortcutMap.put("I", "context-item");
        this.shortcutMap.put("i", "context-item-input");
        this.shortcutMap.put("P", "number-of-output-partitions");
        this.shortcutMap.put("v", "show-error-info");
        this.shortcutMap.put("t", "static-typing");
        this.shortcutMap.put("h", "host");
        this.shortcutMap.put("p", "port");
        this.yesNoShortcuts = new HashSet<>();
        this.yesNoShortcuts.add("O");
        this.yesNoShortcuts.add("v");
        this.yesNoShortcuts.add("t");
    }

    public RumbleRuntimeConfiguration(String[] args) {
        this.arguments = new HashMap<>();
        initShortcuts();
        for (int i = 0; i < args.length; ++i) {
            if (args[i].startsWith(ARGUMENT_PREFIX)) {
                if (args[i].equals("--run") || args[i].equals("--serve") || args[i].equals("--repl")) {
                    System.err.println("Did you know?  🧑‍🏫");
                    System.err.println(
                        "The RumbleDB command line interface was extended with convenient shortcuts. For example:"
                    );
                    System.err.println();
                    System.err.println("spark-submit <spark parameters> rumbledb-<version>.jar run query.jq");
                    System.err.println("spark-submit <spark parameters> rumbledb-<version>.jar serve -p 8001");
                    System.err.println("spark-submit <spark parameters> rumbledb-<version>.jar run -q '1+1'");
                    System.err.println("spark-submit <spark parameters> rumbledb-<version>.jar repl -c 10");
                    System.err.println();
                    System.err.println(
                        "The list of single-dash shortcuts is documented in our documentation page, accessible from www.rumbledb.org."
                    );
                    System.err.println();
                    System.err.println("Try it out! The old parameters will continue to work, though.");
                }
                String argumentName = args[i].trim().replace(ARGUMENT_PREFIX, "");
                if (i + 1 >= args.length || (!(args[i + 1].equals("-")) && args[i + 1].startsWith(ARGUMENT_PREFIX))) {
                    throw new CliException("Missing argument value for a provided argument: " + argumentName + ".");
                }
                String argumentValue = args[i + 1];
                this.arguments.put(argumentName, argumentValue);
                ++i;
                continue;
            }
            if (args[i].startsWith(SHORTCUT_PREFIX)) {
                String argumentName = args[i].trim().replace(SHORTCUT_PREFIX, "");
                if (!this.yesNoShortcuts.contains(argumentName)) {
                    if (
                        i + 1 >= args.length
                            || (!(args[i + 1].equals("-"))
                                &&
                                (args[i + 1].startsWith(ARGUMENT_PREFIX)
                                    || args[i + 1].startsWith(SHORTCUT_PREFIX)))
                    ) {
                        throw new CliException("Missing argument value for a provided argument: " + argumentName + ".");
                    }
                    this.arguments.put(this.shortcutMap.get(argumentName), args[i + 1]);
                    ++i;
                    continue;
                } else {
                    this.arguments.put(this.shortcutMap.get(argumentName), "yes");
                    continue;
                }
            }
            if (i == 0 && args[i].equals("serve")) {
                this.arguments.put("server", "yes");
                continue;
            }
            if (i == 0 && args[i].equals("repl")) {
                this.arguments.put("shell", "yes");
                continue;
            }
            if (i == 0 && args[i].equals("run")) {
                // This is the default, do nothing.
                continue;
            }
            if (i == 0) {
                System.err.println("Missing mode (run/serve/repl), assuming run.");
            }
            this.arguments.put("query-path", args[i]);
        }
        init();
    }

    public static RumbleRuntimeConfiguration getDefaultConfiguration() {
        return RumbleRuntimeConfiguration.defaultConfiguration;
    }

    /**
     * Returns the port number when used in server mode.
     * 
     * @return the port number.
     */
    public int getPort() {
        if (this.arguments.containsKey("port")) {
            return Integer.parseInt(this.arguments.get("port"));
        } else {
            return 8001;
        }
    }

    /**
     * Returns the host name when used in server mode.
     * 
     * @return the host name.
     */
    public String getHost() {
        if (this.arguments.containsKey("host")) {
            return this.arguments.get("host");
        } else {
            return "localhost";
        }
    }

    /**
     * Returns the allowed URI prefixes for read/write (with I/O functions to read data)
     * 
     * @return the allowed URI prefixes.
     */
    public List<String> getAllowedURIPrefixes() {
        return this.allowedPrefixes;
    }

    /**
     * Sets the allowed URI prefixes for read/write (with I/O functions to read data)
     * 
     * @param newValue the allowed URI prefixes.
     */
    public void setAllowedURIPrefixes(List<String> newValue) {
        this.allowedPrefixes = newValue;
    }

    /**
     * Returns the output format for writing the output of the query to the output path.
     * 
     * @return the output format.
     */
    public String getOutputFormat() {
        return this.outputFormat;
    }

    /**
     * Sets the output format for writing the output of the query to the output path.
     * 
     * @param newValue the output format.
     */
    public void setOutputFormat(String newValue) {
        this.outputFormat = newValue;
    }

    /**
     * Returns the input format for reading from standard input.
     * 
     * @return the input format.
     */
    public String getInputFormat() {
        return this.inputFormat;
    }

    /**
     * Sets the input format for reading from standard input.
     * 
     * @param newValue the input format.
     */
    public void setInputFormat(String newValue) {
        this.inputFormat = newValue;
    }

    /**
     * Returns the number of output partitions to write to the output path.
     * 
     * @return the number of output partitions.
     */
    public int getNumberOfOutputPartitions() {
        return this.numberOfOutputPartitions;
    }

    /**
     * Sets the number of output partitions to write to the output path.
     * 
     * @param newValue the number of output partitions.
     */
    public RumbleRuntimeConfiguration setNumberOfOutputPartitions(int newValue) {
        this.numberOfOutputPartitions = newValue;
        return this;
    }

    /**
     * Returns the serialization options to write to the output path.
     * 
     * @return the serialization options.
     */
    public Map<String, String> getOutputFormatOptions() {
        return this.outputFormatOptions;
    }

    /**
     * Sets a serialization option to write to the output path.
     * 
     * @param key the serialization option key.
     * @param value the serialization option value.
     */
    public RumbleRuntimeConfiguration setOutputFormatOption(String key, String value) {
        this.outputFormatOptions.put(key, value);
        return this;
    }

    /**
     * Returns whether the return type of built-in functions is checked.
     * 
     * @return whether the return type of built-in functions is checked.
     */
    public boolean isCheckReturnTypeOfBuiltinFunctions() {
        return this.checkReturnTypeOfBuiltinFunctions;
    }

    /**
     * Set whether the return type of built-in functions is checked.
     * 
     * @param checkReturnTypeOfBuiltinFunctions whether the return type of built-in functions is checked.
     */
    public RumbleRuntimeConfiguration setCheckReturnTypeOfBuiltinFunctions(boolean checkReturnTypeOfBuiltinFunctions) {
        this.checkReturnTypeOfBuiltinFunctions = checkReturnTypeOfBuiltinFunctions;
        return this;
    }

    public void init() {
        if (this.arguments.containsKey("print-iterator-tree")) {
            this.printIteratorTree = this.arguments.get("print-iterator-tree").equals("yes");
        } else {
            this.printIteratorTree = false;
        }
        if (this.arguments.containsKey("show-error-info")) {
            this.showErrorInfo = this.arguments.get("show-error-info").equals("yes");
        } else {
            this.showErrorInfo = false;
        }
        if (this.arguments.containsKey("lax-json-null-validation")) {
            this.laxJSONNullValidation = this.arguments.get("lax-json-null-validation").equals("yes");
        } else {
            this.laxJSONNullValidation = true;
        }
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
        if (this.arguments.containsKey("input-format")) {
            this.inputFormat = this.arguments.get("input-format").toLowerCase();
        } else {
            this.inputFormat = "json";
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
            this.materializationCap = Integer.parseInt(this.arguments.get("materialization-cap"));
        } else {
            this.materializationCap = 100000;
        }
        if (this.arguments.containsKey("result-size")) {
            this.resultsSizeCap = Integer.parseInt(this.arguments.get("result-size"));
        } else {
            this.resultsSizeCap = 10;
        }
        this.externalVariableValues = new HashMap<>();
        this.unparsedExternalVariableValues = new HashMap<>();
        this.externalVariableValuesReadFromFiles = new HashMap<>();
        this.externalVariableValuesReadFromDataFrames = new HashMap<>();
        this.externalVariablesInputFormats = new HashMap<>();
        this.externalVariablesReadFromStandardInput = new HashSet<>();
        for (String s : this.arguments.keySet()) {
            if (s.startsWith("variable:")) {
                String variableLocalName = s.substring(9);
                Name name = Name.createVariableInNoNamespace(variableLocalName);
                this.unparsedExternalVariableValues.put(name, this.arguments.get(s));
            }
        }
        for (String s : this.arguments.keySet()) {
            if (s.startsWith("variable-from-file:")) {
                String variableLocalName = s.substring(9);
                Name name = Name.createVariableInNoNamespace(variableLocalName);
                this.externalVariableValuesReadFromFiles.put(name, this.arguments.get(s));
            }
        }
        if (this.arguments.containsKey("context-item")) {
            Name name = Name.CONTEXT_ITEM;
            this.unparsedExternalVariableValues.put(name, this.arguments.get("context-item"));
            if (this.arguments.containsKey("context-item-input")) {
                throw new CliException("It is not possible to both set --context-item and --context-item-input.");
            }
        }
        if (this.arguments.containsKey("context-item-input")) {
            String arg = this.arguments.get("context-item-input");
            if (arg.equals("-")) {
                this.externalVariablesReadFromStandardInput.add(Name.CONTEXT_ITEM);
            } else {
                this.externalVariableValuesReadFromFiles.put(Name.CONTEXT_ITEM, arg);
            }
        }
        if (this.arguments.containsKey("context-item-input-format")) {
            String arg = this.arguments.get("context-item-input-format");
            this.externalVariablesInputFormats.put(Name.CONTEXT_ITEM, arg);
        } else {
            this.externalVariablesInputFormats.put(Name.CONTEXT_ITEM, "json");
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

        if (this.arguments.containsKey("native-sql-predicates")) {
            this.nativeSQLPredicates = this.arguments.get("native-sql-predicates").equals("yes");
        } else {
            this.nativeSQLPredicates = true;
        }

        if (this.arguments.containsKey("data-frame-execution-mode-detection")) {
            this.dataFrameExecutionModeDetection = this.arguments.get("data-frame-execution-mode-detection")
                .equals("yes");
        } else {
            this.dataFrameExecutionModeDetection = true;
        }

        if (this.arguments.containsKey("dates-with-timezone")) {
            this.datesWithTimeZone = this.arguments.get("dates-with-timezone").equals("yes");
        } else {
            this.datesWithTimeZone = false;
        }

        if (this.arguments.containsKey("parallel-execution")) {
            this.parallelExecution = this.arguments.get("parallel-execution").equals("yes");
        } else {
            this.parallelExecution = true;
        }

        if (this.arguments.containsKey("data-frame-execution")) {
            this.dataFrameExecution = this.arguments.get("data-frame-execution").equals("yes");
        } else {
            this.dataFrameExecution = true;
        }

        if (this.arguments.containsKey("native-execution")) {
            this.nativeExecution = this.arguments.get("native-execution").equals("yes");
        } else {
            this.nativeExecution = true;
        }

        if (this.arguments.containsKey("function-inlining")) {
            this.functionInlining = this.arguments.get("function-inlining").equals("yes");
        } else {
            this.functionInlining = true;
        }

        if (this.arguments.containsKey("apply-updates")) {
            this.applyUpdates = this.arguments.get("apply-updates").equals("yes");
        } else {
            this.applyUpdates = false;
        }

        if (this.arguments.containsKey("optimize-general-comparison-to-value-comparison")) {
            this.optimizeGeneralComparisonToValueComparison = this.arguments.get(
                "optimize-general-comparison-to-value-comparison"
            ).equals("yes");
        } else {
            this.optimizeGeneralComparisonToValueComparison = true;
        }

        if (this.arguments.containsKey("default-language")) {
            this.queryLanguage = this.arguments.get("default-language");
        } else {
            this.queryLanguage = "jsoniq10"; // default is JSONiq 1.0 for now, will be JSONiq 3.1 in future
        }

        if (this.arguments.containsKey("static-base-uri")) {
            this.staticBaseUri = this.arguments.get("static-base-uri");
        }

        if (this.arguments.containsKey("optimize-steps")) {
            this.optimizeSteps = this.arguments.get("optimize-steps").equals("yes");
        } else {
            this.optimizeSteps = true;
        }

        if (this.arguments.containsKey("optimize-steps-experimental")) {
            this.optimizeStepsExperimental = this.arguments.get("optimize-steps-experimental").equals("yes");
        } else {
            this.optimizeStepsExperimental = false;
        }

        if (this.arguments.containsKey("optimize-parent-pointers")) {
            this.optimizeParentPointers = this.arguments.get("optimize-parent-pointers").equals("yes");
        } else {
            this.optimizeParentPointers = true;
        }
    }

    /**
     * Returns whether the output path should be overwritten.
     * 
     * @return whether the output path should be overwritten.
     */
    public boolean getOverwrite() {
        if (this.arguments.containsKey("overwrite")) {
            return this.arguments.get("overwrite").equals("yes");
        } else {
            return false;
        }
    }

    /**
     * Returns whether verbose error info should be shown in case an error is returned.
     * 
     * @return whether verbose error info should be shown in case an error is returned.
     */
    public boolean getShowErrorInfo() {
        return this.showErrorInfo;
    }

    /**
     * Returns whether verbose error info should be shown in case an error is returned.
     * 
     * @param value whether verbose error info should be shown in case an error is returned.
     */
    public void setShowErrorInfo(boolean value) {
        this.showErrorInfo = value;
    }

    /**
     * Returns whether it is fine to consider JSON null values in an object as absent for validation against an optional
     * key.
     * 
     * @return whether it is fine to consider JSON null values in an object as absent for validation against an optional
     *         key.
     */
    public boolean getLaxJSONNullValidation() {
        return this.laxJSONNullValidation;
    }

    /**
     * Sets whether it is fine to consider JSON null values in an object as absent for validation against an optional
     * key.
     * 
     * @param value whether it is fine to consider JSON null values in an object as absent for validation against an
     *        optional key.
     */
    public void setLaxJSONNullValidation(boolean value) {
        this.laxJSONNullValidation = value;
    }

    /**
     * Returns the log path.
     * 
     * @return the log path.
     */
    public String getLogPath() {
        return this.logPath;
    }

    /**
     * Returns the path from which the JSONiq or XQuery query is to be read.
     * 
     * @return the query path.
     */
    public String getQueryPath() {
        return this.queryPath;
    }

    /**
     * Returns the path to which the output path should be written.
     * 
     * @return the output path.
     */
    public String getOutputPath() {
        return this.outputPath;
    }

    /**
     * Returns the query that was passed from the command line.
     * 
     * @return the query.
     */
    public String getQuery() {
        return this.query;
    }

    /**
     * Returns the current shell filter for post-processing output (e.g. JSON beautifier)
     * 
     * @return the shell filter.
     */
    public String getShellFilter() {
        return this.shell;
    }

    /**
     * Returns whether native SQL predicates are enabled.
     * 
     * @return whether native SQL predicates are enabled.
     */
    public boolean getNativeSQLPredicates() {
        return this.nativeSQLPredicates;
    }

    /**
     * Returns whether DataFrame execution mode detection is activated for higher-order functions.
     * If disabled, higher-order functions will be executed locally.
     * 
     * @return whether DataFrame execution mode detection is activated for higher-order functions.
     */
    public boolean getDataFrameExecutionModeDetection() {
        return this.dataFrameExecutionModeDetection;
    }

    /**
     * Sets the log path.
     * 
     * @param path the log path.
     */
    public void setLogPath(String path) {
        this.logPath = path;
    }

    /**
     * Sets the path from which the JSONiq or XQuery query is to be read.
     * 
     * @param path the query path.
     */
    public void setQueryPath(String path) {
        this.queryPath = path;
    }

    /**
     * Sets the path to which the output path should be written.
     * 
     * @param path the output path.
     */
    public void setOutputPath(String path) {
        this.outputPath = path;
    }

    /**
     * Sets the current shell filter for post-processing output (e.g. JSON beautifier)
     * 
     * @param shell the shell filter.
     */
    public void setShellFilter(String shell) {
        this.shell = shell;
    }

    /**
     * Sets whether native SQL predicates are enabled.
     * 
     * @param value whether native SQL predicates are enabled.
     */
    public void setNativeSQLPredicates(boolean value) {
        this.nativeSQLPredicates = value;
    }

    /**
     * Sets whether DataFrame execution mode detection is activated for higher-order functions.
     * If disabled, higher-order functions will be executed locally.
     * 
     * @param value whether DataFrame execution mode detection is activated for higher-order functions.
     */
    public void setDataFrameExecutionModeDetection(boolean value) {
        this.dataFrameExecutionModeDetection = value;
    }

    /**
     * Gets the configured number of Items that should be collected as the overall result of a query.
     *
     * @return the current number of Items to collect.
     */
    public int getResultSizeCap() {
        return this.resultsSizeCap;
    }

    /**
     * Sets the number of Items that should be collected as the overall result of a query.
     *
     * @param i the maximum number of Items to collect.
     */
    public RumbleRuntimeConfiguration setResultSizeCap(int i) {
        this.resultsSizeCap = i;
        return this;
    }

    /**
     * Gets the configured number of Items that should be collected in case of a forced materialization. This applies in
     * particular to a local use of the ItemIterator.
     *
     * @return the current number of Items to collect.
     */
    public int getMaterializationCap() {
        return this.materializationCap;
    }

    /**
     * Sets the number of Items that should be collected in case of a forced materialization. This applies in particular
     * to a local use of the ItemIterator.
     *
     * @param i the maximum number of Items to collect.
     */
    public RumbleRuntimeConfiguration setMaterializationCap(int i) {
        this.materializationCap = i;
        return this;
    }

    /**
     * Returns the names of external variables read from DataFrames.
     *
     * @return the names of external variables read from DataFrames.
     */
    public List<Name> getExternalVariablesReadFromDataFrames() {
        return new java.util.ArrayList<>(this.externalVariableValuesReadFromDataFrames.keySet());
    }

    /**
     * Returns the names of external variables read from lists of items.
     *
     * @return the names of external variables read from lists of items.
     */
    public List<Name> getExternalVariablesReadFromListsOfItems() {
        return new java.util.ArrayList<>(this.externalVariableValues.keySet());
    }

    /**
     * Returns the list of items associated with a specified variable.
     *
     * @param name the variable name (without dollar).
     * @return the list of items associated with the specified variable.
     */
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

    public String getExternalVariableValueReadFromFile(Name name) {
        if (this.externalVariableValuesReadFromFiles.containsKey(name)) {
            return this.externalVariableValuesReadFromFiles.get(name);
        }
        return null;
    }

    public Dataset<Row> getExternalVariableValueReadFromDataFrame(Name name) {
        if (this.externalVariableValuesReadFromDataFrames.containsKey(name)) {
            return this.externalVariableValuesReadFromDataFrames.get(name);
        }
        return null;
    }

    public RumbleRuntimeConfiguration setExternalVariableValue(Name name, Dataset<Row> dataFrame) {
        this.externalVariableValuesReadFromDataFrames.put(name, dataFrame);
        return this;
    }

    public RumbleRuntimeConfiguration setExternalVariableValue(String variableName, Dataset<Row> dataFrame) {
        setExternalVariableValue(new Name(null, null, variableName), dataFrame);
        return this;
    }

    public RumbleRuntimeConfiguration setExternalVariableValue(Name name, List<Item> items) {
        this.externalVariableValues.put(name, items);
        return this;
    }

    public RumbleRuntimeConfiguration setExternalVariableValue(String variableName, List<Item> items) {
        setExternalVariableValue(new Name(null, null, variableName), items);
        return this;
    }

    public RumbleRuntimeConfiguration resetExternalVariableValue(Name name) {
        this.externalVariableValues.remove(name);
        this.externalVariableValuesReadFromDataFrames.remove(name);
        return this;
    }

    public RumbleRuntimeConfiguration resetExternalVariableValue(String variableNameString) {
        resetExternalVariableValue(new Name(null, null, variableNameString));
        return this;
    }

    public boolean readFromStandardInput(Name variableName) {
        return this.externalVariablesReadFromStandardInput.contains(variableName);
    }

    public String getInputFormat(Name variableName) {
        return this.externalVariablesInputFormats.get(variableName);
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

    /**
     * Sets whether verbose information on query plans should be displayed.
     *
     * @param value true if verbose information on query plans should be displayed, false otherwise.
     */
    public void setPrintIteratorTree(boolean value) {
        this.printIteratorTree = value;
    }

    /**
     * Returns whether verbose information on query plans should be displayed.
     *
     * @return true if verbose information on query plans should be displayed, false otherwise.
     */
    public boolean isPrintIteratorTree() {
        return this.printIteratorTree;
    }

    /**
     * Returns whether static analysis is enabled.
     *
     * @return true if static analysis is enabled, false otherwise.
     */
    public boolean doStaticAnalysis() {
        return this.arguments.containsKey("static-typing") && this.arguments.get("static-typing").equals("yes");
    }

    /**
     * Returns whether verbose information inferred types should be shown.
     *
     * @return true if verbose information inferred types should be shown, false otherwise.
     */
    public boolean printInferredTypes() {
        return this.arguments.containsKey("print-inferred-types")
            && this.arguments.get("print-inferred-types").equals("yes");
    }

    /**
     * Returns whether dates with time zones should be supported.
     * If supported, RumbleDB will not be able to use DataFrames for data containing dates.
     *
     * @return true if dates with time zones should be supported, false otherwise.
     */
    public boolean dateWithTimezone() {
        return this.datesWithTimeZone;
    }

    /**
     * Sets whether dates with time zones should be supported.
     * If supported, RumbleDB will not be able to use DataFrames for data containing dates.
     *
     * @param b true if dates with time zones should be supported, false otherwise.
     */
    public void setDateWithTimezone(boolean b) {
        this.datesWithTimeZone = b;
    }

    /**
     * Returns whether parallel execution (RDD, DataFrames) is enabled.
     *
     * @return true if parallel execution is enabled, false otherwise.
     */
    public boolean parallelExecution() {
        return this.parallelExecution;
    }

    /**
     * Sets whether parallel execution (RDD, DataFrames) is enabled.
     *
     * @param b true if parallel execution is enabled, false otherwise.
     */
    public void setParallelExecution(boolean b) {
        this.parallelExecution = b;
    }

    /**
     * Returns whether DataFrame execution is enabled.
     *
     * @return true if DataFrame execution is enabled, false otherwise.
     */
    public boolean dataFrameExecution() {
        return this.dataFrameExecution;
    }

    /**
     * Sets whether DataFrame execution is enabled.
     *
     * @param b true if DataFrame execution is enabled, false otherwise.
     */
    public void setDataFrameExecution(boolean b) {
        this.dataFrameExecution = b;
    }

    /**
     * Returns whether advanced native execution for nested FLWOR queries is enabled.
     *
     * @return true if advanced native execution for nested FLWOR queries is enabled, false otherwise.
     */
    public boolean nativeExecution() {
        return this.nativeExecution;
    }

    /**
     * Sets whether advanced native execution for nested FLWOR queries is enabled.
     *
     * @param b true if advanced native execution for nested FLWOR queries is enabled, false otherwise.
     */
    public void setNativeExecution(boolean b) {
        this.nativeExecution = b;
    }

    /**
     * Returns whether function inlining is enabled.
     *
     * @return true if function inlining is enabled, false otherwise.
     */
    public boolean functionInlining() {
        return this.functionInlining;
    }

    /**
     * Sets whether function inlining is enabled.
     *
     * @param b true if function inlining is enabled, false otherwise.
     */
    public void setFunctionInlining(boolean b) {
        this.functionInlining = b;
    }

    /**
     * Returns whether the returned Pending Update List should be applied when executed on the command line.
     *
     * @return true if the Pending Update List should be applied, false otherwise.
     */
    public boolean applyUpdates() {
        return this.applyUpdates;
    }

    /**
     * Sets whether the returned Pending Update List should be applied when executed on the command line.
     *
     * @param b true if the Pending Update List should be applied, false otherwise.
     */
    public void setApplyUpdates(boolean b) {
        this.applyUpdates = b;
    }

    /**
     * Returns whether general comparisons may be rewritten using value comparisons.
     *
     * @return true if general comparisons may be rewritten using value comparisons, false otherwise.
     */
    public boolean optimizeGeneralComparisonToValueComparison() {
        return this.optimizeGeneralComparisonToValueComparison;
    }

    /**
     * Sets whether general comparisons may be rewritten using value comparisons.
     *
     * @param b true if general comparisons may be rewritten using value comparisons, false otherwise.
     */
    public void setOptimizeGeneralComparisonToValueComparison(boolean b) {
        this.optimizeGeneralComparisonToValueComparison = b;
    }

    /**
     * Returns the version of the query language in use.
     *
     * @return the version of the query language in use.
     */
    public String getQueryLanguage() {
        return this.queryLanguage;
    }

    /**
     * Sets the version of the query language to use.
     * Possible values: jsoniq10, jsoniq31, xquery30, xquery31.
     *
     * @param version the version of the query language to use.
     */
    public void setQueryLanguage(String version) {
        this.queryLanguage = version;
    }

    /**
     * Returns the static base URI against which relative URIs are resolved when reading or writing data.
     *
     * @return the static base URI against which relative URIs are resolved.
     */
    public String getStaticBaseUri() {
        return this.staticBaseUri;
    }

    /**
     * Sets the static base URI against which relative URIs are resolved when reading or writing data.
     *
     * @param value the static base URI against which relative URIs are resolved.
     */
    public void setStaticBaseUri(String value) {
        this.staticBaseUri = value;
    }

    /**
     * Returns whether XPath steps should be optimized.
     *
     * @return true if XPath steps should be optimized, false otherwise.
     */
    public boolean optimizeSteps() {
        return this.optimizeSteps;
    }

    /**
     * Sets whether XPath steps should be optimized.
     *
     * @param value true if XPath steps should be optimized, false otherwise.
     */
    public void setOptimizeSteps(boolean value) {
        this.optimizeSteps = value;
    }

    /**
     * Returns whether XPath steps should be optimized including experimental algorithms.
     *
     * @return true if XPath steps should be optimized including experimental algorithms, false otherwise.
     */
    public boolean optimizeStepExperimental() {
        return this.optimizeStepsExperimental;
    }

    /**
     * Sets whether XPath steps should be optimized including experimental algorithms.
     *
     * @param value true if XPath steps should be optimized including experimental algorithms, false otherwise.
     */
    public void setOptimizeStepsExperimental(boolean value) {
        this.optimizeStepsExperimental = value;
    }

    /**
     * Returns whether parent pointers can be optimized away in XPath expressions.
     *
     * @return true if parent pointers can be optimized away in XPath expressions, false otherwise.
     */
    public boolean optimizeParentPointers() {
        return this.optimizeParentPointers;
    }

    /**
     * Sets whether parent pointers can be optimized away in XPath expressions.
     *
     * @param value true if parent pointers can be optimized away in XPath expressions, false otherwise.
     */
    public void setOptimizeParentPointers(boolean value) {
        this.optimizeParentPointers = value;
    }

    /**
     * Returns whether the underlying Spark instance is local or not.
     *
     * @return true if the underlying Spark instance is local, false otherwise.
     */
    public boolean isLocal() {
        String masterConfig = SparkSessionManager.getInstance().getJavaSparkContext().getConf().get("spark.master");
        return masterConfig.contains("local");
    }

    /**
     * Returns high-level aspects of the current configuration as a string.
     *
     * @return a string representation of the current configuration.
     */
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

    public enum XMLVersion { XML10, XML11 }
    private XMLVersion xmlVersion = XMLVersion.XML10; // default fallback
    public XMLVersion getXmlVersion() { return this.xmlVersion; }
    public void setXmlVersion(XMLVersion v) { this.xmlVersion = v; }

    /**
     * Returns the serializer in use according to the output format specified.
     *
     * @return the serializer in use according to the output format specified.
     */
    public Serializer getSerializer() {
        Serializer.Method method = Serializer.Method.XML_JSON_HYBRID;
        if (this.getOutputFormat().equals("tyson")) {
            method = Serializer.Method.TYSON;
        }
        if (this.getOutputFormat().equals("json")) {
            method = Serializer.Method.JSON;
        }
        if (this.getOutputFormat().equals("yaml")) {
            method = Serializer.Method.YAML;
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
