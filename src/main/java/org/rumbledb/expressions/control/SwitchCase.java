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

package org.rumbledb.expressions.control;


import org.rumbledb.expressions.Expression;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a helper class that organizes the children expressions of a switch expression.
 * From a tree perspective, all expressions in this class are considered
 * to be direct children of the SwitchExpression.
 */
public class SwitchCase {

    private final List<Expression> conditionExpressions;
    private final Expression returnExpression;

    public SwitchCase(
            List<Expression> conditionExpressions,
            Expression returnExpression
    ) {
        this.conditionExpressions = conditionExpressions;
        this.returnExpression = returnExpression;
    }

    public List<Expression> getAllExpressions() {
        List<Expression> result = new ArrayList<>();
        result.addAll(this.conditionExpressions);
        result.add(this.returnExpression);
        return result;
    }

    public Expression getReturnExpression() {
        return this.returnExpression;
    }

    public List<Expression> getConditionExpressions() {
        return this.conditionExpressions;
    }

}
