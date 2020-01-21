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

package sparksoniq.jsoniq.compiler.translator.expr;

import sparksoniq.exceptions.OurBadException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * abstract class, base for all AST nodes
 */
public abstract class ExpressionOrClause {

    private ExpressionMetadata metadata;

    protected ExecutionMode _highestExecutionMode = ExecutionMode.UNSET;

    protected ExpressionOrClause() {
    }

    protected ExpressionOrClause(ExpressionMetadata metadata) {
        this.metadata = metadata;
    }

    /**
     * initHighestExecutionMode is meant to be overridden by expression subclasses which support higher execution modes
     */
    public void initHighestExecutionMode() {
        _highestExecutionMode = ExecutionMode.LOCAL;
    }

    /**
     * When extending this method, make sure to perform a super() call to prevent UNSET accesses.
     * 
     * @return
     */
    public ExecutionMode getHighestExecutionMode() {
        if (_highestExecutionMode == ExecutionMode.UNSET) {
            throw new OurBadException("An execution mode is access without being set.");
        }
        return _highestExecutionMode;
    }

    // Visitor pattern implementation
    public abstract List<ExpressionOrClause> getDescendants(boolean depthSearch);

    /**
     * Accept method for the pattern Visitor.
     */
    public abstract <T> T accept(AbstractExpressionOrClauseVisitor<T> visitor, T argument);

    public abstract String serializationString(boolean prefix);

    public final List<ExpressionOrClause> getDescendants() {
        return getDescendants(false);
    }

    public List<ExpressionOrClause> getDescendantsOfType(Predicate<ExpressionOrClause> predicate, boolean depthSearch) {
        List<ExpressionOrClause> result = this.getDescendants(depthSearch);
        List<ExpressionOrClause> filter = new ArrayList<>();
        result.stream().filter(predicate).forEach(r -> filter.add(r));
        return filter;
    }

    public ExpressionMetadata getMetadata() {
        return metadata;
    }

    protected List<ExpressionOrClause> getDescendantsFromChildren(
            List<ExpressionOrClause> result,
            boolean depthSearch
    ) {
        if (depthSearch) {
            List<ExpressionOrClause> childrenResults = new ArrayList<>();
            result.forEach(r -> childrenResults.addAll(r.getDescendants(depthSearch)));
            result.addAll(childrenResults);
        }
        return result;
    }

}
