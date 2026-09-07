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

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.SchemaDVFactory;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.xpath.regex.RegularExpression;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;

import lombok.NonNull;

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

    @NonNull private final XmlSchemaTypeMapper typeMapper;

    @NonNull private final XercesTypedValueConverter typedValueConverter;

    XercesSimpleTypeCaster(
            @NonNull XmlSchemaTypeMapper typeMapper, @NonNull XercesTypedValueConverter typedValueConverter) {
        this.typeMapper = typeMapper;
        this.typedValueConverter = typedValueConverter;
    }

    List<Item> cast(
            @NonNull Name typeName,
            @NonNull XSSimpleTypeDefinition schemaType,
            @NonNull Item item,
            @NonNull NamespaceResolver namespaceResolver,
            @NonNull ExceptionMetadata metadata) {
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
        ItemType itemType = this.typeMapper
                .mapGeneralizedAtomicType(schemaType)
                .filter(ItemType::isAtomicItemType)
                .orElseThrow(() -> new OurBadException("Xerces did not identify an atomic cast target.", metadata));
        if (item.getDynamicType().equals(itemType)) {
            return List.of(item);
        }
        if (item.isString() || item.isUntypedAtomic()) {
            return validate(
                    typeName,
                    schemaType,
                    item.getStringValue(),
                    item,
                    new SimpleTypeValidationContext(namespaceResolver),
                    metadata);
        }
        Item converted = CastIterator.castItemToType(item, itemType, metadata, namespaceResolver);
        if (converted == null) {
            throw castException(typeName, item, metadata);
        }
        if (!item.getDynamicType().isSubtypeOf(itemType)) {
            try {
                validatedCanonicalValue(schemaType, item, converted, namespaceResolver, metadata);
            } catch (InvalidDatatypeValueException exception) {
                throw validationException(exception, typeName, item, metadata);
            }
        }
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

        // Only pure unions support the subtype shortcut; restricted unions must try members in order.
        boolean pureUnion = this.typeMapper.mapGeneralizedAtomicType(schemaType).isPresent();
        for (XSSimpleType memberType : atomicMemberTypes) {
            ItemType memberItemType =
                    this.typeMapper.mapGeneralizedAtomicType(memberType).orElse(null);
            if (pureUnion && memberItemType != null && item.getDynamicType().isSubtypeOf(memberItemType)) {
                return List.of(CastIterator.castItemToType(item, memberItemType, metadata, namespaceResolver));
            }
        }

        // Try castable atomic union members in declaration order.
        for (XSSimpleType memberType : atomicMemberTypes) {
            ItemType itemType =
                    this.typeMapper.mapGeneralizedAtomicType(memberType).orElse(null);
            if (itemType == null || !itemType.isAtomicItemType()) {
                continue;
            }
            Item converted;
            try {
                converted = CastIterator.castItemToType(item, itemType, metadata, namespaceResolver);
            } catch (CastException | UnexpectedTypeException | NoNamespaceFoundForPrefixException exception) {
                continue;
            }
            if (converted == null) {
                continue;
            }

            ValidatedInfo memberValue;
            SimpleTypeValidationContext convertedContext = validationContext(converted, namespaceResolver);
            try {
                memberValue = item.getDynamicType().isSubtypeOf(itemType)
                        ? validateValue(builtInBaseType(memberType), lexicalValue(converted), convertedContext)
                        : validatedCanonicalValue(memberType, item, converted, namespaceResolver, metadata);
            } catch (InvalidDatatypeValueException exception) {
                continue;
            }
            // Once a member cast succeeds, failure of the union's own facets fails the whole cast.
            try {
                checkPatterns(schemaType, memberValue.getActualValue().toString());
                schemaType.validate(convertedContext.forFacetCheckingOnly(), memberValue);
                return List.of(converted);
            } catch (InvalidDatatypeValueException exception) {
                throw castException(typeName, item, metadata);
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
            throw validationException(exception, typeName, sourceItem, metadata);
        }
    }

    private static RumbleException validationException(
            InvalidDatatypeValueException exception, Name typeName, Item sourceItem, ExceptionMetadata metadata) {
        if ("UndeclaredPrefix".equals(exception.getKey())) {
            return new NoNamespaceFoundForPrefixException(exception.getMessage(), metadata);
        }
        return castException(typeName, sourceItem, metadata);
    }

    private static void checkPatterns(XSSimpleType schemaType, String lexical) throws InvalidDatatypeValueException {
        // Xerces validate(context, value) checks value facets only. Check patterns separately to
        // preserve the selected atomic member instead of parsing the value as the union again.
        StringList patterns = schemaType.getLexicalPattern();
        for (int index = 0; index < patterns.getLength(); index++) {
            String pattern = patterns.item(index);
            if (!new RegularExpression(pattern, "X").matches(lexical)) {
                throw new InvalidDatatypeValueException(
                        "cvc-pattern-valid", new Object[] {lexical, pattern, schemaType.getName()});
            }
        }
    }

    private ValidatedInfo validatedCanonicalValue(
            XSSimpleType schemaType,
            Item source,
            Item value,
            NamespaceResolver namespaceResolver,
            ExceptionMetadata metadata)
            throws InvalidDatatypeValueException {
        SimpleTypeValidationContext validationContext = validationContext(value, namespaceResolver);
        ValidatedInfo schemaValue = validateValue(builtInBaseType(schemaType), lexicalValue(value), validationContext);
        // F&O casting primitives include integer and the duration subtypes, unlike XSD primitives.
        // Across casting families, check the canonical form of the converted primitive value.
        ItemType primitiveType = this.typeMapper
                .mapGeneralizedAtomicType(schemaType)
                .orElseThrow()
                .getCastingPrimitiveType();
        Item patternSource = source.getDynamicType().getCastingPrimitiveType().equals(primitiveType)
                ? source
                : CastIterator.castItemToType(source, primitiveType, metadata, namespaceResolver);
        ItemType sourceBase = patternSource.getDynamicType();
        while (!sourceBase.hasName() || !Name.XS_NS.equals(sourceBase.getName().getNamespace())) {
            sourceBase = sourceBase.getBaseType();
        }
        XSSimpleType sourceSchemaType = SchemaDVFactory.getInstance()
                .getBuiltInType(sourceBase.getName().getLocalName());
        ValidatedInfo sourceValue = validateValue(
                sourceSchemaType, lexicalValue(patternSource), validationContext(patternSource, namespaceResolver));
        checkPatterns(schemaType, sourceValue.getActualValue().toString());
        // Do not parse as the target again: that would check its patterns against the target's form.
        schemaType.validate(validationContext.forFacetCheckingOnly(), schemaValue);
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
            // Xerces QName equality relies on interned local names and namespace URIs.
            return symbol.intern();
        }

        @Override
        public String getURI(String prefix) {
            String namespace = this.namespaceResolver.resolvePrefix(prefix);
            return namespace == null || namespace.isEmpty() ? null : getSymbol(namespace);
        }

        @Override
        public Locale getLocale() {
            return Locale.ROOT;
        }
    }
}
