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
import java.util.Locale;
import java.util.Objects;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.NoNamespaceFoundForPrefixException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.runtime.xml.NamespaceBindingUtils.NamespaceResolver;
import org.rumbledb.types.ItemType;

/** Applies one Xerces simple type to an atomic XDM value. */
final class XercesSimpleTypeCaster {

    private final XmlSchemaTypeMapper typeMapper;
    private final XercesTypedValueConverter typedValueConverter;

    XercesSimpleTypeCaster(XmlSchemaTypeMapper typeMapper, XercesTypedValueConverter typedValueConverter) {
        this.typeMapper = Objects.requireNonNull(typeMapper, "typeMapper must not be null");
        this.typedValueConverter = Objects.requireNonNull(typedValueConverter, "typedValueConverter must not be null");
    }

    List<Item> cast(
            Name typeName,
            XSSimpleTypeDefinition schemaType,
            Item item,
            NamespaceResolver namespaceResolver,
            ExceptionMetadata metadata) {
        Objects.requireNonNull(typeName, "typeName must not be null");
        Objects.requireNonNull(schemaType, "schemaType must not be null");
        Objects.requireNonNull(item, "item must not be null");
        Objects.requireNonNull(namespaceResolver, "namespaceResolver must not be null");
        Objects.requireNonNull(metadata, "metadata must not be null");

        if (!(schemaType instanceof XSSimpleType xercesType)) {
            throw new OurBadException("The Xerces simple type cannot validate values.", metadata);
        }
        if (!item.isAtomic() || item.isNull()) {
            throw new UnexpectedTypeException("An XML Schema cast operand must be atomic after atomization.", metadata);
        }
        SimpleTypeValidationContext validationContext = new SimpleTypeValidationContext(namespaceResolver);
        return switch (schemaType.getVariety()) {
            case XSSimpleTypeDefinition.VARIETY_ATOMIC -> castAtomic(
                    typeName, xercesType, item, namespaceResolver, metadata);
            case XSSimpleTypeDefinition.VARIETY_LIST -> castList(
                    typeName, xercesType, item, validationContext, metadata);
            case XSSimpleTypeDefinition.VARIETY_UNION -> castUnion(
                    typeName, xercesType, item, namespaceResolver, validationContext, metadata);
            default -> throw new OurBadException("Xerces returned an unsupported simple type variety.", metadata);
        };
    }

    private List<Item> castAtomic(
            Name typeName,
            XSSimpleType schemaType,
            Item item,
            NamespaceResolver namespaceResolver,
            ExceptionMetadata metadata) {
        if (item.isString() || item.isUntypedAtomic()) {
            return validate(
                    typeName,
                    schemaType,
                    item.getStringValue(),
                    item,
                    new SimpleTypeValidationContext(namespaceResolver),
                    metadata);
        }
        ItemType itemType = this.typeMapper
                .mapGeneralizedAtomicType(schemaType)
                .filter(ItemType::isAtomicItemType)
                .orElseThrow(() -> new OurBadException("Xerces did not identify an atomic cast target.", metadata));
        Item converted = CastIterator.castItemToType(item, itemType, metadata, namespaceResolver);
        if (converted == null) {
            throw castException(typeName, item, metadata);
        }
        validateOnly(typeName, schemaType, converted, item, namespaceResolver, metadata);
        return List.of(converted);
    }

    private List<Item> castList(
            Name typeName,
            XSSimpleType schemaType,
            Item item,
            ValidationContext validationContext,
            ExceptionMetadata metadata) {
        if (!item.isString() && !item.isUntypedAtomic()) {
            throw new UnexpectedTypeException(
                    "Only xs:string and xs:untypedAtomic values can be cast to an XML Schema list type.", metadata);
        }
        return validate(typeName, schemaType, item.getStringValue(), item, validationContext, metadata);
    }

