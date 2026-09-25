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
package org.rumbledb.compiler.context.scripting;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record VarDeclStatementContext<
        AnnotationsCtx extends ParserRuleContext,
        VarBindingCtx extends ParserRuleContext,
        SeqTypeCtx extends ParserRuleContext,
        ExprSingleCtx extends ParserRuleContext>(
        AnnotationsCtx annotations,
        List<VarDecl<VarBindingCtx, SeqTypeCtx, ExprSingleCtx>> varDecls,
        ParserRuleContext context) {

    public record VarDecl<
            VarBindingCtx extends ParserRuleContext,
            SeqTypeCtx extends ParserRuleContext,
            ExprSingleCtx extends ParserRuleContext>(
            VarBindingCtx varRef, SeqTypeCtx sequenceType, ExprSingleCtx exprSingle, ParserRuleContext context) {}

    public static VarDeclStatementContext<
                    JsoniqParser.AnnotationsContext,
                    JsoniqParser.VarBindingContext,
                    JsoniqParser.SequenceTypeContext,
                    JsoniqParser.ExprSingleContext>
            from(JsoniqParser.VarDeclStatementContext c) {
        List<VarDecl<JsoniqParser.VarBindingContext, JsoniqParser.SequenceTypeContext, JsoniqParser.ExprSingleContext>>
                varDecls = new ArrayList<>(
                        c.varDeclForStatement() != null
                                ? c.varDeclForStatement().size()
                                : 0);
        if (c.varDeclForStatement() != null) {
            for (JsoniqParser.VarDeclForStatementContext varDecl : c.varDeclForStatement()) {
                varDecls.add(new VarDecl<>(varDecl.var_ref, varDecl.sequenceType(), varDecl.exprSingle(), varDecl));
            }
        }
        return new VarDeclStatementContext<>(c.annotations(), varDecls, c);
    }

    public static VarDeclStatementContext<
                    XQueryParser.AnnotationsContext,
                    XQueryParser.VarBindingContext,
                    XQueryParser.SequenceTypeContext,
                    XQueryParser.ExprSingleContext>
            from(XQueryParser.VarDeclStatementContext c) {
        List<VarDecl<XQueryParser.VarBindingContext, XQueryParser.SequenceTypeContext, XQueryParser.ExprSingleContext>>
                varDecls = new ArrayList<>(
                        c.varDeclForStatement() != null
                                ? c.varDeclForStatement().size()
                                : 0);
        if (c.varDeclForStatement() != null) {
            for (XQueryParser.VarDeclForStatementContext varDecl : c.varDeclForStatement()) {
                varDecls.add(new VarDecl<>(varDecl.var_ref, varDecl.sequenceType(), varDecl.exprSingle(), varDecl));
            }
        }
        return new VarDeclStatementContext<>(c.annotations(), varDecls, c);
    }
}
