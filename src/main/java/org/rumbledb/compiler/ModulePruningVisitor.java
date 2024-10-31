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

package org.rumbledb.compiler;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.Prolog;


/**
 * This visitor removes duplicate module import, by depopulating variable and function declarations upon
 * duplicate visits.
 * 
 */
public class ModulePruningVisitor extends AbstractNodeVisitor<Void> {

    @SuppressWarnings("unused")
    private RumbleRuntimeConfiguration rumbleRuntimeConfiguration;
    private List<String> visitedModules;

    /**
     * Builds a new visitor.
     * 
     * @param rumbleRuntimeConfiguration the configuration. This is used for trigerring or not debug output.
     */
    ModulePruningVisitor(RumbleRuntimeConfiguration rumbleRuntimeConfiguration) {
        this.rumbleRuntimeConfiguration = rumbleRuntimeConfiguration;
        this.visitedModules = new ArrayList<>();
    }

    @Override
    public Void visitLibraryModule(LibraryModule libraryModule, Void argument) {
        if (this.visitedModules.contains(libraryModule.getNamespace())) {
            Prolog prolog = libraryModule.getProlog();
            prolog.clearDeclarations();
        }
        visitDescendants(libraryModule, argument);
        this.visitedModules.add(libraryModule.getNamespace());
        return argument;
    }

}
