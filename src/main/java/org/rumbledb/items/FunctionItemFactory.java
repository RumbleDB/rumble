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

package org.rumbledb.items;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.BuiltinFunction;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.BuiltinNamedFunctionReferenceMarkerIterator;
import org.rumbledb.types.SequenceType;

/**
 * Construction helpers for {@link FunctionItem} values used by the runtime.
 */
public final class FunctionItemFactory {

    private FunctionItemFactory() {
    }

    /**
     * Builds a function item for a builtin named function reference, using synthetic parameter names
     * {@code $p0}, {@code $p1}, ... aligned with the catalogue signature order.
     */
    public static FunctionItem createBuiltinNamedReference(
            FunctionIdentifier identifier,
            DynamicContext moduleContext,
            RumbleRuntimeConfiguration conf,
            ExceptionMetadata metadata,
            BuiltinFunction builtinFunction
    ) {
        List<Name> paramNames = new ArrayList<>();
        int arity = builtinFunction.getSignature().getParameterTypes().size();
        for (int i = 0; i < arity; i++) {
            paramNames.add(Name.createVariableInNoNamespace("$p" + i));
        }
        SequenceType returnType = builtinFunction.getSignature().getReturnType();
        RuntimeStaticContext markerContext = new RuntimeStaticContext(
                conf,
                returnType,
                ExecutionMode.LOCAL,
                metadata
        );
        RuntimeIterator markerBody = new BuiltinNamedFunctionReferenceMarkerIterator(markerContext);
        return new FunctionItem(
                identifier,
                paramNames,
                builtinFunction.getSignature(),
                moduleContext,
                markerBody,
                true
        );
    }
}
