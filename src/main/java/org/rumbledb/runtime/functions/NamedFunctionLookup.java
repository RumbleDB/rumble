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
package org.rumbledb.runtime.functions;

import org.rumbledb.api.Item;
import org.rumbledb.context.BuiltinFunction;
import org.rumbledb.context.BuiltinFunctionCatalogue;
import org.rumbledb.context.ConstructorFunctionResolver;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.FunctionItemFactory;

/**
 * Resolves a {@link FunctionIdentifier} against user-defined and built-in named functions in the dynamic context.
 */
public final class NamedFunctionLookup {

    private NamedFunctionLookup() {}

    /**
     * @return a {@link FunctionItem} bound to the current dynamic context, or {@code null} if none exists
     */
    public static Item lookupOrNull(
            FunctionIdentifier identifier, DynamicContext dynamicContext, RuntimeStaticContext staticContext) {
        if (dynamicContext.getNamedFunctions().checkUserDefinedFunctionExists(identifier)) {
            FunctionItem result = dynamicContext.getNamedFunctions().getUserDefinedFunction(identifier);
            result.populateClosureFromDynamicContext(dynamicContext, staticContext.getMetadata());
            return result;
        }
        var constructor = ConstructorFunctionResolver.resolve(identifier, staticContext);
        if (constructor != null) {
            return FunctionItemFactory.createConstructorReference(
                    constructor, dynamicContext.getModuleContext(), staticContext);
        }
        BuiltinFunction builtin =
                BuiltinFunctionCatalogue.getBuiltinFunction(identifier, staticContext.getQueryLanguage());
        if (builtin != null) {
            FunctionItem result = FunctionItemFactory.createBuiltinNamedReference(
                    builtin.getIdentifier(),
                    dynamicContext.getModuleContext(),
                    staticContext.getConfiguration(),
                    staticContext.getMetadata(),
                    builtin);
            result.populateClosureFromDynamicContext(dynamicContext, staticContext.getMetadata());
            return result;
        }
        return null;
    }
}
