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

package org.rumbledb.expressions.primary;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * XQuery 3.1 map constructor: {@code map { key : value, ... }}.
 */
public class MapConstructorExpression extends Expression {

    private List<Expression> keys;
    private List<Expression> values;

    public MapConstructorExpression(
            List<Expression> keys,
            List<Expression> values,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.keys = keys;
        this.values = values;
    }

    public List<Expression> getKeys() {
        return this.keys;
    }

    public List<Expression> getValues() {
        return this.values;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.addAll(this.keys);
        result.addAll(this.values);
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("map {\n");
        if (this.keys != null) {
            for (int i = 0; i < this.keys.size(); i++) {
                this.keys.get(i).serializeToJSONiq(sb, indent + 1);
                sb.append(" : ");
                this.values.get(i).serializeToJSONiq(sb, 0);
                if (i < this.keys.size() - 1) {
                    sb.append(",\n");
                } else {
                    sb.append("\n");
                }
            }
        }
        indentIt(sb, indent);
        sb.append("}");
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitMapConstructor(this, argument);
    }
}
