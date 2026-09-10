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
package org.rumbledb.xml.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.xml.validation.Schema;

import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.xs.XSValue;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.xml.XmlSchemaTypeAnnotation;
import org.rumbledb.runtime.xml.NamespaceBindingUtils.NamespaceResolver;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

/**
 * Wrapper around Xerces’s model
 * It can find schema type definitions and map them to RumbleDB ItemTypes or typed values.
 */
public final class XmlSchemaCatalog {

    @Getter(AccessLevel.PACKAGE)
    private final XSModel schemaModel;

    @Getter(AccessLevel.PACKAGE)
    private final Schema validationSchema;

    private final XmlSchemaTypeMapper typeMapper;
    private final XercesTypedValueConverter typedValueConverter;
    private final XercesSimpleTypeCaster simpleTypeCaster;

    XmlSchemaCatalog(@NonNull XSModel schemaModel, @NonNull Schema validationSchema) {
        this.schemaModel = schemaModel;
        this.validationSchema = validationSchema;
        this.typeMapper = new XmlSchemaTypeMapper();
        this.typedValueConverter = new XercesTypedValueConverter(this.typeMapper);
        this.simpleTypeCaster = new XercesSimpleTypeCaster(this.typeMapper, this.typedValueConverter);
    }

    public Optional<XSTypeDefinition> getTypeDefinition(@NonNull Name name) {
        return Optional.ofNullable(this.schemaModel.getTypeDefinition(name.getLocalName(), name.getNamespace()));
    }

    public boolean containsNamespace(String namespace) {
        return this.schemaModel.getNamespaces().contains(XmlNameCodec.emptyToNull(namespace));
    }

    /** Whether a name denotes a user-imported XML Schema simple type. */
    public boolean isImportedSimpleType(Name name) {
        if (name == null || Name.XS_NS.equals(name.getNamespace())) {
            return false;
        }
        return getTypeDefinition(name)
                .filter(XSSimpleTypeDefinition.class::isInstance)
                .isPresent();
    }

    /**
     * Returns the XDM sequence type produced by casting to an imported simple type.
     * XML Schema list types are cast targets, not XDM item types, so their item type and
     * cardinality describe the list's typed-value sequence.
     */
    public SequenceType getSimpleTypeCastResultType(Name name) {
        XSSimpleTypeDefinition schemaType = importedSimpleType(name);
        if (mayProduceMultipleValues(schemaType)) {
            ItemType itemType = this.typeMapper.getListItemType(schemaType).orElse(BuiltinTypesCatalogue.atomicItem);
            return new SequenceType(itemType, SequenceType.Arity.ZeroOrMore);
        }
        ItemType itemType =
                this.typeMapper.mapGeneralizedAtomicType(schemaType).orElse(BuiltinTypesCatalogue.atomicItem);
        return new SequenceType(itemType, SequenceType.Arity.One);
    }

    /** Casts one atomized value with the matching definition from this catalog. */
    public List<Item> castSimpleType(
            Name name, Item item, NamespaceResolver namespaceResolver, ExceptionMetadata metadata) {
        return this.simpleTypeCaster.cast(name, importedSimpleType(name), item, namespaceResolver, metadata);
    }

    public List<ItemType> getNamedGeneralizedAtomicItemTypes() {
        XSNamedMap schemaTypes = this.schemaModel.getComponents(XSConstants.TYPE_DEFINITION);
        List<ItemType> result = new ArrayList<>();
        for (int index = 0; index < schemaTypes.getLength(); index++) {
            XSTypeDefinition schemaType = (XSTypeDefinition) schemaTypes.item(index);
            this.typeMapper
                    .mapGeneralizedAtomicType(schemaType)
                    .filter(ItemType::hasName)
                    .filter(type -> !BuiltinTypesCatalogue.typeExists(type.getName()))
                    .ifPresent(result::add);
        }
        return List.copyOf(result);
    }

    Optional<ItemType> getAtomicItemType(XSTypeDefinition schemaType) {
        if (!(schemaType instanceof XSSimpleTypeDefinition simpleType)
                || simpleType.getVariety() != XSSimpleTypeDefinition.VARIETY_ATOMIC) {
            return Optional.empty();
        }
        return this.typeMapper.mapGeneralizedAtomicType(schemaType);
    }

    Optional<ItemType> getGeneralizedAtomicItemType(XSTypeDefinition schemaType) {
        return this.typeMapper.mapGeneralizedAtomicType(schemaType);
    }

    Optional<ItemType> getListItemType(XSTypeDefinition schemaType) {
        return this.typeMapper.getListItemType(schemaType);
    }

    XmlSchemaTypeAnnotation getTypeAnnotation(XSTypeDefinition schemaType) {
        return this.typeMapper.mapTypeAnnotation(schemaType);
    }

    List<Item> convertTypedValue(XSValue schemaValue) {
        return this.typedValueConverter.convert(schemaValue);
    }

    private XSSimpleTypeDefinition importedSimpleType(Name name) {
        return getTypeDefinition(name)
                .filter(type -> !Name.XS_NS.equals(name.getNamespace()))
                .filter(XSSimpleTypeDefinition.class::isInstance)
                .map(XSSimpleTypeDefinition.class::cast)
                .orElseThrow(
                        () -> new OurBadException("The type " + name + " is not an imported XML Schema simple type."));
    }

    private static boolean mayProduceMultipleValues(XSSimpleTypeDefinition schemaType) {
        if (schemaType.getVariety() == XSSimpleTypeDefinition.VARIETY_LIST) {
            return true;
        }
        if (schemaType.getVariety() != XSSimpleTypeDefinition.VARIETY_UNION) {
            return false;
        }
        XSObjectList memberTypes = schemaType.getMemberTypes();
        for (int index = 0; index < memberTypes.getLength(); index++) {
            if (memberTypes.item(index) instanceof XSSimpleTypeDefinition memberType
                    && memberType.getVariety() == XSSimpleTypeDefinition.VARIETY_LIST) {
                return true;
            }
        }
        return false;
    }
}
