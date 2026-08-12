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

import java.util.Objects;
import java.util.Optional;

import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSTypeDefinition;

import org.rumbledb.context.Name;

/** The Xerces XML Schema component model available to one XQuery module. */
public final class XmlSchemaCatalog {

    private final XSModel schemaModel;

    XmlSchemaCatalog(XSModel schemaModel) {
        this.schemaModel = Objects.requireNonNull(schemaModel, "schemaModel must not be null");
    }

    public Optional<XSTypeDefinition> getTypeDefinition(Name name) {
        Objects.requireNonNull(name, "name must not be null");
        return Optional.ofNullable(
                this.schemaModel.getTypeDefinition(name.getLocalName(), emptyToNull(name.getNamespace())));
    }

    public boolean containsNamespace(String namespace) {
        return this.schemaModel.getNamespaces().contains(emptyToNull(namespace));
    }

    XSModel getSchemaModel() {
        return this.schemaModel;
    }

    private static String emptyToNull(String value) {
        return value == null || value.isEmpty() ? null : value;
    }
}
