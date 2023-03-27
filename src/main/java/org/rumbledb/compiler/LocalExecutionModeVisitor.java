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

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.StaticContext;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.Node;

/**
 * Static context visitor implements a multi-pass algorithm that enables function hoisting
 */
public class LocalExecutionModeVisitor extends AbstractNodeVisitor<StaticContext> {

	LocalExecutionModeVisitor(RumbleRuntimeConfiguration configuration) {
    }

    void setVisitorConfig(VisitorConfig visitorConfig) {
    }

    @Override
    protected StaticContext defaultAction(Node node, StaticContext argument) {
        visitDescendants(node, argument);
        node.setHighestExecutionMode(ExecutionMode.LOCAL);
        return argument;
    }
}
