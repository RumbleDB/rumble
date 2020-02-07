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

package sparksoniq.jsoniq.runtime.iterator;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class CommaExpressionIterator extends LocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator currentChild;
    private Item nextResult;
    private int childIndex;

    public CommaExpressionIterator(
            List<RuntimeIterator> childIterators,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(childIterators, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in Comma expression", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.childIndex = 0;

        this.currentChild = this.children.get(this.childIndex);
        this.currentChild.open(this.currentDynamicContextForLocalExecution);

        setNextResult();
    }

    public void setNextResult() {
        this.nextResult = null;

        while (this.nextResult == null) {
            if (this.currentChild.hasNext()) {
                this.nextResult = this.currentChild.next();
            } else {
                this.currentChild.close();
                if (++this.childIndex == this.children.size()) {
                    break;
                }
                this.currentChild = this.children.get(this.childIndex);
                this.currentChild.open(this.currentDynamicContextForLocalExecution);
            }
        }

        this.hasNext = this.nextResult != null;
    }
}
