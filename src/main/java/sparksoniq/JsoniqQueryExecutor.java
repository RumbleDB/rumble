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

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;

import sparksoniq.config.SparksoniqRuntimeConfiguration;
import sparksoniq.exceptions.ParsingException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.compiler.JsoniqExpressionTreeVisitor;
import sparksoniq.jsoniq.compiler.parser.JsoniqLexer;
import sparksoniq.jsoniq.compiler.parser.JsoniqParser;
import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.visitor.RuntimeIteratorVisitor;
import sparksoniq.semantics.visitor.StaticContextVisitor;
import sparksoniq.spark.SparkSessionManager;
import sparksoniq.utils.FileUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


public class JsoniqQueryExecutor {
    public static final String TEMP_QUERY_FILE_NAME = "Temp_Query";
    private SparksoniqRuntimeConfiguration _configuration;
    private boolean _useLocalOutputLog;

    public JsoniqQueryExecutor(boolean useLocalOutputLog, SparksoniqRuntimeConfiguration configuration) {
        _configuration = configuration;
        _useLocalOutputLog = useLocalOutputLog;
        SparkSessionManager.COLLECT_ITEM_LIMIT = configuration.getResultSizeCap();
    }

    public void runLocal(String queryFile, String outputPath) throws IOException {
        File outputFile = null;
        if(outputPath != null)
        {
            outputFile = new File(outputPath);
            if(outputFile.exists())
            {
                if(!_configuration.getOverwrite())
                {
                    System.err.println("Output path " + outputPath + " already exists. Please use --overwrite yes to overwrite.");
                    System.exit(1);
                } else if(outputFile.isDirectory()) {
                    org.apache.commons.io.FileUtils.deleteDirectory(outputFile);
                } else {
                    outputFile.delete();
                }
            }
        }

        CharStream charStream = CharStreams.fromFileName(queryFile);
        long startTime = System.currentTimeMillis();
        JsoniqExpressionTreeVisitor visitor = this.parse(new JsoniqLexer(charStream));
        // generate static context
        generateStaticContext(visitor.getMainModule());
        // generate iterators
        RuntimeIterator result = generateRuntimeIterators(visitor.getMainModule());
        if(_configuration.isPrintIteratorTree())
        {
            StringBuffer sb = new StringBuffer();
            result.print(sb, 0);
            System.out.println(sb);
            return;
        }
        if (result.isRDD() && outputPath != null) {
            JavaRDD<Item> rdd = result.getRDD(new DynamicContext());
            JavaRDD<String> output = rdd.map(o -> o.serialize());
            output.saveAsTextFile(outputPath);
        } else {
            String output = runIterators(result);
            if (outputPath != null) {
                List<String> lines = Arrays.asList(output);
                org.apache.commons.io.FileUtils.writeLines(outputFile, "UTF-8", lines);
            } else {
                System.out.println(output);
            }
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        if (_configuration.getLogPath() != null) {
            writeTimeLog(totalTime);
        }
    }

    public void run(String queryFile, String outputPath) throws IOException {
        JsoniqLexer lexer = getInputSource(queryFile);
        long startTime = System.currentTimeMillis();
        JsoniqExpressionTreeVisitor visitor = this.parse(lexer);
        // generate static context
        generateStaticContext(visitor.getMainModule());
        // generate iterators
        RuntimeIterator result = generateRuntimeIterators(visitor.getMainModule());
        // collect output in memory and write to filesystem from java
        if (_useLocalOutputLog) {
            String output = runIterators(result);
            org.apache.hadoop.fs.FileSystem fileSystem = org.apache.hadoop.fs.FileSystem
                    .get(SparkSessionManager.getInstance().getJavaSparkContext().hadoopConfiguration());
            FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path(outputPath));
            BufferedOutputStream stream = new BufferedOutputStream(fsDataOutputStream);
            stream.write(output.getBytes());
            stream.close();
            // else write from Spark RDD
        } else {
            if (!result.isRDD())
                throw new SparksoniqRuntimeException("Could not find any RDD iterators in executor");
            JavaRDD<Item> rdd = result.getRDD(new DynamicContext());
            JavaRDD<String> output = rdd.map(o -> o.serialize());
            output.saveAsTextFile(outputPath);
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        if (_configuration.getLogPath() != null) {
            writeTimeLog(totalTime);
        }
    }

    private void writeTimeLog(long totalTime) throws IOException {
        String result = "[ExecTime]" + totalTime;
        if (_configuration.getLogPath().startsWith("file://") || _configuration.getLogPath().startsWith("/")) {
            String timeLogPath = _configuration.getLogPath().substring(0, _configuration.getLogPath().lastIndexOf("/"));
            timeLogPath += Path.SEPARATOR + "time_log_";
            java.nio.file.Path finalPath = FileUtils.getUniqueFileName(timeLogPath);
            java.nio.file.Files.write(finalPath, result.getBytes());
        }
        if (_configuration.getLogPath().startsWith("hdfs://")) {
            org.apache.hadoop.fs.FileSystem fileSystem = org.apache.hadoop.fs.FileSystem
                    .get(SparkSessionManager.getInstance().getJavaSparkContext().hadoopConfiguration());
            FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path(_configuration.getLogPath()));
            BufferedOutputStream stream = new BufferedOutputStream(fsDataOutputStream);
            stream.write(result.getBytes());
            stream.close();
        }
        if (_configuration.getLogPath().startsWith("./")) {
            List<String> lines = Arrays.asList(result);
            java.nio.file.Path file = Paths.get(_configuration.getLogPath());
            Files.write(file, lines, Charset.forName("UTF-8"));
        }
    }

