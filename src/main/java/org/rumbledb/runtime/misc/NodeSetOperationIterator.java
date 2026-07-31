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

package org.rumbledb.runtime.misc;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.miscellaneous.NodeSetExpression;
import org.rumbledb.items.xml.XMLDocumentPosition;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class NodeSetOperationIterator extends ItemRuntimePlan
        implements
            LocalRuntimePlan<Item>,
            RDDRuntimePlan<Item> {

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(
                () -> computeNodeSet(context).iterator(),
                getMetadata()
        );
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private RuntimePlan<Item> leftIterator;
    private RuntimePlan<Item> rightIterator;
    private NodeSetExpression.NodeSetOperator operator;

    public NodeSetOperationIterator(
            RuntimePlan<Item> leftIterator,
            RuntimePlan<Item> rightIterator,
            NodeSetExpression.NodeSetOperator operator,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(leftIterator, rightIterator), staticContext);
        this.leftIterator = leftIterator;
        this.rightIterator = rightIterator;
        this.operator = operator;
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext context) {
        JavaRDD<Item> leftNodes = buildNodeRDD(
            this.leftIterator.getRDD(context),
            "left"
        );
        JavaRDD<Item> rightNodes = buildNodeRDD(
            this.rightIterator.getRDD(context),
            "right"
        );

        JavaRDD<Item> result;
        switch (this.operator) {
            case UNION:
                result = leftNodes.union(rightNodes).distinct();
                break;
            case INTERSECT:
                result = leftNodes.intersection(rightNodes);
                break;
            case EXCEPT:
                result = leftNodes.subtract(rightNodes);
                break;
            default:
                throw new IteratorFlowException("Unrecognized node set operator: " + this.operator, getMetadata());
        }
        return result.sortBy(Item::getXmlDocumentPosition, true, 1);
    }

    private List<Item> computeNodeSet(DynamicContext context) {
        Set<Item> leftNodes = buildNodeSet(this.leftIterator, context, "left");
        Set<Item> rightNodes = buildNodeSet(this.rightIterator, context, "right");

        switch (this.operator) {
            case UNION:
                leftNodes.addAll(rightNodes);
                return sortNodes(leftNodes);
            case INTERSECT:
                leftNodes.retainAll(rightNodes);
                return sortNodes(leftNodes);
            case EXCEPT:
                leftNodes.removeAll(rightNodes);
                return sortNodes(leftNodes);
            default:
                throw new IteratorFlowException("Unrecognized node set operator: " + this.operator, getMetadata());
        }
    }

    /**
     * Builds an ordered set of nodes while validating that every item is an XML node with a document position.
     */
    private Set<Item> buildNodeSet(
            RuntimePlan<Item> iterator,
            DynamicContext context,
            String side
    ) {
        Set<Item> nodes = new TreeSet<>(Comparator.comparing(Item::getXmlDocumentPosition));
        for (Item item : iterator.materialize(context)) {
            validateAndGetNodePosition(item, side, getMetadata());
            nodes.add(item);
        }
        return nodes;
    }

    private List<Item> sortNodes(Set<Item> nodes) {
        return new ArrayList<>(nodes);
    }

    private JavaRDD<Item> buildNodeRDD(JavaRDD<Item> items, String side) {
        ExceptionMetadata metadata = getMetadata();
        return items.map(item -> {
            validateAndGetNodePosition(item, side, metadata);
            return item;
        });
    }

    private static XMLDocumentPosition validateAndGetNodePosition(
            Item item,
            String side,
            ExceptionMetadata metadata
    ) {
        if (!item.isNode()) {
            throw new UnexpectedTypeException(
                    "The "
                        + side
                        + " operand of a node set operation must contain only nodes, got: "
                        + item.getDynamicType(),
                    metadata
            );
        }
        XMLDocumentPosition position = item.getXmlDocumentPosition();
        if (position == null) {
            throw new UnexpectedTypeException(
                    "The " + side + " operand of a node set operation contains a node without document position.",
                    metadata
            );
        }
        return position;
    }
}
