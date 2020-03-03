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

import com.jsoniter.JsonIterator;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.DynamicContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class JsonDocFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;

    public JsonDocFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.iterator = this.children.get(0);
        this.iterator.open(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.iterator.hasNext();
        this.iterator.close();
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            Item path = this.iterator.materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
            try {
                File f = new File(path.getStringValue());
                FileInputStream fis = new FileInputStream(f);
                JsonIterator object = JsonIterator.parse(fis, 1024);
                return ItemParser.getItemFromObject(object, getMetadata());
            } catch (IteratorFlowException e) {
                throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
            } catch (FileNotFoundException e) {
                throw new CannotRetrieveResourceException(
                        "File " + path.getStringValue() + " not found.",
                        getMetadata()
                );
            }
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " json-doc function", getMetadata());
    }


}
