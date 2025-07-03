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
 * Authors: Matteo Agnoletto (EPMatt)
 *
 */

package org.rumbledb.runtime.misc;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.comparison.NodeComparisonExpression;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.xml.XMLDocumentPosition;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.Arrays;

/**
 * Runtime iterator for node comparisons.
 * 
 * Node comparisons are used to compare two nodes, by their identity or by their document order.
 * 
 * @see https://www.w3.org/TR/xquery-31/#id-node-comparisons
 * 
 */
public class NodeComparisonRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {

    private NodeComparisonExpression.NodeComparisonOperator operator;
    private RuntimeIterator leftIterator;
    private RuntimeIterator rightIterator;

    public NodeComparisonRuntimeIterator(
            RuntimeIterator leftIterator,
            RuntimeIterator rightIterator,
            NodeComparisonExpression.NodeComparisonOperator operator,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(leftIterator, rightIterator), staticContext);
        this.leftIterator = leftIterator;
        this.rightIterator = rightIterator;
        this.operator = operator;
    }

    public NodeComparisonExpression.NodeComparisonOperator getOperator() {
        return this.operator;
    }

    public RuntimeIterator getLeftIterator() {
        return this.leftIterator;
    }

    public RuntimeIterator getRightIterator() {
        return this.rightIterator;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        // 1. The operands of a node comparison are evaluated in implementation-dependent order.
        Item leftItem;
        Item rightItem;

        try {
            leftItem = this.leftIterator.materializeAtMostOneItemOrNull(dynamicContext);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "Left operand of node comparison must be a single node or empty sequence, got more than one item",
                    getMetadata()
            );
        }

        try {
            rightItem = this.rightIterator.materializeAtMostOneItemOrNull(dynamicContext);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "Right operand of node comparison must be a single node or empty sequence, got more than one item",
                    getMetadata()
            );
        }

        // 2. If either operand is an empty sequence, the result of the comparison is an empty sequence,
        // and the implementation need not evaluate the other operand or apply the operator.
        // However, an implementation may choose to evaluate the other operand in order to determine whether it raises
        // an error.
        if (leftItem == null || rightItem == null) {
            return null;
        }

        // 3. Each operand must be either a single node or an empty sequence; otherwise a type error
        // is raised [err:XPTY0004].
        if (!leftItem.isNode()) {
            throw new UnexpectedTypeException(
                    "Left operand of node comparison must be a node, got: " + leftItem.getDynamicType(),
                    getMetadata()
            );
        }

        if (!rightItem.isNode()) {
            throw new UnexpectedTypeException(
                    "Right operand of node comparison must be a node, got: " + rightItem.getDynamicType(),
                    getMetadata()
            );
        }

        // Compare document order using XMLDocumentPosition
        XMLDocumentPosition leftPos = leftItem.getXmlDocumentPosition();
        XMLDocumentPosition rightPos = rightItem.getXmlDocumentPosition();

        int comparison = leftPos.compareTo(rightPos);

        boolean result;
        switch (this.operator) {
            case NC_IS: // is
                // 4. A comparison with the is operator is true if the two operand nodes are the same node;
                // otherwise it is false.
                result = leftItem.equals(rightItem);
                break;
            case NC_PRECEDES: // <<
                // 5. A comparison with the << operator returns true if the left operand node precedes
                // the right operand node in document order; otherwise it returns false.
                result = comparison < 0;
                break;
            case NC_FOLLOWS: // >>
                // 6. A comparison with the >> operator returns true if the left operand node follows
                // the right operand node in document order; otherwise it returns false.
                result = comparison > 0;
                break;
            default:
                throw new OurBadException("Unrecognized node comparison operator: " + this.operator);
        }

        return ItemFactory.getInstance().createBooleanItem(result);
    }
}
