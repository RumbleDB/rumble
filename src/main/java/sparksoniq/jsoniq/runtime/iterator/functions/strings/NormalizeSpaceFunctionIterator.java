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

import org.apache.commons.lang.StringUtils;
import org.rumbledb.api.Item;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.util.List;

public class NormalizeSpaceFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;

    public NormalizeSpaceFunctionIterator(
            List<RuntimeIterator> arguments,
            IteratorMetadata iteratorMetadata
    ) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            this._hasNext = false;

            StringItem stringItem = this.getSingleItemOfTypeFromIterator(
                this._children.get(0),
                StringItem.class
            );

            if (stringItem == null) {
                return ItemFactory.getInstance().createStringItem("");
            }

            return ItemFactory.getInstance().createStringItem(StringUtils.normalizeSpace(stringItem.getStringValue()));
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " normalize-space function",
                    getMetadata()
            );
    }
}
