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

import java.io.Serial;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class AnalyzeStringFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Name ANALYZE_STRING_RESULT_NAME = new Name(Name.FN_NS, "fn", "analyze-string-result");
    private static final Name MATCH_NAME = new Name(Name.FN_NS, "fn", "match");
    private static final Name NON_MATCH_NAME = new Name(Name.FN_NS, "fn", "non-match");
    private static final Name GROUP_NAME = new Name(Name.FN_NS, "fn", "group");
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

        Item inputItem = this.getChild(0).materializeFirstItemOrNull(context);
        String input = inputItem == null ? "" : inputItem.getStringValue();

        String pattern = this.getChild(1).materializeFirstItemOrNull(context).getStringValue();
        String flags = null;
        if (this.getChildren().size() == 3) {
            Item flagsItem = this.getChild(2).materializeFirstItemOrNull(context);
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
            resultChildren.add(createMatchElement(factory, input, matcher, compiledRegex.isQuote()));
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
        root.addOrReplaceNamespace(factory.createXmlNamespaceNode("fn", Name.FN_NS));
        assignParentsRecursively(root);
        root.setXmlDocumentPosition(XMLDocumentPosition.generateConstructedTreePath(), 0);
        return root;
    }

    private Item createMatchElement(ItemFactory factory, String input, Matcher matcher, boolean quotedPattern) {
        int matchStart = matcher.start();
        int matchEnd = matcher.end();
        RegexStructure regexStructure = buildRegexStructure(
            matcher.pattern().pattern(),
            matcher.groupCount(),
            quotedPattern
        );
        Map<Integer, GroupCapture> captures = new LinkedHashMap<>();
        GroupCapture rootCapture = new GroupCapture(0, matchStart, matchEnd, true, 0);
        captures.put(0, rootCapture);

        for (Integer number : regexStructure.orderedGroups) {
            GroupSpec spec = regexStructure.groupSpecs.get(number);
            int start = matcher.start(number);
            captures.put(
                number,
                new GroupCapture(
                        number,
                        start,
                        start == -1 ? -1 : matcher.end(number),
                        start != -1,
                        spec.branchIndex
                )
            );
        }
        for (Integer number : regexStructure.orderedGroups) {
            GroupSpec spec = regexStructure.groupSpecs.get(number);
            GroupCapture capture = captures.get(number);
            GroupCapture parent = captures.get(spec.parentNumber);
            if (parent != null) {
                parent.children.add(capture);
            }
        }

        List<Item> children = materializeChildren(factory, input, rootCapture);
        return factory.createXmlElementNode(MATCH_NAME, children, Collections.emptyList());
    }

    private List<Item> materializeChildren(ItemFactory factory, String input, GroupCapture parent) {
        List<Item> result = new ArrayList<>();
        int activeBranch = determineActiveBranch(parent);
        int position = parent.getStart();
        for (GroupCapture child : parent.children) {
            if (activeBranch != -1 && child.branchIndex != activeBranch) {
                continue;
            }
            if (child.matched && position < child.getStart()) {
                result.add(factory.createXmlTextNode(input.substring(position, child.getStart())));
            }
            result.add(createGroupElement(factory, input, child));
            if (child.matched) {
                position = child.getEnd();
            }
        }
        if (position < parent.getEnd()) {
            result.add(factory.createXmlTextNode(input.substring(position, parent.getEnd())));
        }
        return result;
    }

    private int determineActiveBranch(GroupCapture parent) {
        int activeBranch = -1;
        for (GroupCapture child : parent.children) {
            if (child.matched) {
                activeBranch = child.branchIndex;
                break;
            }
        }
        return activeBranch;
    }

    private Item createGroupElement(ItemFactory factory, String input, GroupCapture capture) {
        List<Item> attributes = Collections.singletonList(
            factory.createXmlAttributeNode(NR_ATTRIBUTE_NAME, String.valueOf(capture.getNumber()))
        );
        List<Item> children = capture.matched
            ? materializeChildren(factory, input, capture)
            : Collections.emptyList();
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

    private RegexStructure buildRegexStructure(String pattern, int groupCount, boolean quotedPattern) {
        if (quotedPattern || groupCount == 0) {
            return new RegexStructure(Collections.emptyMap(), Collections.emptyList());
        }
        Map<Integer, GroupSpec> groupSpecs = new LinkedHashMap<>();
        Deque<ParseFrame> stack = new ArrayDeque<>();
        stack.push(new ParseFrame(0));
        int nextGroupNumber = 1;

        for (int i = 0; i < pattern.length(); i++) {
            char current = pattern.charAt(i);
            if (current == '[') {
                i = skipCharacterClass(pattern, i);
                continue;
            }
            if (current == '\\') {
                if (i + 1 < pattern.length()) {
                    i++;
                }
                continue;
            }
            if (current == '(') {
                boolean capturing = !(i + 1 < pattern.length() && pattern.charAt(i + 1) == '?');
                ParseFrame currentFrame = stack.peek();
                int parentCaptureNumber = currentFrame.nearestCapturingAncestor;
                if (capturing && nextGroupNumber <= groupCount) {
                    int groupNumber = nextGroupNumber++;
                    groupSpecs.put(groupNumber, new GroupSpec(parentCaptureNumber, currentFrame.currentBranch));
                    stack.push(new ParseFrame(groupNumber));
                } else {
                    stack.push(new ParseFrame(parentCaptureNumber));
                }
                continue;
            }
            if (current == '|') {
                stack.peek().currentBranch++;
                continue;
            }
            if (current == ')' && stack.size() > 1) {
                stack.pop();
            }
        }

        List<Integer> orderedGroups = new ArrayList<>(groupSpecs.keySet());
        return new RegexStructure(groupSpecs, orderedGroups);
    }

    private int skipCharacterClass(String pattern, int start) {
        int i = start + 1;
        if (i < pattern.length() && pattern.charAt(i) == '^') {
            i++;
        }
        while (i < pattern.length()) {
            char current = pattern.charAt(i);
            if (current == '\\' && i + 1 < pattern.length()) {
                i += 2;
                continue;
            }
            if (current == ']') {
                return i;
            }
            i++;
        }
        return pattern.length() - 1;
    }

    private static final class GroupCapture {
        private final int number;
        private final int start;
        private final int end;
        private final boolean matched;
        private final int branchIndex;
        private final List<GroupCapture> children;

        private GroupCapture(int number, int start, int end, boolean matched, int branchIndex) {
            this.number = number;
            this.start = start;
            this.end = end;
            this.matched = matched;
            this.branchIndex = branchIndex;
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

    private static final class GroupSpec {
        private final int parentNumber;
        private final int branchIndex;

        private GroupSpec(int parentNumber, int branchIndex) {
            this.parentNumber = parentNumber;
            this.branchIndex = branchIndex;
        }
    }

    private static final class ParseFrame {
        private final int nearestCapturingAncestor;
        private int currentBranch;

        private ParseFrame(int nearestCapturingAncestor) {
            this.nearestCapturingAncestor = nearestCapturingAncestor;
            this.currentBranch = 0;
        }
    }

    private static final class RegexStructure {
        private final Map<Integer, GroupSpec> groupSpecs;
        private final List<Integer> orderedGroups;

        private RegexStructure(Map<Integer, GroupSpec> groupSpecs, List<Integer> orderedGroups) {
            this.groupSpecs = groupSpecs;
            this.orderedGroups = orderedGroups;
        }
    }
}
