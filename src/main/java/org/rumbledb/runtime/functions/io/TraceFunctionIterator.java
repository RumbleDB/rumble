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

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import java.io.Serial;
import java.net.URI;
import java.util.Collections;
import java.util.List;

public class TraceFunctionIterator extends LocalFunctionCallIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public TraceFunctionIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new TraceLocalCursor(
                this.getChild(0),
                this.getChildren().size() == 2 ? this.getChild(1) : null,
                context,
                getMetadata()
        );
    }

    private static void writeTrace(
            Item result,
            String label,
            int position,
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        RumbleConfiguration configuration = context.getRumbleConfiguration();
        if (configuration == null || configuration.output().logPath() == null) {
            return;
        }
        URI uri = FileSystemUtil.resolveURIAgainstWorkingDirectory(
            configuration.output().logPath(),
            metadata
        );
        FileSystemUtil.append(
            uri,
            Collections.singletonList(label + " [" + position + "]: " + result.serialize()),
            metadata
        );
    }

    private static final class TraceLocalCursor extends AbstractLocalCursor<Item> {

        private final ItemRuntimePlan valuePlan;
        private final ItemRuntimePlan labelPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private Cursor<Item> valueCursor;
        private String label;
        private int position;

        private TraceLocalCursor(
                ItemRuntimePlan valuePlan,
                ItemRuntimePlan labelPlan,
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
                : this.labelPlan.materializeFirstOrNull(this.context).getStringValue();
            this.position = 0;
            this.valueCursor = this.valuePlan.getCursor(this.context);
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
