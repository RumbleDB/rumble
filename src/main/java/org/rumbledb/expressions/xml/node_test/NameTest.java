package org.rumbledb.expressions.xml.node_test;

import org.rumbledb.context.Name;

// TODO: Add support for name test
public class NameTest implements NodeTest {
    private Name qname;
    private String wildcardWithNCName;

    public NameTest(Name qname) {
        this.qname = qname;
        this.wildcardWithNCName = null;
    }

    public NameTest(String wildcardWithNCName) {
        this.qname = null;
        this.wildcardWithNCName = wildcardWithNCName;
    }
}
