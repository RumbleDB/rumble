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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rumbledb.context.Name;
import org.rumbledb.exceptions.DuplicateFunctionAnnotationException;
import org.rumbledb.exceptions.DuplicateVariableAnnotationException;
import org.rumbledb.exceptions.InvalidInlineFunctionAnnotationException;
import org.rumbledb.expressions.scripting.annotations.Annotation;
import org.rumbledb.expressions.scripting.annotations.AnnotationConstants;

/**
 * Static validation of annotations on function declarations, variable declarations, and inline function expressions.
 */
public final class AnnotationValidator {

    private AnnotationValidator() {}

    /**
     * Validates annotations on a function declaration according to W3C XQuery 3.1 §4.18 [err:XQST0106].
     *
     * @param annotations the list of annotations
     */
    public static void validateFunctionAnnotations(List<Annotation> annotations) {
        if (annotations == null || annotations.isEmpty()) {
            return;
        }
        Set<Name> seen = new HashSet<>();
        boolean hasVisibility = false;
        for (Annotation annotation : annotations) {
            Name name = annotation.getAnnotationName();
            if (!seen.add(name)) {
                throw new DuplicateFunctionAnnotationException(
                        "Duplicate annotation '" + name + "' on function declaration.", annotation.getMetadata());
            }
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
     *
     * @param annotations the list of annotations
     */
    public static void validateVariableAnnotations(List<Annotation> annotations) {
        if (annotations == null || annotations.isEmpty()) {
            return;
        }
        Set<Name> seen = new HashSet<>();
        boolean hasVisibility = false;
        for (Annotation annotation : annotations) {
            Name name = annotation.getAnnotationName();
            if (!seen.add(name)) {
                throw new DuplicateVariableAnnotationException(
                        "Duplicate annotation '" + name + "' on variable declaration.", annotation.getMetadata());
            }
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
