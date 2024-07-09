package org.rumbledb.expressions.scripting.annotations;

import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidAnnotationException;
import org.rumbledb.expressions.Expression;

import java.util.List;

import static org.rumbledb.expressions.scripting.annotations.AnnotationConstants.ASSIGNABLE;
import static org.rumbledb.expressions.scripting.annotations.AnnotationConstants.NON_ASSIGNABLE;

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

    public static boolean checkAssignable(
            List<Annotation> annotations,
            boolean defaultAssignable,
            ExceptionMetadata exceptionMetadata
    ) {
        boolean isAssignable = defaultAssignable;
        boolean hasAssignableAnnotation = false;
        boolean hasNonAssignableAnnotation = false;
        for (Annotation annotation : annotations) {
            if (annotation.getAnnotationName().equals(ASSIGNABLE)) {
                isAssignable = true;
                hasAssignableAnnotation = true;
            } else if (annotation.getAnnotationName().equals(NON_ASSIGNABLE)) {
                isAssignable = false;
                hasNonAssignableAnnotation = true;
            }
            if (hasAssignableAnnotation && hasNonAssignableAnnotation) {
                throw new InvalidAnnotationException(
                        "Both %an:assignable and %an:nonassignable annotations cannot be used for the same declaration",
                        exceptionMetadata
                );
            }
        }
        return isAssignable;
    }
}
