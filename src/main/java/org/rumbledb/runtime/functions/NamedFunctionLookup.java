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
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.BuiltinFunction;
import org.rumbledb.context.BuiltinFunctionCatalogue;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.FunctionItemFactory;

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
            RumbleRuntimeConfiguration configuration,
            ExceptionMetadata metadata
    ) {
        if (dynamicContext.getNamedFunctions().checkUserDefinedFunctionExists(identifier)) {
            FunctionItem function = dynamicContext.getNamedFunctions().getUserDefinedFunction(identifier);
            FunctionItem result = function.deepCopy();
            result.populateClosureFromDynamicContext(dynamicContext, metadata);
            return result;
        }
        BuiltinFunction builtin = BuiltinFunctionCatalogue.getBuiltinFunction(identifier);
        if (builtin != null) {
            FunctionItem result = FunctionItemFactory.createBuiltinNamedReference(
                identifier,
                dynamicContext.getModuleContext(),
                configuration,
                metadata,
                builtin
            );
            result.populateClosureFromDynamicContext(dynamicContext, metadata);
            return result;
        }
        return null;
    }
}
