package sparksoniq.jsoniq.runtime.metadata;

import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;

import java.io.Serializable;

public class IteratorMetadata implements Serializable {


    public ExpressionMetadata getExpressionMetadata() {
        return _expressionMetadata;
    }

    public IteratorMetadata(ExpressionMetadata expressionMetadata) {
        this._expressionMetadata = expressionMetadata;

    }

    private final ExpressionMetadata _expressionMetadata;
}
