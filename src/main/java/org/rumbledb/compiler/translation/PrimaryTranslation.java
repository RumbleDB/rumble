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

import java.util.function.Function;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.ValueExprContext;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.Node;

public final class PrimaryTranslation {

    private PrimaryTranslation() {}

    public static <SimpleMapExprCtx extends ParserRuleContext, ValidateExprCtx extends ParserRuleContext>
            Node valueExpr(
                    ValueExprContext<SimpleMapExprCtx, ValidateExprCtx> ctx,
                    TranslationContext translationContext,
                    Function<SimpleMapExprCtx, Node> visitSimpleMapExpr,
                    Function<ValidateExprCtx, Node> visitValidateExpr) {
        if (ctx.simpleMapExpr() != null) {
            return visitSimpleMapExpr.apply(ctx.simpleMapExpr());
        }
        if (ctx.validateExpr() != null) {
            return visitValidateExpr.apply(ctx.validateExpr());
        }
        // TODO: extension expression still unsupported
        throw new UnsupportedFeatureException(
                "Extension expression still unsupported", translationContext.metadata(ctx.context()));
    }
}
