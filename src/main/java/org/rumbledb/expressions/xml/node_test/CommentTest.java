package org.rumbledb.expressions.xml.node_test;


import java.io.Serial;

/**
 * XQuery 3.1 Section 2.5.5 - SequenceType Matching
 * CommentTest ::= "comment" "(" ")"
 * A CommentTest matches any comment node.
 */
public class CommentTest implements NodeTest {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "comment()";
    }


}

