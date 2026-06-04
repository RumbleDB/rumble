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
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class NodeSetOperationIterator extends HybridRuntimeIterator {
    private static final long serialVersionUID = 1L;

    private RuntimeIterator leftIterator;
    private RuntimeIterator rightIterator;
    private NodeSetExpression.NodeSetOperator operator;
    private List<Item> localResults;
    private int nextResultIndex;

    public NodeSetOperationIterator(
            RuntimeIterator leftIterator,
            RuntimeIterator rightIterator,
            NodeSetExpression.NodeSetOperator operator,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(leftIterator, rightIterator), staticContext);
        this.leftIterator = leftIterator;
        this.rightIterator = rightIterator;
        this.operator = operator;
    }

    @Override
    public boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    public Item nextLocal() {
        if (!this.hasNext) {
            throw new IteratorFlowException("Invalid next call in node set operation", getMetadata());
        }
        Item result = this.localResults.get(this.nextResultIndex++);
        this.hasNext = this.nextResultIndex < this.localResults.size();
        return result;
    }

    @Override
    public void openLocal() {
        this.localResults = computeNodeSet(this.currentDynamicContextForLocalExecution);
        this.nextResultIndex = 0;
        this.hasNext = !this.localResults.isEmpty();
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
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

    @Override
    protected void closeLocal() {
        this.localResults = null;
        this.nextResultIndex = 0;
    }

    @Override
    protected void resetLocal() {
        openLocal();
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
            RuntimeIterator iterator,
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
