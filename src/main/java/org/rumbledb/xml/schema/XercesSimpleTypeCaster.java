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

/**
 * Casts values to simple types defined in an imported XML Schema (XSD).
 * Uses RumbleDB to convert values and Xerces to check schema restrictions, such as number ranges
 * and text patterns. Supports single values (atomic types), lists, and unions of allowed types.
 */
final class XercesSimpleTypeCaster {

    @NonNull private final XmlSchemaTypeMapper typeMapper;

    @NonNull private final XercesTypedValueConverter typedValueConverter;

    XercesSimpleTypeCaster(
            @NonNull XmlSchemaTypeMapper typeMapper, @NonNull XercesTypedValueConverter typedValueConverter) {
        this.typeMapper = typeMapper;
        this.typedValueConverter = typedValueConverter;
    }

    /**
     * Takes one atomic value, such as a string or number, and chooses the casting method for the
     * target type: atomic, list, or union. Any XML node must already have been replaced by its value.
     * Returns a list of items because casting to a schema list type can produce several values.
     */
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

    /**
     * Converts the input to a single typed value, such as an integer with a minimum allowed value.
     * Xerces parses string and untyped text inputs. RumbleDB converts other inputs, and Xerces checks
     * any remaining schema restrictions. If the input already has exactly the target type, returns it unchanged.
     */
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

    /**
     * Converts text such as "10 20" into a sequence of values using the schema's list definition.
     * Accepts only strings or xs:untypedAtomic values (text without a specific schema type),
     * and checks list restrictions such as the required number of values.
     */
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

    /**
     * Casts to a union, which allows a value to have one of several member types.
     * For strings and untyped text, Xerces chooses the matching member. For other values, an existing
     * member type can be used directly when the union qualifies as a pure union (see the check below).
     * Otherwise, tries the atomic member types in schema order. Once a member succeeds, the value
     * must also satisfy the union's own restrictions; failure at that point ends the cast.
     */
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

        // The mapper recognizes pure unions: unions of atomic types without extra union restrictions.
        // For these, an input that already belongs to a member type can use that member directly.
        boolean pureUnion = this.typeMapper.mapGeneralizedAtomicType(schemaType).isPresent();
        for (XSSimpleType memberType : atomicMemberTypes) {
            ItemType memberItemType =
                    this.typeMapper.mapGeneralizedAtomicType(memberType).orElse(null);
            if (pureUnion && memberItemType != null && item.getDynamicType().isSubtypeOf(memberItemType)) {
                return List.of(CastIterator.castItemToType(item, memberItemType, metadata, namespaceResolver));
            }
        }

        // Try each atomic member in the order written in the schema; skip members that reject the value.
        for (XSSimpleType memberType : atomicMemberTypes) {
            ItemType itemType =
                    this.typeMapper.mapGeneralizedAtomicType(memberType).orElse(null);
            if (itemType == null || !itemType.isAtomicItemType()) {
                // Cannot find corresponding RumbleDB type for this member; skip it
                continue;
            }
            Item converted;
            try {
                // Try converting the original input to the member type.
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

            // This member accepted the value. The union's additional restrictions must also pass.
            try {
                checkPatterns(schemaType, memberValue.getActualValue().toString());
                schemaType.validate(convertedContext, memberValue);
                return List.of(converted);
            } catch (InvalidDatatypeValueException exception) {
                throw castException(typeName, item, metadata);
            }
        }
        throw castException(typeName, item, metadata);
    }

    /**
     * Asks Xerces to parse the input text and check it against the target schema type.
     * Converts the result into RumbleDB items and reports validation failures as query errors.
     */
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

    /**
     * Turns a Xerces validation error into a RumbleDB query error. Keeps missing namespace prefixes
     * as a distinct error; other failures report that the input cannot be cast to the target type.
     */
    private static RumbleException validationException(
            InvalidDatatypeValueException exception, Name typeName, Item sourceItem, ExceptionMetadata metadata) {
        if ("UndeclaredPrefix".equals(exception.getKey())) {
            return new NoNamespaceFoundForPrefixException(exception.getMessage(), metadata);
        }
        return castException(typeName, sourceItem, metadata);
    }

    /**
     * Checks whether the text matches each pattern returned by the schema type.
     * Uses XML Schema regular-expression syntax. This separate check is needed because Xerces's
     * validation of an already parsed value does not check text patterns.
     */
    private static void checkPatterns(XSSimpleType schemaType, String lexical) throws InvalidDatatypeValueException {
        // Check the text directly so Xerces does not parse the union again and choose a different member.
        StringList patterns = schemaType.getLexicalPattern();
        for (int index = 0; index < patterns.getLength(); index++) {
            String pattern = patterns.item(index);
            if (!new RegularExpression(pattern, "X").matches(lexical)) {
                throw new InvalidDatatypeValueException(
                        "cvc-pattern-valid", new Object[] {lexical, pattern, schemaType.getName()});
            }
        }
    }

