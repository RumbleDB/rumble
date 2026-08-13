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
 */

package org.rumbledb.expressions.xml.node_test;

import java.io.Serial;

import lombok.Getter;

import org.rumbledb.types.SchemaElementNodeItemType;

/** A declaration-aware {@code schema-element(E)} axis test. */
@Getter
public final class SchemaElementTest implements NodeTest {

    @Serial
    private static final long serialVersionUID = 1L;

    private final SchemaElementNodeItemType itemType;

    public SchemaElementTest(SchemaElementNodeItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public String toString() {
        return this.itemType.toString();
    }
}
