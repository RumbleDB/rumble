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
import sparksoniq.jsoniq.ExecutionMode;

import java.util.ArrayList;
import java.util.List;

public class DynamicFunctionCallExtension extends PostfixExtension {

    private List<Expression> _arguments;

    public DynamicFunctionCallExtension(List<Expression> arguments, ExceptionMetadata metadata) {
        super(metadata);
        this._arguments = arguments;
    }

    public List<Expression> getArguments() {
        return this._arguments;
    }

    @Override
    public List<Node> getDescendants(boolean depthSearch) {
        List<Node> result = new ArrayList<>();
        if (this._arguments != null)
            result.addAll(this._arguments);
        return getDescendantsFromChildren(result, depthSearch);
    }

    /**
     * DynamicFunctionCall is always locally evaluated as execution mode cannot be determined at static analysis phase.
     * This behavior is different from all other postfix extensions, hence this override is required.
     */

    @Override
    public void initHighestExecutionMode() {
        this.highestExecutionMode = ExecutionMode.LOCAL;
    }

    @Override
    public String serializationString(boolean prefix) {
        StringBuilder result = new StringBuilder("(argumentList ( ");
        for (Expression arg : this._arguments) {
            result.append("(argument (exprSingle ");
            result.append(arg.serializationString(false));
            result.append((this._arguments.indexOf(arg) < this._arguments.size() - 1 ? ")) , " : ")) "));
        }
        result.append("))");
        return result.toString();
    }
}
