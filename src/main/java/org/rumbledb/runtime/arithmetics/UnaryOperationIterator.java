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
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.runtime.arithmetics;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

import java.io.Serial;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;

public class UnaryOperationIterator extends AtMostOneItemLocalRuntimeIterator {

    private final boolean negated;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> child;
    @Serial
    private static final long serialVersionUID = 1L;

    public UnaryOperationIterator(
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> child,
            boolean negated,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(child), staticContext);
        this.child = child;
        this.negated = negated;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        Item item;
        try {
            item = this.child.materializeAtMostOne(dynamicContext);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "Unary expression requires at most one item in its input sequence.",
                    getMetadata()
            );
        }
        return applyOperator(item, this.negated, getMetadata());
    }

    private static Item applyOperator(Item item, boolean negated, ExceptionMetadata metadata) {
        if (item == null) {
            return null;
        }
        if (item.isUntypedAtomic()) {
            item = ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
        }

        if (!negated) {
            if (!item.isNumeric()) {
                throw new UnexpectedTypeException(
                        "Unary expression has non numeric args "
                            +
                            item.serialize(),
                        metadata
                );
            }
            return item;
        }
        if (item.isInt()) {
            return ItemFactory.getInstance().createIntItem(-1 * item.getIntValue());
        }
        if (item.isInteger()) {
            return ItemFactory.getInstance()
                .createIntegerItem(BigInteger.valueOf(-1).multiply(item.getIntegerValue()));
        }
        if (item.isFloat()) {
            return ItemFactory.getInstance().createFloatItem(-1 * item.getFloatValue());
        }
        if (item.isDouble()) {
            return ItemFactory.getInstance().createDoubleItem(-1 * item.getDoubleValue());
        }
        if (item.isDecimal()) {
            return ItemFactory.getInstance()
                .createDecimalItem(item.getDecimalValue().multiply(new BigDecimal(-1)));
        }
        throw new UnexpectedTypeException(
                "Unary expression has non numeric args "
                    +
                    item.serialize(),
                metadata
        );
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext leftResult = org.rumbledb.runtime.plan.NativeQueryRuntimePlan.generate(
            this.child,
            nativeClauseContext
        );
        if (leftResult == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (!leftResult.getResultingType().getArity().equals(Arity.One)) {
            return NativeClauseContext.NoNativeQuery;
        }
        String leftQuery = leftResult.getResultingQuery();
        SequenceType resultType = leftResult.getResultingType();
        if (this.negated) {
            String resultingQuery = "( "
                + " - "
                + leftQuery
                + " )";
            return new NativeClauseContext(nativeClauseContext, resultingQuery, resultType);
        } else {
            String resultingQuery = "( "
                + leftQuery
                + " )";
            return new NativeClauseContext(nativeClauseContext, resultingQuery, resultType);
        }
    }
}
