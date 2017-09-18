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
import jiqs.io.FileUtils;
import jiqs.jsoniq.compiler.JsoniqExpressionTreeVisitor;
import jiqs.jsoniq.compiler.parser.JsoniqLexer;
import jiqs.jsoniq.compiler.parser.JsoniqParser;
import jiqs.jsoniq.compiler.translator.expr.Expression;
import jiqs.jsoniq.exceptions.SparksoniqRuntimeException;
import jiqs.jsoniq.exceptions.ParsingException;
import jiqs.jsoniq.item.Item;
import jiqs.jsoniq.runtime.iterator.RuntimeIterator;
import jiqs.semantics.DynamicContext;
import jiqs.semantics.visitor.RuntimeIteratorVisitor;
import jiqs.semantics.visitor.StaticContextVisitor;
import jiqs.spark.SparkContextManager;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import org.apache.spark.api.java.JavaRDD;

import java.io.*;
import java.net.URI;


public class JsoniqQueryExecutor {
    public static final String TEMP_QUERY_FILE_NAME = "Temp_Query";
    private boolean _useLocalOutputLog;
    private boolean _outputTimeLog;
    private int _itemOutputLimit;
    private String _logFilePath = null;

    public JsoniqQueryExecutor(boolean useLocalOutputLog){
        this._useLocalOutputLog = useLocalOutputLog;
        this._itemOutputLimit = 0;
        this._outputTimeLog = false;
        SparkContextManager.COLLECT_ITEM_LIMIT = 0;
    }

    public JsoniqQueryExecutor(boolean useLocalOutputLog, int itemLimit){
        this._useLocalOutputLog = useLocalOutputLog;
        this._itemOutputLimit = itemLimit;
        this._outputTimeLog = false;
        SparkContextManager.COLLECT_ITEM_LIMIT = itemLimit;
    }

    public JsoniqQueryExecutor(boolean useLocalOutputLog, int itemLimit, String logFilePath) {
        this._useLocalOutputLog = useLocalOutputLog;
        this._itemOutputLimit = itemLimit;
        this._outputTimeLog = true;
        this._logFilePath = logFilePath;
        SparkContextManager.COLLECT_ITEM_LIMIT = itemLimit;
    }

    public String runLocal(String arg) throws IOException {
        JsoniqExpressionTreeVisitor visitor = this.parse( new JsoniqLexer(new ANTLRInputStream(new Main()
                .getClass().getResourceAsStream("/queries/runQuery.iq"))));
        //generate static context
        generateStaticContext(visitor.getQueryExpression());
        //generate iterators
        RuntimeIterator result = generateRuntimeIterators(visitor.getQueryExpression());
        String output = runIterators(result, true);
        return output;
    }

