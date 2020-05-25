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

package iq;


import iq.base.AnnotationsTestsBase;
import org.junit.Assert;
import org.junit.Test;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.primary.VariableReferenceExpression;
import org.rumbledb.types.ItemType;
import java.io.File;
import java.util.Arrays;
import java.util.List;


public class FrontendTests extends AnnotationsTestsBase {

    public static final File grammarTestsDirectory = new File(
            System.getProperty("user.dir")
                +
                "/src/test/resources/test_files/parser"
    );
    public static final File astTestsDirectory = new File(
            System.getProperty("user.dir")
                +
                "/src/test/resources/test_files/ast"
    );
    public static final File semanticTestsDirectory = new File(
            System.getProperty("user.dir")
                +
                "/src/test/resources/test_files/semantics"
    );
    public static final String[] manualSemanticChecksFiles = new String[] { "TypesCheck.jq" };

    /**
     * Tests Grammar, uses ANTLR generated visitor
     *
     * @throws Throwable
     */
    @Test(timeout = 1000000)
    public void testGrammarAndParser() throws Throwable {
        initializeTests(grammarTestsDirectory);
        for (File testFile : this.testFiles) {
            System.err.println(counter++ + " : " + testFile);
            // FileReader reader = getReaderForFile(testFile.getAbsolutePath());
            testAnnotations(testFile.getAbsolutePath());
        }

    }
    /**
     * Tests ast, expression tree builder
     * 
     * @throws Throwable
     */
    // @Test(timeout = 1000000)
    // public void testASTBuilder() throws Throwable
    // {
    // initializeTests(astTestsDirectory);
    // for (File testFile : testFiles){
    // System.err.println(counter++ + " : " + testFile);
    // //FileReader reader = getReaderForFile(testFile.getAbsolutePath());
    // JsoniqExpressionTreeVisitor visitor = new JsoniqExpressionTreeVisitor();
    // JsoniqParser.MainModuleContext context = testAnnotations(testFile.getAbsolutePath(), visitor);
    // System.out.println("Found " + visitor.getQueryExpression().getDescendants().size() + " single expressions in
    // query");
    // testAstGeneration(testFile, visitor, context);
    // }
    //
    // }

    /**
     * Tests semantics
     *
     * @throws Throwable
     */
    @Test(timeout = 1000000)
    public void testSematicChecks() throws Throwable {
        initializeTests(semanticTestsDirectory);
        for (File testFile : this.testFiles) {
            System.err.println(counter++ + " : " + testFile);
            MainModule mainModule = testAnnotations(testFile.getAbsolutePath());
            if (Arrays.asList(manualSemanticChecksFiles).contains(testFile.getName()))
                testVariableTypes(testFile, mainModule);
        }
    }

