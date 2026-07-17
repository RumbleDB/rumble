/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rumbledb.runtime.functions;

import org.rumbledb.api.Item;
import org.rumbledb.context.BuiltinFunction;
import org.rumbledb.context.BuiltinFunctionCatalogue;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.FunctionItemFactory;
import org.rumbledb.xml.schema.XmlSchemaConstructorFunction;

/**
 * Resolves a {@link FunctionIdentifier} against user-defined and built-in named functions in the dynamic context.
 */
public final class NamedFunctionLookup {

    private NamedFunctionLookup() {
    }

    /**
     * @return a {@link FunctionItem} bound to the current dynamic context, or {@code null} if none exists
     */
    public static Item lookupOrNull(
            FunctionIdentifier identifier,
            DynamicContext dynamicContext,
            RuntimeStaticContext staticContext
    ) {
        XmlSchemaConstructorFunction schemaConstructor = staticContext.getXmlSchemaConstructor(identifier);
        if (schemaConstructor != null) {
            FunctionItem result = FunctionItemFactory.createXmlSchemaConstructorNamedReference(
                schemaConstructor,
                dynamicContext.getModuleContext(),
                staticContext
            );
            result.populateClosureFromDynamicContext(dynamicContext, staticContext.getMetadata());
            return result;
        }
        if (dynamicContext.getNamedFunctions().checkUserDefinedFunctionExists(identifier)) {
            FunctionItem function = dynamicContext.getNamedFunctions().getUserDefinedFunction(identifier);
            FunctionItem result = function.deepCopy();
            result.populateClosureFromDynamicContext(dynamicContext, staticContext.getMetadata());
            return result;
        }
        BuiltinFunction builtin = BuiltinFunctionCatalogue.getBuiltinFunction(
            identifier,
            staticContext.getConfiguration().getQueryLanguage()
        );
        if (builtin != null) {
            FunctionItem result = FunctionItemFactory.createBuiltinNamedReference(
                builtin.getIdentifier(),
                dynamicContext.getModuleContext(),
                staticContext.getConfiguration(),
                staticContext.getMetadata(),
                builtin
            );
            result.populateClosureFromDynamicContext(dynamicContext, staticContext.getMetadata());
            return result;
        }
        return null;
    }
}
