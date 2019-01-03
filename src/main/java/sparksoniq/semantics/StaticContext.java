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
 package sparksoniq.semantics;

import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.types.SequenceType;
import sparksoniq.exceptions.SemanticException;
import sparksoniq.utils.Tuple;

import java.util.HashMap;
import java.util.Map;

public class StaticContext {

    public StaticContext getParent() {
        return _parent;
    }

    public StaticContext(){
        this._parent = null;
        this._inScopeVariables = new HashMap<>();
        }

    public StaticContext(StaticContext parent){
        this._parent = parent;
        this._inScopeVariables = new HashMap<>();
    }

    public boolean isInScope(String varName){
        boolean found = false;
        if(_inScopeVariables.containsKey(varName))
            return true;
        else{
            StaticContext ancestor = _parent;
            while (ancestor != null) {
                found = found || ancestor.getInScopeVariables().containsKey(varName);
                ancestor = ancestor.getParent();
            }
        }
        return found;
    }

    public SequenceType getVariableSequenceType(String varName) {
        if(_inScopeVariables.containsKey(varName))
            return _inScopeVariables.get(varName).getFirst();
        else if(_parent !=null)
            return _parent.getVariableSequenceType(varName);
        else
            throw new SemanticException("Variable " + varName + " not in scope", null);
    }

    public void addVariable(String varName, SequenceType type, ExpressionMetadata metadata){
        this._inScopeVariables.put(varName, new Tuple<>(type, metadata));
    }


    protected Map<String, Tuple<SequenceType, ExpressionMetadata>> getInScopeVariables() {
        return _inScopeVariables;
    }
    private Map<String, Tuple<SequenceType, ExpressionMetadata>> _inScopeVariables;
    private StaticContext _parent;
}
