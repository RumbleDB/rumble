package org.rumbledb.expressions.scripting.annotations;

import org.rumbledb.context.Name;
import org.rumbledb.expressions.Expression;

import java.util.List;

public class Annotation {
    private final Name annotationName;
    private final List<Expression> literals;

    public Annotation(Name annotationName, List<Expression> literals) {
        this.annotationName = annotationName;
        this.literals = literals;
    }

    public Name getAnnotationName() {
        return annotationName;
    }

    public List<Expression> getLiterals() {
        return literals;
    }
}
