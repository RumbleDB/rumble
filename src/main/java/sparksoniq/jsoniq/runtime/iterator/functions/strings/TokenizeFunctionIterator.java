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

package sparksoniq.jsoniq.runtime.iterator.functions.strings;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

import org.rumbledb.api.Item;

public class TokenizeFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private String[] _results;
    private Item _nextResult;
    private int _currentPosition;
    private boolean _lastEmptyString;

    public TokenizeFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (_nextResult != null) {
            Item result = _nextResult;
            setNextResult();
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "tokenize function", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        _results = null;
        _currentPosition = -1;
        setNextResult();
    }

    public void setNextResult() {
        if (_results == null) {
            // Getting first parameter
            RuntimeIterator stringIterator = _children.get(0);
            stringIterator.open(_currentDynamicContext);
            if (!stringIterator.hasNext()) {
                _hasNext = false;
                stringIterator.close();
                return;
            }
            String input = null;
            String separator = null;
            Item stringItem = stringIterator.next();
            if (stringIterator.hasNext())
                throw new UnexpectedTypeException(
                        "First parameter of tokenize must be a string or the empty sequence.",
                        getMetadata()
                );
            stringIterator.close();
            if (!stringItem.isString())
                throw new UnexpectedTypeException(
                        "First parameter of tokenize must be a string or the empty sequence.",
                        getMetadata()
                );
            try {
                input = stringItem.getStringValue();
                stringIterator.close();
            } catch (Exception e) {
                throw new UnexpectedTypeException(
                        "First parameter of tokenize must be a string or the empty sequence.",
                        getMetadata()
                );
            }

            // Getting second parameter
            if (_children.size() == 1) {
                separator = "\\s+";
            } else {
                RuntimeIterator separatorIterator = _children.get(1);
                separatorIterator.open(_currentDynamicContext);
                if (!separatorIterator.hasNext()) {
                    throw new UnexpectedTypeException("Second parameter of tokenize must be a string.", getMetadata());
                }
                stringItem = separatorIterator.next();
                if (separatorIterator.hasNext())
                    throw new UnexpectedTypeException("Second parameter of tokenize must be a string.", getMetadata());
                separatorIterator.close();
                if (!stringItem.isString())
                    throw new UnexpectedTypeException("Second parameter of tokenize must be a string.", getMetadata());
                try {
                    separator = stringItem.getStringValue();
                } catch (Exception e) {
                    throw new UnexpectedTypeException("Second parameter of tokenize must be a string.", getMetadata());
                }
            }
            _results = input.split(separator);
            _currentPosition = 0;
            if (_children.size() == 1 && _results.length != 0 && _results[0].equals("")) {
                _currentPosition++;
            }
            if (_children.size() == 2 && input.matches(".*" + separator + "$")) {
                _lastEmptyString = true;
            } else {
                _lastEmptyString = false;
            }
        }
        if (_currentPosition < _results.length) {
            _nextResult = ItemFactory.getInstance().createStringItem(_results[_currentPosition]);
            _currentPosition++;
            _hasNext = true;
        } else if (_lastEmptyString) {
            _nextResult = ItemFactory.getInstance().createStringItem(new String(""));
            _hasNext = true;
            _lastEmptyString = false;
        } else {
            _hasNext = false;
        }
    }
}
