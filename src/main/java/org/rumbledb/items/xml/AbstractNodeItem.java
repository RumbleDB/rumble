/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
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
 */

package org.rumbledb.items.xml;

import java.io.Serial;

import org.rumbledb.api.Item;

/** Centralizes XDM node identity for Java collections. */
public abstract class AbstractNodeItem implements Item {
    @Serial private static final long serialVersionUID = 1L;

    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Item otherItem) || !otherItem.isNode()) {
            return false;
        }

        // Note: we do not check if `this` and `other` has the same Java class
        // Because XMLDocumentPosition should uniquely identify a node across all node kinds
        XMLDocumentPosition position = getXmlDocumentPosition();
        XMLDocumentPosition otherPosition = otherItem.getXmlDocumentPosition();
        return position != null && position.equals(otherPosition);
    }

    @Override
    public final int hashCode() {
        XMLDocumentPosition position = getXmlDocumentPosition();
        return position == null ? System.identityHashCode(this) : position.hashCode();
    }
}
