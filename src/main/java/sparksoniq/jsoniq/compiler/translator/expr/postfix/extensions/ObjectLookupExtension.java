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
 package sparksoniq.jsoniq.compiler.translator.expr.postfix.extensions;

import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;

import java.util.ArrayList;
import java.util.List;

public class ObjectLookupExtension extends PostfixExtension {

    Expression _expression;

    public Expression getExpression() {
        return _expression;
    }

    public ObjectLookupExtension(Expression expr, ExpressionMetadata metadata) {
        super(metadata);
        this._expression = expr;
    }

    @Override
    public List<ExpressionOrClause> getDescendants(boolean depthSearch) {
        List<ExpressionOrClause> result =  new ArrayList<>();
        if(this._expression !=null)       //field
            result.add(this._expression); //field
        return getDescendantsFromChildren(result,depthSearch);
    }

    @Override
    public String serializationString(boolean prefix){
        String result = "(objectLookup . " + _expression.serializationString(false) + ")";
        return result;
    }
}
