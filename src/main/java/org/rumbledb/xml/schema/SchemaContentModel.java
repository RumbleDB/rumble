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
import org.rumbledb.types.DocumentNodeItemType;
import org.rumbledb.types.ElementNodeItemType;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SchemaElementNodeItemType;
import org.rumbledb.types.SequenceCardinality;
import org.rumbledb.types.SequenceType;

/** Reads the content models of a schema to infer which children and attributes a path step selects. */
public final class SchemaContentModel {

    private final XmlSchemaCatalog catalog;

    SchemaContentModel(XmlSchemaCatalog catalog) {
        this.catalog = catalog;
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
                .flatMap(this.catalog::resolveType)
                .filter(definition -> !XmlSchemaCatalog.isAnyType(definition));
        if (type.isEmpty()) {
            return Optional.empty();
        }
        Set<ItemType> nodeTypes = new LinkedHashSet<>();
        SequenceCardinality cardinality = null;
        // An instance may select a derived type with xsi:type.
        for (XSTypeDefinition derived : this.catalog.typeAndDerivedTypes(type.get())) {
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
                    elementTypes.add(this.catalog.elementType(candidate));
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
            XSObjectList substitutions = this.catalog.getSchemaModel().getSubstitutionGroup(declaration);
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
                attributeTypes.add(this.catalog.attributeType(declaration));
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
}
