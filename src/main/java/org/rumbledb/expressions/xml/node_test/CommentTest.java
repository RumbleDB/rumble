package org.rumbledb.expressions.xml.node_test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * XQuery 3.1 Section 2.5.5 - SequenceType Matching
 * CommentTest ::= "comment" "(" ")"
 * A CommentTest matches any comment node.
 */
public class CommentTest implements NodeTest {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "comment()";
    }

    @Override
    public void write(Kryo kryo, Output output) {

    }

    @Override
    public void read(Kryo kryo, Input input) {

    }
}

