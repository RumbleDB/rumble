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

package org.rumbledb.runtime;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;

public class ConstantRDDRuntimeIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private JavaRDD<Item> items;

    public ConstantRDDRuntimeIterator(
            JavaRDD<Item> items,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, ExecutionMode.RDD, iteratorMetadata);
        this.items = items;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        return this.items;
    }

    @Override
    protected void openLocal() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void closeLocal() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void resetLocal() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean hasNextLocal() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected Item nextLocal() {
        // TODO Auto-generated method stub
        return null;
    }
}
