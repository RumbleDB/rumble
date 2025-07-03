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
 * Authors: Stefan Irimescu, Can Berker Cikis, Matteo Agnoletto (EPMatt)
 *
 */

package org.rumbledb.runtime.functions.sequences.value;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.FlatMapFunction2;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.DefaultCollationException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import scala.Tuple2;

import java.util.Iterator;
import java.util.List;

public class DeepEqualFunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;


    public DeepEqualFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        RuntimeIterator sequenceIterator1 = this.children.get(0);
        RuntimeIterator sequenceIterator2 = this.children.get(1);
        if (this.children.size() == 3) {
            String collation = this.children.get(2).materializeFirstItemOrNull(context).getStringValue();
            if (!collation.equals("http://www.w3.org/2005/xpath-functions/collation/codepoint")) {
                throw new DefaultCollationException("Wrong collation parameter", getMetadata());
            }
        }

        if (sequenceIterator1.isRDDOrDataFrame() && sequenceIterator2.isRDDOrDataFrame()) {
            JavaRDD<Item> rdd1 = sequenceIterator1.getRDD(context);
            JavaRDD<Item> rdd2 = sequenceIterator2.getRDD(context);
            if (rdd1.partitions().size() == rdd2.partitions().size()) {
                FlatMapFunction2<Iterator<Item>, Iterator<Item>, Boolean> filter =
                    new SameElementsAndLengthClosure();
                JavaRDD<Boolean> differences = rdd1.zipPartitions(rdd2, filter);
                return ItemFactory.getInstance().createBooleanItem(differences.isEmpty());
            } else {
                JavaPairRDD<Long, Item> rdd1Zipped = rdd1.zipWithIndex().mapToPair(Tuple2::swap);
                JavaPairRDD<Long, Item> rdd2Zipped = rdd2.zipWithIndex().mapToPair(Tuple2::swap);
                JavaPairRDD<Long, Tuple2<Optional<Item>, Optional<Item>>> rddJoined = rdd1Zipped.fullOuterJoin(
                    rdd2Zipped
                );
                JavaPairRDD<Long, Tuple2<Optional<Item>, Optional<Item>>> rddFiltered = rddJoined.filter(
                    tuple -> !tuple._2()._1().equals(tuple._2()._2())
                );
                return ItemFactory.getInstance().createBooleanItem(rddFiltered.isEmpty());
            }
        }
        List<Item> items1 = sequenceIterator1.materialize(context);
        List<Item> items2 = sequenceIterator2.materialize(context);

        boolean res = checkDeepEqual(items1, items2);
        return ItemFactory.getInstance().createBooleanItem(res);
    }

    public boolean checkDeepEqual(List<Item> items1, List<Item> items2) {
        if (items1.size() != items2.size()) {
            return false;
        } else {
            for (int i = 0; i < items1.size(); i++) {
                Item item1 = items1.get(i);
                Item item2 = items2.get(i);

                if (!checkItemsDeepEqual(item1, item2)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Checks if two items are deep-equal according to XQuery 3.1 specification.
     * This method handles the comparison logic for individual items, including
     * special cases for nodes and atomic values.
     *
     * @param item1 The first item to compare
     * @param item2 The second item to compare
     * @return true if the items are deep-equal, false otherwise
     */
    private boolean checkItemsDeepEqual(Item item1, Item item2) {
        // Specific to deep-equal but does not apply to eq operator
        // Special handling for NaN values - NaN is deep-equal to NaN
        if (
            ((item1.isFloat() && Float.isNaN(item1.getFloatValue()))
                || (item1.isDouble() && Double.isNaN(item1.getDoubleValue())))
                && ((item2.isFloat() && Float.isNaN(item2.getFloatValue()))
                    || (item2.isDouble() && Double.isNaN(item2.getDoubleValue())))
        ) {
            return true;
        }

        // If both items are nodes, use node-specific deep-equal logic
        if (item1.isNode() && item2.isNode()) {
            return checkNodesDeepEqual(item1, item2);
        }

        // For non-node items, use the default equals implementation
        return item1.equals(item2);
    }

    /**
     * Checks if two nodes are deep-equal according to XQuery 3.1 specification.
     * Reference: https://www.w3.org/TR/xpath-functions-31/#func-deep-equal
     * 
     * @param node1 The first node to compare
     * @param node2 The second node to compare
     * @return true if the nodes are deep-equal, false otherwise
     */
    private boolean checkNodesDeepEqual(Item node1, Item node2) {
        // 1: If the two nodes are of different kinds, the result is false.
        if (!sameNodeKind(node1, node2)) {
            return false;
        }

        // 2: If the two nodes are both document nodes then they are deep-equal
        // if and only if the sequence $i1/(*|text()) is deep-equal to the sequence $i2/(*|text()).
        if (node1.isDocumentNode() && node2.isDocumentNode()) {
            return checkDeepEqual(getElementsAndTextNodes(node1), getElementsAndTextNodes(node2));
        }

        // 3: If the two nodes are both element nodes then they are deep-equal
        // if and only if all of the following conditions are satisfied:
        if (node1.isElementNode() && node2.isElementNode()) {
            return checkElementNodesDeepEqual(node1, node2);
        }

        // 4: If the two nodes are both attribute nodes then they are deep-equal
        // if and only if both the following conditions are satisfied:
        if (node1.isAttributeNode() && node2.isAttributeNode()) {
            return checkAttributeNodesDeepEqual(node1, node2);
        }

        // 5: If the two nodes are both processing instruction nodes, then they are deep-equal
        // if and only if both the following conditions are satisfied:
        // Note: Processing instruction nodes are not yet implemented in Rumble

        // 6: If the two nodes are both namespace nodes, then they are deep-equal
        // if and only if both the following conditions are satisfied:
        // Note: Namespace nodes are not yet implemented in Rumble

        // 7: If the two nodes are both text nodes or comment nodes, then they are deep-equal
        // if and only if their string-values are equal.
        if (node1.isTextNode() && node2.isTextNode()) {
            return node1.getTextValue().equals(node2.getTextValue());
        }

        // Note: Comment nodes are not yet implemented in Rumble

        // In all other cases the result is false.
        return false;
    }

    /**
     * Checks if two nodes are of the same kind.
     * 
     * @param node1 The first node
     * @param node2 The second node
     * @return true if nodes are of the same kind, false otherwise
     */
    private boolean sameNodeKind(Item node1, Item node2) {
        return (node1.isDocumentNode() && node2.isDocumentNode())
            ||
            (node1.isElementNode() && node2.isElementNode())
            ||
            (node1.isAttributeNode() && node2.isAttributeNode())
            ||
            (node1.isTextNode() && node2.isTextNode());
        // TODO: Add support for processing instruction, comment, and namespace nodes when implemented
    }

    /**
     * Gets the child elements and text nodes of a document or element node.
     * This corresponds to the XPath expression (*|text()).
     * 
     * @param node The document or element node
     * @return List of child elements and text nodes
     */
    private List<Item> getElementsAndTextNodes(Item node) {
        return node.children()
            .stream()
            .filter(child -> child.isElementNode() || child.isTextNode())
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 3: Checks if two element nodes are deep-equal according to XQuery 3.1 specification.
     * 
     * @param element1 The first element node
     * @param element2 The second element node
     * @return true if the element nodes are deep-equal, false otherwise
     */
    private boolean checkElementNodesDeepEqual(Item element1, Item element2) {
        // Point 3a: The two nodes have the same name, that is (node-name($i1) eq node-name($i2)).
        if (!element1.nodeName().equals(element2.nodeName())) {
            return false;
        }

        // 3b: Either both nodes are annotated as having simple content or both nodes
        // are annotated as having complex content. For this purpose "simple content" means
        // either a simple type or a complex type with simple content; "complex content" means
        // a complex type whose variety is mixed, element-only, or empty.
        // Note: Schema type information is not yet implemented in Rumble, so we skip this check

        // 3c: The two nodes have the same number of attributes, and for every attribute
        // $a1 in $i1/@* there exists an attribute $a2 in $i2/@* such that $a1 and $a2 are deep-equal.
        if (!checkAttributesDeepEqual(element1.attributes(), element2.attributes())) {
            return false;
        }

        // 3d: One of the following conditions holds:
        // i. Both element nodes are annotated as having simple content (as defined in 3(b) above), and the typed value
        // of $i1 is deep-equal to the typed value of $i2.
        // ii. Both element nodes have a type annotation that is a complex type with variety element-only, and the
        // sequence $i1/* is deep-equal to the sequence $i2/*.
        // iii. Both element nodes have a type annotation that is a complex type with variety mixed, and the sequence
        // $i1/(*|text()) is deep-equal to the sequence $i2/(*|text()).
        // iv. Both element nodes have a type annotation that is a complex type with variety empty.
        // For now, we assume mixed content and compare all child nodes (elements and text) (corresponds to iii.)
        // Note: element type annotations are not yet implemented in Rumble
        return checkDeepEqual(getElementsAndTextNodes(element1), getElementsAndTextNodes(element2));
    }

    /**
     * 4: Checks if two attribute nodes are deep-equal according to XQuery 3.1 specification.
     * 
     * @param attr1 The first attribute node
     * @param attr2 The second attribute node
     * @return true if the attribute nodes are deep-equal, false otherwise
     */
    private boolean checkAttributeNodesDeepEqual(Item attr1, Item attr2) {
        // 4a: The two nodes have the same name, that is (node-name($i1) eq node-name($i2)).
        if (!attr1.nodeName().equals(attr2.nodeName())) {
            return false;
        }

        // 4b: The typed value of $i1 is deep-equal to the typed value of $i2.
        // Note: we do not support type annotations on attribute nodes yet.
        // For now, the typed value of the attribute node is the same as its string value
        return checkDeepEqual(attr1.atomizedValue(), attr2.atomizedValue());
    }

    /**
     * Checks if two lists of attributes are deep-equal.
     * Each attribute in the first list must have a corresponding deep-equal attribute in the second list.
     * 
     * @param attrs1 The first list of attributes
     * @param attrs2 The second list of attributes
     * @return true if the attribute lists are deep-equal, false otherwise
     */
    private boolean checkAttributesDeepEqual(List<Item> attrs1, List<Item> attrs2) {
        if (attrs1.size() != attrs2.size()) {
            return false;
        }

        // For each attribute in attrs1, find a corresponding deep-equal attribute in attrs2
        for (Item attr1 : attrs1) {
            boolean found = false;
            for (Item attr2 : attrs2) {
                if (checkAttributeNodesDeepEqual(attr1, attr2)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }

        return true;
    }
}
