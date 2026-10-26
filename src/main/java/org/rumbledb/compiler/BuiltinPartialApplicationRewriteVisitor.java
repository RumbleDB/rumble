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

import java.util.List;
import java.util.stream.Collectors;

import org.rumbledb.context.BuiltinFunction;
import org.rumbledb.context.BuiltinFunctionCatalogue;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.postfix.DynamicFunctionCallExpression;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.NamedFunctionReferenceExpression;

/**
 * Rewrites direct partial application of builtins, e.g. {@code fn:max(0, ?)},
 * into a dynamic partial application of the corresponding named function reference.
 * Supplied arguments are evaluated in the caller's focus and captured when the
 * partial function is created.
 */
public class BuiltinPartialApplicationRewriteVisitor extends CloneVisitor {

    private StaticContext moduleContext;

    private BuiltinFunction resolveFunction(Expression expression, FunctionIdentifier identifier) {
        StaticContext context =
                expression.getStaticContext() != null ? expression.getStaticContext() : this.moduleContext;
        return context == null
                ? BuiltinFunctionCatalogue.getBuiltinFunction(identifier, (String) null)
                : BuiltinFunctionCatalogue.getBuiltinFunction(identifier, context);
    }

    @Override
    public Node visitMainModule(MainModule module, Node argument) {
        StaticContext previousContext = this.moduleContext;
        this.moduleContext = module.getStaticContext();
        try {
            return super.visitMainModule(module, argument);
        } finally {
            this.moduleContext = previousContext;
        }
    }

    @Override
    public Node visitLibraryModule(LibraryModule module, Node argument) {
        StaticContext previousContext = this.moduleContext;
        this.moduleContext = module.getStaticContext();
        try {
            return super.visitLibraryModule(module, argument);
        } finally {
            // Resume resolving constructors in the importing module after visiting this library.
            this.moduleContext = previousContext;
        }
    }

    private Expression rewriteBuiltinPartialApplication(
            Name functionName, List<Expression> arguments, Expression sourceExpression) {
        NamedFunctionReferenceExpression reference = new NamedFunctionReferenceExpression(
                new FunctionIdentifier(functionName, arguments.size()), sourceExpression.getMetadata());
        return new DynamicFunctionCallExpression(reference, arguments, sourceExpression.getMetadata());
    }

    @Override
    public Node visitFunctionCall(FunctionCallExpression expression, Node argument) {
        BuiltinFunction builtin = resolveFunction(expression, expression.getFunctionIdentifier());

        if (!expression.isPartialApplication() || builtin == null) {
            // In case of non-partial application or non-builtin function, we still need to keep descending Because a
            // partial builtin function might be in the nested level
            // See qt3 test hof-041
            return super.visitFunctionCall(expression, argument);
        }

        List<Expression> arguments = expression.getArguments().stream()
                .map(expr -> expr != null ? (Expression) visit(expr, argument) : null)
                .collect(Collectors.toList());
        return rewriteBuiltinPartialApplication(expression.getFunctionName(), arguments, expression);
    }
}
