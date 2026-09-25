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
package org.rumbledb.expressions.update;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.runtime.update.primitives.Mode;

@Getter
public class DeleteIndexFromCollectionExpression extends Expression {
    private Expression collection;
    private Expression numDelete;
    private boolean isFirst;
    private Mode mode;

    public DeleteIndexFromCollectionExpression(
            Expression collection, Expression numDelete, boolean isFirst, Mode mode, ExceptionMetadata metadata) {
        // TODO: The current implementations only accounts for two callening modes- table, and delta-file
        // Extension to other modes can be done by increasing flags for using enum instead
        super(metadata);
        if (collection == null) {
            throw new OurBadException("Collection must be identified for indexed Deletion.");
        }
        this.collection = collection;
        this.numDelete = numDelete;
        this.isFirst = isFirst;
        this.mode = mode;
    }

    @Override
    public List<Node> getChildren() {
        return this.numDelete == null ? Arrays.asList(this.collection) : Arrays.asList(this.collection, this.numDelete);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitDeleteIndexFromCollectionExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        sb.append("delete ");
        sb.append(this.isFirst ? "first " : "last ");
        sb.append(this.numDelete);
        sb.append(" from collection ");
        this.collection.serializeToJSONiq(sb, 0);
        sb.append("\n");
    }
}
