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

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.compiler.TranslationVisitor;
import org.rumbledb.compiler.VisitorHelpers;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.parser.JsoniqLexer;
import org.rumbledb.parser.JsoniqParser;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import sparksoniq.spark.SparkSessionManager;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class JsoniqQueryExecutor {
    private RumbleRuntimeConfiguration configuration;

    public JsoniqQueryExecutor(RumbleRuntimeConfiguration configuration) {
        this.configuration = configuration;
        SparkSessionManager.COLLECT_ITEM_LIMIT = configuration.getResultSizeCap();
    }

    private void checkOutputFile(String outputPath) throws IOException {
        if (outputPath != null) {
            if (FileSystemUtil.exists(outputPath, new ExceptionMetadata(0, 0))) {
                if (!this.configuration.getOverwrite()) {
                    System.err.println(
                        "Output path " + outputPath + " already exists. Please use --overwrite yes to overwrite."
                    );
                    System.exit(1);
                }
            }
        }
    }

    public void runQuery(String queryFile, String outputPath) throws IOException {
        checkOutputFile(outputPath);
        ExceptionMetadata metadata = new ExceptionMetadata(0, 0);
        if (!FileSystemUtil.exists(queryFile, metadata)) {
            throw new CannotRetrieveResourceException("Query file does not exist.", metadata);
        }
        FSDataInputStream in = FileSystemUtil.getDataInputStream(queryFile, metadata);
        JsoniqLexer lexer = new JsoniqLexer(CharStreams.fromStream(in));

        long startTime = System.currentTimeMillis();
        MainModule mainModule = this.parse(lexer);
        generateStaticContext(mainModule);
        if (this.configuration.isPrintIteratorTree()) {
            System.out.println(mainModule.serializationString(true));
        }
        DynamicContext dynamicContext = VisitorHelpers.createDynamicContext(mainModule, this.configuration);
        RuntimeIterator result = generateRuntimeIterators(mainModule);
        if (this.configuration.isPrintIteratorTree()) {
            StringBuffer sb = new StringBuffer();
            result.print(sb, 0);
            System.out.println(sb);
            return;
        }

        if (result.isRDD() && outputPath != null) {
            JavaRDD<Item> rdd = result.getRDD(dynamicContext);
            JavaRDD<String> output = rdd.map(o -> o.serialize());
            output.saveAsTextFile(outputPath);
        } else {
            String output = runIterators(result, dynamicContext);
            if (outputPath != null) {
                List<String> lines = Arrays.asList(output);
                FileSystemUtil.write(outputPath, lines, metadata);
            } else {
                System.out.println(output);
            }
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        String logPath = this.configuration.getLogPath();
        if (logPath != null) {
            String time = "[ExecTime] " + totalTime;
            FileSystemUtil.write(logPath, Collections.singletonList(time), metadata);
        }
    }

    public String runInteractive(String query) throws IOException {
        // create temp file
        JsoniqLexer lexer = new JsoniqLexer(CharStreams.fromString(query));
        MainModule mainModule = this.parse(lexer);
        generateStaticContext(mainModule);
        DynamicContext dynamicContext = VisitorHelpers.createDynamicContext(mainModule, this.configuration);
        RuntimeIterator runtimeIterator = generateRuntimeIterators(mainModule);
        // execute locally for simple expressions
        if (!runtimeIterator.isRDD()) {
            String localOutput = this.runIterators(runtimeIterator, dynamicContext);
            return localOutput;
        }
        String rddOutput = this.getRDDResults(runtimeIterator);
        return rddOutput;
    }

    private MainModule parse(JsoniqLexer lexer) {
        JsoniqParser parser = new JsoniqParser(new CommonTokenStream(lexer));
        parser.setErrorHandler(new BailErrorStrategy());
        TranslationVisitor visitor = new TranslationVisitor();
        try {
            // TODO Handle module extras
            JsoniqParser.ModuleContext module = parser.module();
            JsoniqParser.MainModuleContext main = module.main;
            return (MainModule) visitor.visit(main);
        } catch (ParseCancellationException ex) {
            ParsingException e = new ParsingException(
                    lexer.getText(),
                    new ExceptionMetadata(
                            lexer.getLine(),
                            lexer.getCharPositionInLine()
                    )
            );
            e.initCause(ex);
            throw e;
        }
    }

    private void generateStaticContext(MainModule mainModule) {
        VisitorHelpers.populateStaticContext(mainModule);
    }

    private RuntimeIterator generateRuntimeIterators(MainModule mainModule) {
        return VisitorHelpers.generateRuntimeIterator(mainModule);
    }

    protected String runIterators(RuntimeIterator iterator, DynamicContext dynamicContext) {
        return getIteratorOutput(iterator, dynamicContext);
    }

    private String getIteratorOutput(RuntimeIterator iterator, DynamicContext dynamicContext) {
        iterator.open(dynamicContext);
        Item result = null;
        if (iterator.hasNext()) {
            result = iterator.next();
        }
        if (result == null) {
            return "";
        }
        String singleOutput = result.serialize();
        if (!iterator.hasNext()) {
            return singleOutput;
        } else {
            int itemCount = 0;
            StringBuilder sb = new StringBuilder();
            sb.append(result.serialize());
            sb.append("\n");
            while (
                iterator.hasNext()
                    &&
                    ((itemCount < this.configuration.getResultSizeCap() && this.configuration.getResultSizeCap() > 0)
                        ||
                        this.configuration.getResultSizeCap() == 0)
            ) {
                sb.append(iterator.next().serialize());
                sb.append("\n");
                itemCount++;
            }
            // remove last comma
            return sb.toString();
        }
    }

    private String getRDDResults(RuntimeIterator result) {
        JavaRDD<Item> rdd = result.getRDD(new DynamicContext());
        JavaRDD<String> output = rdd.map(o -> o.serialize());
        long resultCount = output.take(2).size();
        if (resultCount == 0) {
            return "";
        }
        if (resultCount == 1) {
            return output.collect().get(0);
        }
        if (resultCount > 1) {
            List<String> collectedOutput = SparkSessionManager.collectRDDwithLimit(output, new ExceptionMetadata(0, 0));

            StringBuilder sb = new StringBuilder();
            for (String item : collectedOutput) {
                sb.append(item);
                sb.append("\n");
            }

            return sb.toString();
        }
        throw new OurBadException("Unexpected rdd result count in getRDDResults()");
    }
}
