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
import org.rumbledb.exceptions.NamespacePrefixBoundTwiceException;
import org.rumbledb.exceptions.OurBadException;

/** Shared mutable state used while either grammar translates a module. */
public final class TranslationContext {

    private final StaticContext moduleContext;
    private final CompilationConfiguration compilationConfiguration;
    private final RumbleConfiguration configuration;
    private final ExternalBindings externalBindings;
    private final boolean mainModule;
    private final String source;
    private final ArrayDeque<Map<String, String>> constructorNamespaceFrames;
    private final TranslationNameResolver nameResolver;

    public TranslationContext(
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

    public StaticContext moduleContext() {
        return this.moduleContext;
    }

    public CompilationConfiguration compilationConfiguration() {
        return this.compilationConfiguration;
    }

    public RumbleConfiguration configuration() {
        return this.configuration;
    }

    public ExternalBindings externalBindings() {
        return this.externalBindings;
    }

    public boolean isMainModule() {
        return this.mainModule;
    }

    public TranslationNameResolver names() {
        return this.nameResolver;
    }

    public void bindNamespace(String prefix, String namespace, ExceptionMetadata metadata) {
        boolean success = !prefix.isEmpty() && namespace.isEmpty()
                ? this.moduleContext.unbindNamespace(prefix)
                : this.moduleContext.bindNamespace(prefix, namespace);
        if (!success) {
            throw new NamespacePrefixBoundTwiceException("Prefix " + prefix + " is bound twice.", metadata);
        }
    }

    public void pushConstructorNamespaceFrame() {
        this.constructorNamespaceFrames.push(new HashMap<>());
    }

    public void popConstructorNamespaceFrame() {
        this.constructorNamespaceFrames.pop();
    }

    public void bindConstructorNamespace(String prefix, String namespace) {
        if (!this.constructorNamespaceFrames.isEmpty()) {
            this.constructorNamespaceFrames.peek().put(prefix, namespace);
        }
    }

    public String resolveNamespace(String prefix) {
        for (Map<String, String> frame : this.constructorNamespaceFrames) {
            if (frame.containsKey(prefix)) {
                return frame.get(prefix);
            }
        }
        return this.moduleContext.resolveNamespace(prefix);
    }

    public ExceptionMetadata metadata(ParserRuleContext context) {
        return metadata(context.getStart(), context.getStop());
    }

    public ExceptionMetadata metadata(ParseTree tree) {
        return metadata(startToken(tree), stopToken(tree));
    }

    public ExceptionMetadata metadata(ParseTree startTree, ParseTree endTree) {
        return metadata(startToken(startTree), stopToken(endTree));
    }

    public ExceptionMetadata metadata(Token start, Token end) {
        return ExceptionMetadata.fromTokens(
                this.moduleContext.getStaticBaseURI().toString(), start, end, this.source);
    }

    public Token startToken(ParseTree tree) {
        if (tree instanceof ParserRuleContext parserRuleContext) {
            return parserRuleContext.getStart();
        }
        if (tree instanceof TerminalNode terminalNode) {
            return terminalNode.getSymbol();
        }
        throw new OurBadException(
                "Cannot get start token from parse tree: " + tree.getClass().getName());
    }

    public Token stopToken(ParseTree tree) {
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
