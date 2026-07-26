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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import java.io.Serial;
import java.net.URI;
import java.util.Collections;
import java.util.List;

public class TraceFunctionIterator extends LocalFunctionCallIterator {

    @Serial
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
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new TraceLocalCursor(
                this.getChild(0),
                this.getChildren().size() == 2 ? this.getChild(1) : null,
                context,
                getMetadata()
        );
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.valueIterator = this.getChild(0);
        if (this.getChildren().size() == 2) {
            this.labelIterator = this.getChild(1);
            this.label = this.labelIterator.materializeFirstItemOrNull(context).getStringValue();
        } else {
            this.label = "";
        }
        this.valueIterator.open(context);
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
            writeTrace(
                result,
                this.label,
                ++this.position,
                this.currentDynamicContextForLocalExecution,
                getMetadata()
            );
            this.hasNext = this.valueIterator.hasNext();
            return result;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " trace function", getMetadata());
    }

    private static void writeTrace(
            Item result,
            String label,
            int position,
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        RumbleRuntimeConfiguration configuration = context.getRumbleRuntimeConfiguration();
        if (configuration == null || configuration.getLogPath() == null) {
            return;
        }
        URI uri = FileSystemUtil.resolveURIAgainstWorkingDirectory(
            configuration.getLogPath(),
            configuration,
            metadata
        );
        FileSystemUtil.append(
            uri,
            Collections.singletonList(label + " [" + position + "]: " + result.serialize()),
            configuration,
            metadata
        );
    }

    private static final class TraceLocalCursor extends AbstractLocalCursor<Item> {

        private final RuntimeIterator valuePlan;
        private final RuntimeIterator labelPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private LocalCursor<Item> valueCursor;
        private String label;
        private int position;

        private TraceLocalCursor(
                RuntimeIterator valuePlan,
                RuntimeIterator labelPlan,
                DynamicContext context,
                ExceptionMetadata metadata
        ) {
            super(metadata);
            this.valuePlan = valuePlan;
            this.labelPlan = labelPlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            this.label = this.labelPlan == null
                ? ""
                : LocalCursorUtils.materializeFirst(this.labelPlan, this.context).getStringValue();
            this.position = 0;
            this.valueCursor = this.valuePlan.createLocalCursor(this.context);
        }

        @Override
        protected boolean hasNextLocal() {
            return this.valueCursor.hasNext();
        }

        @Override
        protected Item nextLocal() {
            if (!this.valueCursor.hasNext()) {
                throw invalidState("No more trace results are available.");
            }
            Item result = this.valueCursor.next();
            writeTrace(result, this.label, ++this.position, this.context, this.metadata);
            return result;
        }

        @Override
        protected void closeLocal() {
            if (this.valueCursor != null) {
                this.valueCursor.close();
                this.valueCursor = null;
            }
            this.label = null;
            this.position = 0;
        }
    }
}
