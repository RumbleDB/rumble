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
 package sparksoniq.jsoniq.compiler.translator.expr.primary;


import java.io.Serializable;

import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.types.SequenceType;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

public class VariableReference extends PrimaryExpression implements Serializable {

    public String getVariableName() {
        return name;
    }

    public VariableReference(String _name, ExpressionMetadata metadata) {
        super(metadata);
        this.name = _name;
    }


    public SequenceType getType() {
        return _type;
    }

    public void setType(SequenceType type){
        this._type = type;
    }

    @Override
    public  <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument){
        return visitor.visitVariableReference(this, argument);
    }

    @Override
    public String serializationString(boolean prefix){
        String result = (prefix? "(primaryExpr " : "") + "(varRef $ " + name;
        result += (prefix? ")" : "") +  ")";
        return result;
    }

    private String name;
    private SequenceType _type;


}
