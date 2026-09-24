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
package org.rumbledb.compiler;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;

/** Shared mutable state used while either grammar translates a module. */
final class TranslationContext {

    private final StaticContext moduleContext;
    private final CompilationConfiguration compilationConfiguration;
    private final RumbleConfiguration configuration;
    private final ExternalBindings externalBindings;
    private final boolean mainModule;
    private final String source;
    private final ArrayDeque<Map<String, String>> constructorNamespaceFrames;
    private final TranslationNameResolver nameResolver;

    TranslationContext(
            StaticContext moduleContext,
            CompilationConfiguration compilationConfiguration,
            ExternalBindings externalBindings,
            boolean mainModule,
            String source) {
        this.moduleContext = moduleContext;
        this.moduleContext.bindDefaultNamespaces();
        this.compilationConfiguration = compilationConfiguration;
        this.configuration = compilationConfiguration.runtimeConfiguration();
        this.externalBindings = externalBindings;
        this.mainModule = mainModule;
        this.source = source;
        this.constructorNamespaceFrames = new ArrayDeque<>();
        this.nameResolver = new TranslationNameResolver(this);
    }

    StaticContext moduleContext() {
        return this.moduleContext;
    }

    CompilationConfiguration compilationConfiguration() {
        return this.compilationConfiguration;
    }

    RumbleConfiguration configuration() {
        return this.configuration;
    }

    ExternalBindings externalBindings() {
        return this.externalBindings;
    }

    boolean isMainModule() {
        return this.mainModule;
    }

    TranslationNameResolver names() {
        return this.nameResolver;
    }

    void pushConstructorNamespaceFrame() {
        this.constructorNamespaceFrames.push(new HashMap<>());
    }

    void popConstructorNamespaceFrame() {
        this.constructorNamespaceFrames.pop();
    }

    void bindConstructorNamespace(String prefix, String namespace) {
        if (!this.constructorNamespaceFrames.isEmpty()) {
            this.constructorNamespaceFrames.peek().put(prefix, namespace);
        }
    }

    String resolveNamespace(String prefix) {
        for (Map<String, String> frame : this.constructorNamespaceFrames) {
            if (frame.containsKey(prefix)) {
                return frame.get(prefix);
            }
        }
        return this.moduleContext.resolveNamespace(prefix);
    }

    ExceptionMetadata metadata(ParserRuleContext context) {
        return this.metadata(context.getStart(), context.getStop());
    }

    ExceptionMetadata metadata(ParseTree tree) {
        return this.metadata(this.startToken(tree), this.stopToken(tree));
    }

    ExceptionMetadata metadata(ParseTree startTree, ParseTree endTree) {
        return this.metadata(this.startToken(startTree), this.stopToken(endTree));
    }

    ExceptionMetadata metadata(Token start, Token end) {
        return ExceptionMetadata.fromTokens(
                this.moduleContext.getStaticBaseURI().toString(), start, end, this.source);
    }

    Token startToken(ParseTree tree) {
        if (tree instanceof ParserRuleContext parserRuleContext) {
            return parserRuleContext.getStart();
        }
        if (tree instanceof TerminalNode terminalNode) {
            return terminalNode.getSymbol();
        }
        throw new OurBadException(
                "Cannot get start token from parse tree: " + tree.getClass().getName());
    }

    Token stopToken(ParseTree tree) {
        if (tree instanceof ParserRuleContext parserRuleContext) {
            return parserRuleContext.getStop();
        }
        if (tree instanceof TerminalNode terminalNode) {
            return terminalNode.getSymbol();
        }
        throw new OurBadException(
                "Cannot get stop token from parse tree: " + tree.getClass().getName());
    }
}
