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
 package jiqs.jsoniq.compiler.translator.expr.postfix.extensions;

import jiqs.jsoniq.compiler.translator.expr.Expression;
import jiqs.jsoniq.compiler.translator.expr.ExpressionOrClause;

import java.util.ArrayList;
import java.util.List;


public class ArrayLookupExtension extends PostfixExtension {

    public Expression getExpression() {
        return _expression;
    }

    Expression _expression;

    public ArrayLookupExtension(Expression commaExpr) {
        super();
        this._expression = commaExpr;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result =  new ArrayList<>();
        if(this._expression !=null)
            result.add(this._expression);
        return getDescendantsFromChildren(result,depthSearch);
    }

    @Override
    public String serializationString(boolean prefix){
        String result = "(arrayLookup [[" +
                this._expression.serializationString(true) + "]])";
        return result;
    }
}