    /*
     * private void testAstGeneration(File testFile, JsoniqExpressionTreeVisitor visitor,
     * JsoniqParser.MainModuleContext context) {
     * 
     * //check serialization of ANTLR parse tree vs expression tree serialization
     * Assert.assertTrue("AST Serialization mismatch",
     * AstSerialization.checkSerialization(visitor, context));
     * 
     * 
     * //MANUAL CHECKS
     * if (testFile.getName().contains("ManualFlowr.")) {
     * try {
     * FlworExpression node = (FlworExpression) visitor.getQueryExpression().getDescendants().get(0);
     * Assert.assertTrue(node.getContentClauses().size() == 4);
     * Assert.assertTrue(node.getStartClause().getClauseType() == FLWOR_CLAUSES.FOR);
     * 
     * ForClause startClause = (ForClause) node.getStartClause();
     * Assert.assertTrue(startClause.getForVariables().get(0)
     * .getVariableReference().getVariableName().equals("var"));
     * Assert.assertTrue(startClause.getForVariables().get(0).getIterator()
     * .getDescendantsOfType(d -> d instanceof RangeExpression, true).size() == 1);
     * 
     * RangeExpression range = (RangeExpression) startClause.getForVariables().get(0).getIterator()
     * .getDescendantsOfType(d -> d instanceof RangeExpression, true).get(0);
     * UnaryExpression unary = (UnaryExpression) range.
     * getDescendantsOfType(d -> d instanceof UnaryExpression, true).get(0);
     * Assert.assertTrue(((IntegerLiteral) unary.getPostfixExpression()
     * .getPrimaryExpressionNode()).getValue() == 1);
     * unary = (UnaryExpression) range.
     * getDescendantsOfType(d -> d instanceof UnaryExpression, true).get(1);
     * Assert.assertTrue(((IntegerLiteral) unary.getPostfixExpression()
     * .getPrimaryExpressionNode()).getValue() == 10);
     * 
     * Assert.assertTrue(node.getContentClauses().get(0) instanceof LetClause);
     * Assert.assertTrue(node.getContentClauses().get(1) instanceof WhereClause);
     * Assert.assertTrue(node.getContentClauses().get(2) instanceof WhereClause);
     * Assert.assertTrue(node.getContentClauses().get(3) instanceof GroupByClause);
     * 
     * VariableReference var = (VariableReference) node.getDescendantsOfType(d -> d instanceof VariableReference,
     * true).get(0);
     * Assert.assertTrue(var.getVariableName().equals("var"));
     * VariableReference j = (VariableReference) node.getDescendantsOfType(d -> d instanceof VariableReference,
     * true).get(1);
     * Assert.assertTrue(j.getVariableName().equals("j"));
     * 
     * } catch (Exception ex) {
     * Assert.fail("Unexpected AST expression in file " + testFile.getName());
     * }
     * 
     * 
     * } else if (testFile.getName().contains("ManualOrExpression.")) {
     * 
     * try {
     * OrExpression node = (OrExpression) visitor.getQueryExpression().getDescendants().get(0);
     * Assert.assertTrue(node.getMainExpression().getDescendants().get(0) instanceof NotExpression);
     * PostFixExpression array = (PostFixExpression) node
     * .getDescendantsOfType(d -> d instanceof PostFixExpression, true)
     * .stream().filter(p -> ((PostFixExpression) p)
     * .getPrimaryExpressionNode() instanceof ArrayConstructor).findFirst().get();
     * 
     * Assert.assertTrue(array != null);
     * 
     * PostFixExpression object = (PostFixExpression) node
     * .getDescendantsOfType(d -> d instanceof PostFixExpression, true)
     * .stream().filter(p -> ((PostFixExpression) p)
     * .getPrimaryExpressionNode() instanceof ObjectConstructor).findFirst().get();
     * Assert.assertTrue(object.getPrimaryExpressionNode() != null);
     * 
     * } catch (Exception ex) {
     * Assert.fail("Unexpected AST expression in file " + testFile.getName());
     * }
     * }
     * 
     * }
     */

    private void testVariableTypes(File testFile, MainModule mainModule) {

        List<Node> vars = mainModule
            .getDescendantsMatching(
                d -> d instanceof VariableReferenceExpression
                    && ((VariableReferenceExpression) d).getVariableName().equals("var")
            );
        vars.forEach(
            var -> Assert.assertTrue(
                ((VariableReferenceExpression) var).getType().getItemType().equals(ItemType.integerItem)
            )
        );

        List<Node> js = mainModule
            .getDescendantsMatching(
                d -> d instanceof VariableReferenceExpression
                    && ((VariableReferenceExpression) d).getVariableName().equals("j")
            );
        js.forEach(
            j -> Assert.assertTrue(
                ((VariableReferenceExpression) j).getType().getItemType().equals(ItemType.item)
                    ||
                    ((VariableReferenceExpression) j).getType().getItemType().equals(ItemType.stringItem)
            )
        );

        List<Node> internals = mainModule
            .getDescendantsMatching(
                d -> d instanceof VariableReferenceExpression
                    && ((VariableReferenceExpression) d).getVariableName().equals("internal")
            );
        internals.forEach(
            j -> Assert.assertTrue(
                ((VariableReferenceExpression) j).getType().getItemType().equals(ItemType.integerItem)
            )
        );

        List<Node> arry = mainModule
            .getDescendantsMatching(
                d -> d instanceof VariableReferenceExpression
                    && ((VariableReferenceExpression) d).getVariableName().equals("arry")
            );
        arry.forEach(
            j -> Assert.assertTrue(
                ((VariableReferenceExpression) j).getType().getItemType().equals(ItemType.arrayItem)
            )
        );

    }


}
