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

package org.rumbledb.expressions;

import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.semantics.StaticContext;

/**
 * An expression is the first-class citizen in JSONiq syntax. Any expression
 * returns a sequence of items.
 * 
 * Expressions form a tree, but this tree may contain other nodes, such as clauses
 * and function declarations.
 * 
 * An expression is associated with a static context containing information such as
 * the in-scope variables.
 */
public abstract class Expression extends Node {

    protected StaticContext _staticContext;

    protected Expression(ExceptionMetadata metadata) {
        super(metadata);
    }

    public StaticContext getStaticContext() {
        return _staticContext;
    }

    public void setStaticContext(StaticContext _staticContext) {
        this._staticContext = _staticContext;
    }
}
