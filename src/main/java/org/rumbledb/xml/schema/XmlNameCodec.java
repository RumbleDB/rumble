/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.xml.schema;

import org.rumbledb.context.Name;

/** Converts XML expanded names between Rumble and SAX representations. */
final class XmlNameCodec {

    private XmlNameCodec() {
    }

    static Name fromSax(String namespace, String localName, String qualifiedName) {
        if (namespace == null || namespace.isEmpty()) {
            return new Name(null, null, localName);
        }
        int colon = qualifiedName.indexOf(':');
        String prefix = colon < 0 ? null : qualifiedName.substring(0, colon);
        return new Name(namespace, prefix, localName);
    }

    static Name fromExpandedName(String namespace, String prefix, String localName) {
        return namespace == null || namespace.isEmpty()
            ? new Name(null, null, localName)
            : new Name(namespace, prefix, localName);
    }

    static String namespaceUri(Name name) {
        return normalizeNamespace(name.getNamespace());
    }

    static String qualifiedName(Name name) {
        String prefix = name.getPrefix();
        return prefix == null || prefix.isEmpty() ? name.getLocalName() : prefix + ":" + name.getLocalName();
    }

    static String normalizeNamespace(String namespace) {
        return namespace == null ? "" : namespace;
    }
}
