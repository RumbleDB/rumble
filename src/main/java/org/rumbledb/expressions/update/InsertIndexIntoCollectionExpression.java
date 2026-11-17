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

public class InsertIndexIntoCollectionExpression extends Expression {
    @Getter
    private final Expression collection;

    @Getter
    private final Expression contentExpression;

    private final Expression pos;

    @Getter
    private final Mode mode;

    @Getter
    private final boolean isFirst;

    @Getter
    private final boolean isLast;

    public InsertIndexIntoCollectionExpression(
            Expression collection,
            Expression contentExpression,
            Expression pos,
            Mode mode,
            boolean isFirst,
            boolean isLast,
            ExceptionMetadata metadata) {
        super(metadata);
        if (collection == null) {
            throw new OurBadException("Collection must be identified for insertion.");
        }
        if (contentExpression == null) {
            throw new OurBadException("Content must be specified for insertion.");
        }

        this.collection = collection;
        this.contentExpression = contentExpression;
        this.mode = mode;
        this.pos = pos;
        this.isFirst = isFirst;
        this.isLast = isLast;
    }

    public Expression getPosition() {
        return this.pos;
    }

    @Override
    public List<Node> getChildren() {
        return this.pos != null
                ? Arrays.asList(this.contentExpression, this.collection, this.pos)
                : Arrays.asList(this.contentExpression, this.collection);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitInsertIndexIntoCollectionExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        sb.append("insert ");
        this.contentExpression.serializeToJSONiq(sb, 1);
        if (this.isLast) {
            sb.append(" last ");
        } else if (this.isFirst) {
            sb.append(" first ");
        } else {
            sb.append(" at ");
            this.pos.serializeToJSONiq(sb, 1);
        }
        sb.append(" into collection ");
        if (this.mode == Mode.HIVE) {
            sb.append("table");
        } else if (this.mode == Mode.DELTA) {
            sb.append("delta-file");
        } else if (this.mode == Mode.ICEBERG) {
            sb.append("iceberg-table");
        }
        sb.append("(");
        this.collection.serializeToJSONiq(sb, 0);
        sb.append(")");
        sb.append("\n");
    }
}
