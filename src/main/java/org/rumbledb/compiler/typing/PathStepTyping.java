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
package org.rumbledb.compiler.typing;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.rumbledb.context.Name;
import org.rumbledb.expressions.xml.StepExpr;
import org.rumbledb.expressions.xml.axis.ForwardAxis;
import org.rumbledb.expressions.xml.axis.ForwardStepExpr;
import org.rumbledb.expressions.xml.axis.ReverseAxis;
import org.rumbledb.expressions.xml.axis.ReverseStepExpr;
import org.rumbledb.expressions.xml.node_test.AnyKindTest;
import org.rumbledb.expressions.xml.node_test.AttributeTest;
import org.rumbledb.expressions.xml.node_test.CommentTest;
import org.rumbledb.expressions.xml.node_test.DocumentTest;
import org.rumbledb.expressions.xml.node_test.ElementTest;
import org.rumbledb.expressions.xml.node_test.NameTest;
import org.rumbledb.expressions.xml.node_test.NamespaceNodeTest;
import org.rumbledb.expressions.xml.node_test.NodeTest;
import org.rumbledb.expressions.xml.node_test.PITest;
import org.rumbledb.expressions.xml.node_test.SchemaNodeTest;
import org.rumbledb.expressions.xml.node_test.TextTest;
import org.rumbledb.types.AttributeNodeItemType;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ElementNodeItemType;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SchemaElementNodeItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.xml.schema.SchemaContentModel;

/** Typing rules of axis steps, which use the schema of a schema-typed context node. */
public final class PathStepTyping {

    /** How many declared names an error for an undeclared schema step lists before summarizing the rest. */
    private static final int MAX_LISTED_STEP_NAMES = 10;

    private final StaticTypeErrors errors;

    /**
     * The context type E of each type that descendant-or-self::node() inferred from a schema, so that an error in the
     * step S of E//S can name E. Compared by identity: S steps from that same type object.
     */
    private final Map<ItemType, ItemType> descendantOrSelfContexts = new IdentityHashMap<>();

    public PathStepTyping(StaticTypeErrors errors) {
        this.errors = errors;
    }

    public SequenceType inferStepType(StepExpr stepExpr) {
        SequenceType contextType = stepExpr.getStaticContext().getContextItemStaticType();
        SequenceType inferredType;
        if (contextType != null
                && contextType.getItemType().isNodeItemType()
                && isStaticallyEmptyStep(stepExpr, contextType.getItemType())) {
            inferredType = SequenceType.createSequenceType("()");
        } else {
            inferredType = inferSchemaStepType(stepExpr, contextType)
                    .orElseGet(() -> new SequenceType(
                            inferStepResultItemType(stepExpr), inferStepResultArity(stepExpr, contextType)));
        }

        this.errors.rejectInferredEmpty(inferredType, stepExpr);
        return inferredType;
    }

