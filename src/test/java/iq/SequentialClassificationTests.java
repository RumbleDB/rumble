package iq;

import iq.base.AnnotationsTestsBase;
import org.junit.Test;
import org.rumbledb.compiler.VisitorHelpers;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.scripting.Program;
import org.rumbledb.expressions.scripting.block.BlockStatement;
import org.rumbledb.expressions.scripting.control.ConditionalStatement;
import org.rumbledb.expressions.scripting.control.SwitchCaseStatement;
import org.rumbledb.expressions.scripting.control.SwitchStatement;
import org.rumbledb.expressions.scripting.control.TryCatchStatement;
import org.rumbledb.expressions.scripting.control.TypeSwitchStatement;
import org.rumbledb.expressions.scripting.control.TypeSwitchStatementCase;
import org.rumbledb.expressions.scripting.declaration.VariableDeclStatement;
import org.rumbledb.expressions.scripting.loops.ExitStatement;
import org.rumbledb.expressions.scripting.loops.FlowrStatement;
import org.rumbledb.expressions.scripting.loops.WhileStatement;
import org.rumbledb.expressions.scripting.mutation.ApplyStatement;
import org.rumbledb.expressions.scripting.statement.Statement;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SequentialClassificationTests extends AnnotationsTestsBase {

    private MainModule parseAndCompile(String filePath) throws IOException {
        URI uri = FileSystemUtil.resolveURIAgainstWorkingDirectory(
            filePath,
            getConfiguration(),
            ExceptionMetadata.EMPTY_METADATA
        );
        return VisitorHelpers.parseMainModuleFromLocation(
            uri,
            getConfiguration()
        );
    }

    @Test(timeout = 100000)
    public void testBlockStatementWithSequentialStatement() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/sequential/BlockStatementSequential.jq";

        MainModule mainModule = parseAndCompile(filePath);
        Program program = (Program) mainModule.getDescendantsMatching(stmt -> stmt instanceof Program).get(0);
        StatementsAndOptionalExpr statementsAndOptionalExpr = program.getStatementsAndOptionalExpr();
        assertEquals(2, statementsAndOptionalExpr.getStatements().size());
        BlockStatement statement1 = (BlockStatement) statementsAndOptionalExpr.getStatements().get(0);
        BlockStatement statement2 = (BlockStatement) statementsAndOptionalExpr.getStatements().get(1);
        assertTrue(statement1.isSequential());
        assertFalse(statement2.isSequential());
    }

    @Test(timeout = 100000)
    public void testWhileStatementWithNestedBreak() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/sequential/WhileStatementWithNestedBreak.jq";
        MainModule mainModule = parseAndCompile(filePath);
        Program program = (Program) mainModule.getDescendantsMatching(stmt -> stmt instanceof Program).get(0);
        StatementsAndOptionalExpr statementsAndOptionalExpr = program.getStatementsAndOptionalExpr();
        assertEquals(2, statementsAndOptionalExpr.getStatements().size());
        WhileStatement whileStatement = (WhileStatement) statementsAndOptionalExpr.getStatements().get(1);
        BlockStatement blockStatement = (BlockStatement) whileStatement.getStatement();
        ConditionalStatement conditionalStatement = (ConditionalStatement) blockStatement.getBlockStatements().get(1);
        assertTrue(blockStatement.isSequential());
        assertTrue(conditionalStatement.isSequential());
        assertFalse(whileStatement.isSequential());
    }

    @Test(timeout = 100000)
    public void testAssignStatementSequential() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/sequential/AssignStatementSequential.jq";
        MainModule mainModule = parseAndCompile(filePath);
        Program program = (Program) mainModule.getDescendantsMatching(stmt -> stmt instanceof Program).get(0);
        StatementsAndOptionalExpr statementsAndOptionalExpr = program.getStatementsAndOptionalExpr();
        assertEquals(3, statementsAndOptionalExpr.getStatements().size());
        ConditionalStatement conditionalStatement = (ConditionalStatement) statementsAndOptionalExpr.getStatements()
            .get(2);
        assertTrue(conditionalStatement.isSequential());
    }

    @Test(timeout = 100000)
    public void testAssignStatementNestedSequential() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/sequential/AssignStatementNestedSequential.jq";
        MainModule mainModule = parseAndCompile(filePath);
        Program program = (Program) mainModule.getDescendantsMatching(stmt -> stmt instanceof Program).get(0);
        StatementsAndOptionalExpr statementsAndOptionalExpr = program.getStatementsAndOptionalExpr();
        assertEquals(3, statementsAndOptionalExpr.getStatements().size());
        ConditionalStatement conditionalStatement = (ConditionalStatement) statementsAndOptionalExpr.getStatements()
            .get(2);
        assertTrue(conditionalStatement.isSequential());
    }

    @Test(timeout = 100000)
    public void testApplyStatementSequential() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/sequential/ApplyStatementSequential.jq";
        MainModule mainModule = parseAndCompile(filePath);
        Program program = (Program) mainModule.getDescendantsMatching(stmt -> stmt instanceof Program).get(0);
        StatementsAndOptionalExpr statementsAndOptionalExpr = program.getStatementsAndOptionalExpr();
        assertEquals(2, statementsAndOptionalExpr.getStatements().size());
        ApplyStatement applyStatement = (ApplyStatement) statementsAndOptionalExpr.getStatements().get(1);
        assertTrue(applyStatement.isSequential());
    }

    @Test(timeout = 100000)
    public void testApplyStatementNestedSequential() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/sequential/ApplyStatementNestedSequential.jq";
        MainModule mainModule = parseAndCompile(filePath);
        Program program = (Program) mainModule.getDescendantsMatching(stmt -> stmt instanceof Program).get(0);
        StatementsAndOptionalExpr statementsAndOptionalExpr = program.getStatementsAndOptionalExpr();
        assertEquals(1, statementsAndOptionalExpr.getStatements().size());
        BlockStatement blockStatement = (BlockStatement) statementsAndOptionalExpr.getStatements().get(0);
        assertTrue(blockStatement.isSequential());
        VariableDeclStatement variableDeclStatement = (VariableDeclStatement) blockStatement.getBlockStatements()
            .get(0);
        FlowrStatement flowrStatement = (FlowrStatement) blockStatement.getBlockStatements().get(1);
        assertTrue(variableDeclStatement.isSequential());
        assertTrue(flowrStatement.isSequential());
        SwitchStatement switchStatement = (SwitchStatement) flowrStatement.getReturnStatementClause()
            .getReturnStatement();
        assertTrue(switchStatement.isSequential());
        SwitchCaseStatement case1 = switchStatement.getCases().get(0);
        SwitchCaseStatement case2 = switchStatement.getCases().get(1);
        ApplyStatement defaultStatement = (ApplyStatement) switchStatement.getDefaultStatement();
        assertTrue(case1.getReturnStatement().isSequential());
        assertFalse(case2.getReturnStatement().isSequential());
        assertFalse(defaultStatement.isSequential());
    }

    @Test(timeout = 100000)
    public void testApplyStatementNestedSequential2() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/sequential/ApplyStatementNestedSequential2.jq";
        MainModule mainModule = parseAndCompile(filePath);
        Program program = (Program) mainModule.getDescendantsMatching(stmt -> stmt instanceof Program).get(0);
        StatementsAndOptionalExpr statementsAndOptionalExpr = program.getStatementsAndOptionalExpr();
        assertEquals(1, statementsAndOptionalExpr.getStatements().size());
        WhileStatement whileStatement = (WhileStatement) statementsAndOptionalExpr.getStatements().get(0);
        assertTrue(whileStatement.isSequential());
        BlockStatement blockStatement = (BlockStatement) whileStatement.getStatement();
        assertTrue(blockStatement.isSequential());
        assertEquals(3, blockStatement.getBlockStatements().size());
        ConditionalStatement blockConditionalStatement = (ConditionalStatement) blockStatement.getBlockStatements()
            .get(0);
        WhileStatement blockWhileStatement = (WhileStatement) blockStatement.getBlockStatements().get(1);
        TryCatchStatement blockTryCatchStatement = (TryCatchStatement) blockStatement.getBlockStatements().get(2);
        assertFalse(blockConditionalStatement.isSequential());
        assertFalse(blockWhileStatement.isSequential());
        assertTrue(blockTryCatchStatement.isSequential());
        VariableDeclStatement variableDeclStatement = (VariableDeclStatement) blockTryCatchStatement.getTryStatement()
            .getBlockStatements()
            .get(0);
        FlowrStatement flowrStatement = (FlowrStatement) blockTryCatchStatement.getTryStatement()
            .getBlockStatements()
            .get(1);
        assertTrue(variableDeclStatement.isSequential());
        assertTrue(flowrStatement.isSequential());
        ConditionalStatement flowrConditionStatement = (ConditionalStatement) flowrStatement.getReturnStatementClause()
            .getReturnStatement();
        assertTrue(flowrConditionStatement.isSequential());
        ApplyStatement applyStatement1 = (ApplyStatement) flowrConditionStatement.getBranch();
        ApplyStatement applyStatement2 = (ApplyStatement) flowrConditionStatement.getElseBranch();
        assertTrue(applyStatement1.isSequential());
        assertFalse(applyStatement2.isSequential());
    }

    @Test(timeout = 100000)
    public void testTypeSwitchWithExitSequential() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/sequential/TypeSwitchWithExitSequential.jq";
        MainModule mainModule = parseAndCompile(filePath);
        Program program = (Program) mainModule.getDescendantsMatching(stmt -> stmt instanceof Program).get(0);
        StatementsAndOptionalExpr statementsAndOptionalExpr = program.getStatementsAndOptionalExpr();
        assertEquals(1, statementsAndOptionalExpr.getStatements().size());
        TypeSwitchStatement typeSwitchStatement = (TypeSwitchStatement) statementsAndOptionalExpr.getStatements()
            .get(0);
        assertTrue(typeSwitchStatement.isSequential());
        assertEquals(3, typeSwitchStatement.getCases().size());
        TypeSwitchStatementCase case1 = typeSwitchStatement.getCases().get(0);
        TypeSwitchStatementCase case2 = typeSwitchStatement.getCases().get(1);
        TypeSwitchStatementCase caseWithExit = typeSwitchStatement.getCases().get(2);
        TypeSwitchStatementCase defaultStatement = typeSwitchStatement.getDefaultCase();
        assertFalse(case1.getReturnStatement().isSequential());
        assertFalse(case2.getReturnStatement().isSequential());
        assertTrue(caseWithExit.getReturnStatement().isSequential());
        assertFalse(defaultStatement.getReturnStatement().isSequential());
        ExitStatement exitStatement = (ExitStatement) caseWithExit.getReturnStatement();
        assertTrue(exitStatement.isSequential());
    }

    @Test(timeout = 100000)
    public void testFunctionWithExitSequential() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/sequential/FunctionWithExitSequential.jq";
        MainModule mainModule = parseAndCompile(filePath);
        Prolog prolog = mainModule.getProlog();
        assertEquals(1, prolog.getFunctionDeclarations().size());
        FunctionDeclaration functionDeclaration = prolog.getFunctionDeclarations().get(0);
        assertTrue(functionDeclaration.getExpression().isSequential());
        StatementsAndOptionalExpr functionBody = ((InlineFunctionExpression) functionDeclaration.getExpression())
            .getBody();
        assertEquals(2, functionBody.getStatements().size());
        VariableDeclStatement variableDeclStatement = (VariableDeclStatement) functionBody.getStatements().get(0);
        ExitStatement exitStatement = (ExitStatement) functionBody.getStatements().get(1);
        assertTrue(variableDeclStatement.isSequential());
        assertTrue(exitStatement.isSequential());
    }

    @Test(timeout = 100000)
    public void testBlockStatementWithExitSequential() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/sequential/BlockStatementWithExitSequential.jq";
        MainModule mainModule = parseAndCompile(filePath);
        Program program = (Program) mainModule.getDescendantsMatching(stmt -> stmt instanceof Program).get(0);
        StatementsAndOptionalExpr statementsAndOptionalExpr = program.getStatementsAndOptionalExpr();
        assertEquals(5, statementsAndOptionalExpr.getStatements().size());
        VariableDeclStatement variableDeclStatement1 = (VariableDeclStatement) statementsAndOptionalExpr.getStatements()
            .get(0);
        VariableDeclStatement variableDeclStatement2 = (VariableDeclStatement) statementsAndOptionalExpr.getStatements()
            .get(1);
        VariableDeclStatement variableDeclStatement3 = (VariableDeclStatement) statementsAndOptionalExpr.getStatements()
            .get(2);
        VariableDeclStatement variableDeclStatement4 = (VariableDeclStatement) statementsAndOptionalExpr.getStatements()
            .get(3);
        assertTrue(variableDeclStatement1.isSequential());
        assertTrue(variableDeclStatement2.isSequential());
        assertTrue(variableDeclStatement3.isSequential());
        assertTrue(variableDeclStatement4.isSequential());
        ConditionalStatement conditionalStatement = (ConditionalStatement) statementsAndOptionalExpr.getStatements()
            .get(4);
        assertTrue(conditionalStatement.isSequential());
        BlockStatement thenStatement = (BlockStatement) conditionalStatement.getBranch();
        BlockStatement elseStatement = (BlockStatement) conditionalStatement.getElseBranch();
        assertTrue(thenStatement.isSequential());
        assertTrue(elseStatement.isSequential());
    }

    @Test(timeout = 100000)
    public void testNonSequential() throws Throwable {
        File nonsequentialTestsDirectory = new File(
                System.getProperty("user.dir")
                    +
                    "/src/test/resources/test_files/sequential/non-sequential"
        );
        initializeTests(nonsequentialTestsDirectory);
        for (File testFile : this.testFiles) {
            System.err.println(counter++ + " : " + testFile);
            MainModule mainModule = parseAndCompile(testFile.getAbsolutePath());
            for (Node descendant : mainModule.getDescendants()) {
                if (descendant instanceof Expression) {
                    assertFalse(((Expression) descendant).isSequential());
                } else if (descendant instanceof Statement) {
                    if (descendant instanceof VariableDeclStatement) {
                        assertTrue(((Statement) descendant).isSequential());
                    } else {
                        assertFalse(((Statement) descendant).isSequential());
                    }
                }
            }
        }
    }
}
