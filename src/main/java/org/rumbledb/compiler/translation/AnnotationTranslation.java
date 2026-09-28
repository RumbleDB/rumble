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
package org.rumbledb.compiler.translation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.AnnotationsContext;
import org.rumbledb.compiler.context.AnnotationsContext.AnnotationContext;
import org.rumbledb.compiler.translation.TranslationNameResolver.NameRole;
import org.rumbledb.context.Name;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.scripting.annotations.Annotation;

public final class AnnotationTranslation {

    private AnnotationTranslation() {}

    public static <EqNameCtx extends ParserRuleContext, LiteralCtx extends ParserRuleContext>
            List<Annotation> processAnnotations(
                    AnnotationsContext<EqNameCtx, LiteralCtx> ctx,
                    TranslationContext translationContext,
                    BiFunction<EqNameCtx, NameRole, Name> parseEqName,
                    Function<LiteralCtx, Expression> visitLiteral) {
        if (ctx == null || ctx.annotations() == null) {
            return Collections.emptyList();
        }
        List<Annotation> parsedAnnotations = new ArrayList<>();
        for (AnnotationContext<EqNameCtx, LiteralCtx> annotationContext : ctx.annotations()) {
            // for backwards compatibility, the specification allows for updating without % sign
            if (annotationContext.isUpdating()) {
                Name name = Name.createNameInDefaultXQueryAnnotationsNamespace("updating");
                parsedAnnotations.add(new Annotation(name, null));
                continue;
            }
            Name name = parseEqName.apply(annotationContext.eqName(), NameRole.ANNOTATION);
            Annotation.validateAnnotationName(name, translationContext.metadata(annotationContext.context()));
            List<Expression> literals = null;
            if (annotationContext.literals() != null
                    && !annotationContext.literals().isEmpty()) {
                literals = new ArrayList<>();
                for (LiteralCtx literalContext : annotationContext.literals()) {
                    literals.add(visitLiteral.apply(literalContext));
                }
            }
            parsedAnnotations.add(new Annotation(name, literals));
        }
        return parsedAnnotations;
    }
}
