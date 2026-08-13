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
import java.util.Objects;
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

import org.rumbledb.api.Item;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.xml.XmlSchemaTypeAnnotation;
import org.rumbledb.runtime.xml.NamespaceBindingUtils.NamespaceResolver;
import org.rumbledb.types.AttributeNodeItemType;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ElementNodeItemType;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SchemaElementNodeItemType;
import org.rumbledb.types.SequenceType;

/** The Xerces XML Schema component model available to one XQuery module. */
public final class XmlSchemaCatalog {

    private final XSModel schemaModel;
    private final Schema validationSchema;
    private final XmlSchemaTypeMapper typeMapper;
    private final XercesTypedValueConverter typedValueConverter;
    private final XercesSimpleTypeCaster simpleTypeCaster;

    XmlSchemaCatalog(XSModel schemaModel, Schema validationSchema) {
        this.schemaModel = Objects.requireNonNull(schemaModel, "schemaModel must not be null");
        this.validationSchema = Objects.requireNonNull(validationSchema, "validationSchema must not be null");
        this.typeMapper = new XmlSchemaTypeMapper();
        this.typedValueConverter = new XercesTypedValueConverter(this.typeMapper);
        this.simpleTypeCaster = new XercesSimpleTypeCaster(this.typeMapper, this.typedValueConverter);
    }

    public Optional<XSTypeDefinition> getTypeDefinition(Name name) {
        Objects.requireNonNull(name, "name must not be null");
        return Optional.ofNullable(
                this.schemaModel.getTypeDefinition(name.getLocalName(), emptyToNull(name.getNamespace())));
    }

    /** Resolves a global attribute declaration to its XDM node-test type. */
    public Optional<AttributeNodeItemType> getSchemaAttributeItemType(Name name) {
        Objects.requireNonNull(name, "name must not be null");
        XSAttributeDeclaration declaration =
                this.schemaModel.getAttributeDeclaration(name.getLocalName(), emptyToNull(name.getNamespace()));
        if (declaration == null) {
            return Optional.empty();
        }
        Name declarationName = componentName(declaration.getNamespace(), declaration.getName());
        Name typeName = getTypeAnnotation(declaration.getTypeDefinition()).name();
        return Optional.of(ItemTypeFactory.resolvedAttributeNodeItemType(declarationName, typeName));
    }

    /**
     * Resolves a global element declaration and all declarations that are validly substitutable for it.
     */
    public Optional<SchemaElementNodeItemType> getSchemaElementItemType(Name name) {
        Objects.requireNonNull(name, "name must not be null");
        XSElementDeclaration head =
                this.schemaModel.getElementDeclaration(name.getLocalName(), emptyToNull(name.getNamespace()));
        if (head == null) {
            return Optional.empty();
        }

        List<ElementNodeItemType> alternatives = new ArrayList<>();
        addElementAlternative(alternatives, head);
        XSObjectList substitutionGroup = this.schemaModel.getSubstitutionGroup(head);
        for (int index = 0; index < substitutionGroup.getLength(); index++) {
            addElementAlternative(alternatives, (XSElementDeclaration) substitutionGroup.item(index));
        }
        return Optional.of(new SchemaElementNodeItemType(name, alternatives));
    }

    public boolean containsNamespace(String namespace) {
        return this.schemaModel.getNamespaces().contains(emptyToNull(namespace));
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

    /** Whether a type uses the Xerces cast path instead of Rumble's atomic item-type path. */
    public boolean isSimpleTypeCastTarget(Name name) {
        if (isImportedSimpleType(name)) {
            return true;
        }
        if (name == null || !Name.XS_NS.equals(name.getNamespace())) {
            return false;
        }
        return getTypeDefinition(name)
                .filter(XSSimpleTypeDefinition.class::isInstance)
                .map(XSSimpleTypeDefinition.class::cast)
                .filter(type -> type.getVariety() == XSSimpleTypeDefinition.VARIETY_LIST)
                .isPresent();
    }

    public boolean isSimpleTypeConstructor(FunctionIdentifier identifier) {
        return identifier != null && identifier.getArity() == 1 && isSimpleTypeCastTarget(identifier.getName());
    }

    public FunctionSignature getSimpleTypeConstructorSignature(FunctionIdentifier identifier) {
        if (!isSimpleTypeConstructor(identifier)) {
            throw new OurBadException("The function name does not identify an XML Schema constructor.");
        }
        SequenceType returnType = getSimpleTypeCastResultType(identifier.getName());
        if (returnType.getArity() == SequenceType.Arity.One) {
            returnType = new SequenceType(returnType.getItemType(), SequenceType.Arity.OneOrZero);
        }
        return new FunctionSignature(List.of(SequenceType.createSequenceType("anyAtomicType?")), returnType);
    }

    /**
     * Returns the XDM sequence type produced by casting to an imported simple type.
     * XML Schema list types are cast targets, not XDM item types, so their item type and
     * cardinality describe the list's typed-value sequence.
     */
    public SequenceType getSimpleTypeCastResultType(Name name) {
        XSSimpleTypeDefinition schemaType = simpleTypeCastTarget(name);
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
        return this.simpleTypeCaster.cast(name, simpleTypeCastTarget(name), item, namespaceResolver, metadata);
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

    XSModel getSchemaModel() {
        return this.schemaModel;
    }

    Schema getValidationSchema() {
        return this.validationSchema;
    }

    private XSSimpleTypeDefinition simpleTypeCastTarget(Name name) {
        if (!isSimpleTypeCastTarget(name)) {
            throw new OurBadException("The type " + name + " is not an XML Schema cast target.");
        }
        return (XSSimpleTypeDefinition) getTypeDefinition(name).orElseThrow();
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
                    && mayProduceMultipleValues(memberType)) {
                return true;
            }
        }
        return false;
    }

    private static String emptyToNull(String value) {
        return value == null || value.isEmpty() ? null : value;
    }

    private void addElementAlternative(List<ElementNodeItemType> alternatives, XSElementDeclaration declaration) {
        if (declaration.getAbstract()) {
            return;
        }
        Name declarationName = componentName(declaration.getNamespace(), declaration.getName());
        Name typeName = getTypeAnnotation(declaration.getTypeDefinition()).name();
        alternatives.add(
                ItemTypeFactory.resolvedElementNodeItemType(declarationName, typeName, declaration.getNillable()));
    }

    private static Name componentName(String namespace, String localName) {
        String normalizedNamespace = emptyToNull(namespace);
        return new Name(normalizedNamespace, Name.XS_NS.equals(normalizedNamespace) ? "xs" : null, localName);
    }
}