    public String runInteractive(java.nio.file.Path queryFile) throws IOException {
        // create temp file
        JsoniqLexer lexer = getInputSource(queryFile.toString());
        JsoniqExpressionTreeVisitor visitor = this.parse(lexer);
        // generate static context
        generateStaticContext(visitor.getMainModule());
        // generate iterators
        RuntimeIterator runtimeIterator = generateRuntimeIterators(visitor.getMainModule());
        // execute locally for simple expressions
        if (!runtimeIterator.isRDD()) {
            String localOutput = this.runIterators(runtimeIterator);
            return localOutput;
        }
        String rddOutput = this.getRDDResults(runtimeIterator);
        return rddOutput;
    }

    private JsoniqLexer getInputSource(String arg) throws IOException {
        arg = arg.trim();
        //return embedded file
        if (arg.isEmpty())
            new JsoniqLexer(CharStreams.fromStream(Main.class.getResourceAsStream("/queries/runQuery.iq")));
        if (arg.startsWith("file://") || arg.startsWith("/")) {
            return new JsoniqLexer(CharStreams.fromFileName(arg));
        }
        if (arg.startsWith("hdfs://")) {
            org.apache.hadoop.fs.FileSystem fileSystem = org.apache.hadoop.fs.FileSystem
                    .get(URI.create(arg), SparkSessionManager.getInstance().getJavaSparkContext().hadoopConfiguration());
            FSDataInputStream in;
            try {
                in = fileSystem.open(new Path(arg));
            } catch (Exception ex) {
                // ex.printStackTrace();
                throw ex;
            }
            return new JsoniqLexer(CharStreams.fromStream(in));
        }
        throw new RuntimeException("Unknown url protocol");
    }

    private JsoniqExpressionTreeVisitor parse(JsoniqLexer lexer) {
        JsoniqParser parser = new JsoniqParser(new CommonTokenStream(lexer));
        parser.setErrorHandler(new BailErrorStrategy());
        JsoniqExpressionTreeVisitor visitor = new JsoniqExpressionTreeVisitor();
        try {
            // TODO Handle module extras
            JsoniqParser.ModuleContext module = parser.module();
            JsoniqParser.MainModuleContext main = module.main;
            visitor.visit(main);
        } catch (ParseCancellationException ex) {
            ParsingException e = new ParsingException(lexer.getText(), new ExpressionMetadata(lexer.getLine(),
                    lexer.getCharPositionInLine()));
            e.initCause(ex);
            throw e;
        }
        return visitor;
    }

    private void generateStaticContext(Expression expression) {
        new StaticContextVisitor().visit(expression, expression.getStaticContext());
    }

    private RuntimeIterator generateRuntimeIterators(Expression expression) {
        RuntimeIterator result = new RuntimeIteratorVisitor().visit(expression, null);
        return result;
    }

    protected String runIterators(RuntimeIterator iterator) {
        String actualOutput = getIteratorOutput(iterator);
        return actualOutput;
    }

    private String getIteratorOutput(RuntimeIterator iterator) {
        iterator.open(new DynamicContext());
        Item result = null;
        if (iterator.hasNext()) {
            result = iterator.next();
        }
        if (result == null) {
            return "";
        }
        String singleOutput = result.serialize();
        if (!iterator.hasNext())
            return singleOutput;
        else {
            int itemCount = 0;
            StringBuilder sb = new StringBuilder();
            sb.append(result.serialize());
            sb.append("\n");
            while (iterator.hasNext() &&
                    ((itemCount < _configuration.getResultSizeCap() && _configuration.getResultSizeCap() > 0) ||
                            _configuration.getResultSizeCap() == 0)) {
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
        long resultCount = output.count();
        if (resultCount == 0) {
            return "";
        }
        if (resultCount == 1) {
            return output.collect().get(0);
        }
        if (resultCount > 1) {
            List<String> collectedOutput;
            if (SparkSessionManager.LIMIT_COLLECT()) {
                collectedOutput = output.take(SparkSessionManager.COLLECT_ITEM_LIMIT);
                if (collectedOutput.size() == SparkSessionManager.COLLECT_ITEM_LIMIT) {
                    if (Main.terminal == null) {
                        System.out.println("Results have been truncated to:" + SparkSessionManager.COLLECT_ITEM_LIMIT
                                + " items. This value can be configured with the --result-size parameter at startup.\n");
                    } else {
                        Main.terminal.output("\nWarning: Results have been truncated to: " + SparkSessionManager.COLLECT_ITEM_LIMIT
                                + " items. This value can be configured with the --result-size parameter at startup.\n");
                    }
                }
            } else {
                collectedOutput = output.collect();
            }

            StringBuilder sb = new StringBuilder();
            for (String item : collectedOutput) {
                sb.append(item);
                sb.append("\n");
            }

            return sb.toString();
        }
        throw new SparksoniqRuntimeException("Unexpected rdd result count in getRDDResults()");
    }
}
