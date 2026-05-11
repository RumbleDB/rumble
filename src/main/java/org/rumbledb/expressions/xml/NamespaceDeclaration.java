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
 * Authors: Matteo Agnoletto (EPMatt)
 *
 */
package org.rumbledb.expressions.xml;

import org.rumbledb.exceptions.ExceptionMetadata;

/**
 * Namespace declaration in a direct element constructor start tag.
 *
 * This is intentionally not an attribute node expression: namespace declarations
 * contribute namespace nodes and static namespace bindings, but do not construct
 * attribute nodes.
 */
public class NamespaceDeclaration {

    private final String prefix;
    private final String uri;
    private final ExceptionMetadata metadata;

    public NamespaceDeclaration(String prefix, String uri, ExceptionMetadata metadata) {
        this.prefix = prefix;
        this.uri = uri;
        this.metadata = metadata;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getUri() {
        return this.uri;
    }

    public ExceptionMetadata getMetadata() {
        return this.metadata;
    }

    public void serializeToJSONiq(StringBuffer sb) {
        if (this.prefix == null || this.prefix.isEmpty()) {
            sb.append("xmlns=\"");
        } else {
            sb.append("xmlns:");
            sb.append(this.prefix);
            sb.append("=\"");
        }
        sb.append(this.uri);
        sb.append("\"");
    }
}
