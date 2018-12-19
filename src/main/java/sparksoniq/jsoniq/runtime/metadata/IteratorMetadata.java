package sparksoniq.jsoniq.runtime.metadata;

import java.io.Serializable;

import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;

public class IteratorMetadata implements Serializable {


    public ExpressionMetadata getExpressionMetadata() {
        return _expressionMetadata;
    }

    public IteratorMetadata(ExpressionMetadata expressionMetadata) {
        this._expressionMetadata = expressionMetadata;

    }

    private final ExpressionMetadata _expressionMetadata;
}
