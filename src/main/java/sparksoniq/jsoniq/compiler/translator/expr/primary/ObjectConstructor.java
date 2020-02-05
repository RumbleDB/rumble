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

package sparksoniq.jsoniq.compiler.translator.expr.primary;

import sparksoniq.jsoniq.compiler.translator.expr.CommaExpression;
import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import org.rumbledb.exceptions.ExceptionMetadata;;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.util.ArrayList;
import java.util.List;

public class ObjectConstructor extends PrimaryExpression {

    private boolean isMergedConstructor = false;
    private List<Expression> _values;
    private List<Expression> _keys;
    private CommaExpression childExpression;

    public ObjectConstructor(
            List<Expression> keys,
            List<Expression> values,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this._keys = keys;
        this._values = values;
    }

    public ObjectConstructor(CommaExpression expression, ExceptionMetadata metadata) {
        super(metadata);
        this.childExpression = expression;
        this.isMergedConstructor = true;
    }

    public List<Expression> getKeys() {
        return _keys;
    }

    public List<Expression> getValues() {
        return _values;
    }

    public boolean isMergedConstructor() {
        return isMergedConstructor;
    }

    public CommaExpression getChildExpression() {
        return childExpression;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result = new ArrayList<>();
        if (!isMergedConstructor) {
            result.addAll(this._keys);
            result.addAll(this._values);
        } else
            result.add(childExpression);
        return getDescendantsFromChildren(result, depthSearch);
    }

    @Override
    public <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument) {
        return visitor.visitObjectConstructor(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(primaryExpr (objectConstructor {";
        if (!isMergedConstructor) {
            result += " ";
            for (Expression key : _keys)
                result += new PairConstructor(key, _values.get(_keys.indexOf(key)), key.getMetadata())
                    .serializationString(true)
                    + (_keys.indexOf(key) < _keys.size() - 1 ? " , " : " ");
        } else
            result += "| " + childExpression.serializationString(prefix) + " |";
        result += "}))";
        return result;

    }

    public static class PairConstructor extends PrimaryExpression {

        private Expression _key;
        private Expression _value;

        public PairConstructor(Expression key, Expression value, ExceptionMetadata metadata) {
            super(metadata);
            this._key = key;
            this._value = value;
        }

        public Expression get_key() {
            return _key;
        }

        public Expression get_value() {
            return _value;
        }

        @Override
        public String serializationString(boolean prefix) {
            String result = "(pairConstructor (exprSingle "
                + _key.serializationString(false)
                + ") : (exprSingle "
                + _value.serializationString(false);
            result += "))";
            return result;

        }


    }


}
