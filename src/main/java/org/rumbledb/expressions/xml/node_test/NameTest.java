package org.rumbledb.expressions.xml.node_test;

import org.rumbledb.context.Name;

// TODO: Add support for name test
public class NameTest implements NodeTest {
    private Name qname;
    private String ncName;

    public NameTest(Name qname, String ncName) {
        this.qname = qname;
        this.ncName = ncName;
    }
}