    /**
     * Child, attribute, and descendant steps from a schema-typed element or document select the nodes its schema
     * declares. E//S abbreviates E/descendant-or-self::node()/S, so S steps from the union of the context and its
     * descendants, and keeps its predicates.
     */
    private Optional<SequenceType> inferSchemaStepType(StepExpr stepExpr, SequenceType contextType) {
        if (contextType == null
                || !SchemaContentModel.isSchemaTyped(contextType.getItemType())
                || !(stepExpr instanceof ForwardStepExpr forwardStep)) {
            return Optional.empty();
        }
        SchemaContentModel contentModel = stepExpr.getStaticContext()
                .getInScopeSchemaTypes()
                .getXmlSchemaCatalog()
                .getContentModel();
        ForwardAxis axis = forwardStep.getForwardAxis();
        NodeTest nodeTest = stepExpr.getNodeTest();
        if (axis.equals(ForwardAxis.DESCENDANT_OR_SELF)) {
            if (!(nodeTest instanceof AnyKindTest)) {
                return Optional.empty();
            }
            Optional<SequenceType> descendantsOrSelf = contentModel.getDescendantOrSelfType(contextType.getItemType());
            descendantsOrSelf.ifPresent(
                    type -> this.descendantOrSelfContexts.put(type.getItemType(), contextType.getItemType()));
            return descendantsOrSelf.map(type -> type.repeated(contextType.getCardinality()));
        }
        boolean attributeAxis = axis.equals(ForwardAxis.ATTRIBUTE);
        if (!attributeAxis && !axis.equals(ForwardAxis.CHILD) && !axis.equals(ForwardAxis.DESCENDANT)) {
            return Optional.empty();
        }
        Name name;
        if (nodeTest instanceof NameTest nameTest && (nameTest.hasQName() || nameTest.hasWildcardOnly())) {
            name = nameTest.hasQName() ? nameTest.getExpandedName() : null;
        } else if (!attributeAxis && nodeTest instanceof ElementTest elementTest) {
            if (elementTest.isNameWithoutTypeCheck()) {
                name = elementTest.getElementName();
            } else if (elementTest.isEmptyCheck() || elementTest.isWildcardOnly()) {
                name = null;
            } else {
                return Optional.empty();
            }
        } else if (attributeAxis
                && nodeTest instanceof AttributeTest attributeTest
                && attributeTest.isNameWithoutTypeCheck()) {
            name = attributeTest.getAttributeName();
        } else {
            return Optional.empty();
        }
        boolean descendantAxis = axis.equals(ForwardAxis.DESCENDANT);
        // The schema only says which children each type has, so descendant::N is computed as
        // descendant-or-self::node()/child::N: take every type at or below the context, then their N children.
        // Every descendant is the child of some node there, and the context is not, so it is never included.
        Optional<SequenceType> stepType = descendantAxis
                ? contentModel
                        .getDescendantOrSelfType(contextType.getItemType())
                        .flatMap(nodes -> contentModel
                                .getStepType(nodes.getItemType(), false, name)
                                .map(type -> type.repeated(nodes.getCardinality())))
                : contentModel.getStepType(contextType.getItemType(), attributeAxis, name);
        if (stepType.isPresent() && stepType.get().isEmptySequence()) {
            this.errors.reportInferredEmpty(
                    undeclaredStepMessage(contentModel, contextType.getItemType(), attributeAxis, descendantAxis, name),
                    stepExpr);
        }
        return stepType.map(type -> type.repeated(contextType.getCardinality()));
    }

    /** Explains a step that selects nothing, listing the names that the schema declares instead. */
    private String undeclaredStepMessage(
            SchemaContentModel contentModel,
            ItemType stepContextType,
            boolean attributeAxis,
            boolean descendantAxis,
            Name name) {
        // In E//S, S steps from the type of descendant-or-self::node(), so the message names E instead.
        ItemType descendantOrSelfContext = this.descendantOrSelfContexts.get(stepContextType);
        boolean descendants = descendantAxis || descendantOrSelfContext != null;
        ItemType contextType = descendantOrSelfContext != null ? descendantOrSelfContext : stepContextType;
        String kind = attributeAxis ? "attribute" : descendants ? "descendant element" : "child element";
        String where = descendants ? (attributeAxis ? " on or below " : " below ") : " for ";
        if (name == null) {
            return "The schema declares no " + kind + "s" + where + contextType + ".";
        }
        List<Name> declared = contentModel
                .getStepNames(contextType, attributeAxis, descendants)
                .orElse(List.of());
        String alternatives = declared.isEmpty()
                ? "It declares none."
                : "It declares "
                        + declared.stream()
                                .limit(MAX_LISTED_STEP_NAMES)
                                .map(Name::toString)
                                .collect(Collectors.joining(", "))
                        + (declared.size() > MAX_LISTED_STEP_NAMES
                                ? ", and " + (declared.size() - MAX_LISTED_STEP_NAMES) + " more."
                                : ".");
        return "The schema declares no " + kind + " " + name + where + contextType + ". " + alternatives;
    }

    private SequenceType.Arity inferStepResultArity(StepExpr stepExpr, SequenceType contextType) {
        if (contextType == null || !contextType.isAritySubtypeOf(SequenceType.Arity.OneOrZero)) {
            return SequenceType.Arity.ZeroOrMore;
        }
        if ((stepExpr instanceof ForwardStepExpr forwardStep
                        && forwardStep.getForwardAxis().equals(ForwardAxis.SELF))
                || (stepExpr instanceof ReverseStepExpr reverseStep
                        && reverseStep.getReverseAxis().equals(ReverseAxis.PARENT))) {
            return SequenceType.Arity.OneOrZero;
        }
        return SequenceType.Arity.ZeroOrMore;
    }

