package org.rumbledb.runtime.functions.strings;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.MatchesEmptyStringException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.xml.XMLDocumentPosition;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;

public class AnalyzeStringFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    private static final long serialVersionUID = 1L;
    private static final Name ANALYZE_STRING_RESULT_NAME = new Name(Name.FN_NS, "", "analyze-string-result");
    private static final Name MATCH_NAME = new Name(Name.FN_NS, "", "match");
    private static final Name NON_MATCH_NAME = new Name(Name.FN_NS, "", "non-match");
    private static final Name GROUP_NAME = new Name(Name.FN_NS, "", "group");
    private static final Name NR_ATTRIBUTE_NAME = new Name(null, null, "nr");

    public AnalyzeStringFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        ItemFactory factory = ItemFactory.getInstance();

        Item inputItem = this.children.get(0).materializeFirstItemOrNull(context);
        String input = inputItem == null ? "" : inputItem.getStringValue();

        String pattern = this.children.get(1).materializeFirstItemOrNull(context).getStringValue();
        String flags = null;
        if (this.children.size() == 3) {
            Item flagsItem = this.children.get(2).materializeFirstItemOrNull(context);
            if (flagsItem != null) {
                flags = flagsItem.getStringValue();
            }
        }

        RegexPatternUtils.CompiledRegex compiledRegex = RegexPatternUtils.compileRegex(
            pattern,
            flags,
            getMetadata()
        );
        if (RegexPatternUtils.matchesEmptyString(compiledRegex.getPattern())) {
            throw new MatchesEmptyStringException(
                    "'" + compiledRegex.getEffectivePattern() + "' matches empty string",
                    getMetadata()
            );
        }

        List<Item> resultChildren = new ArrayList<>();
        Matcher matcher = compiledRegex.getPattern().matcher(input);
        int currentPosition = 0;
        while (matcher.find()) {
            if (currentPosition < matcher.start()) {
                resultChildren.add(
                    createTextContainer(factory, NON_MATCH_NAME, input.substring(currentPosition, matcher.start()))
                );
            }
            resultChildren.add(createMatchElement(factory, input, matcher));
            currentPosition = matcher.end();
        }
        if (currentPosition < input.length()) {
            resultChildren.add(createTextContainer(factory, NON_MATCH_NAME, input.substring(currentPosition)));
        }

        Item root = factory.createXmlElementNode(
            ANALYZE_STRING_RESULT_NAME,
            resultChildren,
            Collections.emptyList()
        );
        assignParentsRecursively(root);
        root.setXmlDocumentPosition(XMLDocumentPosition.generateConstructedTreePath(), 0);
        return root;
    }

    private Item createMatchElement(ItemFactory factory, String input, Matcher matcher) {
        int matchStart = matcher.start();
        int matchEnd = matcher.end();
        GroupCapture rootCapture = new GroupCapture(0, matchStart, matchEnd);
        List<GroupCapture> captures = new ArrayList<>();

        for (int i = 1; i <= matcher.groupCount(); i++) {
            int start = matcher.start(i);
            if (start == -1) {
                continue;
            }
            captures.add(new GroupCapture(i, start, matcher.end(i)));
        }

        captures.sort(
            Comparator.comparingInt(GroupCapture::getStart)
                .thenComparing(Comparator.comparingInt(GroupCapture::getEnd).reversed())
                .thenComparingInt(GroupCapture::getNumber)
        );

        Deque<GroupCapture> stack = new ArrayDeque<>();
        stack.push(rootCapture);
        for (GroupCapture capture : captures) {
            while (!contains(stack.peek(), capture)) {
                stack.pop();
            }
            stack.peek().children.add(capture);
            stack.push(capture);
        }

        List<Item> children = materializeChildren(factory, input, rootCapture);
        return factory.createXmlElementNode(MATCH_NAME, children, Collections.emptyList());
    }

    private boolean contains(GroupCapture parent, GroupCapture child) {
        return parent.getStart() <= child.getStart() && child.getEnd() <= parent.getEnd();
    }

    private List<Item> materializeChildren(ItemFactory factory, String input, GroupCapture parent) {
        List<Item> result = new ArrayList<>();
        int position = parent.getStart();
        for (GroupCapture child : parent.children) {
            if (position < child.getStart()) {
                result.add(factory.createXmlTextNode(input.substring(position, child.getStart())));
            }
            result.add(createGroupElement(factory, input, child));
            position = child.getEnd();
        }
        if (position < parent.getEnd()) {
            result.add(factory.createXmlTextNode(input.substring(position, parent.getEnd())));
        }
        return result;
    }

    private Item createGroupElement(ItemFactory factory, String input, GroupCapture capture) {
        List<Item> attributes = Collections.singletonList(
            factory.createXmlAttributeNode(NR_ATTRIBUTE_NAME, String.valueOf(capture.getNumber()))
        );
        List<Item> children = materializeChildren(factory, input, capture);
        return factory.createXmlElementNode(GROUP_NAME, children, attributes);
    }

    private Item createTextContainer(ItemFactory factory, Name nodeName, String content) {
        return factory.createXmlElementNode(
            nodeName,
            Collections.singletonList(factory.createXmlTextNode(content)),
            Collections.emptyList()
        );
    }

    private void assignParentsRecursively(Item item) {
        if (!item.isNode()) {
            return;
        }
        for (Item attribute : item.attributes()) {
            attribute.setParent(item);
        }
        for (Item child : item.children()) {
            child.setParent(item);
            assignParentsRecursively(child);
        }
    }

    private static final class GroupCapture {
        private final int number;
        private final int start;
        private final int end;
        private final List<GroupCapture> children;

        private GroupCapture(int number, int start, int end) {
            this.number = number;
            this.start = start;
            this.end = end;
            this.children = new ArrayList<>();
        }

        private int getNumber() {
            return this.number;
        }

        private int getStart() {
            return this.start;
        }

        private int getEnd() {
            return this.end;
        }
    }
}
