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
 package iq.base;

import jiqs.jsoniq.compiler.JsoniqExpressionTreeVisitor;
import jiqs.jsoniq.compiler.parser.JsoniqBaseVisitor;
import jiqs.jsoniq.compiler.parser.JsoniqLexer;
import jiqs.jsoniq.compiler.parser.JsoniqParser;
import jiqs.jsoniq.exceptions.ParsingException;
import jiqs.jsoniq.exceptions.SemanticException;
import jiqs.semantics.visitor.StaticContextVisitor;
import jiqs.semantics.visitor.RuntimeIteratorVisitor;
import jiqs.jsoniq.exceptions.SparksoniqRuntimeException;
import jiqs.jsoniq.runtime.iterator.RuntimeIterator;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.Assert;
import utils.FileManager;
import utils.annotations.AnnotationParseException;
import utils.annotations.AnnotationProcessor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnnotationsTestsBase {
    protected AnnotationProcessor.TestAnnotation currentAnnotation;
    protected static int counter = 0;
    protected List<File> testFiles = new ArrayList<>();


    public void initializeTests(File dir) {
        FileManager.loadJiqFiles(dir).forEach(file -> testFiles.add(file));
    }

    /**
     Tests annotations
     */
    protected JsoniqParser.MainModuleContext testAnnotations(String path , JsoniqBaseVisitor visitor) throws IOException
    {
        JsoniqParser.MainModuleContext context = null;
        RuntimeIterator runtimeIterator =  null;
        try {
            currentAnnotation = AnnotationProcessor.readAnnotation(new FileReader(path));
        } catch (AnnotationParseException e) {
            e.printStackTrace();
            Assert.fail();
        }

        try {
            context = this.parse(new FileReader(path), visitor);

            //generate static context and runtime iterators
            if(visitor instanceof JsoniqExpressionTreeVisitor) {
                JsoniqExpressionTreeVisitor completeVisitor = ((JsoniqExpressionTreeVisitor)visitor);
                //generate static context
                new StaticContextVisitor().visit( completeVisitor.getQueryExpression(),  completeVisitor.getQueryExpression().getStaticContext());
                //generate iterators
                runtimeIterator = new RuntimeIteratorVisitor().visit( completeVisitor.getQueryExpression(), null);
            }
            //PARSING
        } catch (ParsingException exception) {
            String errorOutput = exception.getMessage();
            checkErrorCode(errorOutput, currentAnnotation.getErrorCode());
            if(currentAnnotation.shouldParse()) {
                Assert.fail("Program did not parse when expected to.\nError output: " + errorOutput + "\n");
                return context;
            }
            else{
                System.out.println(errorOutput);
                Assert.assertTrue(true);
                return context;
            }

            //SEMANTIC
        } catch (SemanticException exception){
            String errorOutput = exception.getMessage();
            checkErrorCode(errorOutput, currentAnnotation.getErrorCode());
            try{if(currentAnnotation.shouldCompile()) {
                Assert.fail("Program did not compile when expected to.\nError output: " + errorOutput + "\n");
                return context;
            } else {
                System.out.println(errorOutput);
                Assert.assertTrue(true);
                return context;}
            }catch (Exception ex){}

            //RUNTIME
        } catch (SparksoniqRuntimeException exception){
            String errorOutput = exception.getMessage();
            checkErrorCode(errorOutput, currentAnnotation.getErrorCode());
            try{
            if(currentAnnotation.shouldRun()) {
                Assert.fail("Program did not runLocal when expected to.\nError output: " + errorOutput + "\n");
                return context;
            }else {
                System.out.println(errorOutput);
                Assert.assertTrue(true);
                return context;
            }}catch (Exception ex){}

        }

        try{
            if(!currentAnnotation.shouldCompile()) {
                Assert.fail("Program compiled when not expected to.\n");
                return context;
            }} catch (Exception ex){}

        if(!currentAnnotation.shouldParse()) {
            Assert.fail("Program parsed when not expected to.\n");
            return context;
        }

        //PROGRAM SHOULD RUN
        if(currentAnnotation instanceof AnnotationProcessor.RunnableTestAnnotation &&
                currentAnnotation.shouldRun()){
            try {
                checkExpectedOutput( currentAnnotation.getOutput(), runtimeIterator);
            } catch (SparksoniqRuntimeException exception){
                String errorOutput = exception.getMessage();
                Assert.fail("Program did not runLocal when expected to.\nError output: " + errorOutput + "\n");
            }
        } else {
            //PROGRAM SHOULD CRASH
            if(currentAnnotation instanceof AnnotationProcessor.UnrunnableTestAnnotation &&
                    !currentAnnotation.shouldRun()) {
                try {
                    checkExpectedOutput( currentAnnotation.getOutput(), runtimeIterator);
                }  catch (SparksoniqRuntimeException exception){
                    String errorOutput = exception.getMessage();
                    checkErrorCode(errorOutput, currentAnnotation.getErrorCode());
                    return context;
                }

                Assert.fail("Program runLocal when not expected to");
            }
        }

        return context;
    }

    protected void checkErrorCode(String errorOutput, String expectedErrorCode) {
        if(errorOutput != null && expectedErrorCode != null)
            Assert.assertTrue("Unexpected error code returned; Expected: " + expectedErrorCode +
                    "; Error: " + errorOutput,errorOutput.contains(expectedErrorCode));
    }

    protected void checkExpectedOutput(String expectedOutput, RuntimeIterator runtimeIterator) {
        Assert.assertTrue(true);
    }

    private  JsoniqParser.MainModuleContext parse(FileReader reader, JsoniqBaseVisitor visitor) throws IOException {
        JsoniqLexer lexer = new JsoniqLexer(new ANTLRInputStream(reader));
        JsoniqParser parser = new JsoniqParser(new CommonTokenStream(lexer));
        parser.setErrorHandler(new BailErrorStrategy());

        try
        {
            JsoniqParser.MainModuleContext unit = parser.mainModule();
            visitor.visit(unit);
            return unit;

        } catch (ParseCancellationException ex)
        {
            ParsingException e = new ParsingException(lexer.getText(), lexer.getLine());
            e.initCause(ex);
            throw e;
        }

    }


}
