package org.rumbledb.runtime.xml;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.xml.node_test.AnyKindTest;
import org.rumbledb.expressions.xml.node_test.AttributeTest;
import org.rumbledb.expressions.xml.node_test.CommentTest;
import org.rumbledb.expressions.xml.node_test.DocumentTest;
import org.rumbledb.expressions.xml.node_test.ElementTest;
import org.rumbledb.expressions.xml.node_test.NameTest;
import org.rumbledb.expressions.xml.node_test.NamespaceNodeTest;
import org.rumbledb.expressions.xml.node_test.NodeTest;
import org.rumbledb.expressions.xml.node_test.PITest;
import org.rumbledb.expressions.xml.node_test.SchemaAttributeTest;
import org.rumbledb.expressions.xml.node_test.SchemaElementTest;
import org.rumbledb.expressions.xml.node_test.TextTest;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.cursor.FlatMappingLocalCursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.typing.InstanceOfIterator;
import org.rumbledb.runtime.xml.axis.forward.AttributeAxisIterator;
import org.rumbledb.types.ItemType;

public class StepExprIterator extends ItemRuntimePlan implements LocalRuntimePlan<Item> {
    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan axisIterator;
    private final NodeTest nodeTest;

    public StepExprIterator(ItemRuntimePlan axisIterator, NodeTest nodeTest, RuntimeStaticContext staticContext) {
        super(List.of(axisIterator), staticContext);
        this.axisIterator = axisIterator;
        this.nodeTest = nodeTest;
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new FlatMappingLocalCursor<>(
                this.axisIterator,
                context,
                node -> {
                    Item result = nodeTestItem(node, this.nodeTest);
                    return result == null
                            ? List.<Item>of().iterator()
                            : List.of(result).iterator();
                },
                getMetadata());
    }

    private static String nodeNameLexical(Item node) {
        Name n = node.nodeName();
        return n == null ? "" : n.toString();
    }

    private Item nodeTestItem(Item node, NodeTest test) {
        if (test instanceof AnyKindTest) {
            return anyKindTest(node);
        } else if (test instanceof TextTest) {
            return textKindTest(node);
        } else if (test instanceof CommentTest) {
            return commentKindTest(node);
        } else if (test instanceof PITest piTest) {
            return piKindTest(node, piTest);
        } else if (test instanceof NamespaceNodeTest) {
            return namespaceNodeKindTest(node);
        } else if (test instanceof SchemaAttributeTest schemaAttributeTest) {
            return schemaKindTest(node, schemaAttributeTest.getItemType());
        } else if (test instanceof SchemaElementTest schemaElementTest) {
            return schemaKindTest(node, schemaElementTest.getItemType());
        } else if (test instanceof AttributeTest attributeTest) {
            return attributeKindTest(node, attributeTest);
        } else if (test instanceof ElementTest elementTest) {
            return elementKindTest(node, elementTest);
        } else if (test instanceof NameTest nameTest) {
            return nameKindTest(node, nameTest);
        } else if (test instanceof DocumentTest documentTest) {
            return documentKindTest(node, documentTest);
        } else {
            throw new UnsupportedFeatureException("Unsupported node test: " + test, getMetadata());
        }
    }

    private Item schemaKindTest(Item node, ItemType itemType) {
        return InstanceOfIterator.doesItemTypeMatchItem(itemType, node) ? node : null;
    }

    private Item documentKindTest(Item node, DocumentTest documentTest) {
        if (!node.isDocumentNode()) {
            return null;
        }
        if (documentTest.isEmptyCheck()) {
            return node;
        }
        Item documentElement = getDocumentElement(node);
        if (documentElement == null) {
            return null;
        }
        Item innerMatch = nodeTestItem(documentElement, documentTest.getNodeTest());
        return innerMatch == null ? null : node;
    }

