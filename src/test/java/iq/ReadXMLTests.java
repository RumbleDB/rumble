package iq;

import iq.base.AnnotationsTestsBase;
import org.junit.Before;
import org.junit.Test;
import org.rumbledb.api.Item;
import org.rumbledb.api.Rumble;
import org.rumbledb.api.SequenceOfItems;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

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
}
