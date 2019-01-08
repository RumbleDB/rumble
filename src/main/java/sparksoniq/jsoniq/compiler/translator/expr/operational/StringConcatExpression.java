/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
 package sparksoniq.jsoniq.compiler.translator.expr.operational;

import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.NaryExpressionBase;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.util.List;

public class StringConcatExpression extends NaryExpressionBase {
    public StringConcatExpression(Expression mainExpression, List<Expression> rhs, ExpressionMetadata metadata) {
        super(mainExpression, rhs, Operator.CONCAT, metadata);
    }

    public StringConcatExpression(RangeExpression mainExpression, ExpressionMetadata metadata) {
        super(mainExpression, metadata);
    }

    @Override
    public  <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument){
        return visitor.visitStringConcatExpr(this, argument);
    }

    @Override
    public String serializationString(boolean prefix){
        String result = "(stringConcatExpr ";
        result += _mainExpression.serializationString(true);
        if(this.getRightExpressions() != null && this.getRightExpressions().size() > 0)
            for(Expression expr : this.getRightExpressions())
                result += " || " + expr.serializationString(true);
        result += ")";
        return result;
    }
}