    private List<Item> castUnion(
            Name typeName,
            XSSimpleType schemaType,
            Item item,
            NamespaceResolver namespaceResolver,
            ValidationContext validationContext,
            ExceptionMetadata metadata) {
        if (item.isString() || item.isUntypedAtomic()) {
            return validate(typeName, schemaType, item.getStringValue(), item, validationContext, metadata);
        }

        List<XSSimpleType> atomicMemberTypes = new ArrayList<>();
        collectAtomicMemberTypes(schemaType, atomicMemberTypes);

        // F&O 3.1 union rule 2: preserve a value that is already an instance of a member type.
        for (XSSimpleType memberType : atomicMemberTypes) {
            ItemType memberItemType =
                    this.typeMapper.mapGeneralizedAtomicType(memberType).orElse(null);
            if (memberItemType != null && item.getDynamicType().isSubtypeOf(memberItemType)) {
                try {
                    SimpleTypeValidationContext memberContext = validationContext(item, namespaceResolver);
                    ValidatedInfo memberValue = validatedCanonicalValue(memberType, item, memberContext);
                    schemaType.validate(memberContext.forFacetCheckingOnly(), memberValue);
                    return List.of(item);
                } catch (InvalidDatatypeValueException exception) {
                    throw castException(typeName, item, metadata);
                }
            }
        }

        // F&O 3.1 union rule 3: try castable atomic members in declaration order.
        for (XSSimpleType memberType : atomicMemberTypes) {
            ItemType itemType =
                    this.typeMapper.mapGeneralizedAtomicType(memberType).orElse(null);
            if (itemType == null || !itemType.isAtomicItemType()) {
                continue;
            }
            Item converted;
            try {
                converted = CastIterator.castItemToType(item, itemType, metadata, namespaceResolver);
            } catch (RumbleException exception) {
                continue;
            }
            if (converted == null) {
                continue;
            }

            try {
                SimpleTypeValidationContext convertedContext = validationContext(converted, namespaceResolver);
                ValidatedInfo memberValue = validatedCanonicalValue(memberType, converted, convertedContext);
                // Validate restrictions declared on the union, while retaining the selected member's value.
                schemaType.validate(convertedContext.forFacetCheckingOnly(), memberValue);
                return List.of(converted);
            } catch (InvalidDatatypeValueException exception) {
                // Try the next atomic member.
            }
        }
        throw castException(typeName, item, metadata);
    }

    private List<Item> validate(
            Name typeName,
            XSSimpleType schemaType,
            String lexicalValue,
            Item sourceItem,
            ValidationContext validationContext,
            ExceptionMetadata metadata) {
        try {
            return this.typedValueConverter.convert(validateValue(schemaType, lexicalValue, validationContext));
        } catch (InvalidDatatypeValueException exception) {
            if ("UndeclaredPrefix".equals(exception.getKey())) {
                throw new NoNamespaceFoundForPrefixException(exception.getMessage(), metadata);
            }
            throw castException(typeName, sourceItem, metadata);
        }
    }

    private static void validateOnly(
            Name typeName,
            XSSimpleType schemaType,
            Item value,
            Item sourceItem,
            NamespaceResolver namespaceResolver,
            ExceptionMetadata metadata) {
        try {
            SimpleTypeValidationContext validationContext = validationContext(value, namespaceResolver);
            validatedCanonicalValue(schemaType, value, validationContext);
        } catch (InvalidDatatypeValueException exception) {
            if ("UndeclaredPrefix".equals(exception.getKey())) {
                throw new NoNamespaceFoundForPrefixException(exception.getMessage(), metadata);
            }
            throw castException(typeName, sourceItem, metadata);
        }
    }

    private static ValidatedInfo validatedCanonicalValue(
            XSSimpleType schemaType, Item value, SimpleTypeValidationContext validationContext)
            throws InvalidDatatypeValueException {
        ValidatedInfo schemaValue = validateValue(builtInBaseType(schemaType), lexicalValue(value), validationContext);
        // F&O casting applies pattern facets to the canonical lexical representation of the converted value.
        schemaValue.normalizedValue = schemaValue.getActualValue().toString();
        // Preserve the base type's actual value for facet comparison.
        schemaType.validate(schemaValue.getActualValue(), validationContext.forFacetCheckingOnly(), schemaValue);
        return schemaValue;
    }

