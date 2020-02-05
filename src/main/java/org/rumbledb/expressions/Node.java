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

package org.rumbledb.expressions;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.rumbledb.exceptions.OurBadException;

/**
 * This is the top-level class for nodes in the intermediate representation of a
 * JSONiq query. Nodes include expressions, clauses, function declarations, etc.
 */
public abstract class Node {

    private ExpressionMetadata metadata;

    protected ExecutionMode _highestExecutionMode = ExecutionMode.UNSET;

    protected Node() {
    }

    protected Node(ExpressionMetadata metadata) {
        this.metadata = metadata;
    }

    /**
     * Initializes the highest execution mode of this node, which determines
     * whether evaluation will be done locally, with RDDs or with DataFrames.
     *
     * This method is used during the static analysis. It is meant to be
     * overridden by subclasses that support higher execution modes. By
     * default, the highest execution mode is assumed to be local.
     */
    public void initHighestExecutionMode() {
        _highestExecutionMode = ExecutionMode.LOCAL;
    }

    /**
     * Gets the highest execution mode of this node, which determines
     * whether evaluation will be done locally, with RDDs or with DataFrames.
     *
     * This method is used during the static analysis. It is meant to be
     * overridden by subclasses that support higher execution modes. By
     * default, the highest execution mode is assumed to be local.
     * 
     * If the mode is unset, which should not happen, an unexpected error will be thrown.
     * 
     * When extending this method, make sure to perform a super() call to prevent UNSET accesses.
     * 
     * @return the highest execution mode.
     */
    public final ExecutionMode getHighestExecutionMode() {
        return getHighestExecutionMode(false);
    }

    /**
     * Gets the highest execution mode of this node, which determines
     * whether evaluation will be done locally, with RDDs or with DataFrames.
     *
     * This method is used during the static analysis. It is meant to be
     * overridden by subclasses that support higher execution modes. By
     * default, the highest execution mode is assumed to be local.
     * 
     * When extending this method, make sure to perform a super() call to prevent UNSET accesses.
     * 
     * @param ignoreUsetError if true, then an error is thrown if an UNSET mode is found.
     * If false, it might silently return UNSET.
     * 
     * @return the highest execution mode.
     */
    public ExecutionMode getHighestExecutionMode(boolean ignoreUnsetError) {
        if (!ignoreUnsetError && _highestExecutionMode == ExecutionMode.UNSET) {
            throw new OurBadException("An execution mode is accessed without being set.");
        }
        return _highestExecutionMode;
    }

    /**
     * For gathering descendant nodes.
     * 
     * @param depthSearch activates depth-first search if true. If false, only returns children.
     * 
     * @return the descendant nodes as a list.
     */
    public abstract List<Node> getDescendants(boolean depthSearch);

    /**
     * Accept method for the visitor pattern.
     */
    public abstract <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument);

    /**
     * Serializes the node.
     * 
     * @param prefix for indentation purposes.
     * 
     * @return the serialized node.
     */
    public abstract String serializationString(boolean prefix);

    /**
     * Returns all children nodes.
     * 
     * @return the children nodes as a list.
     */
    public final List<Node> getChildren() {
        return getDescendants(false);
    }

    /**
     * For gathering descendant nodes that match a predicate.
     * 
     * @param predicate a predicate to filter with.
     * @param depthSearch to switch between all descendants or only direct children.
     * 
     * @return the descendant nodes as a list.
     */
    public List<Node> getDescendantsOfType(Predicate<Node> predicate, boolean depthSearch) {
        List<Node> result = this.getDescendants(depthSearch);
        List<Node> filter = new ArrayList<>();
        result.stream().filter(predicate).forEach(r -> filter.add(r));
        return filter;
    }

    /**
     * Access the metadata of the node, i.e., the line and column number.
     * This is used for displaying informative error messages.
     * 
     * @return the metadata.
     */
    public ExpressionMetadata getMetadata() {
        return metadata;
    }

    /**
     * Utility method that can be used in overrides of getDescendants().
     * It iterates over the list of nodes and, if depth-first search is activated,
     * returns a list with recursively added descendants.
     * 
     * @return the metadata.
     */
    protected static List<Node> getDescendantsFromChildren(
            List<Node> result,
            boolean depthSearch
    ) {
        if (depthSearch) {
            List<Node> childrenResults = new ArrayList<>();
            result.forEach(r -> childrenResults.addAll(r.getDescendants(depthSearch)));
            result.addAll(childrenResults);
        }
        return result;
    }

}
