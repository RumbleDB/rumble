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

import org.apache.log4j.LogManager;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.DataFrameWriter;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.api.Rumble;
import org.rumbledb.api.SequenceOfItems;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.CannotInferSchemaOnNonStructuredDataException;
import org.rumbledb.exceptions.CliException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.optimizations.Profiler;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import sparksoniq.spark.SparkSessionManager;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class JsoniqQueryExecutor {
    private RumbleRuntimeConfiguration configuration;

    public JsoniqQueryExecutor(RumbleRuntimeConfiguration configuration) {
        this.configuration = configuration;
        SparkSessionManager.COLLECT_ITEM_LIMIT = configuration.getResultSizeCap();
    }

    private void checkOutputFile(URI outputUri) throws IOException {
        if (FileSystemUtil.exists(outputUri, this.configuration, ExceptionMetadata.EMPTY_METADATA)) {
            if (!this.configuration.getOverwrite()) {
                throw new CliException(
                        "Output path " + outputUri + " already exists. Please use --overwrite yes to overwrite."
                );
            } else {
                FileSystemUtil.delete(outputUri, this.configuration, ExceptionMetadata.EMPTY_METADATA);
            }
        }
    }

    public List<Item> runQuery() throws IOException {
        String queryFile = this.configuration.getQueryPath();
        URI queryUri = null;
        if (queryFile != null) {
            queryUri = FileSystemUtil.resolveURIAgainstWorkingDirectory(
                queryFile,
                this.configuration,
                ExceptionMetadata.EMPTY_METADATA
            );
        }
        String outputPath = this.configuration.getOutputPath();
        URI outputUri = null;
        if (outputPath != null) {
            outputUri = FileSystemUtil.resolveURIAgainstWorkingDirectory(
                outputPath,
                this.configuration,
                ExceptionMetadata.EMPTY_METADATA
            );
            checkOutputFile(outputUri);
        }

        String logPath = this.configuration.getLogPath();
        URI logUri = null;
        if (logPath != null) {
            logUri = FileSystemUtil.resolveURIAgainstWorkingDirectory(
                logPath,
                this.configuration,
                ExceptionMetadata.EMPTY_METADATA
            );
            if (FileSystemUtil.exists(logUri, this.configuration, ExceptionMetadata.EMPTY_METADATA)) {
                FileSystemUtil.delete(logUri, this.configuration, ExceptionMetadata.EMPTY_METADATA);
            }
        }

        List<Item> outputList = null;

        long startTime = System.currentTimeMillis();
        Rumble rumble = new Rumble(this.configuration);
        SequenceOfItems sequence = null;
        if (this.configuration.getQuery() != null) {
            if (this.configuration.getQueryPath() != null) {
                throw new CliException(
                        "It is not possible to specify both a --query and a --query-path. It is either or."
                );
            }
            sequence = rumble.runQuery(this.configuration.getQuery());
        } else {
            sequence = rumble.runQuery(queryUri);
        }

        if (outputPath != null) {
            System.err.println(this.configuration.getOutputFormat());
            if(this.configuration.getOutputFormat().equals("xml-json-hybrid"))
            {
                outputRDDToFile(outputPath, outputUri, sequence);
            } else {
                try {
                    Dataset<Row> df = sequence.getAsDataFrame();
                    if (this.configuration.getNumberOfOutputPartitions() > 0) {
                        df = df.repartition(this.configuration.getNumberOfOutputPartitions());
                    }
                    DataFrameWriter<Row> writer = df.write();
                    Map<String, String> options = this.configuration.getOutputFormatOptions();
                    for (String key : options.keySet()) {
                        writer.option(key, options.get(key));
                        LogManager.getLogger("JsoniqQueryExecutor")
                            .info("Writing with option " + key + " : " + options.get(key));
                    }
                    String format = this.configuration.getOutputFormat();
                    LogManager.getLogger("JsoniqQueryExecutor").info("Writing to format " + format);
                    switch (format) {
                        case "json":
                            writer.json(outputPath);
                            break;
                        case "csv":
                            writer.csv(outputPath);
                            break;
                        case "parquet":
                            writer.parquet(outputPath);
                            break;
                        default:
                            writer.format(format).save(outputPath);
                    }
                } catch (CannotInferSchemaOnNonStructuredDataException e) {
                    // The output is not available as a data frame so we serialize.
                    outputRDDToFile(outputPath, outputUri, sequence);
                }
            }
        } else {
            // No output path specified, we serialize to the standard output.
            outputList = new ArrayList<>();
            long materializationCount = sequence.populateList(outputList, this.configuration.getResultSizeCap());
            RumbleRuntimeConfiguration configuration = this.configuration;
            List<String> lines = outputList.stream()
                .map(x -> configuration.getSerializer().serialize(x))
                .collect(Collectors.toList());
            System.out.println(String.join("\n", lines));
            if (materializationCount != -1) {
                issueMaterializationWarning(materializationCount);
                if (outputPath == null) {
                    System.err.println(
                        "Did you really intend to collect results to the standard input? If you want the complete output, consider using --output-path to select a destination on any file system."
                    );
                }
            }
        }

        if (this.configuration.applyUpdates() && sequence.availableAsPUL()) {
            sequence.applyPUL();
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        if (logPath != null) {
            String time = "[ExecTime] " + totalTime;
            time += "\n[ProfilerCount] " + Profiler.get();
            FileSystemUtil.append(
                logUri,
                Collections.singletonList(time),
                this.configuration,
                ExceptionMetadata.EMPTY_METADATA
            );
        }
        return outputList;
    }

    private void outputRDDToFile(String outputPath, URI outputUri, SequenceOfItems sequence) {
        JavaRDD<Item> rdd = sequence.getAsRDD();
        RumbleRuntimeConfiguration configuration = this.configuration;
        JavaRDD<String> outputRDD = rdd.map(o -> configuration.getSerializer().serialize(o));
        // If the user explicitly requests exactly one partition, then we collect all items
        // and write them to a single file.
        if (this.configuration.getNumberOfOutputPartitions() == 1) {
            List<String> lines = outputRDD.take(1000000000);
            FileSystemUtil.write(outputUri, lines, this.configuration, ExceptionMetadata.EMPTY_METADATA);
        } else {
            if (this.configuration.getNumberOfOutputPartitions() > 0) {
                outputRDD = outputRDD.repartition(this.configuration.getNumberOfOutputPartitions());
            }
            outputRDD.saveAsTextFile(outputPath);
        }
    }

    public static void issueMaterializationWarning(long materializationCount) {
        if (materializationCount == Long.MAX_VALUE) {
            System.err.println(
                "Warning! The output sequence contains "
                    + "too many items and its materialization was capped at "
                    + SparkSessionManager.COLLECT_ITEM_LIMIT
                    + " items. This value can be configured to something higher with the --materialization-cap parameter (or its deprecated equivalent --result-size) at startup"
            );
        } else {
            System.err.println(
                "Warning! The output sequence contains "
                    + materializationCount
                    + " items but its materialization was capped at "
                    + SparkSessionManager.COLLECT_ITEM_LIMIT
                    + " items. This value can be configured to something higher with the --materialization-cap parameter (or its deprecated equivalent --result-size) at startup"
            );
        }
    }

    public long runInteractive(String query, List<Item> resultList) throws IOException {
        resultList.clear();
        Rumble rumble = new Rumble(this.configuration);
        SequenceOfItems sequence = rumble.runQuery(query);
        if (this.configuration.applyUpdates() && sequence.availableAsPUL()) {
            sequence.applyPUL();
        }
        return sequence.populateList(resultList, this.configuration.getResultSizeCap());
    }

}
