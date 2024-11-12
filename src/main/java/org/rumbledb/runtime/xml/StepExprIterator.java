package org.rumbledb.runtime.xml;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.xml.node_test.AnyKindTest;
import org.rumbledb.expressions.xml.node_test.AttributeTest;
import org.rumbledb.expressions.xml.node_test.DocumentTest;
import org.rumbledb.expressions.xml.node_test.ElementTest;
import org.rumbledb.expressions.xml.node_test.NameTest;
import org.rumbledb.expressions.xml.node_test.NodeTest;
import org.rumbledb.expressions.xml.node_test.TextTest;
import org.rumbledb.items.xml.AttributeItem;
import org.rumbledb.items.xml.DocumentItem;
import org.rumbledb.items.xml.ElementItem;
import org.rumbledb.items.xml.TextItem;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.xml.axis.forward.AttributeAxisIterator;

import java.util.ArrayList;
import java.util.List;

public class StepExprIterator extends LocalRuntimeIterator {
    private final RuntimeIterator axisIterator;
    private NodeTest nodeTest;
    private List<Item> results;
    private Item nextResult;
    private int resultCounter = 0;

    public StepExprIterator(
            RuntimeIterator axisIterator,
            NodeTest nodeTest,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        this.children.add(axisIterator);
        this.axisIterator = axisIterator;
        this.nodeTest = nodeTest;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        setNextResult();
    }

    @Override
    public void close() {
        super.close();
        this.results = null;
        this.nextResult = null;
        this.resultCounter = 0;
        this.axisIterator.close();
    }

    private void setNextResult() {
        if (this.results == null) {
            List<Item> axisResult = applyAxis();
            this.results = applyNodeTest(axisResult);
        }
        storeNextResult();
    }

    private List<Item> applyAxis() {
        return this.axisIterator.materialize(this.currentDynamicContextForLocalExecution);
    }

    public List<Item> applyInPath(DynamicContext ctx) {
        List<Item> result = this.axisIterator.materialize(ctx);
        return applyNodeTest(result);
    }

    private void storeNextResult() {
        if (this.resultCounter < this.results.size()) {
            this.nextResult = this.results.get(this.resultCounter++);
        } else {
            this.hasNext = false;
        }
    }

    private List<Item> applyNodeTest(List<Item> axisResult) {
        List<Item> nodeTestResults = new ArrayList<>();
        for (Item node : axisResult) {
            Item nodeTestResult = nodeTestItem(node);
            if (nodeTestResult != null) {
                nodeTestResults.add(nodeTestResult);
            }
        }
        return nodeTestResults;
    }

    private Item nodeTestItem(Item node) {
        if (this.nodeTest instanceof AnyKindTest) {
            return anyKindTest(node);
        } else if (this.nodeTest instanceof TextTest) {
            return textKindTest(node);
        } else if (this.nodeTest instanceof AttributeTest) {
            return attributeKindTest(node);
        } else if (this.nodeTest instanceof ElementTest) {
            return elementKindTest(node);
        } else if (this.nodeTest instanceof NameTest) {
            return nameKindTest(node);
        } else if (this.nodeTest instanceof DocumentTest) {
            return documentKindTest(node);
        } else {
            throw new UnsupportedFeatureException(
                    "Only node, text, attribute, element, document and name node tests are supported.",
                    getMetadata()
            );
        }
    }

    private Item documentKindTest(Item node) {
        DocumentTest documentTest = (DocumentTest) this.nodeTest;
        if (documentTest.isEmptyCheck()) {
            if (node instanceof DocumentItem) {
                return node;
            }
            return node;
        }
        this.nodeTest = documentTest.getNodeTest();
        return nodeTestItem(node);
    }

    private Item nameKindTest(Item node) {
        NameTest nameTest = (NameTest) this.nodeTest;
        if (nameTest.hasQName()) {
            if (!isPrincipalNodeKind(node)) {
                return null;
            }
            if (node.nodeName().equals(nameTest.getQName())) {
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
        if (nameTest.getWildcardQName().equals(node.nodeName())) {
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

    private Item elementKindTest(Item node) {
        ElementTest elementTest = (ElementTest) this.nodeTest;
        if (elementTest.isEmptyCheck()) {
            if (node instanceof ElementItem) {
                return node;
            }
            return null;
        }
        if (elementTest.isNameWithoutTypeCheck()) {
            if (node instanceof ElementItem && node.nodeName().equals(elementTest.getElementName())) {
                return node;
            }
            return null;
        }
        if (elementTest.isWildcardOnly()) {
            if (node instanceof ElementItem) {
                return node;
            }
            return null;
        }
        // TODO: add support for name and type
        return null;
    }

    private Item attributeKindTest(Item node) {
        AttributeTest attributeTest = (AttributeTest) this.nodeTest;
        if (attributeTest.isEmptyCheck()) {
            if (node instanceof AttributeItem) {
                return node;
            }
            return null;
        }
        if (attributeTest.isNameWithoutTypeCheck()) {
            if (node instanceof AttributeItem && node.nodeName().equals(attributeTest.getAttributeName())) {
                return node;
            }
            return null;
        }
        if (attributeTest.isWildcardOnly()) {
            if (node instanceof AttributeItem) {
                return node;
            }
            return null;
        }
        // TODO: add support for name and type
        return null;
    }

    private Item textKindTest(Item node) {
        if (node instanceof TextItem) {
            return node;
        }
        return null;
    }

    private Item anyKindTest(Item node) {
        return node;
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            Item result = this.nextResult;
            setNextResult();
            return result;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in step expr",
                getMetadata()
        );
    }

    @Override
    public boolean hasNext() {
        return super.hasNext();
    }
}