    private Item getDocumentElement(Item documentNode) {
        List<Item> children = documentNode.children();
        List<Item> elements = new ArrayList<>();
        if (children == null) {
            return null;
        }
        for (Item child : children) {
            if (child.isElementNode()) {
                elements.add(child);
            }
        }
        if (elements.size() == 1) {
            // document-node(N) matches a document node with exactly one element child
            return elements.get(0);
        }
        return null;
    }

    private Item nameKindTest(Item node, NameTest nameTest) {
        if (nameTest.hasQName()) {
            if (!isPrincipalNodeKind(node)) {
                return null;
            }
            Name qItem = node.nodeName();
            if (qItem == null) {
                return null;
            }
            // Compare expanded names, not lexical strings: e.g. default element NS uses prefix "" in the name test
            // while DOM nodes often have prefix null, so Name.toString() differs for the same expanded QName.
            if (nameTest.getExpandedName().equals(qItem)) {
                return node;
            }
            return null;
        }
        if (nameTest.hasWildcardOnly()) {
            if (!isPrincipalNodeKind(node)) {
                return null;
            }
            return node;
        }
        if (!isPrincipalNodeKind(node)) {
            return null;
        }
        String wildcard = nameTest.getWildcardQName();
        Name nodeName = node.nodeName();
        if (nodeName == null) {
            return null;
        }
        if (wildcard.startsWith("*:")) {
            String localName = wildcard.substring(2);
            if (localName.equals(nodeName.getLocalName())) {
                return node;
            }
            return null;
        }
        if (wildcard.equals(nodeNameLexical(node))) {
            return node;
        }
        return null;
    }

    // TODO: Add support for namespace nodes.
    private boolean isPrincipalNodeKind(Item node) {
        if (this.axisIterator instanceof AttributeAxisIterator) {
            return node.isAttributeNode();
        }
        return node.isElementNode();
    }

    private Item elementKindTest(Item node, ElementTest elementTest) {
        if (elementTest.isEmptyCheck()) {
            if (node.isElementNode()) {
                return node;
            }
            return null;
        }
        if (elementTest.isNameWithoutTypeCheck()) {
            if (node.isElementNode() && elementTest.getElementName().equals(node.nodeName())) {
                return node;
            }
            return null;
        }
        if (elementTest.isWildcardOnly()) {
            if (node.isElementNode()) {
                return node;
            }
            return null;
        }
        // TODO: add support for type test
        return null;
    }

    private Item attributeKindTest(Item node, AttributeTest attributeTest) {
        if (attributeTest.isEmptyCheck()) {
            if (node.isAttributeNode()) {
                return node;
            }
            return null;
        }
        if (attributeTest.isNameWithoutTypeCheck()) {
            if (node.isAttributeNode() && attributeTest.getAttributeName().equals(node.nodeName())) {
                return node;
            }
            return null;
        }
        if (attributeTest.isWildcardOnly()) {
            if (node.isAttributeNode()) {
                return node;
            }
            return null;
        }
        // TODO: add support for type test
        return null;
    }

    private Item textKindTest(Item node) {
        if (node.isTextNode()) {
            return node;
        }
        return null;
    }

    private Item anyKindTest(Item node) {
        return node;
    }

    private Item commentKindTest(Item node) {
        if (node.isCommentNode()) {
            return node;
        }
        return null;
    }

    private Item piKindTest(Item node, PITest piTest) {
        if (!node.isProcessingInstructionNode()) {
            return null;
        }
        // processing-instruction() matches any PI node
        if (!piTest.hasTargetName()) {
            return node;
        }
        // processing-instruction(target) matches PI nodes whose target name equals the given name
        if (StringUtils.normalizeSpace(nodeNameLexical(node)).equals(piTest.getTargetName())) {
            return node;
        }
        return null;
    }

    private Item namespaceNodeKindTest(Item node) {
        if (node.isNamespaceNode()) {
            return node;
        }
        return null;
    }
}
