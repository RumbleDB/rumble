/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.context;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidSchemaException;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.xml.schema.XmlSchemaCatalog;

public class InScopeSchemaTypes implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final HashMap<Name, ItemType> inScopeSchemaTypes;

    // Xerces definitions stay local; the mapped ItemTypes remain serializable.
    @Getter
    private transient XmlSchemaCatalog xmlSchemaCatalog;

    public InScopeSchemaTypes() {
        this.inScopeSchemaTypes = new HashMap<>();
    }

    /** Installs the catalog for all of a module's schema imports and registers its representable item types. */
    public void importSchema(@NonNull XmlSchemaCatalog catalog, ExceptionMetadata metadata) {
        if (this.xmlSchemaCatalog != null) {
            throw new IllegalStateException("The module's XML Schema catalog has already been imported.");
        }

        // Check every declaration before changing either the registry or the catalog.
        List<ItemType> importedTypes = catalog.getNamedGeneralizedAtomicItemTypes();

        for (ItemType itemType : importedTypes) {
            this.addInScopeSchemaType(itemType, metadata);
        }

        this.xmlSchemaCatalog = catalog;
    }

    public void addInScopeSchemaType(ItemType type, ExceptionMetadata meta) {
        validateNewSchemaType(type, meta);
        this.inScopeSchemaTypes.put(type.getName(), type);
    }

    private void validateNewSchemaType(ItemType type, ExceptionMetadata meta) {
        if (!type.hasName()) {
            throw new InvalidSchemaException("A top-level user-defined type must have a name.", meta);
        }
        if (BuiltinTypesCatalogue.typeExists(type.getName()) || this.inScopeSchemaTypes.containsKey(type.getName())) {
            throw new InvalidSchemaException("This type is already defined: " + type.getName(), meta);
        }
    }

    public boolean checkInScopeSchemaTypeExists(Name name) {
        if (BuiltinTypesCatalogue.typeExists(name)) {
            return true;
        }
        return this.inScopeSchemaTypes.containsKey(name);
    }

    public ItemType getInScopeSchemaType(Name name) {
        if (BuiltinTypesCatalogue.typeExists(name)) {
            return BuiltinTypesCatalogue.getItemTypeByName(name);
        }
        return this.inScopeSchemaTypes.get(name);
    }

    public List<ItemType> getInScopeSchemaTypes() {
        return new ArrayList<ItemType>(this.inScopeSchemaTypes.values());
    }

    public void importModuleTypes(InScopeSchemaTypes inScopeSchemaTypes) {
        for (Name name : inScopeSchemaTypes.inScopeSchemaTypes.keySet()) {
            ItemType itemType = inScopeSchemaTypes.inScopeSchemaTypes.get(name);
            this.inScopeSchemaTypes.put(name, itemType);
        }
    }
}
