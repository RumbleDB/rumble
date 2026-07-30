package org.rumbledb.runtime.functions.xml;

import org.rumbledb.runtime.plan.EvaluationArguments;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.io.Serial;
import java.util.List;

public class LangFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public LangFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return evaluate(
            EvaluationArguments.lazy(
                this.getChildren().size(),
                index -> this.getChild(index).materializeFirstItemOrNull(context)
            ),
            context
        );
    }

    private Item evaluate(EvaluationArguments<Item> arguments, DynamicContext context) {
        Item testlangItem = arguments.get(0);
        String testlang = testlangItem == null ? "" : testlangItem.getStringValue();

        Item node = arguments.size() == 2
            ? arguments.get(1)
            : context.getVariableValues()
                .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata())
                .get(0);
        if (node == null || !node.isNode()) {
            throw new UnexpectedTypeException("The argument to fn:lang must be a node", getMetadata());
        }

        Item current = node.isElementNode() ? node : node.parent();
        while (current != null && current.isElementNode()) {
            for (Item attribute : current.attributes()) {
                Name name = attribute.nodeName();
                if (name != null && Name.XML_NS.equals(name.getNamespace()) && "lang".equals(name.getLocalName())) {
                    return ItemFactory.getInstance()
                        .createBooleanItem(matchesLanguage(attribute.getStringValue(), testlang));
                }
            }
            current = current.parent();
        }
        return ItemFactory.getInstance().createBooleanItem(false);
    }

    private static boolean matchesLanguage(String lang, String testlang) {
        if (lang.equalsIgnoreCase(testlang)) {
            return true;
        }
        return lang.length() > testlang.length()
            && lang.charAt(testlang.length()) == '-'
            && lang.regionMatches(true, 0, testlang, 0, testlang.length());
    }

}
