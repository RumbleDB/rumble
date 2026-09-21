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
package org.rumbledb.compiler.translation.scripting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.compiler.context.scripting.VarDeclStatementContext;
import org.rumbledb.compiler.translation.TranslationContext;
import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.scripting.annotations.Annotation;
import org.rumbledb.expressions.scripting.declaration.CommaVariableDeclStatement;
import org.rumbledb.expressions.scripting.declaration.VariableDeclStatement;
import org.rumbledb.expressions.scripting.statement.Statement;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.types.SequenceType;

public final class DeclarationStatementTranslation {

    private DeclarationStatementTranslation() {}

    public static <
                    AnnotationsCtx extends ParserRuleContext,
                    VarBindingCtx extends ParserRuleContext,
                    SeqTypeCtx extends ParserRuleContext,
                    ExprSingleCtx extends ParserRuleContext>
            Statement varDeclStatement(
                    VarDeclStatementContext<AnnotationsCtx, VarBindingCtx, SeqTypeCtx, ExprSingleCtx> ctx,
                    TranslationContext translationContext,
                    Function<AnnotationsCtx, List<Annotation>> processAnnotations,
                    Function<VarBindingCtx, Name> parseVariableBinding,
                    Function<SeqTypeCtx, SequenceType> processSequenceType,
                    Function<ExprSingleCtx, Expression> visitExprSingle) {
        List<Annotation> annotations = processAnnotations.apply(ctx.annotations());
        List<VariableDeclStatement> variables = new ArrayList<>(ctx.varDecls().size());
        for (VarDeclStatementContext.VarDecl<VarBindingCtx, SeqTypeCtx, ExprSingleCtx> varDecl : ctx.varDecls()) {
            SequenceType seq = null;
            Name var = parseVariableBinding.apply(varDecl.varRef());
            Expression exprSingle = null;

            if (varDecl.sequenceType() != null) {
                seq = processSequenceType.apply(varDecl.sequenceType());
            }
            if (varDecl.exprSingle() != null) {
                exprSingle = visitExprSingle.apply(varDecl.exprSingle());
                if (seq != null) {
                    exprSingle = new TreatExpression(
                            exprSingle, seq, ErrorCode.UnexpectedTypeErrorCode, exprSingle.getMetadata());
                }
            }
            variables.add(new VariableDeclStatement(
                    annotations, var, seq, exprSingle, translationContext.metadata(varDecl.context())));
        }
        if (variables.size() == 1) {
            return variables.get(0);
        }
        return new CommaVariableDeclStatement(variables, translationContext.metadata(ctx.context()));
    }
}
