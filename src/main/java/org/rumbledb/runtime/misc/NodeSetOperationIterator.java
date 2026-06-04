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
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.miscellaneous.NodeSetExpression;
import org.rumbledb.items.xml.XMLDocumentPosition;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

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
        /// TODO: Make this really distributed by implementing the node set operations in a distributed manner, instead of materializing the entire result on the driver.
        return SparkSessionManager.getInstance().getJavaSparkContext().parallelize(computeNodeSet(context));
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
        TreeMap<XMLDocumentPosition, Item> leftNodes = buildNodePositionMap(this.leftIterator, context, "left");
        TreeMap<XMLDocumentPosition, Item> rightNodes = buildNodePositionMap(this.rightIterator, context, "right");

        switch (this.operator) {
            case UNION:
                leftNodes.putAll(rightNodes);
                return new ArrayList<>(leftNodes.values());
            case INTERSECT:
                leftNodes.keySet().retainAll(rightNodes.keySet());
                return new ArrayList<>(leftNodes.values());
            case EXCEPT:
                Set<XMLDocumentPosition> excludedPositions = new HashSet<>(rightNodes.keySet());
                leftNodes.keySet().removeIf(excludedPositions::contains);
                return new ArrayList<>(leftNodes.values());
            default:
                throw new IteratorFlowException("Unrecognized node set operator: " + this.operator, getMetadata());
        }
    }

    /**
     * Builds a map of XML document positions to items.
     * 
     * Because the result of a node set operation must be ordered according to document order
     * We use a TreeMap to store the nodes, which will automatically sort them by their document position.
     */
    private TreeMap<XMLDocumentPosition, Item> buildNodePositionMap(
            RuntimeIterator iterator,
            DynamicContext context,
            String side
    ) {
        TreeMap<XMLDocumentPosition, Item> nodesByPosition = new TreeMap<>();
        for (Item item : iterator.materialize(context)) {
            if (!item.isNode()) {
                throw new UnexpectedTypeException(
                        "The "
                            + side
                            + " operand of a node set operation must contain only nodes, got: "
                            + item.getDynamicType(),
                        getMetadata()
                );
            }
            XMLDocumentPosition position = item.getXmlDocumentPosition();
            if (position == null) {
                throw new UnexpectedTypeException(
                        "The " + side + " operand of a node set operation contains a node without document position.",
                        getMetadata()
                );
            }
            nodesByPosition.put(position, item);
        }
        return nodesByPosition;
    }
}
