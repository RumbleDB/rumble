package org.rumbledb.compiler.wrapper;

import org.rumbledb.expressions.Node;

public class BlockExpressionMetadata {
    private final Node innerMostBlock;
    private final Node innerMostLoopStatement;

    public BlockExpressionMetadata(Node innerMostBlock, Node innerMostLoopStatement) {
        this.innerMostBlock = innerMostBlock;
        this.innerMostLoopStatement = innerMostLoopStatement;
    }

    public Node getInnerMostBlock() {
        return innerMostBlock;
    }

    public Node getInnerMostLoopStatement() {
        return innerMostLoopStatement;
    }
}
