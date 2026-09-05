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

package org.rumbledb.xml.schema;

import java.util.Optional;

import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;

import org.rumbledb.context.Name;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

/** Maps Xerces definitions for built-in atomic XML Schema types to Rumble item types. */
final class XercesBuiltinAtomicTypeMapper {

    Optional<ItemType> map(XSTypeDefinition schemaType) {
        if (!(schemaType instanceof XSSimpleTypeDefinition simpleType)) {
            return Optional.empty();
        }
        if (simpleType.getVariety() != XSSimpleTypeDefinition.VARIETY_ATOMIC) {
            return Optional.empty();
        }
        if (!Name.XS_NS.equals(simpleType.getNamespace()) || simpleType.getName() == null) {
            return Optional.empty();
        }

        Name typeName = new Name(Name.XS_NS, "xs", simpleType.getName());
        if (!BuiltinTypesCatalogue.typeExists(typeName)) {
            return Optional.empty();
        }
        ItemType result = BuiltinTypesCatalogue.getItemTypeByName(typeName);
        return result.isAtomicItemType() ? Optional.of(result) : Optional.empty();
    }
}
