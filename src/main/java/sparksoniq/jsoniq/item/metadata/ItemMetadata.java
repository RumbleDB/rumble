package sparksoniq.jsoniq.item.metadata;

import java.io.Serializable;

import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

public class ItemMetadata implements Serializable {
    public static ItemMetadata fromIteratorMetadata(IteratorMetadata iteratorMetadata){
        return new ItemMetadata(iteratorMetadata.getExpressionMetadata());
    }


    public ExpressionMetadata getExpressionMetadata() {
        return _expressionMetadata;
    }

    public ItemMetadata(ExpressionMetadata expressionMetadata) {
        this._expressionMetadata = expressionMetadata;

    }

    private final ExpressionMetadata _expressionMetadata;
}
