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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.xml.validation.Schema;

import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.xs.XSValue;
import org.apache.xerces.xs.XSWildcard;

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
import org.rumbledb.types.DocumentNodeItemType;
import org.rumbledb.types.ElementNodeItemType;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SchemaElementNodeItemType;
import org.rumbledb.types.SequenceCardinality;
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
    private final List<XSComplexTypeDefinition> complexTypes;
    // Typed values depend only on the immutable schema model, but finding derived types scans every complex type.
    private final Map<XSTypeDefinition, Optional<SequenceType>> typedValueTypes = new IdentityHashMap<>();

    XmlSchemaCatalog(
            @NonNull XSModel schemaModel, @NonNull Schema validationSchema, Map<String, String> namespacePrefixes) {
        this.schemaModel = schemaModel;
        this.validationSchema = validationSchema;
        this.typeMapper = new XmlSchemaTypeMapper(namespacePrefixes);
        this.typedValueConverter = new XercesTypedValueConverter(this.typeMapper);
        this.simpleTypeCaster = new XercesSimpleTypeCaster(this.typeMapper, this.typedValueConverter);
        this.complexTypes = collectComplexTypes(schemaModel);
    }

    public Optional<XSTypeDefinition> getTypeDefinition(@NonNull Name name) {
        return Optional.ofNullable(this.schemaModel.getTypeDefinition(name.getLocalName(), name.getNamespace()));
    }

    /**
     * Returns all global named types in the catalog, including built-in types, lists, unions, and complex types.
     * Names retain schema-import prefixes; callers can use {@link #isSchemaCastTarget(Name)} to select constructor
     * candidates.
     */
    public List<Name> getNamedTypeNames() {
        XSNamedMap types = this.schemaModel.getComponents(XSConstants.TYPE_DEFINITION);
        List<Name> names = new ArrayList<>();
        for (int index = 0; index < types.getLength(); index++) {
            XSTypeDefinition type = (XSTypeDefinition) types.item(index);
            names.add(this.typeMapper.nameOf(type));
        }
        return List.copyOf(names);
    }

    /** Returns the name of the only global element declaration that an element can have, if there is one. */
    public Optional<Name> getOnlyElementDeclarationName() {
        XSNamedMap elements = this.schemaModel.getComponents(XSConstants.ELEMENT_DECLARATION);
        XSElementDeclaration only = null;
        for (int index = 0; index < elements.getLength(); index++) {
            XSElementDeclaration declaration = (XSElementDeclaration) elements.item(index);
            if (!declaration.getAbstract()) {
                if (only != null) {
                    return Optional.empty();
                }
                only = declaration;
            }
        }
        return Optional.ofNullable(only)
                .map(declaration -> this.typeMapper.declarationName(declaration.getNamespace(), declaration.getName()));
    }

    /** Resolves a global declaration and the substitutions allowed by its blocking constraints. */
    public SchemaElementNodeItemType getSchemaElementTest(Name name, ExceptionMetadata metadata) {
        return findSchemaElementTest(name)
                .orElseThrow(() -> new SemanticException(
                        "Unknown global schema element: " + name, ErrorCode.UndeclaredVariableErrorCode, metadata));
    }

    /** Like {@link #getSchemaElementTest}, but empty when there is no global declaration with this name. */
    public Optional<SchemaElementNodeItemType> findSchemaElementTest(Name name) {
        XSElementDeclaration declaration = this.schemaModel.getElementDeclaration(
                name.getLocalName(), XmlNameCodec.emptyToNull(name.getNamespace()));
        if (declaration == null) {
            return Optional.empty();
        }
        List<ElementNodeItemType> alternatives = new ArrayList<>();
        addElementAlternative(declaration, alternatives);

        XSObjectList substitutions = this.schemaModel.getSubstitutionGroup(declaration);
        for (int i = 0; i < substitutions.getLength(); i++) {
            addElementAlternative((XSElementDeclaration) substitutions.item(i), alternatives);
        }

        return Optional.of(new SchemaElementNodeItemType(name, alternatives));
    }

    private void addElementAlternative(XSElementDeclaration declaration, List<ElementNodeItemType> alternatives) {
        if (!declaration.getAbstract()) {
            alternatives.add(elementType(declaration));
        }
    }

    private ElementNodeItemType elementType(XSElementDeclaration declaration) {
        XmlSchemaTypeAnnotation annotation = this.typeMapper.mapTypeAnnotation(declaration.getTypeDefinition());
        return new ElementNodeItemType(
                this.typeMapper.declarationName(declaration.getNamespace(), declaration.getName()),
                annotation.name(),
                annotation.typeHierarchy(),
                declaration.getNillable(),
                matchingTypeNames(declaration.getTypeDefinition()),
                typedValueType(declaration.getTypeDefinition()).orElse(null));
    }

    /** Attribute declaration tests have the same matching rules as a named, typed attribute test. */
    public AttributeNodeItemType getSchemaAttributeTest(Name name, ExceptionMetadata metadata) {
        XSAttributeDeclaration declaration = this.schemaModel.getAttributeDeclaration(
                name.getLocalName(), XmlNameCodec.emptyToNull(name.getNamespace()));
        if (declaration == null) {
            throw new SemanticException(
                    "Unknown global schema attribute: " + name, ErrorCode.UndeclaredVariableErrorCode, metadata);
        }
        return attributeType(name, declaration);
    }

    private AttributeNodeItemType attributeType(Name name, XSAttributeDeclaration declaration) {
        XmlSchemaTypeAnnotation annotation = this.typeMapper.mapTypeAnnotation(declaration.getTypeDefinition());
        return new AttributeNodeItemType(
                name,
                annotation.name(),
                annotation.typeHierarchy(),
                matchingTypeNames(declaration.getTypeDefinition()),
                typedValueType(declaration.getTypeDefinition()).orElse(null));
    }

    /** The test element(nodeName, typeName), or element(*, typeName) when nodeName is null. */
    public ElementNodeItemType getElementTest(
            Name nodeName, Name typeName, boolean nillable, ExceptionMetadata metadata) {
        return new ElementNodeItemType(
                nodeName,
                typeName,
                getTypeHierarchy(typeName, metadata),
                nillable,
                getTypedValueType(typeName).orElse(null));
    }

    /** The test attribute(nodeName, typeName), or attribute(*, typeName) when nodeName is null. */
    public AttributeNodeItemType getAttributeTest(Name nodeName, Name typeName, ExceptionMetadata metadata) {
        return new AttributeNodeItemType(
                nodeName,
                typeName,
                getTypeHierarchy(typeName, metadata),
                getTypedValueType(typeName).orElse(null));
    }

    /**
     * Whether every member of the type is an element or document whose schema type a step can look up. A step from
     * a union can only be inferred from the schema if every member describes the nodes it selects.
     */
    public static boolean isSchemaTyped(ItemType type) {
        return type.allMemberTypesMatch(member -> member instanceof DocumentNodeItemType document
                ? isSchemaTypedElement(document.getElementTestType())
                : isSchemaTypedElement(member));
    }

    private static boolean isSchemaTypedElement(ItemType type) {
        return type instanceof SchemaElementNodeItemType
                || (type instanceof ElementNodeItemType element && element.getSchemaTypeName() != null);
    }

    /**
     * Returns the nodes that a child or attribute step selects from one schema-typed element or document, by name or
     * with any name when name is null. It is empty when the schema does not describe every node the step can select,
     * for example through a wildcard.
     */
    public Optional<SequenceType> getStepType(ItemType contextType, boolean attributeAxis, Name name) {
        // Instances also carry xsi attributes, such as xsi:type, which the schema does not declare.
        if (!isSchemaTyped(contextType)
                || (attributeAxis && (name == null || Name.XSI_NS.equals(name.getNamespace())))) {
            return Optional.empty();
        }
        return select(contextType, attributeAxis, name).map(Selection::sequenceType);
    }

    /** Nodes that a step selects, before their types are combined. */
    private record Selection(Set<ItemType> nodeTypes, SequenceCardinality cardinality) {
        private static final Selection NONE = new Selection(Set.of(), SequenceCardinality.EMPTY);

        /** One context node has one of the types, so it selects the nodes of either selection. */
        private Selection or(Selection other) {
            Set<ItemType> nodeTypes = new LinkedHashSet<>(this.nodeTypes);
            nodeTypes.addAll(other.nodeTypes);
            return new Selection(nodeTypes, this.cardinality.union(other.cardinality));
        }

        /** Different declarations keep their own types, joined into a union. */
        private SequenceType sequenceType() {
            return this.nodeTypes.isEmpty()
                    ? SequenceType.createSequenceType("()")
                    : new SequenceType(
                            ItemTypeFactory.createInferredUnionType(new ArrayList<>(this.nodeTypes)), this.cardinality);
        }
    }

    private Optional<Selection> select(ItemType contextType, boolean attributeAxis, Name name) {
        Selection result = null;
        for (ItemType member : contextType.getMemberTypes()) {
            List<? extends ItemType> contexts =
                    member instanceof DocumentNodeItemType ? List.of(member) : alternatives(member);
            for (ItemType context : contexts) {
                Optional<Selection> selection = context instanceof DocumentNodeItemType document
                        ? Optional.of(
                                attributeAxis
                                        ? Selection.NONE
                                        : selectDocumentElement(document.getElementTestType(), name))
                        : selectFromElement((ElementNodeItemType) context, attributeAxis, name);
                if (selection.isEmpty()) {
                    return Optional.empty();
                }
                result = result == null ? selection.get() : result.or(selection.get());
            }
        }
        return Optional.of(result == null ? Selection.NONE : result);
    }

    private Optional<Selection> selectFromElement(ElementNodeItemType context, boolean attributeAxis, Name name) {
        Optional<XSTypeDefinition> type = Optional.ofNullable(context.getSchemaTypeName())
                .flatMap(this::resolveType)
                .filter(definition -> !isAnyType(definition));
        if (type.isEmpty()) {
            return Optional.empty();
        }
        Set<ItemType> nodeTypes = new LinkedHashSet<>();
        SequenceCardinality cardinality = null;
        // An instance may select a derived type with xsi:type.
        for (XSTypeDefinition derived : typeAndDerivedTypes(type.get())) {
            Optional<Occurrences> occurrences = derived instanceof XSComplexTypeDefinition complexType
                    ? attributeAxis
                            ? collectAttributes(complexType, name, nodeTypes)
                            : collectChildElements(complexType.getParticle(), name, nodeTypes)
                    : Optional.of(Occurrences.NONE);
            if (occurrences.isEmpty()) {
                return Optional.empty();
            }
            cardinality = cardinality == null
                    ? occurrences.get().cardinality()
                    : cardinality.union(occurrences.get().cardinality());
        }
        // A nilled element has no children.
        if (!attributeAxis && context.isNillable()) {
            cardinality = cardinality.union(SequenceCardinality.EMPTY);
        }
        return Optional.of(new Selection(nodeTypes, cardinality));
    }

    /** A document node that matches document-node(E) has exactly one element child, which matches E. */
    private static Selection selectDocumentElement(ItemType elementType, Name name) {
        if (name == null) {
            return new Selection(Set.of(elementType), SequenceCardinality.ONE);
        }
        List<ElementNodeItemType> alternatives = alternatives(elementType);
        // An element(*, T) child may have any name.
        List<ElementNodeItemType> matching = alternatives.stream()
                .filter(alternative -> alternative.getNodeName() == null
                        || alternative.getNodeName().equals(name))
                .toList();
        boolean alwaysMatches = matching.size() == alternatives.size()
                && matching.stream().allMatch(alternative -> alternative.getNodeName() != null);
        return new Selection(
                new LinkedHashSet<>(matching),
                matching.isEmpty()
                        ? SequenceCardinality.EMPTY
                        : alwaysMatches ? SequenceCardinality.ONE : SequenceCardinality.ZERO_OR_ONE);
    }

    private static List<ElementNodeItemType> alternatives(ItemType elementType) {
        return elementType instanceof SchemaElementNodeItemType schemaElement
                ? schemaElement.getAlternatives()
                : List.of((ElementNodeItemType) elementType);
    }

    private Optional<XSTypeDefinition> resolveType(Name name) {
        return getTypeDefinition(name).or(() -> this.typeMapper.anonymousType(name));
    }

    private static boolean isAnyType(XSTypeDefinition type) {
        return Name.XS_NS.equals(type.getNamespace()) && "anyType".equals(type.getName());
    }

    /** Collects the declarations that the particle allows with the name, or is empty if a wildcard allows it. */
    private Optional<Occurrences> collectChildElements(XSParticle particle, Name name, Set<ItemType> elementTypes) {
        if (particle == null) {
            return Optional.of(Occurrences.NONE);
        }
        Occurrences termOccurrences;
        if (particle.getTerm() instanceof XSElementDeclaration declaration) {
            boolean matched = false;
            boolean unmatched = false;
            for (XSElementDeclaration candidate : substitutableDeclarations(declaration)) {
                if (name == null || hasName(candidate.getNamespace(), candidate.getName(), name)) {
                    elementTypes.add(elementType(candidate));
                    matched = true;
                } else {
                    unmatched = true;
                }
            }
            termOccurrences = !matched ? Occurrences.NONE : unmatched ? Occurrences.OPTIONAL : Occurrences.ONE;
        } else if (particle.getTerm() instanceof XSWildcard wildcard) {
            if (name == null || allowsNamespace(wildcard, name.getNamespace())) {
                return Optional.empty();
            }
            termOccurrences = Occurrences.NONE;
        } else {
            XSModelGroup group = (XSModelGroup) particle.getTerm();
            boolean choice = group.getCompositor() == XSModelGroup.COMPOSITOR_CHOICE;
            termOccurrences = null;
            XSObjectList particles = group.getParticles();
            for (int index = 0; index < particles.getLength(); index++) {
                Optional<Occurrences> occurrences =
                        collectChildElements((XSParticle) particles.item(index), name, elementTypes);
                if (occurrences.isEmpty()) {
                    return occurrences;
                }
                termOccurrences = termOccurrences == null
                        ? occurrences.get()
                        : choice ? termOccurrences.or(occurrences.get()) : termOccurrences.plus(occurrences.get());
            }
            if (termOccurrences == null) {
                termOccurrences = Occurrences.NONE;
            }
        }
        return Optional.of(termOccurrences.times(
                particle.getMinOccurs(),
                particle.getMaxOccursUnbounded() ? Occurrences.UNBOUNDED : particle.getMaxOccurs()));
    }

    /** A global declaration's position also accepts the members of its substitution group. */
    private List<XSElementDeclaration> substitutableDeclarations(XSElementDeclaration declaration) {
        List<XSElementDeclaration> result = new ArrayList<>();
        result.add(declaration);
        if (declaration.getScope() == XSConstants.SCOPE_GLOBAL) {
            XSObjectList substitutions = this.schemaModel.getSubstitutionGroup(declaration);
            for (int index = 0; index < substitutions.getLength(); index++) {
                result.add((XSElementDeclaration) substitutions.item(index));
            }
        }
        result.removeIf(XSElementDeclaration::getAbstract);
        return result;
    }

    private Optional<Occurrences> collectAttributes(
            XSComplexTypeDefinition type, Name name, Set<ItemType> attributeTypes) {
        XSWildcard wildcard = type.getAttributeWildcard();
        if (wildcard != null && allowsNamespace(wildcard, name.getNamespace())) {
            return Optional.empty();
        }
        XSObjectList uses = type.getAttributeUses();
        for (int index = 0; index < uses.getLength(); index++) {
            XSAttributeUse use = (XSAttributeUse) uses.item(index);
            XSAttributeDeclaration declaration = use.getAttrDeclaration();
            if (hasName(declaration.getNamespace(), declaration.getName(), name)) {
                attributeTypes.add(attributeType(
                        this.typeMapper.declarationName(declaration.getNamespace(), declaration.getName()),
                        declaration));
                return Optional.of(use.getRequired() ? Occurrences.ONE : Occurrences.OPTIONAL);
            }
        }
        return Optional.of(Occurrences.NONE);
    }

    private static boolean hasName(String namespace, String localName, Name name) {
        return Objects.equals(XmlNameCodec.emptyToNull(namespace), XmlNameCodec.emptyToNull(name.getNamespace()))
                && localName.equals(name.getLocalName());
    }

    private static boolean allowsNamespace(XSWildcard wildcard, String namespace) {
        boolean listed = wildcard.getNsConstraintList().contains(XmlNameCodec.emptyToNull(namespace));
        return switch (wildcard.getConstraintType()) {
            case XSWildcard.NSCONSTRAINT_LIST -> listed;
            case XSWildcard.NSCONSTRAINT_NOT -> !listed;
            default -> true;
        };
    }

    /** Bounds on how many matching nodes a content model contains. */
    private record Occurrences(long min, long max) {
        private static final long UNBOUNDED = Long.MAX_VALUE;
        private static final Occurrences NONE = new Occurrences(0, 0);
        private static final Occurrences OPTIONAL = new Occurrences(0, 1);
        private static final Occurrences ONE = new Occurrences(1, 1);

        private Occurrences plus(Occurrences other) {
            return new Occurrences(
                    this.min + other.min,
                    this.max == UNBOUNDED || other.max == UNBOUNDED ? UNBOUNDED : this.max + other.max);
        }

        private Occurrences or(Occurrences other) {
            return new Occurrences(Math.min(this.min, other.min), Math.max(this.max, other.max));
        }

        private Occurrences times(long minOccurs, long maxOccurs) {
            long max = this.max == 0 || maxOccurs == 0
                    ? 0
                    : this.max == UNBOUNDED || maxOccurs == UNBOUNDED ? UNBOUNDED : this.max * maxOccurs;
            return new Occurrences(this.min * minOccurs, max);
        }

        private SequenceCardinality cardinality() {
            return SequenceCardinality.fromPossibilities(this.min == 0, this.min <= 1 && this.max >= 1, this.max >= 2);
        }
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
        return this.schemaModel.getNamespaces().contains(XmlNameCodec.emptyToNull(namespace));
    }

    /** Returns the named schema type followed by its base-type chain. */
    public List<Name> getTypeHierarchy(@NonNull Name name, @NonNull ExceptionMetadata metadata) {
        Optional<XSTypeDefinition> definition = getTypeDefinition(name);
        Optional<ItemType> atomicType = definition.isEmpty() ? xqueryAtomicType(name) : Optional.empty();
        if (atomicType.isPresent()) {
            return XmlSchemaTypeAnnotation.forAtomicItemType(atomicType.get()).typeHierarchy();
        }
        if (definition.isEmpty() && isUntyped(name)) {
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
        return this.getTypeDefinition(name)
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
        return simpleTypedValueType(this.simpleType(name));
    }

    /**
     * Returns the typed value of a node annotated with the named type, or empty when it is no narrower than
     * xs:anyAtomicType*. Some XQuery types, such as xs:untyped, have no Xerces definition.
     */
    public Optional<SequenceType> getTypedValueType(Name typeName) {
        Optional<XSTypeDefinition> definition = getTypeDefinition(typeName);
        if (definition.isPresent()) {
            return definition.flatMap(this::typedValueType);
        }
        if (isUntyped(typeName)) {
            return Optional.of(new SequenceType(BuiltinTypesCatalogue.untypedAtomicItem));
        }
        return xqueryAtomicType(typeName).map(SequenceType::new);
    }

    /**
     * XQuery adds atomic types, such as untypedAtomic and the duration subtypes, that Xerces's XSD 1.0 catalog does
     * not contain.
     */
    private static Optional<ItemType> xqueryAtomicType(Name name) {
        if (!Name.XS_NS.equals(name.getNamespace()) || !BuiltinTypesCatalogue.typeExists(name)) {
            return Optional.empty();
        }
        return Optional.of(BuiltinTypesCatalogue.getItemTypeByName(name)).filter(ItemType::isAtomicItemType);
    }

    private static boolean isUntyped(Name name) {
        return Name.XS_NS.equals(name.getNamespace()) && "untyped".equals(name.getLocalName());
    }

    /**
     * Returns the typed value of a node annotated with this type or with a type derived from it, since an instance
     * may select one with xsi:type. Derivation keeps simple content within its base simple type, but a derived
     * complex type can change the content type: for example, an extension of an empty type can add mixed content.
     */
    private Optional<SequenceType> typedValueType(XSTypeDefinition type) {
        // Not computeIfAbsent: the computation recurses into this cache for simple content.
        Optional<SequenceType> cached = this.typedValueTypes.get(type);
        if (cached == null) {
            cached = computeTypedValueType(type);
            this.typedValueTypes.put(type, cached);
        }
        return cached;
    }

    private Optional<SequenceType> computeTypedValueType(XSTypeDefinition type) {
        if (type instanceof XSSimpleTypeDefinition simpleType) {
            // Values of xs:anySimpleType may also be lists.
            return simpleType.getVariety() == XSSimpleTypeDefinition.VARIETY_ABSENT
                    ? Optional.empty()
                    : Optional.of(simpleTypedValueType(simpleType));
        }
        // Simple types are also derived from xs:anyType.
        if (isAnyType(type)) {
            return Optional.empty();
        }
        SequenceType result = null;
        for (XSTypeDefinition derivedType : typeAndDerivedTypes(type)) {
            XSComplexTypeDefinition derived = (XSComplexTypeDefinition) derivedType;
            SequenceType typedValue;
            switch (derived.getContentType()) {
                case XSComplexTypeDefinition.CONTENTTYPE_EMPTY:
                    typedValue = SequenceType.createSequenceType("()");
                    break;
                case XSComplexTypeDefinition.CONTENTTYPE_MIXED:
                    typedValue = new SequenceType(BuiltinTypesCatalogue.untypedAtomicItem);
                    break;
                case XSComplexTypeDefinition.CONTENTTYPE_SIMPLE:
                    Optional<SequenceType> simpleContent = typedValueType(derived.getSimpleType());
                    if (simpleContent.isEmpty()) {
                        return Optional.empty();
                    }
                    typedValue = simpleContent.get();
                    break;
                default:
                    // Atomizing element-only content raises an error instead of producing values.
                    continue;
            }
            result = result == null ? typedValue : result.leastCommonSupertypeWith(typedValue);
        }
        // Atomization callers expect a node's typed value to have an atomic item type.
        return Optional.ofNullable(result).filter(typedValue -> !typedValue.isEmptySequence());
    }

    /** A type and the complex types derived from it, since simple types derived from a simple type stay simple. */
    private List<XSTypeDefinition> typeAndDerivedTypes(XSTypeDefinition type) {
        List<XSTypeDefinition> result = new ArrayList<>();
        result.add(type);
        for (XSComplexTypeDefinition candidate : this.complexTypes) {
            if (candidate != type && candidate.derivedFromType(type, XSConstants.DERIVATION_NONE)) {
                result.add(candidate);
            }
        }
        return result;
    }

    /** Named complex types, and the anonymous complex types of global and local element declarations. */
    private static List<XSComplexTypeDefinition> collectComplexTypes(XSModel schemaModel) {
        Set<XSComplexTypeDefinition> complexTypes = Collections.newSetFromMap(new IdentityHashMap<>());
        Deque<XSTypeDefinition> pending = new ArrayDeque<>();
        XSNamedMap types = schemaModel.getComponents(XSConstants.TYPE_DEFINITION);
        for (int index = 0; index < types.getLength(); index++) {
            pending.add((XSTypeDefinition) types.item(index));
        }
        XSNamedMap elements = schemaModel.getComponents(XSConstants.ELEMENT_DECLARATION);
        for (int index = 0; index < elements.getLength(); index++) {
            pending.add(((XSElementDeclaration) elements.item(index)).getTypeDefinition());
        }
        while (!pending.isEmpty()) {
            if (pending.pop() instanceof XSComplexTypeDefinition complexType && complexTypes.add(complexType)) {
                addLocalElementTypes(complexType.getParticle(), pending);
            }
        }
        return List.copyOf(complexTypes);
    }

    private static void addLocalElementTypes(XSParticle particle, Deque<XSTypeDefinition> pending) {
        if (particle == null) {
            return;
        }
        if (particle.getTerm() instanceof XSElementDeclaration element) {
            pending.add(element.getTypeDefinition());
        } else if (particle.getTerm() instanceof XSModelGroup group) {
            XSObjectList particles = group.getParticles();
            for (int index = 0; index < particles.getLength(); index++) {
                addLocalElementTypes((XSParticle) particles.item(index), pending);
            }
        }
    }

    private SequenceType simpleTypedValueType(XSSimpleTypeDefinition schemaType) {
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
        return this.simpleTypeCaster.cast(name, this.simpleType(name), item, namespaceResolver, metadata);
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
        return this.getTypeDefinition(name)
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
}
