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

package org.rumbledb.expressions.module;




import sparksoniq.semantics.visitor.AbstractNodeVisitor;




import java.util.ArrayList;
import java.util.List;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

public class MainModule extends Expression {

    private final Prolog _prolog;
    private final CommaExpression _commaExpression;

    public MainModule(Prolog _prolog, CommaExpression _commaExpression, ExceptionMetadata metadata) {
        super(metadata);
        this._prolog = _prolog;
        this._commaExpression = _commaExpression;
    }

    public Prolog get_prolog() {
        return _prolog;
    }

    public CommaExpression get_commaExpression() {
        return _commaExpression;
    }

    @Override
    public List<Node> getDescendants(boolean depthSearch) {
        List<Node> result = new ArrayList<>();
        if (_prolog != null) {
            result.add(_prolog);
        }
        if (_commaExpression != null) {
            result.add(_commaExpression);
        }
        return getDescendantsFromChildren(result, depthSearch);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitMainModule(this, argument);
    }

    @Override
    public String serializationString(boolean prefix) {
        String result = "(mainModule ";
        result += " (prolog " + _prolog.serializationString(false) + "), ";
        result += " (expr " + _commaExpression.serializationString(false) + ") ";
        result += ")";
        return result;
    }
}

