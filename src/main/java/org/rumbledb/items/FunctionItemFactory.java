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
import org.rumbledb.runtime.functions.ConstructorFunctionIterator;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.xml.schema.XmlSchemaConstructorFunction;

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

    public static FunctionItem createXmlSchemaConstructorNamedReference(
            XmlSchemaConstructorFunction constructor,
            DynamicContext moduleContext,
            RuntimeStaticContext referenceContext
    ) {
        Name parameterName = Name.createVariableInNoNamespace("p0");
        SequenceType parameterType = constructor.signature().getParameterTypes().get(0);
        SequenceType returnType = constructor.signature().getReturnType();
        RuntimeIterator parameter = new VariableReferenceIterator(
                parameterName,
                referenceContext.withStaticType(parameterType).withExecutionMode(ExecutionMode.LOCAL)
        );
        ItemType targetType = constructor.targetType();
        RuntimeIterator body = new ConstructorFunctionIterator(
                targetType,
                constructor.validator(),
                List.of(parameter),
                referenceContext.withStaticType(returnType).withExecutionMode(ExecutionMode.LOCAL)
        );
        return new FunctionItem(
                constructor.identifier(),
                List.of(parameterName),
                constructor.signature(),
                moduleContext,
                body
        );
    }
}
