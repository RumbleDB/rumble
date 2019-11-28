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

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.SparkRuntimeException;
import sparksoniq.io.json.JiqsItemParser;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public abstract class RDDRuntimeIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    protected RDDRuntimeIterator(List<RuntimeIterator> children, IteratorMetadata iteratorMetadata) {
        super(children, iteratorMetadata);
        this.parser = new JiqsItemParser();
    }

    @Override
    protected boolean initIsRDD(DynamicContext context) {
        return true;
    }

    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new SparkRuntimeException("RDDs are not implemented for the iterator", getMetadata());
    }

    @Override
    protected void openLocal() {
        throw new SparkRuntimeException("Local evaluation are not implemented for the iterator", getMetadata());
    }

    @Override
    protected void closeLocal() {
        throw new SparkRuntimeException("Local evaluation are not implemented for the iterator", getMetadata());
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        throw new SparkRuntimeException("Local evaluation are not implemented for the iterator", getMetadata());
    }

    @Override
    protected boolean hasNextLocal() {
        throw new SparkRuntimeException("Local evaluation are not implemented for the iterator", getMetadata());
    }

    @Override
    protected Item nextLocal() {
        throw new SparkRuntimeException("Local evaluation are not implemented for the iterator", getMetadata());
    }
}
