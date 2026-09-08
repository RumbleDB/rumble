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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.xml.validation.Schema;

import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSElementDeclaration;
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
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.items.xml.XmlSchemaTypeAnnotation;
import org.rumbledb.runtime.xml.NamespaceBindingUtils.NamespaceResolver;
import org.rumbledb.types.AttributeNodeItemType;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ElementNodeItemType;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SchemaElementNodeItemType;
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
        return Optional.ofNullable(
                this.schemaModel.getTypeDefinition(name.getLocalName(), emptyToNull(name.getNamespace())));
    }

    /** Resolves a global declaration and the substitutions allowed by its blocking constraints. */
    public SchemaElementNodeItemType getSchemaElementTest(Name name, ExceptionMetadata metadata) {
        XSElementDeclaration declaration =
                this.schemaModel.getElementDeclaration(name.getLocalName(), emptyToNull(name.getNamespace()));
        if (declaration == null) {
            throw new SemanticException(
                    "Unknown global schema element: " + name, ErrorCode.UndeclaredVariableErrorCode, metadata);
        }
        List<ElementNodeItemType> alternatives = new ArrayList<>();
        addElementAlternative(declaration, alternatives);
        XSObjectList substitutions = this.schemaModel.getSubstitutionGroup(declaration);
        for (int i = 0; i < substitutions.getLength(); i++) {
            addElementAlternative((XSElementDeclaration) substitutions.item(i), alternatives);
        }
        return new SchemaElementNodeItemType(name, alternatives);
    }

    private void addElementAlternative(XSElementDeclaration declaration, List<ElementNodeItemType> alternatives) {
        if (declaration.getAbstract()) {
            return;
        }
        XmlSchemaTypeAnnotation annotation = this.typeMapper.mapTypeAnnotation(declaration.getTypeDefinition());
        alternatives.add(new ElementNodeItemType(
                new Name(declaration.getNamespace(), null, declaration.getName()),
                annotation.name(),
                annotation.typeHierarchy(),
                declaration.getNillable(),
                matchingTypeNames(declaration.getTypeDefinition())));
    }

    /** Attribute declaration tests have the same matching rules as a named, typed attribute test. */
    public AttributeNodeItemType getSchemaAttributeTest(Name name, ExceptionMetadata metadata) {
        XSAttributeDeclaration declaration =
                this.schemaModel.getAttributeDeclaration(name.getLocalName(), emptyToNull(name.getNamespace()));
        if (declaration == null) {
            throw new SemanticException(
                    "Unknown global schema attribute: " + name, ErrorCode.UndeclaredVariableErrorCode, metadata);
        }
        XmlSchemaTypeAnnotation annotation = this.typeMapper.mapTypeAnnotation(declaration.getTypeDefinition());
        return new AttributeNodeItemType(
                name,
                annotation.name(),
                annotation.typeHierarchy(),
                matchingTypeNames(declaration.getTypeDefinition()));
    }

    /** A pure union also accepts annotations derived from any of its atomic member types. */
    private List<Name> matchingTypeNames(XSTypeDefinition definition) {
        List<Name> names = new ArrayList<>();
        names.add(this.typeMapper.mapTypeAnnotation(definition).name());
        this.typeMapper
                .mapGeneralizedAtomicType(definition)
                .filter(ItemType::isUnionType)
                .ifPresent(union -> union.getTypes().forEach(member -> names.add(member.getName())));
        return List.copyOf(names);
    }

    public boolean containsNamespace(String namespace) {
        return this.schemaModel.getNamespaces().contains(emptyToNull(namespace));
    }

    /** Returns the named schema type followed by its base-type chain. */
    public List<Name> getTypeHierarchy(@NonNull Name name, @NonNull ExceptionMetadata metadata) {
        Optional<XSTypeDefinition> definition = getTypeDefinition(name);
        // XQuery adds atomic types, such as untypedAtomic and the duration subtypes,
        // that Xerces's XSD 1.0 catalog does not contain.
        if (definition.isEmpty() && Name.XS_NS.equals(name.getNamespace()) && BuiltinTypesCatalogue.typeExists(name)) {
            ItemType itemType = BuiltinTypesCatalogue.getItemTypeByName(name);
            if (itemType.isAtomicItemType()) {
                return XmlSchemaTypeAnnotation.forAtomicItemType(itemType).typeHierarchy();
            }
        }
        if (definition.isEmpty() && Name.XS_NS.equals(name.getNamespace()) && "untyped".equals(name.getLocalName())) {
            return List.of(name, new Name(Name.XS_NS, "xs", "anyType"));
        }
        if (definition.isEmpty()
                && Name.XS_NS.equals(name.getNamespace())
                && "anyAtomicType".equals(name.getLocalName())) {
            return List.of(name, new Name(Name.XS_NS, "xs", "anySimpleType"), new Name(Name.XS_NS, "xs", "anyType"));
        }
        if (definition.isEmpty() && Name.XS_NS.equals(name.getNamespace()) && "numeric".equals(name.getLocalName())) {
            return List.of(
                    name,
                    new Name(Name.XS_NS, "xs", "anyAtomicType"),
                    new Name(Name.XS_NS, "xs", "anySimpleType"),
                    new Name(Name.XS_NS, "xs", "anyType"));
        }
        XSTypeDefinition type = definition.orElseThrow(() -> new SemanticException(
                "Unknown XML Schema type: " + name, ErrorCode.UndeclaredVariableErrorCode, metadata));
        return this.typeMapper.mapTypeAnnotation(type).typeHierarchy();
    }

    /** Whether the schema caster handles this target (imported simple types and built-in lists). */
    public boolean isSchemaCastTarget(Name name) {
        if (name == null || (Name.XS_NS.equals(name.getNamespace()) && !isBuiltInListType(name))) {
            return false;
        }
        return getTypeDefinition(name)
                .filter(XSSimpleTypeDefinition.class::isInstance)
                .isPresent();
    }

    private static boolean isBuiltInListType(Name name) {
        return name != null
                && Name.XS_NS.equals(name.getNamespace())
                && switch (name.getLocalName()) {
                    case "IDREFS", "NMTOKENS", "ENTITIES" -> true;
                    default -> false;
                };
    }

    /**
     * Returns the XDM sequence type produced by a schema cast.
     * XML Schema list types are cast targets, not XDM item types, so their item type and
     * cardinality describe the list's typed-value sequence.
     */
    public SequenceType getSimpleTypeCastResultType(Name name) {
        XSSimpleTypeDefinition schemaType = simpleType(name);
        if (mayProduceMultipleValues(schemaType)) {
            ItemType itemType = this.typeMapper.getListItemType(schemaType).orElse(BuiltinTypesCatalogue.atomicItem);
            return new SequenceType(itemType, SequenceType.Arity.ZeroOrMore);
        }
        ItemType itemType = nearestGeneralizedAtomicType(schemaType);
        return new SequenceType(itemType, SequenceType.Arity.One);
    }

    /**
     * A restricted union may not itself have an XDM item type, but its typed value is still
     * within the nearest representable generalized atomic base type.
     */
    private ItemType nearestGeneralizedAtomicType(XSTypeDefinition schemaType) {
        XSTypeDefinition current = schemaType;
        while (current != null) {
            Optional<ItemType> mappedType = this.typeMapper.mapGeneralizedAtomicType(current);
            if (mappedType.isPresent()) {
                return mappedType.get();
            }
            XSTypeDefinition baseType = current.getBaseType();
            if (baseType == current) {
                break;
            }
            current = baseType;
        }
        return BuiltinTypesCatalogue.atomicItem;
    }

    /** Casts one atomized value with the matching definition from this catalog. */
    public List<Item> castSimpleType(
            Name name, Item item, NamespaceResolver namespaceResolver, ExceptionMetadata metadata) {
        return this.simpleTypeCaster.cast(name, simpleType(name), item, namespaceResolver, metadata);
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

    private XSSimpleTypeDefinition simpleType(Name name) {
        return getTypeDefinition(name)
                .filter(type -> !Name.XS_NS.equals(name.getNamespace()) || isBuiltInListType(name))
                .filter(XSSimpleTypeDefinition.class::isInstance)
                .map(XSSimpleTypeDefinition.class::cast)
                .orElseThrow(
                        () -> new OurBadException("The type " + name + " is not handled by the XML Schema caster."));
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

    private static String emptyToNull(String value) {
        return value == null || value.isEmpty() ? null : value;
    }
}