    private ItemType inferStepResultItemType(StepExpr stepExpr) {
        NodeTest nodeTest = stepExpr.getNodeTest();
        if (nodeTest instanceof SchemaNodeTest schemaTest) {
            return schemaTest.itemType();
        }
        if (nodeTest instanceof AnyKindTest) {
            return BuiltinTypesCatalogue.nodeItem;
        }
        if (nodeTest instanceof TextTest) {
            return BuiltinTypesCatalogue.textNode;
        }
        if (nodeTest instanceof CommentTest) {
            return BuiltinTypesCatalogue.commentNode;
        }
        if (nodeTest instanceof NamespaceNodeTest) {
            return BuiltinTypesCatalogue.namespaceNode;
        }
        if (nodeTest instanceof PITest piTest) {
            return piTest.hasTargetName()
                    ? ItemTypeFactory.processingInstructionNodeItemType(piTest.getTargetName())
                    : BuiltinTypesCatalogue.processingInstructionNode;
        }
        if (nodeTest instanceof DocumentTest documentTest) {
            if (documentTest.isEmptyCheck()) {
                return BuiltinTypesCatalogue.documentNode;
            }
            NodeTest innerTest = documentTest.getNodeTest();
            if (innerTest instanceof ElementTest elementTest && elementTest.isNameWithoutTypeCheck()) {
                return ItemTypeFactory.documentNodeItemType(
                        ItemTypeFactory.elementNodeItemType(elementTest.getElementName()));
            }
            return BuiltinTypesCatalogue.documentNode;
        }
        if (nodeTest instanceof AttributeTest attributeTest) {
            return attributeTest.isNameWithoutTypeCheck()
                    ? ItemTypeFactory.attributeNodeItemType(attributeTest.getAttributeName())
                    : BuiltinTypesCatalogue.attributeNode;
        }
        if (nodeTest instanceof ElementTest elementTest) {
            return elementTest.isNameWithoutTypeCheck()
                    ? ItemTypeFactory.elementNodeItemType(elementTest.getElementName())
                    : BuiltinTypesCatalogue.elementNode;
        }
        if (nodeTest instanceof NameTest nameTest) {
            boolean attributePrincipalKind = stepExpr instanceof ForwardStepExpr forwardStep
                    && forwardStep.getForwardAxis().equals(ForwardAxis.ATTRIBUTE);
            if (attributePrincipalKind) {
                return nameTest.hasQName()
                        ? ItemTypeFactory.attributeNodeItemType(nameTest.getExpandedName())
                        : BuiltinTypesCatalogue.attributeNode;
            }
            return nameTest.hasQName()
                    ? ItemTypeFactory.elementNodeItemType(nameTest.getExpandedName())
                    : BuiltinTypesCatalogue.elementNode;
        }
        return BuiltinTypesCatalogue.nodeItem;
    }

    private static boolean hasNoChildren(ItemType nodeType) {
        return nodeType.isSubtypeOf(BuiltinTypesCatalogue.attributeNode)
                || nodeType.isSubtypeOf(BuiltinTypesCatalogue.textNode)
                || nodeType.isSubtypeOf(BuiltinTypesCatalogue.commentNode)
                || nodeType.isSubtypeOf(BuiltinTypesCatalogue.namespaceNode)
                || nodeType.isSubtypeOf(BuiltinTypesCatalogue.processingInstructionNode);
    }

