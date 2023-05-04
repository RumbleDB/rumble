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
 * Authors: Ghislain Fourny
 *
 */


package org.rumbledb.runtime.functions.io;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import java.net.URI;
import java.util.Collections;
import java.util.List;

public class TraceFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator valueIterator;
    private RuntimeIterator labelIterator;
    private String label;
    private int position = 0;

    public TraceFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.position = 0;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.valueIterator = this.children.get(0);
        this.labelIterator = this.children.get(1);
        this.label = this.labelIterator.materializeFirstItemOrNull(context).getStringValue();
        this.valueIterator.open(context);
        this.hasNext = this.valueIterator.hasNext();
        this.position = 0;
    }

    @Override
    public void reset(DynamicContext context) {
        super.open(context);
        this.label = this.labelIterator.materializeFirstItemOrNull(context).getStringValue();
        this.valueIterator.reset(context);
        this.hasNext = this.valueIterator.hasNext();
        this.position = 0;
    }


    @Override
    public void close() {
        super.close();
        this.valueIterator.close();
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            Item result = this.valueIterator.next();
            RumbleRuntimeConfiguration conf = this.currentDynamicContextForLocalExecution
                .getRumbleRuntimeConfiguration();
            if (conf != null) {
                String path = conf.getLogPath();
                if (path != null) {
                    URI uri = FileSystemUtil.resolveURIAgainstWorkingDirectory(
                        path,
                        this.currentDynamicContextForLocalExecution.getRumbleRuntimeConfiguration(),
                        getMetadata()
                    );
                    FileSystemUtil.append(
                        uri,
                        Collections.singletonList(this.label + " [" + (++this.position) + "]: " + result.serialize()),
                        this.currentDynamicContextForLocalExecution.getRumbleRuntimeConfiguration(),
                        getMetadata()
                    );
                }
            }
            this.hasNext = this.valueIterator.hasNext();
            return result;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " trace function", getMetadata());
    }


}
