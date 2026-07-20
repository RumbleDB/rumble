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
 * Authors: OpenAI Codex
 *
 */

package org.rumbledb.runtime.xml;

import org.rumbledb.api.Item;

import java.util.ArrayList;
import java.util.List;

final class XmlConstructorContentUtils {

    private XmlConstructorContentUtils() {
    }

    static List<Item> expandArrayItems(List<Item> items) {
        List<Item> result = new ArrayList<>();
        for (Item item : items) {
            appendExpandedItem(item, result);
        }
        return result;
    }

    static void appendExpandedItem(Item item, List<Item> result) {
        if (item.isDocumentNode()) {
            for (Item child : item.children()) {
                appendExpandedItem(child, result);
            }
            return;
        }
        if (!item.isArray()) {
            result.add(item);
            return;
        }
        if (item.isArrayOfItems()) {
            for (Item member : item.getItemMembers()) {
                appendExpandedItem(member, result);
            }
            return;
        }
        for (List<Item> memberSequence : item.getSequenceMembers()) {
            for (Item member : memberSequence) {
                appendExpandedItem(member, result);
            }
        }
    }
}
