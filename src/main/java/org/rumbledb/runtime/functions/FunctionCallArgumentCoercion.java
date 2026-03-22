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
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.typing.AtMostOneItemTypePromotionIterator;
import org.rumbledb.runtime.typing.TypePromotionIterator;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

import java.util.List;

import static org.rumbledb.types.SequenceType.ITEM_STAR;

/**
 * Shared argument arity checks and type-promotion wrapping for dynamic calls on
 * {@link org.rumbledb.items.FunctionItem}s.
 */
public final class FunctionCallArgumentCoercion {

    private FunctionCallArgumentCoercion() {
    }

    public static void validateArity(
            Item functionItem,
            List<RuntimeIterator> functionArguments,
            ExceptionMetadata metadata
    ) {
        if (functionItem.getParameterNames().size() != functionArguments.size()) {
            throw new UnexpectedTypeException(
                    "Dynamic function "
                        + functionItem.getIdentifier().getName()
                        + " invoked with incorrect number of arguments. Expected: "
                        + functionItem.getParameterNames().size()
                        + ", Found: "
                        + functionArguments.size(),
                    metadata
            );
        }
    }

    public static void wrapAccordingToSignature(
            Item functionItem,
            List<RuntimeIterator> functionArguments,
            RuntimeStaticContext callerStaticContext
    ) {
        if (functionItem.getSignature().getParameterTypes() == null) {
            return;
        }
        for (int i = 0; i < functionArguments.size(); i++) {
            if (
                functionArguments.get(i) != null
                    && !functionItem.getSignature().getParameterTypes().get(i).equals(ITEM_STAR)
            ) {
                SequenceType sequenceType = functionItem.getSignature().getParameterTypes().get(i);
                ExecutionMode executionMode = functionArguments.get(i).getHighestExecutionMode();
                if (
                    sequenceType.isEmptySequence()
                        || sequenceType.getArity().equals(Arity.One)
                        || sequenceType.getArity().equals(Arity.OneOrZero)
                ) {
                    executionMode = ExecutionMode.LOCAL;
                }
                RuntimeStaticContext runtimeStaticContext = new RuntimeStaticContext(
                        callerStaticContext.getConfiguration(),
                        sequenceType,
                        executionMode,
                        functionArguments.get(i).getMetadata(),
                        callerStaticContext.getStaticallyKnownNamespaces()
                );
                if (
                    sequenceType.isEmptySequence()
                        || sequenceType.getArity().equals(Arity.One)
                        || sequenceType.getArity().equals(Arity.OneOrZero)
                ) {
                    RuntimeIterator typePromotionIterator = new AtMostOneItemTypePromotionIterator(
                            functionArguments.get(i),
                            sequenceType,
                            "Invalid argument for " + functionItem.getIdentifier().getName() + " function. ",
                            runtimeStaticContext
                    );
                    functionArguments.set(i, typePromotionIterator);
                } else {
                    RuntimeIterator typePromotionIterator = new TypePromotionIterator(
                            functionArguments.get(i),
                            sequenceType,
                            "Invalid argument for " + functionItem.getIdentifier().getName() + " function. ",
                            runtimeStaticContext
                    );
                    functionArguments.set(i, typePromotionIterator);
                }
            }
        }
    }
}