    private boolean isStaticallyEmptyStep(StepExpr stepExpr, ItemType contextItemType) {
        if (stepExpr instanceof ForwardStepExpr forwardStep) {
            ForwardAxis axis = forwardStep.getForwardAxis();
            if (axis.equals(ForwardAxis.ATTRIBUTE)) {
                // Only elements have attributes, but a context such as node() may still be one.
                return hasNoChildren(contextItemType)
                        || contextItemType.isSubtypeOf(BuiltinTypesCatalogue.documentNode);
            }
            if (axis.equals(ForwardAxis.SELF)) {
                return !nodeTestCanMatchContextNode(stepExpr.getNodeTest(), contextItemType, axis);
            }
            if (axis.equals(ForwardAxis.CHILD)
                    || axis.equals(ForwardAxis.DESCENDANT)
                    || axis.equals(ForwardAxis.DESCENDANT_OR_SELF)) {
                boolean hasNoDescendants = hasNoChildren(contextItemType);
                if (axis.equals(ForwardAxis.DESCENDANT_OR_SELF)) {
                    return hasNoDescendants
                            && !nodeTestCanMatchContextNode(stepExpr.getNodeTest(), contextItemType, axis);
                }
                return hasNoDescendants;
            }
            return false;
        }
        if (stepExpr instanceof ReverseStepExpr reverseStep) {
            ReverseAxis axis = reverseStep.getReverseAxis();
            if (axis.equals(ReverseAxis.PARENT)) {
                return contextItemType.isSubtypeOf(BuiltinTypesCatalogue.documentNode);
            }
            if (axis.equals(ReverseAxis.ANCESTOR_OR_SELF)) {
                return !nodeTestCanMatchContextNode(stepExpr.getNodeTest(), contextItemType, null);
            }
        }
        return false;
    }

    private boolean nodeTestCanMatchContextNode(NodeTest nodeTest, ItemType contextItemType, ForwardAxis axis) {
        if (contextItemType instanceof SchemaElementNodeItemType schemaType) {
            return schemaType.getAlternatives().stream()
                    .anyMatch(type -> nodeTestCanMatchContextNode(nodeTest, type, axis));
        }
        if (nodeTest instanceof AnyKindTest) {
            return true;
        }
        if (nodeTest instanceof TextTest) {
            return contextItemType.isSubtypeOf(BuiltinTypesCatalogue.textNode);
        }
        if (nodeTest instanceof CommentTest) {
            return contextItemType.isSubtypeOf(BuiltinTypesCatalogue.commentNode);
        }
        if (nodeTest instanceof NamespaceNodeTest) {
            return contextItemType.isSubtypeOf(BuiltinTypesCatalogue.namespaceNode);
        }
        if (nodeTest instanceof PITest) {
            return contextItemType.isSubtypeOf(BuiltinTypesCatalogue.processingInstructionNode);
        }
        if (nodeTest instanceof DocumentTest) {
            return contextItemType.isSubtypeOf(BuiltinTypesCatalogue.documentNode);
        }
        if (nodeTest instanceof AttributeTest attributeTest) {
            if (!contextItemType.isSubtypeOf(BuiltinTypesCatalogue.attributeNode)) {
                return false;
            }
            if (!attributeTest.isNameWithoutTypeCheck()) {
                return true;
            }
            if (contextItemType instanceof AttributeNodeItemType namedAttributeType) {
                return attributeTest.getAttributeName().equals(namedAttributeType.getNodeName());
            }
            return true;
        }
        if (nodeTest instanceof ElementTest elementTest) {
            if (!contextItemType.isSubtypeOf(BuiltinTypesCatalogue.elementNode)) {
                return false;
            }
            if (!elementTest.isNameWithoutTypeCheck()) {
                return true;
            }
            if (contextItemType instanceof ElementNodeItemType namedElementType) {
                return elementTest.getElementName().equals(namedElementType.getNodeName());
            }
            return true;
        }
        if (nodeTest instanceof NameTest nameTest) {
            boolean attributePrincipalKind = axis != null && axis.equals(ForwardAxis.ATTRIBUTE);
            if (attributePrincipalKind) {
                if (!contextItemType.isSubtypeOf(BuiltinTypesCatalogue.attributeNode)) {
                    return false;
                }
                if (!nameTest.hasQName()) {
                    return true;
                }
                if (contextItemType instanceof AttributeNodeItemType namedAttributeType) {
                    return nameTest.getExpandedName().equals(namedAttributeType.getNodeName());
                }
                return true;
            }
            if (!contextItemType.isSubtypeOf(BuiltinTypesCatalogue.elementNode)) {
                return false;
            }
            if (!nameTest.hasQName()) {
                return true;
            }
            if (contextItemType instanceof ElementNodeItemType namedElementType) {
                return nameTest.getExpandedName().equals(namedElementType.getNodeName());
            }
            return true;
        }
        return true;
    }
}
