/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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


package sparksoniq.jsoniq.runtime.iterator.functions.io;

import sparksoniq.exceptions.ErrorRetrievingResourceException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.io.json.JiqsItemParser;
import sparksoniq.io.json.StringToItemMapper;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.rumbledb.api.Item;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;

public class JsonDocFunctionIterator extends LocalFunctionCallIterator {

	private static final long serialVersionUID = 1L;
	private RuntimeIterator _iterator;

    public JsonDocFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        _iterator = this._children.get(0);
        _iterator.open(_currentDynamicContext);
        if (_iterator.hasNext()) {
            this._hasNext = true;
        } else {
            this._hasNext = false;
        }
        _iterator.close();
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;
            Item path = this.getSingleItemOfTypeFromIterator(_iterator, Item.class);
            if (path.isString()) {
                try {
                	File f = new File(path.getStringValue());
                	FileInputStream fis = new FileInputStream(f);
                	JsonIterator object = JsonIterator.parse(fis, 1024);
                    return JiqsItemParser.getItemFromObject (object, getMetadata());
                } catch (IteratorFlowException e) {
                    throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
                } catch (FileNotFoundException e) {
                	throw new ErrorRetrievingResourceException("File " + path.getStringValue() + " not found.", getMetadata());
                }
            } else {
                throw new UnexpectedTypeException("json-doc function has non-strong arg " +
                		path.serialize(), getMetadata());
            }

        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " json-doc function", getMetadata());
    }


}
