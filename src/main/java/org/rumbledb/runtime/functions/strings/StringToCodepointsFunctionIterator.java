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

package org.rumbledb.runtime.functions.strings;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.util.List;

public class StringToCodepointsFunctionIterator extends LocalFunctionCallIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public StringToCodepointsFunctionIterator(
            List<RuntimePlan<Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new StringToCodepointsLocalCursor(
                this.getChild(0),
                context,
                getMetadata()
        );
    }

    private static final class StringToCodepointsLocalCursor extends AbstractLocalCursor<Item> {

        private final RuntimePlan<Item> argumentPlan;
        private final DynamicContext context;
        private String input;
        private int position;

        private StringToCodepointsLocalCursor(
                RuntimePlan<Item> argumentPlan,
                DynamicContext context,
                ExceptionMetadata metadata
        ) {
            super(metadata);
            this.argumentPlan = argumentPlan;
            this.context = context;
        }

        @Override
        protected void openLocal() {
            Item argument = this.argumentPlan.materializeFirstOrNull(this.context);
            this.input = argument == null ? null : argument.getStringValue();
            this.position = 0;
        }

        @Override
        protected boolean hasNextLocal() {
            return this.input != null && this.position < this.input.length();
        }

        @Override
        protected Item nextLocal() {
            if (!hasNextLocal()) {
                throw invalidState("String-to-codepoints cursor is exhausted.");
            }
            int codepoint = this.input.codePointAt(this.position);
            this.position = this.input.offsetByCodePoints(this.position, 1);
            return ItemFactory.getInstance().createIntItem(codepoint);
        }

        @Override
        protected void closeLocal() {
            this.input = null;
        }
    }
}
