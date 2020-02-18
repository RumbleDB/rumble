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

package org.rumbledb.runtime.quantifiers;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.SequenceType;

public class QuantifiedExpressionVarIterator extends LocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private final String variableReference;
    // private final SequenceType sequenceType;
    private RuntimeIterator iterator;
    private Item nextResult;

    /*
     * private List<Item> result = null;
     * private int currentResultIndex;
     */

    public QuantifiedExpressionVarIterator(
            String variableReference,
            SequenceType sequenceType,
            RuntimeIterator expression,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        this.children.add(expression);
        this.variableReference = variableReference;
        // this.sequenceType = sequenceType;
    }

    public String getVariableReference() {
        return this.variableReference;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        this.iterator = this.children.get(0);
        this.iterator.open(this.currentDynamicContextForLocalExecution);

        setNextResult();
    }

    @Override
    public Item next() {
        if (this.hasNext == true) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in QuantifiedExpressionVar", getMetadata());
    }

    public void setNextResult() {
        this.nextResult = null;

        while (this.iterator.hasNext()) {
            this.nextResult = this.iterator.next();
            break;
        }


        if (this.nextResult == null) {
            this.hasNext = false;
            this.iterator.close();
        } else {
            this.hasNext = true;
        }
    }
}
