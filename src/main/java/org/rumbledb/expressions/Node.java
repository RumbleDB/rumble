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

import org.rumbledb.compiler.VisitorConfig;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * This is the top-level class for nodes in the intermediate representation of a
 * JSONiq query. Nodes include expressions, clauses, function declarations, etc.
 */
public abstract class Node {

    private ExceptionMetadata metadata;

    protected ExecutionMode highestExecutionMode = ExecutionMode.UNSET;

    protected Node() {
    }

    protected Node(ExceptionMetadata metadata) {
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
    public final void setHighestExecutionMode(ExecutionMode newMode) {
        this.highestExecutionMode = newMode;
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
     * if Node.suppressUnsetExecutionModeAccessedErrors is false, then an error is thrown if an UNSET mode is found.
     * if Node.suppressUnsetExecutionModeAccessedErrors is true, it might silently return UNSET.
     * 
     * @param visitorConfig the configuration of the visitor.
     * @return the highest execution mode.
     */
    public ExecutionMode getHighestExecutionMode(VisitorConfig visitorConfig) {
        if (
            !visitorConfig.suppressErrorsForAccessingUnsetExecutionModes()
                && this.highestExecutionMode == ExecutionMode.UNSET
        ) {
            throw new OurBadException("An execution mode is accessed without being set.");
        }
        return this.highestExecutionMode;
    }

    /**
     * Gets the highest execution mode of this node, which determines
     * whether evaluation will be done locally, with RDDs or with DataFrames.
     *
     * This method is used during the static analysis. It is meant to be
     * overridden by subclasses that support higher execution modes. By
     * default, the highest execution mode is assumed to be local.
     *
     * @return the highest execution mode.
     */
    public ExecutionMode getHighestExecutionMode() {
        return this.highestExecutionMode;
    }

    public int numberOfUnsetExecutionModes() {
        int result = 0;
        for (Node n : this.getChildren()) {
            result += n.numberOfUnsetExecutionModes();
        }
        if (this.highestExecutionMode.equals(ExecutionMode.UNSET)) {
            ++result;
        }
        return result;
    }

    /**
     * Accept method for the visitor pattern.
     *
     * @param <T> the type of the objects returned by the visitor.
     * @param visitor the visitor.
     * @param argument the input from the visitor.
     * @return the object returned by this visitor
     */
    public abstract <T> T accept(AbstractNodeVisitor<T> visitor, T argument);

    /**
     * Returns all children nodes as a list. The list is new and can be modified at will by the caller.
     *
     * @return the children nodes as a list.
     */
    public abstract List<Node> getChildren();

    /**
     * For gathering descendant nodes, as a depth-first search. The list is new and can be modified at will by the
     * caller.
     *
     * @return the descendant nodes as a list.
     */
    public final List<Node> getDescendants() {
        List<Node> result = new ArrayList<>();
        for (Node child : this.getChildren()) {
            result.addAll(child.getDescendants());
            result.add(child);
        }
        return result;
    }

    /**
     * For gathering descendant nodes that match a predicate. The list is new and can be modified at will by the caller.
     *
     * @param predicate a predicate to filter with.
     * @return the descendant nodes as a list.
     */
    public final List<Node> getDescendantsMatching(Predicate<Node> predicate) {
        List<Node> descendants = this.getDescendants();
        List<Node> results = new ArrayList<>();
        descendants.stream().filter(predicate).forEach(r -> results.add(r));
        return results;
    }

    /**
     * Access the metadata of the node, i.e., the line and column number.
     * This is used for displaying informative error messages.
     *
     * @return the metadata.
     */
    public ExceptionMetadata getMetadata() {
        return this.metadata;
    }

    /**
     * Prints the node tree to a string buffer.
     *
     * @param buffer a string buffer to write to
     * @param indent the current level of indentation
     */
    public void print(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(" | " + this.highestExecutionMode);
        buffer.append("\n");
        for (Node iterator : getChildren()) {
            iterator.print(buffer, indent + 1);
        }
    }

    @Override
    public final String toString() {
        StringBuffer sb = new StringBuffer();
        this.print(sb, 0);
        return sb.toString();
    }

    public abstract void serializeToJSONiq(StringBuffer sb, int indent);

    protected void indentIt(StringBuffer buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
    }

    /**
     * Tells whether the expression is context dependent.
     * 
     * @return true if it is context dependent, false otherwise.
     */
    public boolean isContextDependent() {
        for (Node node : this.getChildren()) {
            if (node.isContextDependent()) {
                return true;
            }
        }
        return false;
    }
}
