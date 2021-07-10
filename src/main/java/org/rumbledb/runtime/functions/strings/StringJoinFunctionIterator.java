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

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import java.util.List;

public class StringJoinFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public StringJoinFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item joinString = ItemFactory.getInstance().createStringItem("");
        if (this.children.size() > 1) {
            joinString = this.children.get(1).materializeFirstItemOrNull(context);
        }
        if (!this.children.get(0).isRDDOrDataFrame()) {
            List<Item> strings = this.children.get(0).materialize(context);


            StringBuilder stringBuilder = new StringBuilder();
            for (Item item : strings) {
                if (!(item.isString())) {
                    throw new UnexpectedTypeException("String item expected", this.children.get(0).getMetadata());
                }
                if (!stringBuilder.toString().isEmpty()) {
                    stringBuilder.append(joinString.getStringValue());
                }
                stringBuilder.append(item.getStringValue());
            }
            return ItemFactory.getInstance().createStringItem(stringBuilder.toString());
        }
        JavaRDD<Item> strings = this.children.get(0).getRDD(context);
        StringBuilder stringBuilder = new StringBuilder();
        //Item finalJoinString = joinString;
        //strings.map(item -> stringBuilder.append(constructor(stringBuilder, item, finalJoinString)));
        for (Item item : strings.collect()) {
            if (!(item.isString())) {
                throw new UnexpectedTypeException("String item expected", this.children.get(0).getMetadata());
            }
            if (!stringBuilder.toString().isEmpty()) {
                stringBuilder.append(joinString.getStringValue());
            }
            stringBuilder.append(item.getStringValue());
        }
        return ItemFactory.getInstance().createStringItem(stringBuilder.toString());
    }


    private String constructor(StringBuilder stringBuilder, Item item, Item joinString) {
        if (!(item.isString())) {
            throw new UnexpectedTypeException("String item expected", this.children.get(0).getMetadata());
        }
        if (!stringBuilder.toString().isEmpty()) {
            return joinString.getStringValue().concat(item.getStringValue());
        }
        return item.getStringValue();
    }

}