    private static XSSimpleType builtInBaseType(XSSimpleType schemaType) {
        XSTypeDefinition current = schemaType;
        while (current != null && !Name.XS_NS.equals(current.getNamespace())) {
            XSTypeDefinition baseType = current.getBaseType();
            if (baseType == current) {
                break;
            }
            current = baseType;
        }
        if (!(current instanceof XSSimpleType builtInType)) {
            throw new OurBadException("An imported atomic type has no built-in XML Schema base type.");
        }
        return builtInType;
    }

    private static String lexicalValue(Item value) {
        if (!value.isQName()) {
            return value.getStringValue();
        }
        Name name = value.getQNameValue();
        String prefix = name.getPrefix();
        return prefix == null || prefix.isEmpty() ? name.getLocalName() : prefix + ":" + name.getLocalName();
    }

    private static SimpleTypeValidationContext validationContext(Item value, NamespaceResolver namespaceResolver) {
        if (!value.isQName()) {
            return new SimpleTypeValidationContext(namespaceResolver);
        }
        Name name = value.getQNameValue();
        String qNamePrefix = name.getPrefix() == null ? "" : name.getPrefix();
        return new SimpleTypeValidationContext(
                prefix -> qNamePrefix.equals(prefix) ? name.getNamespace() : namespaceResolver.resolvePrefix(prefix));
    }

    private static ValidatedInfo validateValue(
            XSSimpleType schemaType, String lexicalValue, ValidationContext validationContext)
            throws InvalidDatatypeValueException {
        ValidatedInfo schemaValue = new ValidatedInfo();
        schemaType.validate(lexicalValue, validationContext, schemaValue);
        return schemaValue;
    }

    private static void collectAtomicMemberTypes(XSSimpleTypeDefinition schemaType, List<XSSimpleType> result) {
        XSObjectList memberTypes = schemaType.getMemberTypes();
        for (int index = 0; index < memberTypes.getLength(); index++) {
            XSSimpleType memberType = (XSSimpleType) memberTypes.item(index);
            if (memberType.getVariety() == XSSimpleTypeDefinition.VARIETY_ATOMIC) {
                result.add(memberType);
            } else if (memberType.getVariety() == XSSimpleTypeDefinition.VARIETY_UNION) {
                collectAtomicMemberTypes(memberType, result);
            }
        }
    }

    private static CastException castException(Name typeName, Item item, ExceptionMetadata metadata) {
        return new CastException("\"" + item.getStringValue() + "\" is not valid for type " + typeName + ".", metadata);
    }

    private static final class SimpleTypeValidationContext implements ValidationContext {

        private final NamespaceResolver namespaceResolver;
        private final boolean extraChecking;

        private SimpleTypeValidationContext(NamespaceResolver namespaceResolver) {
            this(namespaceResolver, true);
        }

        private SimpleTypeValidationContext(NamespaceResolver namespaceResolver, boolean extraChecking) {
            this.namespaceResolver = namespaceResolver;
            this.extraChecking = extraChecking;
        }

        private SimpleTypeValidationContext forFacetCheckingOnly() {
            return this.extraChecking ? new SimpleTypeValidationContext(this.namespaceResolver, false) : this;
        }

        @Override
        public boolean needFacetChecking() {
            return true;
        }

        @Override
        public boolean needExtraChecking() {
            return this.extraChecking;
        }

        @Override
        public boolean needToNormalize() {
            return true;
        }

        @Override
        public boolean useNamespaces() {
            return true;
        }

        @Override
        public boolean isEntityDeclared(String name) {
            return false;
        }

        @Override
        public boolean isEntityUnparsed(String name) {
            return false;
        }

        @Override
        public boolean isIdDeclared(String name) {
            return false;
        }

        @Override
        public void addId(String name) {}

        @Override
        public void addIdRef(String name) {}

        @Override
        public String getSymbol(String symbol) {
            return symbol;
        }

        @Override
        public String getURI(String prefix) {
            return this.namespaceResolver.resolvePrefix(prefix);
        }

        @Override
        public Locale getLocale() {
            return Locale.ROOT;
        }
    }
}
