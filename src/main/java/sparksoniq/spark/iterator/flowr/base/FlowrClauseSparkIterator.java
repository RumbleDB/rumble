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
package sparksoniq.spark.iterator.flowr.base;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.io.json.JiqsItemParser;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FLWOR_CLAUSES;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.SparkContextManager;
import sparksoniq.spark.tuple.FlworTuple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static sparksoniq.spark.SparkContextManager.APP_NAME;

public abstract class FlowrClauseSparkIterator implements Serializable {


    protected final JiqsItemParser _parser;
    private final IteratorMetadata metadata;
    private final FLWOR_CLAUSES _clauseType;
    protected FlowrClauseSparkIterator _previousClause = null;
    protected JavaRDD<FlworTuple> _rdd;
    protected DynamicContext _currentDynamicContext;
    protected List<RuntimeIterator> _children;

    protected FlowrClauseSparkIterator(List<RuntimeIterator> children, FLWOR_CLAUSES clauseType,
                                       IteratorMetadata iteratorMetadata) {
        this.metadata = iteratorMetadata;
        this._children = new ArrayList<>();
        if (children != null && !children.isEmpty())
            this._children.addAll(children);
        this._clauseType = clauseType;
        this._parser = new JiqsItemParser();
    }

    public abstract JavaRDD<FlworTuple> getTupleRDD();

    public FLWOR_CLAUSES getClauseType() {
        return _clauseType;
    }

    public IteratorMetadata getMetadata() {
        return metadata;
    }

    public void setPreviousClause(FlowrClauseSparkIterator previousClause) {
        this._previousClause = previousClause;
    }

    public void setDynamicContext(DynamicContext context) {
        this._currentDynamicContext = context;
    }

    protected JavaSparkContext getCurrentContext() {
        try {
            return SparkContextManager.getInstance().getContext();
        } catch (SparksoniqRuntimeException ex) {
            return new JavaSparkContext(new SparkConf().setAppName(APP_NAME).setMaster("local[*]"));
        }
    }
}
