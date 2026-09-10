/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import lombok.Getter;
import lombok.Setter;

import org.rumbledb.exceptions.ExceptionMetadata;

/**
 * This is the top-level class for nodes in the intermediate representation of a
 * JSONiq query. Nodes include expressions, clauses, function declarations, etc.
 */
@Getter
public abstract class Node {

    /**
     * Access the metadata of the node, i.e., the line and column number.
     * This is used for displaying informative error messages.
     */
    private ExceptionMetadata metadata;

    /**
     * Gets the highest execution mode of this node, which determines
     * whether evaluation will be done locally, with RDDs or with DataFrames.
     * May be UNSET while analysis is in progress. Consumers decide whether
     * an unresolved annotation is acceptable at their compilation stage.
     */
    @Setter
    protected ExecutionMode highestExecutionMode = ExecutionMode.UNSET;

    protected boolean isInSequentialBlock;

    protected Node(ExceptionMetadata metadata) {
        this.metadata = metadata;
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
     * Prints the node tree to a string buffer.
     *
     * @param buffer a string buffer to write to
     * @param indent the current level of indentation
     */
    public void print(StringBuilder buffer, int indent) {
        for (int i = 0; i < indent; ++i) {
            buffer.append("  ");
        }
        buffer.append(getClass().getSimpleName());
        buffer.append(" | " + this.highestExecutionMode);
        if (this.isInSequentialBlock) {
            buffer.append(" | " + "in sequential block");
        } else {
            buffer.append(" | " + "not in sequential block");
        }
        buffer.append("\n");
        for (Node iterator : getChildren()) {
            iterator.print(buffer, indent + 1);
        }
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        this.print(sb, 0);
        return sb.toString();
    }

    public abstract void serializeToJSONiq(StringBuilder sb, int indent);

    protected void indentIt(StringBuilder buffer, int indent) {
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

    public void setIsInSequentialBlock(boolean isInSequentialBlock) {
        this.isInSequentialBlock = isInSequentialBlock;
    }
}
