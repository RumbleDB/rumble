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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.xs.XSWildcard;

import org.rumbledb.context.Name;
import org.rumbledb.exceptions.OurBadException;
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
 * Infers which children, attributes and descendants a path step selects from a schema-typed node, by reading the
 * content model of its schema type: the element and attribute declarations that the type allows, and how many times
 * each may occur.
 * <p>
 * For example, if the type of p:order is a sequence of one or more p:item elements followed by one p:customer element,
 * {@code $order/p:customer} selects exactly one {@code element(p:customer, p:Customer)}, and {@code $order/p:missing}
 * selects nothing, which static typing reports as an error.
 * <p>
 * Descendant steps go through {@link #getDescendantOrSelfType}: E//S abbreviates E/descendant-or-self::node()/S, whose
 * type is a union of E's type, every element type that the schema allows below it, and text, comment and
 * processing-instruction nodes. S is then an ordinary child or attribute step from that union.
 * <p>
 * A node may have one of several schema types, and the result covers all of them: each member of a union type, each
 * declaration of a substitution group, and each type derived from the declared one, which an instance may select with
 * xsi:type. When the schema does not describe every node a step can select, for example because a wildcard (xs:any)
 * allows elements it does not declare, the methods return empty and static typing keeps its schema-unaware inference.
 * <p>
 * The {@link XmlSchemaCatalog} creates this class and provides the node types of declarations and the derived types.
 */
public final class SchemaContentModel {

    private final XmlSchemaCatalog catalog;

    SchemaContentModel(XmlSchemaCatalog catalog) {
        this.catalog = catalog;
    }

    /**
     * Whether a step from the type can be inferred from the schema: some member is an element or document whose
     * schema type a step can look up, and every member describes the nodes it selects, as such a node or as a node
     * without children or attributes.
     */
    public static boolean isSchemaTyped(ItemType type) {
        return type.getMemberTypes().stream().anyMatch(SchemaContentModel::isSchemaTypedNode)
                && type.allMemberTypesMatch(member -> isSchemaTypedNode(member) || isLeafNode(member));
    }

    private static boolean isSchemaTypedNode(ItemType type) {
        return type instanceof DocumentNodeItemType document
                ? isSchemaTypedElement(document.getElementTestType())
                : isSchemaTypedElement(type);
    }

    /** Text, comment, and processing-instruction nodes, which descendant-or-self::node() also selects. */
    private static boolean isLeafNode(ItemType type) {
        return type.isSubtypeOf(BuiltinTypesCatalogue.textNode)
                || type.isSubtypeOf(BuiltinTypesCatalogue.commentNode)
                || type.isSubtypeOf(BuiltinTypesCatalogue.processingInstructionNode);
    }

    private static boolean isSchemaTypedElement(ItemType type) {
        return type instanceof SchemaElementNodeItemType
                || (type instanceof ElementNodeItemType element
                        && element.getSchemaTypeName() != null
                        && !describesNoContent(element.getSchemaTypeName()));
    }

    /** xs:untyped and xs:anyType, the annotations of constructed elements, allow any content. */
    private static boolean describesNoContent(Name typeName) {
        return Name.XS_NS.equals(typeName.getNamespace())
                && (typeName.getLocalName().equals("untyped")
                        || typeName.getLocalName().equals("anyType"));
    }

    /**
     * Returns the nodes that a child or attribute step selects from one schema-typed element or document. It is empty
     * when the schema does not describe every node the step can select, for example through a wildcard.
     * <p>
     * The cardinality is for one context node; the caller repeats it for each node of the context sequence. Different
     * declarations keep their own types, joined into a union, so that further steps still use each one's content model.
     *
     * @param name the name of the selected nodes, or null for any name, as in {@code *} or {@code element()}
     */
    public Optional<SequenceType> getStepType(ItemType contextType, boolean attributeAxis, Name name) {
        // Every element may carry xsi:type, xsi:nil, xsi:schemaLocation and xsi:noNamespaceSchemaLocation without a
        // declaration, so a step that may select them is left to the schema-unaware inference.
        if (!isSchemaTyped(contextType)
                || (attributeAxis && (name == null || Name.XSI_NS.equals(name.getNamespace())))) {
            return Optional.empty();
        }
        return select(contextType, attributeAxis, name, SelectionMode.EVERY_NODE)
                .map(Selection::sequenceType);
    }

    /**
     * Returns the nodes that descendant-or-self::node() selects from a schema-typed element or document: the context,
     * every element that the schema allows below it, and text, comment, and processing-instruction nodes. It is empty
     * when a descendant may be an element that the schema does not describe.
     */
    public Optional<SequenceType> getDescendantOrSelfType(ItemType contextType) {
        if (!isSchemaTyped(contextType)) {
            return Optional.empty();
        }
        return selfAndDescendantElements(contextType, SelectionMode.EVERY_NODE).map(nodes -> {
            List<ItemType> nodeTypes = new ArrayList<>(nodes);
            nodeTypes.addAll(List.of(
                    BuiltinTypesCatalogue.textNode,
                    BuiltinTypesCatalogue.commentNode,
                    BuiltinTypesCatalogue.processingInstructionNode));
            // The context node itself is always selected.
            return new SequenceType(
                    ItemTypeFactory.createInferredUnionType(nodeTypes), SequenceCardinality.ONE_OR_MANY);
        });
    }

    /**
     * The context followed by the type of every element below it, each listed once since types can be recursive. With
     * {@link SelectionMode#DECLARED_ONLY}, it only follows the elements that the schema declares.
     */
    private Optional<List<ItemType>> selfAndDescendantElements(ItemType contextType, SelectionMode mode) {
        List<ItemType> result = new ArrayList<>(List.of(contextType));
        Set<ItemType> seen = new HashSet<>(result);
        for (int index = 0; index < result.size(); index++) {
            Optional<Selection> children = select(result.get(index), false, null, mode);
            if (children.isEmpty()) {
                return Optional.empty();
            }
            for (ItemType child : children.get().nodeTypes()) {
                if (seen.add(child)) {
                    result.add(child);
                }
            }
        }
        return Optional.of(result);
    }

    /**
     * Returns the names of the children or attributes that the schema declares for a schema-typed element or document,
     * or with throughDescendants for it and each of its descendant elements. Unlike step types, the names ignore
     * wildcards, so they may not include every node that a step can select.
     * <p>
     * For example, from schema-element(p:order) the child names are p:item, p:special, p:paid, p:due, p:customer and
     * p:meta. The LSP suggests them for completion, and static typing lists them when a step selects nothing.
     */
    public Optional<List<Name>> getStepNames(ItemType contextType, boolean attributeAxis, boolean throughDescendants) {
        if (!isSchemaTyped(contextType)) {
            return Optional.empty();
        }
        List<ItemType> parents = throughDescendants
                ? declared(selfAndDescendantElements(contextType, SelectionMode.DECLARED_ONLY))
                : List.of(contextType);
        return Optional.of(parents.stream()
                .flatMap(parent ->
                        declared(select(parent, attributeAxis, null, SelectionMode.DECLARED_ONLY)).nodeTypes().stream())
                .flatMap(nodeType -> nodeNames(nodeType).stream())
                .distinct()
                .toList());
    }

    /**
     * Unwraps a result computed with {@link SelectionMode#DECLARED_ONLY}, which is never empty: unknown cases and
     * wildcards add nothing instead of making the selection give up.
     */
    private static <T> T declared(Optional<T> result) {
        return result.orElseThrow(() -> new OurBadException("Selecting only declared nodes must not give up."));
    }

    /** The names of a selected attribute or element declaration; element(*, T) has none to list. */
    private static List<Name> nodeNames(ItemType nodeType) {
        if (nodeType instanceof AttributeNodeItemType attribute) {
            return List.of(attribute.getNodeName());
        }
        return alternatives(nodeType).stream()
                .map(ElementNodeItemType::getNodeName)
                .filter(Objects::nonNull)
                .toList();
    }

    /** Which nodes a selection must describe. */
    private enum SelectionMode {
        /**
         * Every node that the step can select, as type inference needs: the selection gives up (is empty) when an
         * unknown type or a wildcard may allow nodes that the schema does not declare.
         */
        EVERY_NODE,
        /**
         * Only the nodes that the schema declares, as tools listing names need: unknown types and wildcards add none.
         */
        DECLARED_ONLY
    }

    /** The node types of the declarations that a step selects from one context node, and how many nodes it selects. */
    private record Selection(Set<ItemType> nodeTypes, SequenceCardinality cardinality) {
        private static final Selection NONE = new Selection(Set.of(), SequenceCardinality.EMPTY);

        /**
         * Combines the selections of two cases that one context node may be, such as two members of a union: the node
         * is one or the other, so the step selects the nodes of either selection, as many as either allows.
         */
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

    /**
     * Selects from each case that a context node of this type may be: each member of a union, and each declaration
     * that a schema-element test allows through its substitution group. It is empty as soon as one case is unknown,
     * since the result must cover every node that the step can select.
     * <p>
     * With {@link SelectionMode#DECLARED_ONLY}, it selects only the nodes that the schema declares: an unknown case or
     * a matching wildcard adds nothing instead of making the result empty.
     */
    private Optional<Selection> select(ItemType contextType, boolean attributeAxis, Name name, SelectionMode mode) {
        Selection result = null;
        for (ItemType member : contextType.getMemberTypes()) {
            List<? extends ItemType> contexts =
                    member instanceof ElementNodeItemType ? alternatives(member) : List.of(member);
            for (ItemType context : contexts) {
                Optional<Selection> selection = selectFromNode(context, attributeAxis, name, mode);
                if (selection.isEmpty()) {
                    return Optional.empty();
                }
                result = result == null ? selection.get() : result.or(selection.get());
            }
        }
        return Optional.of(result == null ? Selection.NONE : result);
    }

    /**
     * Selects from one case: a text, comment or processing-instruction node has no children or attributes, a document
     * has only its root element, and an element selects through its schema type.
     */
    private Optional<Selection> selectFromNode(ItemType context, boolean attributeAxis, Name name, SelectionMode mode) {
        if (isLeafNode(context)) {
            return Optional.of(Selection.NONE);
        }
        if (context instanceof DocumentNodeItemType document) {
            return Optional.of(
                    attributeAxis ? Selection.NONE : selectDocumentElement(document.getElementTestType(), name));
        }
        return selectFromElement((ElementNodeItemType) context, attributeAxis, name, mode);
    }

    /**
     * Selects through the content model of the element's schema type and of every type derived from it. It is empty
     * when the schema type cannot be resolved, or is xs:anyType, whose content can be anything; with
     * {@link SelectionMode#DECLARED_ONLY}, such an element selects nothing instead.
     */
    private Optional<Selection> selectFromElement(
            ElementNodeItemType context, boolean attributeAxis, Name name, SelectionMode mode) {
        Optional<XSTypeDefinition> type = Optional.ofNullable(context.getSchemaTypeName())
                .flatMap(this.catalog::resolveType)
                .filter(definition -> !XmlSchemaCatalog.isAnyType(definition));
        if (type.isEmpty()) {
            return mode == SelectionMode.DECLARED_ONLY ? Optional.of(Selection.NONE) : Optional.empty();
        }
        Set<ItemType> nodeTypes = new LinkedHashSet<>();
        // A nilled element has no children.
        SequenceCardinality cardinality = !attributeAxis && context.isNillable() ? SequenceCardinality.EMPTY : null;
        // An instance may select a derived type with xsi:type.
        for (XSTypeDefinition derived : this.catalog.typeAndDerivedTypes(type.get())) {
            Optional<Occurrences> occurrences = derived instanceof XSComplexTypeDefinition complexType
                    ? attributeAxis
                            ? collectAttributes(complexType, name, mode, nodeTypes)
                            : collectChildElements(complexType.getParticle(), name, mode, nodeTypes)
                    : Optional.of(Occurrences.NONE);
            if (occurrences.isEmpty()) {
                return Optional.empty();
            }
            cardinality = cardinality == null
                    ? occurrences.get().cardinality()
                    : cardinality.union(occurrences.get().cardinality());
        }
        return Optional.of(new Selection(nodeTypes, cardinality));
    }

    /**
     * A document node that matches document-node(E) has exactly one element child, which matches E. With a name, the
     * child is selected when E's declarations have that name, and only possibly when some have another name or, as in
     * element(*, T), any name.
     */
    private static Selection selectDocumentElement(ItemType elementType, Name name) {
        if (name == null) {
            return new Selection(Set.of(elementType), SequenceCardinality.ONE);
        }
        List<ElementNodeItemType> alternatives = alternatives(elementType);
        // The root of element(*, T) has a name that is not known statically, so it may or may not match.
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

    /** The declarations that an element test matches: the alternatives of schema-element(E), or the test itself. */
    private static List<ElementNodeItemType> alternatives(ItemType elementType) {
        return elementType instanceof SchemaElementNodeItemType schemaElement
                ? schemaElement.getAlternatives()
                : List.of((ElementNodeItemType) elementType);
    }

    /**
     * Walks one particle of a content model, collects the element declarations with the name, and returns how many
     * matching children the particle allows. It is empty when a wildcard may allow a matching element that the schema
     * does not declare.
     * <p>
     * A particle is an entry of the content model with its minOccurs and maxOccurs, which repeat what its term allows:
     * <ul>
     * <li>an element declaration matches once, or only possibly when just some of its substitution group has the
     * name;</li>
     * <li>a wildcard (xs:any) matches nothing when its namespaces exclude the name;</li>
     * <li>a sequence or all group contains all of its particles, so their counts add up, and a choice contains one of
     * them.</li>
     * </ul>
     * For example, a sequence of p:item (maxOccurs="unbounded") and p:customer allows one or more p:item children and
     * exactly one p:customer child.
     *
     * @param particle the content model of a complex type, or null when it has empty or simple content
     * @param mode with {@link SelectionMode#DECLARED_ONLY}, wildcards match nothing, to collect only declared elements
     */
    private Optional<Occurrences> collectChildElements(
            XSParticle particle, Name name, SelectionMode mode, Set<ItemType> elementTypes) {
        if (particle == null) {
            return Optional.of(Occurrences.NONE);
        }
        Occurrences termOccurrences;
        if (particle.getTerm() instanceof XSElementDeclaration declaration) {
            boolean matched = false;
            boolean unmatched = false;
            for (XSElementDeclaration candidate : substitutableDeclarations(declaration)) {
                if (name == null || hasName(candidate.getNamespace(), candidate.getName(), name)) {
                    elementTypes.add(this.catalog.elementType(candidate));
                    matched = true;
                } else {
                    unmatched = true;
                }
            }
            termOccurrences = !matched ? Occurrences.NONE : unmatched ? Occurrences.OPTIONAL : Occurrences.ONE;
        } else if (particle.getTerm() instanceof XSWildcard wildcard) {
            if (mode == SelectionMode.EVERY_NODE && (name == null || allowsNamespace(wildcard, name.getNamespace()))) {
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
                        collectChildElements((XSParticle) particles.item(index), name, mode, elementTypes);
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

    /**
     * A reference to a global declaration also accepts the members of its substitution group. Abstract declarations
     * are dropped, since no element can be validated against them.
     */
    private List<XSElementDeclaration> substitutableDeclarations(XSElementDeclaration declaration) {
        List<XSElementDeclaration> result = new ArrayList<>();
        result.add(declaration);
        if (declaration.getScope() == XSConstants.SCOPE_GLOBAL) {
            XSObjectList substitutions = this.catalog.getSchemaModel().getSubstitutionGroup(declaration);
            for (int index = 0; index < substitutions.getLength(); index++) {
                result.add((XSElementDeclaration) substitutions.item(index));
            }
        }
        result.removeIf(XSElementDeclaration::getAbstract);
        return result;
    }

    /**
     * Collects the attribute declarations with the name, or every one when name is null, and returns how many of them
     * an element has: each required attribute counts once and each optional one possibly once. It is empty when an
     * attribute wildcard (xs:anyAttribute) may allow the name, unless {@link SelectionMode#DECLARED_ONLY}. The
     * attribute uses of a type include those inherited from its base types.
     */
    private Optional<Occurrences> collectAttributes(
            XSComplexTypeDefinition type, Name name, SelectionMode mode, Set<ItemType> attributeTypes) {
        XSWildcard wildcard = type.getAttributeWildcard();
        if (mode == SelectionMode.EVERY_NODE
                && wildcard != null
                && (name == null || allowsNamespace(wildcard, name.getNamespace()))) {
            return Optional.empty();
        }
        XSObjectList uses = type.getAttributeUses();
        Occurrences occurrences = Occurrences.NONE;
        for (int index = 0; index < uses.getLength(); index++) {
            XSAttributeUse use = (XSAttributeUse) uses.item(index);
            XSAttributeDeclaration declaration = use.getAttrDeclaration();
            if (name == null || hasName(declaration.getNamespace(), declaration.getName(), name)) {
                attributeTypes.add(this.catalog.attributeType(declaration));
                occurrences = occurrences.plus(use.getRequired() ? Occurrences.ONE : Occurrences.OPTIONAL);
            }
        }
        return Optional.of(occurrences);
    }

    /** Xerces uses null for no namespace, and Rumble may use null or the empty string. */
    private static boolean hasName(String namespace, String localName, Name name) {
        return Objects.equals(XmlNameCodec.emptyToNull(namespace), XmlNameCodec.emptyToNull(name.getNamespace()))
                && localName.equals(name.getLocalName());
    }

    /** Whether a wildcard allows names in the namespace: ##any, a list of namespaces, or ##other (none of a list). */
    private static boolean allowsNamespace(XSWildcard wildcard, String namespace) {
        boolean listed = wildcard.getNsConstraintList().contains(XmlNameCodec.emptyToNull(namespace));
        return switch (wildcard.getConstraintType()) {
            case XSWildcard.NSCONSTRAINT_LIST -> listed;
            case XSWildcard.NSCONSTRAINT_NOT -> !listed;
            default -> true;
        };
    }

    /** Bounds on how many matching nodes a part of a content model contains, like minOccurs and maxOccurs. */
    private record Occurrences(long min, long max) {
        private static final long UNBOUNDED = Long.MAX_VALUE;
        private static final Occurrences NONE = new Occurrences(0, 0);
        private static final Occurrences OPTIONAL = new Occurrences(0, 1);
        private static final Occurrences ONE = new Occurrences(1, 1);

        /** Both parts occur, as in a sequence, so their counts add up. */
        private Occurrences plus(Occurrences other) {
            return new Occurrences(
                    this.min + other.min,
                    this.max == UNBOUNDED || other.max == UNBOUNDED ? UNBOUNDED : this.max + other.max);
        }

        /** One of the parts occurs, as in a choice. */
        private Occurrences or(Occurrences other) {
            return new Occurrences(Math.min(this.min, other.min), Math.max(this.max, other.max));
        }

        /** The part repeats between minOccurs and maxOccurs times. */
        private Occurrences times(long minOccurs, long maxOccurs) {
            long max = this.max == 0 || maxOccurs == 0
                    ? 0
                    : this.max == UNBOUNDED || maxOccurs == UNBOUNDED ? UNBOUNDED : this.max * maxOccurs;
            return new Occurrences(this.min * minOccurs, max);
        }

        /** The cardinality that allows every count between min and max. */
        private SequenceCardinality cardinality() {
            return SequenceCardinality.fromPossibilities(this.min == 0, this.min <= 1 && this.max >= 1, this.max >= 2);
        }
    }
}
