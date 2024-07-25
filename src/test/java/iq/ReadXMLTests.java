package iq;

import iq.base.AnnotationsTestsBase;
import org.junit.Before;
import org.junit.Test;
import org.rumbledb.api.Item;
import org.rumbledb.api.Rumble;
import org.rumbledb.api.SequenceOfItems;
import org.rumbledb.compiler.VisitorHelpers;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.scripting.Program;
import org.rumbledb.expressions.xml.PathExpr;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ReadXMLTests extends AnnotationsTestsBase {
    private Rumble rumble;

    @Before
    public void initializeRumble() {
        this.rumble = new Rumble(getConfiguration());
    }

    private URI resolveUri(String path) {
        return FileSystemUtil.resolveURIAgainstWorkingDirectory(
            path,
            getConfiguration(),
            ExceptionMetadata.EMPTY_METADATA
        );
    }

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

    private PathExpr getPathExprFromMainModule(String filePath) throws IOException {
        MainModule mainModule = parseAndCompile(filePath);
        Program program = (Program) mainModule.getDescendantsMatching(stmt -> stmt instanceof Program).get(0);
        return (PathExpr) program.getStatementsAndOptionalExpr().getExpression();
    }

    @Test(timeout = 100000)
    public void testBlockStatementWithSequentialStatement() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/xml/read-xml/FunctionXmlDoc1.jq";
        URI uri = resolveUri(filePath);
        SequenceOfItems sequence = this.rumble.runQuery(uri);
        sequence.open();
        Item result = sequence.next();
        List<Item> children = result.children();
        Item elementNode1 = children.get(0);
        Item elementNode2 = children.get(1);
        Item elementNode3 = children.get(2);
        Item elementNode4 = children.get(3);
        String content1 = elementNode1.stringValue().trim().replaceAll("(?<=\\n)\\s+", "");
        String content2 = elementNode2.stringValue().trim().replaceAll("(?<=\\n)\\s+", "");;
        String content3 = elementNode3.stringValue().trim().replaceAll("(?<=\\n)\\s+", "");;
        String content4 = elementNode4.stringValue().trim().replaceAll("(?<=\\n)\\s+", "");;
        Item attribute1 = elementNode1.attributes().get(0);
        Item attribute2 = elementNode2.attributes().get(0);
        Item attribute3 = elementNode3.attributes().get(0);
        Item attribute4 = elementNode4.attributes().get(0);
        assertFalse(sequence.hasNext());
        assertEquals(4, children.size());
        assertEquals(4, elementNode1.children().size());
        assertEquals(4, elementNode2.children().size());
        assertEquals(8, elementNode3.children().size());
        assertEquals(4, elementNode4.children().size());
        assertEquals("Everyday Italian\nGiada De Laurentiis\n2005\n30.00", content1);
        assertEquals("Harry Potter\nJ K. Rowling\n2005\n29.99", content2);
        assertEquals(
            "XQuery Kick Start\nJames McGovern\nPer Bothner\nKurt Cagle\nJames Linn\nVaidyanathan Nagarajan\n2003\n49.99",
            content3
        );
        assertEquals("Learning XML\nErik T. Ray\n2003\n39.95", content4);
        assertEquals("category", attribute1.nodeName());
        assertEquals("category", attribute2.nodeName());
        assertEquals("category", attribute3.nodeName());
        assertEquals("category", attribute4.nodeName());
        assertEquals("cooking", attribute1.stringValue());
        assertEquals("children", attribute2.stringValue());
        assertEquals("web", attribute3.stringValue());
        assertEquals("web", attribute4.stringValue());
    }

    @Test(timeout = 100000)
    public void testPathConstructionWithStartingDash() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/xml/compile-xpath/XPathExample1.jq";
        PathExpr pathExpr = getPathExprFromMainModule(filePath);
        StringBuffer sb = new StringBuffer();
        pathExpr.serializeToJSONiq(sb, 0);
        assertEquals("/child::div1/child::div2\n", sb.toString());
    }

    @Test(timeout = 100000)
    public void testPathConstructionWithoutStartingDash() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/xml/compile-xpath/XPathExample2.jq";
        PathExpr pathExpr = getPathExprFromMainModule(filePath);
        StringBuffer sb = new StringBuffer();
        pathExpr.serializeToJSONiq(sb, 0);
        assertEquals("parent::author/child::title\n", sb.toString());
    }

    @Test(timeout = 100000)
    public void testPathConstructionWithPredicates() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/xml/compile-xpath/XPathExample3.jq";
        PathExpr pathExpr = getPathExprFromMainModule(filePath);
        StringBuffer sb = new StringBuffer();
        pathExpr.serializeToJSONiq(sb, 0);
        String res = sb.toString().trim().replaceAll("\n", "");
        res = res.replaceAll(" ", "");
        res = res.replaceAll("[,#]", "");
        assertEquals("/child::book/child::chapter[(fn:position0())eq(5)]/child::section[(fn:position0())eq(2)]", res);
    }

    @Test(timeout = 100000)
    public void testPathConstructionWithDoubleSlashStart() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/xml/compile-xpath/XPathExample4.jq";
        PathExpr pathExpr = getPathExprFromMainModule(filePath);
        StringBuffer sb = new StringBuffer();
        pathExpr.serializeToJSONiq(sb, 0);
        String res = sb.toString().trim().replaceAll("\n", "");
        res = res.replaceAll(" ", "");
        res = res.replaceAll("[,#]", "");
        assertEquals("/descendant-or-self::node()/child::book/child::chapter[5]/child::section[2]", res);
    }

    @Test(timeout = 100000)
    public void testPathConstructionWithOrOperation() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/xml/compile-xpath/XPathExample5.jq";
        PathExpr pathExpr = getPathExprFromMainModule(filePath);
        StringBuffer sb = new StringBuffer();
        pathExpr.serializeToJSONiq(sb, 0);
        String res = sb.toString().trim().replaceAll("\n", "");
        res = res.replaceAll(" ", "");
        res = res.replaceAll("[,#]", "");
        assertEquals("child::book/(child::chapter)or(child::appendix)/descendant-or-self::node()/child::section", res);
    }

    @Test(timeout = 100000)
    public void testPathConstructionWithDoubleSlashes() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/xml/compile-xpath/XPathExample6.jq";
        PathExpr pathExpr = getPathExprFromMainModule(filePath);
        StringBuffer sb = new StringBuffer();
        pathExpr.serializeToJSONiq(sb, 0);
        String res = sb.toString().trim().replaceAll("\n", "");
        res = res.replaceAll(" ", "");
        res = res.replaceAll("[,#]", "");
        assertEquals(
            "child::chapter/descendant-or-self::node()/child::para/descendant-or-self::node()/attribute::version",
            res
        );
    }

    @Test(timeout = 100000)
    public void testPathConstructionWithWildcard() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/xml/compile-xpath/XPathExample7.jq";
        PathExpr pathExpr = getPathExprFromMainModule(filePath);
        StringBuffer sb = new StringBuffer();
        pathExpr.serializeToJSONiq(sb, 0);
        String res = sb.toString().trim().replaceAll("\n", "");
        res = res.replaceAll(" ", "");
        res = res.replaceAll("[,#]", "");
        assertEquals("attribute::*", res);
    }

    @Test(timeout = 100000)
    public void testPathConstructionWithWildcardReverseAxis() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/xml/compile-xpath/XPathExample8.jq";
        PathExpr pathExpr = getPathExprFromMainModule(filePath);
        StringBuffer sb = new StringBuffer();
        pathExpr.serializeToJSONiq(sb, 0);
        String res = sb.toString().trim().replaceAll("\n", "");
        res = res.replaceAll(" ", "");
        res = res.replaceAll("[,#]", "");
        assertEquals("parent::*", res);
    }

    @Test(timeout = 100000)
    public void testPathConstructionWithWildcardMultiPath() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/xml/compile-xpath/XPathExample9.jq";
        PathExpr pathExpr = getPathExprFromMainModule(filePath);
        StringBuffer sb = new StringBuffer();
        pathExpr.serializeToJSONiq(sb, 0);
        String res = sb.toString().trim().replaceAll("\n", "");
        res = res.replaceAll(" ", "");
        res = res.replaceAll("[,#]", "");
        assertEquals("child::*/child::para", res);
    }

    @Test(timeout = 100000)
    public void testPathConstructionWithAttributeTest() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/xml/compile-xpath/XPathExample10.jq";
        PathExpr pathExpr = getPathExprFromMainModule(filePath);
        StringBuffer sb = new StringBuffer();
        pathExpr.serializeToJSONiq(sb, 0);
        String res = sb.toString().trim().replaceAll("\n", "");
        res = res.replaceAll(" ", "");
        res = res.replaceAll("[,#]", "");
        assertEquals("attribute::attribute(price)", res);
    }

    @Test(timeout = 100000)
    public void testPathConstructionWithReverseAxis() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/xml/compile-xpath/XPathExample11.jq";
        PathExpr pathExpr = getPathExprFromMainModule(filePath);
        StringBuffer sb = new StringBuffer();
        pathExpr.serializeToJSONiq(sb, 0);
        String res = sb.toString().trim().replaceAll("\n", "");
        res = res.replaceAll(" ", "");
        res = res.replaceAll("[,#]", "");
        assertEquals("parent::node()", res);
    }

    @Test(timeout = 100000)
    public void testPathConstructionWithReverseAxisAbbreviated() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/xml/compile-xpath/XPathExample12.jq";
        PathExpr pathExpr = getPathExprFromMainModule(filePath);
        StringBuffer sb = new StringBuffer();
        pathExpr.serializeToJSONiq(sb, 0);
        String res = sb.toString().trim().replaceAll("\n", "");
        res = res.replaceAll(" ", "");
        res = res.replaceAll("[,#]", "");
        assertEquals("parent::node()/parent::element(*)", res);
    }

    @Test(timeout = 100000)
    public void testPathConstructionWithReverseAxisMultiPath() throws Throwable {
        String filePath = System.getProperty("user.dir")
            +
            "/src/test/resources/test_files/xml/compile-xpath/XPathExample13.jq";
        PathExpr pathExpr = getPathExprFromMainModule(filePath);
        StringBuffer sb = new StringBuffer();
        pathExpr.serializeToJSONiq(sb, 0);
        String res = sb.toString().trim().replaceAll("\n", "");
        res = res.replaceAll(" ", "");
        res = res.replaceAll("[,#]", "");
        assertEquals("ancestor-or-self::di/ancestor::p/parent::text()", res);
    }
}
