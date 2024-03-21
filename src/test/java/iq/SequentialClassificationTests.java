package iq;

import iq.base.AnnotationsTestsBase;
import org.junit.Test;
import org.rumbledb.compiler.VisitorHelpers;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.scripting.Program;
import org.rumbledb.expressions.scripting.block.BlockStatement;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SequentialClassificationTests extends AnnotationsTestsBase {

    @Test(timeout = 100000)
    public void testBlockStatementWithSequentialStatement() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/sequential/BlockStatementSequential.jq";
        URI uri = FileSystemUtil.resolveURIAgainstWorkingDirectory(
            filePath,
            getConfiguration(),
            ExceptionMetadata.EMPTY_METADATA
        );
        MainModule mainModule = VisitorHelpers.parseMainModuleFromLocation(
            uri,
            getConfiguration()
        );
        Program program = (Program) mainModule.getDescendantsMatching(stmt -> stmt instanceof Program).get(0);
        StatementsAndOptionalExpr statementsAndOptionalExpr = program.getStatementsAndOptionalExpr();
        assertEquals(statementsAndOptionalExpr.getStatements().size(), 2);
        BlockStatement statement1 = (BlockStatement) statementsAndOptionalExpr.getStatements().get(0);
        BlockStatement statement2 = (BlockStatement) statementsAndOptionalExpr.getStatements().get(1);
        assertTrue(statement1.isSequential());
        assertFalse(statement2.isSequential());
    }
}
