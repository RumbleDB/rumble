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
 */

package org.rumbledb.runtime.primary;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayList;
import java.util.List;

public class DirElemConstructorRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private String tagName;
    private List<RuntimeIterator> children;
    private List<RuntimeIterator> attributes;

    public DirElemConstructorRuntimeIterator(
            String tagName,
            List<RuntimeIterator> children,
            List<RuntimeIterator> attributes,
            RuntimeStaticContext staticContext
    ) {
        super(children, staticContext); // TODO: is this correct? or should we pass children and attributes?
        this.children = children;
        this.attributes = attributes;
        this.tagName = tagName;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        List<Item> content = new ArrayList<>();
        List<Item> attributes = new ArrayList<>();
        // Process all child content
        if (this.children != null) {
                for (RuntimeIterator iterator : this.children) {
                    iterator.open(dynamicContext);
                while (iterator.hasNext()) {
                    content.add(iterator.next());
                }
                iterator.close();
            }
        }
        
        // Create and return the element item
        this.hasNext = false;
        return ItemFactory.getInstance().createXmlElementNode(
                this.tagName,
                content,    
                attributes
        );
    }
} 