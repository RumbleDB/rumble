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

import org.rumbledb.context.Name;

/** Converts expanded XML names between Rumble and SAX representations. */
final class XmlNameCodec {

    private XmlNameCodec() {}

    static Name fromSax(String namespace, String localName, String qualifiedName) {
        String normalizedNamespace = emptyToNull(namespace);
        if (normalizedNamespace == null) {
            return new Name(null, null, localName);
        }
        int colon = qualifiedName == null ? -1 : qualifiedName.indexOf(':');
        String prefix = colon < 0 ? null : qualifiedName.substring(0, colon);
        return new Name(normalizedNamespace, prefix, localName);
    }

    static Name fromExpandedName(String namespace, String prefix, String localName) {
        String normalizedNamespace = emptyToNull(namespace);
        return normalizedNamespace == null
                ? new Name(null, null, localName)
                : new Name(normalizedNamespace, emptyToNull(prefix), localName);
    }

    static String namespaceUri(Name name) {
        return name.getNamespace() == null ? "" : name.getNamespace();
    }

    static String qualifiedName(Name name) {
        String prefix = name.getPrefix();
        return prefix == null || prefix.isEmpty() ? name.getLocalName() : prefix + ":" + name.getLocalName();
    }

    static String normalizeNamespace(String namespace) {
        return namespace == null ? "" : namespace;
    }

    private static String emptyToNull(String value) {
        return value == null || value.isEmpty() ? null : value;
    }
}
