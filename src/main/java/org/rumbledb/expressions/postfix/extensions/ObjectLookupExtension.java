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

package org.rumbledb.expressions.postfix.extensions;


import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

public class ObjectLookupExtension extends PostfixExtension {

    Expression expression;

    public ObjectLookupExtension(Expression expr, ExceptionMetadata metadata) {
        super(metadata);
        this.expression = expr;
    }

    public Expression getExpression() {
        return this.expression;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
<<<<<<< HEAD
        if (this.expression != null) // field
            result.add(this.expression); // field
        return getDescendantsFromChildren(result, depthSearch);
=======
        if (this._expression != null) // field
            result.add(this._expression); // field
        return result;
>>>>>>> c94fc8ddae10d0d8652a240536e13bdcdb7fce0d
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(objectLookup . " + this.expression.serializationString(false) + ")";
        return result;
    }
}
