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

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

import org.rumbledb.expressions.module.SchemaImport.BindingKind;
import org.rumbledb.parser.jsoniq.JsoniqParser;
import org.rumbledb.parser.xquery.XQueryParser;

public record SchemaImportContext<UriLiteralCtx extends ParserRuleContext>(
        UriLiteralCtx targetNamespace,
        BindingKind bindingKind,
        String prefix,
        List<UriLiteralCtx> locations,
        ParserRuleContext context) {

    public static SchemaImportContext<JsoniqParser.UriLiteralContext> from(JsoniqParser.SchemaImportContext ctx) {
        BindingKind bindingKind = BindingKind.NONE;
        String prefix = null;
        if (ctx.schemaPrefix() != null) {
            if (ctx.schemaPrefix().ncName() != null) {
                bindingKind = BindingKind.PREFIX;
                prefix = ctx.schemaPrefix().ncName().getText();
            } else {
                bindingKind = BindingKind.DEFAULT_ELEMENT_NAMESPACE;
            }
        }
        return new SchemaImportContext<>(ctx.nsURI, bindingKind, prefix, ctx.locations, ctx);
    }

    public static SchemaImportContext<XQueryParser.UriLiteralContext> from(XQueryParser.SchemaImportContext ctx) {
        BindingKind bindingKind = BindingKind.NONE;
        String prefix = null;
        if (ctx.schemaPrefix() != null) {
            if (ctx.schemaPrefix().ncName() != null) {
                bindingKind = BindingKind.PREFIX;
                prefix = ctx.schemaPrefix().ncName().getText();
            } else {
                bindingKind = BindingKind.DEFAULT_ELEMENT_NAMESPACE;
            }
        }
        return new SchemaImportContext<>(ctx.nsURI, bindingKind, prefix, ctx.locations, ctx);
    }
}
