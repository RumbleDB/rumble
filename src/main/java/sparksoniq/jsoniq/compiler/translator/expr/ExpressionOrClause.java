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

import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.jsoniq.runtime.iterator.DataFrameRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RDDRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.BuiltinFunction;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionIdentifier;
import sparksoniq.jsoniq.runtime.iterator.functions.base.Functions;
import sparksoniq.semantics.visitor.AbstractExpressionOrClauseVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * abstract class, base for all AST nodes
 */
public abstract class ExpressionOrClause {

    private ExpressionMetadata metadata;
    protected boolean isRDD;
    protected boolean isDataFrame;
    private boolean isRDDInitialized = false;

    protected ExpressionOrClause() {
    }

    protected ExpressionOrClause(ExpressionMetadata metadata) {
        this.metadata = metadata;
    }

    public void setIsRDD(boolean isRDD) {
        this.isRDD = isRDD;
    }

    public void setIsDataFrame(boolean isDataFrame) {
        this.isDataFrame = isDataFrame;
    }

    public boolean isRDD() {
        if (!isRDDInitialized) {
            initIsRDD();
        }
        return this.isRDD;
    }

    public boolean isDataFrame() {
        if (!isRDDInitialized) {
            initIsRDD();
        }
        return this.isDataFrame;
    }

    // TODO: make this method abstract
    protected void initIsRDD() {
        this.isRDD = false;
        this.isDataFrame = false;
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
