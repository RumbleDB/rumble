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

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.compiler.VisitorHelpers;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.CliException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import sparksoniq.spark.SparkSessionManager;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class JsoniqQueryExecutor {
    private RumbleRuntimeConfiguration configuration;

    public JsoniqQueryExecutor(RumbleRuntimeConfiguration configuration) {
        this.configuration = configuration;
        SparkSessionManager.COLLECT_ITEM_LIMIT = configuration.getResultSizeCap();
    }

    private void checkOutputFile(String outputPath) throws IOException {
        if (outputPath != null) {
            URI uri = FileSystemUtil.resolveURIAgainstWorkingDirectory(outputPath, ExceptionMetadata.EMPTY_METADATA);
            if (FileSystemUtil.exists(uri, ExceptionMetadata.EMPTY_METADATA)) {
                if (!this.configuration.getOverwrite()) {
                    throw new CliException(
                            "Output path " + uri + " already exists. Please use --overwrite yes to overwrite."
                    );
                } else {
                    FileSystemUtil.delete(uri, ExceptionMetadata.EMPTY_METADATA);
                }
            }
        }
    }

    public List<Item> runQuery(String queryFile, String outputPath) throws IOException {
        List<Item> outputList = null;
        checkOutputFile(outputPath);
        URI outputUri = null;
        if (outputPath != null) {
            outputUri = FileSystemUtil.resolveURIAgainstWorkingDirectory(outputPath, ExceptionMetadata.EMPTY_METADATA);
            if (!FileSystemUtil.exists(outputUri, ExceptionMetadata.EMPTY_METADATA)) {
                throw new CannotRetrieveResourceException(
                        "Query file does not exist.",
                        ExceptionMetadata.EMPTY_METADATA
                );
            }
        }

        String logPath = this.configuration.getLogPath();
        URI logUri = null;
        if (logPath != null) {
            FileSystemUtil.resolveURIAgainstWorkingDirectory(logPath, ExceptionMetadata.EMPTY_METADATA);
            FileSystemUtil.delete(logUri, ExceptionMetadata.EMPTY_METADATA);
        }

        long startTime = System.currentTimeMillis();
        MainModule mainModule = VisitorHelpers.parseMainModuleFromLocation(queryFile, this.configuration);
        DynamicContext dynamicContext = VisitorHelpers.createDynamicContext(mainModule, this.configuration);
        RuntimeIterator result = VisitorHelpers.generateRuntimeIterator(mainModule);
        if (this.configuration.isPrintIteratorTree()) {
            StringBuffer sb = new StringBuffer();
            result.print(sb, 0);
            System.out.println(sb);
        }

        if (result.isRDD() && outputPath != null) {
            JavaRDD<Item> rdd = result.getRDD(dynamicContext);
            JavaRDD<String> outputRDD = rdd.map(o -> o.serialize());
            outputRDD.saveAsTextFile(outputPath);
        } else {
            outputList = new ArrayList<>();
            long materializationCount = getIteratorOutput(result, dynamicContext, outputList);
            List<String> lines = outputList.stream().map(x -> x.serialize()).collect(Collectors.toList());
            if (outputPath != null) {
                FileSystemUtil.write(outputUri, lines, ExceptionMetadata.EMPTY_METADATA);
            } else {
                System.out.println(String.join("\n", lines));
            }
            if (materializationCount != -1) {
                System.err.println(
                    "Warning! The output sequence contains "
                        + materializationCount
                        + " items but its materialization was capped at "
                        + SparkSessionManager.COLLECT_ITEM_LIMIT
                        + " items. This value can be configured with the --result-size parameter at startup"
                );
            }
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        if (logPath != null) {
            String time = "[ExecTime] " + totalTime;
            URI uri = FileSystemUtil.resolveURIAgainstWorkingDirectory(logPath, ExceptionMetadata.EMPTY_METADATA);
            FileSystemUtil.append(uri, Collections.singletonList(time), ExceptionMetadata.EMPTY_METADATA);
        }
        return outputList;
    }

    public long runInteractive(String query, List<Item> resultList) throws IOException {
        // create temp file
        MainModule mainModule = VisitorHelpers.parseMainModuleFromQuery(query, this.configuration);
        DynamicContext dynamicContext = VisitorHelpers.createDynamicContext(mainModule, this.configuration);
        RuntimeIterator runtimeIterator = VisitorHelpers.generateRuntimeIterator(mainModule);
        // execute locally for simple expressions
        if (this.configuration.isPrintIteratorTree()) {
            StringBuffer sb = new StringBuffer();
            runtimeIterator.print(sb, 0);
            System.out.println(sb);
        }
        if (!runtimeIterator.isRDD()) {
            return this.getIteratorOutput(runtimeIterator, dynamicContext, resultList);
        }
        resultList.clear();
        JavaRDD<Item> rdd = runtimeIterator.getRDD(dynamicContext);
        return SparkSessionManager.collectRDDwithLimitWarningOnly(rdd, resultList);
    }

    private long getIteratorOutput(RuntimeIterator iterator, DynamicContext dynamicContext, List<Item> resultList) {
        resultList.clear();
        iterator.open(dynamicContext);
        Item result = null;
        if (iterator.hasNext()) {
            result = iterator.next();
        }
        if (result == null) {
            return -1;
        }
        Item singleOutput = result;
        if (!iterator.hasNext()) {
            resultList.add(singleOutput);
            return -1;
        } else {
            int itemCount = 1;
            resultList.add(result);
            while (
                iterator.hasNext()
                    &&
                    ((itemCount < this.configuration.getResultSizeCap() && this.configuration.getResultSizeCap() > 0)
                        ||
                        this.configuration.getResultSizeCap() == 0)
            ) {
                resultList.add(iterator.next());
                itemCount++;
            }
            if (iterator.hasNext() && itemCount == this.configuration.getResultSizeCap()) {
                return Long.MAX_VALUE;
            }
            return -1;
        }
    }
}
