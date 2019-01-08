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

import sparksoniq.io.json.JiqsItemParser;
import sparksoniq.jsoniq.compiler.translator.expr.flowr.FLWOR_CLAUSES;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.jsoniq.runtime.tupleiterator.RuntimeTupleIterator;
import sparksoniq.jsoniq.runtime.tupleiterator.SparkRuntimeTupleIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.jsoniq.tuple.FlworTuple;
import org.apache.spark.api.java.JavaRDD;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class FlowrClauseSparkIterator extends SparkRuntimeTupleIterator implements Serializable {


    public FLWOR_CLAUSES getClauseType() { return _clauseType; }

    public IteratorMetadata getMetadata() { return metadata; }

//    public void setPreviousClause(FlowrClauseSparkIterator previousClause) {
//        this._previousClause = previousClause;
//    }

    public void setDynamicContext(DynamicContext context){
        this._currentDynamicContext = context;
        if(_child != null)
        {
            ((FlowrClauseSparkIterator)_child).setDynamicContext(context);
        }
    }

    protected FlowrClauseSparkIterator(
            RuntimeTupleIterator child,
            List<RuntimeIterator> children,
            FLWOR_CLAUSES clauseType,
            IteratorMetadata iteratorMetadata) {
        super(child, iteratorMetadata);
        this.metadata = iteratorMetadata;
        this._children = new ArrayList<>();
        if(children!=null && !children.isEmpty())   
            this._children.addAll(children);
        _child = child;
        this._clauseType = clauseType;
        this._parser = new JiqsItemParser();
    }

    private final IteratorMetadata metadata;
    private final FLWOR_CLAUSES _clauseType;
    //protected FlowrClauseSparkIterator _previousClause = null;
    protected JavaRDD<FlworTuple> _rdd;
    protected final JiqsItemParser _parser;
    protected DynamicContext _currentDynamicContext;
    protected List<RuntimeIterator> _children;


}