    /**
     * Checks schema restrictions after RumbleDB has converted a value.
     * Number ranges and other value restrictions are checked against the converted value. Text patterns
     * are checked against a canonical form: the standard text representation produced by Xerces.
     * Uses the source value's form when source and target share a casting primitive (the basic type
     * used by the casting rules); otherwise, first converts the source to the target's casting primitive.
     * Returns the converted value in Xerces's representation after the checks succeed.
     */
    private ValidatedInfo validatedCanonicalValue(
            XSSimpleType schemaType,
            Item source,
            Item value,
            NamespaceResolver namespaceResolver,
            ExceptionMetadata metadata)
            throws InvalidDatatypeValueException {
        SimpleTypeValidationContext validationContext = validationContext(value, namespaceResolver);
        ValidatedInfo schemaValue = validateValue(builtInBaseType(schemaType), lexicalValue(value), validationContext);
        // Use the basic types defined by the query casting rules. These treat integer and the
        // duration subtypes as casting primitives, although XML Schema does not call them primitives.
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
        // Patterns have already been checked against the required text form. Check the remaining
        // restrictions on the parsed value without parsing its text again.
        schemaType.validate(validationContext, schemaValue);
        return schemaValue;
    }

    /**
     * Follows the types that the target was derived from until it reaches a built-in XML Schema type,
     * such as xs:integer. Parsing with this base type lets the caller check the imported type's
     * additional restrictions separately.
     */
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

    /**
     * Returns the value as text for Xerces to parse. For a QName (a name with a namespace),
     * returns a name such as "xs:integer", or just the local name when there is no prefix.
     */
    private static String lexicalValue(Item value) {
        if (!value.isQName()) {
            return value.getStringValue();
        }
        Name name = value.getQNameValue();
        String prefix = name.getPrefix();
        return prefix == null || prefix.isEmpty() ? name.getLocalName() : prefix + ":" + name.getLocalName();
    }

    /**
     * Provides the namespace lookup Xerces needs when validating a value.
     * For an existing QName, uses the namespace stored in that value for its own prefix, even if
     * the query maps that prefix differently. Other prefixes use the supplied namespace lookup.
     */
    private static SimpleTypeValidationContext validationContext(Item value, NamespaceResolver namespaceResolver) {
        if (!value.isQName()) {
            return new SimpleTypeValidationContext(namespaceResolver);
        }
        Name name = value.getQNameValue();
        String qNamePrefix = name.getPrefix() == null ? "" : name.getPrefix();
        return new SimpleTypeValidationContext(
                prefix -> qNamePrefix.equals(prefix) ? name.getNamespace() : namespaceResolver.resolvePrefix(prefix));
    }

    /**
     * Parses text with Xerces and checks it against the given schema type.
     * Returns both the parsed value and its type information, including the chosen member of a union.
     * The caller decides how to handle a validation error.
     */
    private static ValidatedInfo validateValue(
            XSSimpleType schemaType, String lexicalValue, ValidationContext validationContext)
            throws InvalidDatatypeValueException {
        ValidatedInfo schemaValue = new ValidatedInfo();
        schemaType.validate(lexicalValue, validationContext, schemaValue);
        return schemaValue;
    }

    /**
     * Adds the union's atomic member types to the result in schema order, leaving out list types.
     * Xerces already expands nested XML Schema 1.0 unions into their members, so this method only
     * needs to examine one level.
     */
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

    /** Supplies Xerces with namespace lookups and validation settings for casting a standalone value. */
    private static final class SimpleTypeValidationContext implements ValidationContext {

        private final NamespaceResolver namespaceResolver;

        private SimpleTypeValidationContext(NamespaceResolver namespaceResolver) {
            this.namespaceResolver = namespaceResolver;
        }

        /**
         * Tells Xerces to check restrictions such as ranges, lengths, and lists of allowed values.
         */
        @Override
        public boolean needFacetChecking() {
            return true;
        }

        /**
         * Tells Xerces whether to run extra checks, such as XML ID handling, for this call.
         */
        @Override
        public boolean needExtraChecking() {
            // Casting has no document-level ID/IDREF/ENTITY constraints.
            return false;
        }

        /**
         * Tells Xerces to handle whitespace as the schema type requires, for example by trimming spaces.
         */
        @Override
        public boolean needToNormalize() {
            return true;
        }

        /**
         * Tells Xerces to resolve namespace prefixes when checking QName values such as "xs:integer".
         */
        @Override
        public boolean useNamespaces() {
            return true;
        }

        /**
         * Returns false because this cast has no XML document's entity declarations to look up.
         */
        @Override
        public boolean isEntityDeclared(String name) {
            return false;
        }

        /**
         * Returns false because this cast has no declarations of entities that refer to non-XML content.
         */
        @Override
        public boolean isEntityUnparsed(String name) {
            return false;
        }

        /**
         * Returns false because this cast does not keep track of IDs declared in an XML document.
         */
        @Override
        public boolean isIdDeclared(String name) {
            return false;
        }

        /**
         * Ignores ID registration because casting a value does not add an ID to an XML document.
         */
        @Override
        public void addId(String name) {}

        /**
         * Ignores ID references because this cast does not track links to IDs in an XML document.
         */
        @Override
        public void addIdRef(String name) {}

        /**
         * Returns a shared string instance for equal names and namespace URIs (Java string interning).
         * Xerces relies on these shared instances when comparing QName parts.
         */
        @Override
        public String getSymbol(String symbol) {
            return symbol.intern();
        }

        /**
         * Looks up the namespace URI for a prefix and returns its shared string instance for Xerces.
         * Returns null when the prefix has no namespace or maps to an empty namespace.
         */
        @Override
        public String getURI(String prefix) {
            String namespace = this.namespaceResolver.resolvePrefix(prefix);
            return namespace == null || namespace.isEmpty() ? null : getSymbol(namespace);
        }

        /**
         * Uses Locale.ROOT so Xerces messages do not depend on the machine's default language or region.
         */
        @Override
        public Locale getLocale() {
            return Locale.ROOT;
        }
    }
}
