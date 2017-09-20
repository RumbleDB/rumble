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
 package jiqs.semantics;

import jiqs.semantics.types.SequenceType;
import jiqs.exceptions.SemanticException;

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
            return _inScopeVariables.get(varName);
        else if(_parent !=null)
            return _parent.getVariableSequenceType(varName);
        else
            throw new SemanticException("Variable " + varName + " not in scope");
    }

    public void addVariable(String varName, SequenceType type){
//        if(this._inScopeVariables.containsKey(varName) || isInScope(varName))
//            throw new SemanticException("Variable " + varName + " redeclared");
//        else
        //allow variables redeclaration
        this._inScopeVariables.put(varName, type);
    }


    protected Map<String, SequenceType> getInScopeVariables() {
        return _inScopeVariables;
    }

    private Map<String, SequenceType> _inScopeVariables;
    private StaticContext _parent;
}
