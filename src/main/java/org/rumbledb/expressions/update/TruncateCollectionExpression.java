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
public class TruncateCollectionExpression extends Expression {
    private Expression collectionName;
    private Mode mode;

    public TruncateCollectionExpression(Expression collectionName, Mode mode, ExceptionMetadata metadata) {
        super(metadata);
        if (collectionName == null) {
            throw new OurBadException("collection must be identified for truncation.");
        }
        this.mode = mode;
        this.collectionName = collectionName;
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.collectionName);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitTruncateCollectionExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        sb.append("truncate ");
        sb.append("collection table(");
        this.collectionName.serializeToJSONiq(sb, 0);
        sb.append("\n");
    }
}
