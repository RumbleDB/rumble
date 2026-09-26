/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.compiler.utils;

import java.util.List;

import org.rumbledb.context.Name;
import org.rumbledb.exceptions.DuplicateFunctionAnnotationException;
import org.rumbledb.exceptions.DuplicateVariableAnnotationException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidAnnotationException;
import org.rumbledb.exceptions.InvalidAnnotationNamespaceException;
import org.rumbledb.exceptions.InvalidInlineFunctionAnnotationException;
import org.rumbledb.expressions.scripting.annotations.Annotation;
import org.rumbledb.expressions.scripting.annotations.AnnotationConstants;

/**
 * Static validation and resolution of annotations on function declarations, variable declarations,
 * and inline function expressions.
 */
public final class AnnotationValidator {

    private AnnotationValidator() {}

    /**
     * Validates an annotation name against reserved namespaces according to W3C XQuery 3.1 §4.17/§4.18 [err:XQST0045].
     *
     * @param annotationName the annotation name to validate
     * @param metadata the source location metadata
     */
    public static void validateAnnotationName(Name annotationName, ExceptionMetadata metadata) {
        String namespace = annotationName.getNamespace();
        if (namespace == null) {
            return;
        }
        if (namespace.equals(Name.XQUERY_ANNOTATIONS_NS)) {
            String localName = annotationName.getLocalName();
            if ("updating".equals(localName)
                    || "simple".equals(localName)
                    || "public".equals(localName)
                    || "private".equals(localName)) {
                return;
            }
        }
        if (namespace.equals(Name.XML_NS)
                || namespace.equals(Name.XS_NS)
                || namespace.equals(Name.XSI_NS)
                || namespace.equals(Name.FN_NS)
                || namespace.equals(Name.MATH_NS)
                || namespace.equals(Name.MAP_NS)
                || namespace.equals(Name.ARRAY_NS)
                || namespace.equals(Name.XQUERY_ANNOTATIONS_NS)) {
            throw new InvalidAnnotationNamespaceException(
                    "Annotations cannot be declared in the reserved namespace " + namespace + ".", metadata);
        }
    }

    /**
     * Checks assignable annotations for exclusivity and returns whether the declaration is assignable.
     *
     * @param annotations the list of annotations
     * @param defaultAssignable default assignable state if not specified
     * @param exceptionMetadata the source location metadata
     * @return true if assignable, false otherwise
     */
    public static boolean checkAssignable(
            List<Annotation> annotations, boolean defaultAssignable, ExceptionMetadata exceptionMetadata) {
        boolean hasAssignable =
                annotations.stream().anyMatch(a -> AnnotationConstants.ASSIGNABLE.equals(a.getAnnotationName()));
        boolean hasNonAssignable =
                annotations.stream().anyMatch(a -> AnnotationConstants.NON_ASSIGNABLE.equals(a.getAnnotationName()));
        if (hasAssignable && hasNonAssignable) {
            throw new InvalidAnnotationException(
                    "Both %an:assignable and %an:nonassignable annotations cannot be used for the same declaration",
                    exceptionMetadata);
        }
        if (hasAssignable) {
            return true;
        }
        if (hasNonAssignable) {
            return false;
        }
        return defaultAssignable;
    }

    /**
     * Validates annotations on a function declaration according to W3C XQuery 3.1 §4.18 [err:XQST0106].
     * A function declaration cannot contain more than one visibility annotation (%public, %private).
     *
     * @param annotations the list of annotations
     */
    public static void validateFunctionAnnotations(List<Annotation> annotations) {
        if (annotations == null || annotations.isEmpty()) {
            return;
        }
        boolean hasVisibility = false;
        for (Annotation annotation : annotations) {
            Name name = annotation.getAnnotationName();
            if (isVisibilityAnnotation(name)) {
                if (hasVisibility) {
                    throw new DuplicateFunctionAnnotationException(
                            "A function declaration cannot contain more than one visibility annotation (%public, %private).",
                            annotation.getMetadata());
                }
                hasVisibility = true;
            }
        }
    }

    /**
     * Validates annotations on a variable declaration according to W3C XQuery 3.1 §4.17 [err:XQST0116].
     * A variable declaration cannot contain more than one visibility annotation (%public, %private).
     *
     * @param annotations the list of annotations
     */
    public static void validateVariableAnnotations(List<Annotation> annotations) {
        if (annotations == null || annotations.isEmpty()) {
            return;
        }
        boolean hasVisibility = false;
        for (Annotation annotation : annotations) {
            Name name = annotation.getAnnotationName();
            if (isVisibilityAnnotation(name)) {
                if (hasVisibility) {
                    throw new DuplicateVariableAnnotationException(
                            "A variable declaration cannot contain more than one visibility annotation (%public, %private).",
                            annotation.getMetadata());
                }
                hasVisibility = true;
            }
        }
    }

    /**
     * Validates annotations on an inline function expression according to W3C XQuery 3.1 §3.1.7 [err:XQST0125].
     *
     * @param annotations the list of annotations
     */
    public static void validateInlineFunctionAnnotations(List<Annotation> annotations) {
        if (annotations == null || annotations.isEmpty()) {
            return;
        }
        for (Annotation annotation : annotations) {
            Name name = annotation.getAnnotationName();
            if (isVisibilityAnnotation(name)) {
                throw new InvalidInlineFunctionAnnotationException(
                        "An inline function expression cannot have a visibility annotation (" + name + ").",
                        annotation.getMetadata());
            }
        }
    }

    private static boolean isVisibilityAnnotation(Name name) {
        return AnnotationConstants.PUBLIC.equals(name) || AnnotationConstants.PRIVATE.equals(name);
    }
}