    public void run(String queryFile, String  outputPath) throws IOException {
        JsoniqLexer lexer = getInputSource(queryFile);
        long startTime = System.currentTimeMillis();
        JsoniqExpressionTreeVisitor visitor = this.parse(lexer);
        //generate static context
        generateStaticContext(visitor.getQueryExpression());
        //generate iterators
        RuntimeIterator result = generateRuntimeIterators(visitor.getQueryExpression());
        //collect output in memory and write to filesystem from java
        if(_useLocalOutputLog) {
            String output = runIterators(result, true);
            org.apache.hadoop.fs.FileSystem fileSystem = org.apache.hadoop.fs.FileSystem
                    .get(SparkContextManager.getInstance().getContext().hadoopConfiguration());
            FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path(outputPath));
            BufferedOutputStream stream = new BufferedOutputStream(fsDataOutputStream);
            stream.write(output.getBytes());
            stream.close();
            //else write from Spark RDD
        } else {
            if(!result.isRDD())
                throw new SparksoniqRuntimeException("Could not find any RDD iterators in executor");
            JavaRDD<Item> rdd = result.getRDD();
            JavaRDD<String> output = rdd.map(o -> o.serialize());
            output.saveAsTextFile(outputPath);
        }
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        if(this._outputTimeLog)
            writeTimeLog(totalTime);
    }

    public String runInteractive(java.nio.file.Path queryFile,
                                 RuntimeConfiguration configuration) throws IOException, InterruptedException {
        //create temp file
        JsoniqLexer lexer = getInputSource(queryFile.toString());
        JsoniqExpressionTreeVisitor visitor = this.parse(lexer);
        //generate static context
        generateStaticContext(visitor.getQueryExpression());
        //generate iterators
        RuntimeIterator result = generateRuntimeIterators(visitor.getQueryExpression());
        //execute locally for simple expressions
        if(!result.isRDD()) {
            String localOutput = this.runIterators(result, false);
            return localOutput;
        }
        else {
            JavaRDD<Item> rdd = result.getRDD();
            JavaRDD<String> output = rdd.map(o -> o.serialize());
            String localOutput = "";
            for (String item : SparkContextManager.LIMIT_COLLECT()?
                    output.take(SparkContextManager.COLLECT_ITEM_LIMIT) : output.collect())
                localOutput += item + "\n";
            return localOutput;
        }
    }

    private JsoniqLexer getInputSource(String arg) throws IOException {
        arg = arg.trim();
        //return embedded file
        if(arg == null)
            new JsoniqLexer(new ANTLRInputStream(new Main().getClass().getResourceAsStream("/queries/runQuery.iq")));
        if(arg.startsWith("file://") || arg.startsWith("/")) {
            FileReader reader = this.getFileReader(arg);
            return new JsoniqLexer(new ANTLRInputStream(reader));
        }
        if(arg.startsWith("hdfs://")) {
            org.apache.hadoop.fs.FileSystem fileSystem = org.apache.hadoop.fs.FileSystem
                    .get( URI.create(arg) ,SparkContextManager.getInstance().getContext().hadoopConfiguration());
            FSDataInputStream in;
            try {
                in = fileSystem.open(new Path(arg));
            } catch (Exception ex){
                ex.printStackTrace();
                throw ex;
            }
            return new JsoniqLexer(new ANTLRInputStream(in));
        }
        throw new RuntimeException("Unknown url protocol");
    }

    private FileReader getFileReader(String arg) throws FileNotFoundException {
        FileReader reader;
        try {
            reader = new FileReader(new File(arg));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        }

        return reader;
    }

    private JsoniqExpressionTreeVisitor parse(JsoniqLexer lexer) throws IOException {
        JsoniqParser parser = new JsoniqParser(new CommonTokenStream(lexer));
        parser.setErrorHandler(new BailErrorStrategy());
        JsoniqExpressionTreeVisitor visitor = new JsoniqExpressionTreeVisitor();
        try
        {
            JsoniqParser.MainModuleContext unit = parser.mainModule();
            visitor.visit(unit);

        } catch (ParseCancellationException ex)
        {
            ParsingException e = new ParsingException(lexer.getText(), lexer.getLine());
            e.initCause(ex);
            throw e;
        }

        return visitor;

    }

    private RuntimeIterator generateRuntimeIterators(Expression expression){
        RuntimeIterator result = new RuntimeIteratorVisitor().visit(expression, null);
        return result;
    }

    private void generateStaticContext(Expression expression){
        new StaticContextVisitor().visit(expression, expression.getStaticContext());
    }

    private String getIteratorOutput(RuntimeIterator iterator, boolean indent){
        iterator.open(new DynamicContext());
        Item result = iterator.next();
        if(result == null)
            return "";
        String singleOutput = result.serialize();
        if(!iterator.hasNext())
            return singleOutput;
        else {
            int itemCount = 0;
            String output = "( " + result.serialize() +", " + (indent? "\n": "");
            while (iterator.hasNext() &&
                    ((itemCount< this._itemOutputLimit && _itemOutputLimit > 0) ||
                    _itemOutputLimit == 0 )) {
                output += iterator.next().serialize() + ", " + (indent? "\n": "");
                itemCount++;
            }
            //remove last comma
            output = output.substring(0, output.length() - 2);
            output += ")";
            return output;
        }
    }

    private void writeTimeLog(long totalTime) throws IOException {
        String result = "[ExecTime]" + totalTime;
        if(_logFilePath.startsWith("file://") || _logFilePath.startsWith("/")) {
            String timeLogPath = _logFilePath.substring(0, _logFilePath.lastIndexOf("/"));
            timeLogPath += Path.SEPARATOR + "time_log_";
            java.nio.file.Path finalPath = FileUtils.getUniqueFileName(timeLogPath);
            java.nio.file.Files.write(finalPath, result.getBytes());
        }
        if(_logFilePath.startsWith("hdfs://")) {
            org.apache.hadoop.fs.FileSystem fileSystem = org.apache.hadoop.fs.FileSystem
                    .get(SparkContextManager.getInstance().getContext().hadoopConfiguration());
            FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path(_logFilePath));
            BufferedOutputStream stream = new BufferedOutputStream(fsDataOutputStream);
            stream.write(result.getBytes());
            stream.close();
        }
    }

    protected String runIterators(RuntimeIterator iterator, boolean indent){
        String actualOutput = getIteratorOutput(iterator, indent);
        return actualOutput;
    }


}
