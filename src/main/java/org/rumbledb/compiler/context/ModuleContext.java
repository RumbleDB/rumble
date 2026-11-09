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
package org.rumbledb.compiler.context;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record ModuleContext<
        StringLiteralCtx extends ParserRuleContext,
        MainModuleCtx extends ParserRuleContext,
        LibraryModuleCtx extends ParserRuleContext>(
        ParserRuleContext context,
        VersionDeclContext<StringLiteralCtx> versionDecl,
        MainModuleCtx mainModule,
        LibraryModuleCtx libraryModule) {

    public static ModuleContext<
                    JsoniqParser.StringLiteralContext,
                    JsoniqParser.MainModuleContext,
                    JsoniqParser.LibraryModuleContext>
            from(JsoniqParser.ModuleContext ctx) {
        return new ModuleContext<>(
                ctx,
                ctx.decl == null ? null : VersionDeclContext.from(ctx.decl),
                ctx.mainModule(),
                ctx.libraryModule());
    }

    public static ModuleContext<
                    XQueryParser.StringLiteralContext,
                    XQueryParser.MainModuleContext,
                    XQueryParser.LibraryModuleContext>
            from(XQueryParser.ModuleContext ctx) {
        return new ModuleContext<>(
                ctx,
                ctx.decl == null ? null : VersionDeclContext.from(ctx.decl),
                ctx.mainModule(),
                ctx.libraryModule());
    }
}
