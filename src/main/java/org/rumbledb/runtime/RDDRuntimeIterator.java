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
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;

import java.util.List;

public abstract class RDDRuntimeIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    protected RDDRuntimeIterator(
            List<RuntimeIterator> children,
            RuntimeStaticContext staticContext
    ) {
        super(children, staticContext);
    }

    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("RDDs are not implemented for the iterator", getMetadata());
    }

    @Override
    protected boolean implementsLocal() {
        return false;
    }

    @Override
    protected void openLocal() {
        throw new OurBadException("Local evaluation are not implemented for the iterator", getMetadata());
    }

    @Override
    protected void closeLocal() {
        throw new OurBadException("Local evaluation are not implemented for the iterator", getMetadata());
    }

    @Override
    protected void resetLocal() {
        throw new OurBadException("Local evaluation are not implemented for the iterator", getMetadata());
    }

    @Override
    protected boolean hasNextLocal() {
        throw new OurBadException("Local evaluation are not implemented for the iterator", getMetadata());
    }

    @Override
    protected Item nextLocal() {
        throw new OurBadException("Local evaluation are not implemented for the iterator", getMetadata());
    }
}
