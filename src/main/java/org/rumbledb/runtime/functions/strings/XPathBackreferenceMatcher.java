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
package org.rumbledb.runtime.functions.strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matches validated Java-translated XPath patterns containing backreferences. Java handles individual
 * character tests, but XPath capture state is managed here: an absent capture is an empty reference,
 * and captures inside a repeated group are reset on every entry to that group.
 */
final class XPathBackreferenceMatcher {
    private XPathBackreferenceMatcher() {}

    static boolean containsMatch(Pattern pattern, String input) {
        Parser parser = new Parser(pattern.pattern());
        Node root = parser.alternatives(pattern.flags());
        for (int start = 0; start <= input.length(); ) {
            if (root.match(new State(input, start, new String[parser.nextGroup]), state -> true)) {
                return true;
            }
            if (start == input.length()) {
                break;
            }
            start += Character.charCount(input.codePointAt(start));
        }
        return false;
    }

    static boolean hasBackreference(String pattern) {
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == '\\'
                    && ++i < pattern.length()
                    && pattern.charAt(i) >= '1'
                    && pattern.charAt(i) <= '9') {
                return true;
            }
        }
        return false;
    }

    private record State(String input, int position, String[] captures) {
        State advance(int end) {
            return new State(this.input, end, this.captures);
        }
    }

    @FunctionalInterface
    private interface Continuation {
        boolean accept(State state);
    }

    @FunctionalInterface
    private interface Node {
        boolean match(State state, Continuation next);
    }

    private static Node sequence(List<Node> nodes) {
        return (state, next) -> matchSequence(nodes, 0, state, next);
    }

    private static boolean matchSequence(List<Node> nodes, int index, State state, Continuation next) {
        return index == nodes.size()
                ? next.accept(state)
                : nodes.get(index).match(state, result -> matchSequence(nodes, index + 1, result, next));
    }

    private static Node characterTest(String source, int flags) {
        Pattern pattern = Pattern.compile(source, flags);
        return (state, next) -> {
            Matcher matcher = pattern.matcher(state.input)
                    .region(state.position, state.input.length())
                    .useTransparentBounds(true)
                    .useAnchoringBounds(false);
            return matcher.lookingAt() && next.accept(state.advance(matcher.end()));
        };
    }

    private static Node reference(int group, int flags) {
        return (state, next) -> {
            String value = state.captures[group];
            if (value == null || value.isEmpty()) {
                return next.accept(state);
            }
            return characterTest(Pattern.quote(value), flags).match(state, next);
        };
    }

    private static Node group(Node body, int number, int firstCapture, int endCapture) {
        return (state, next) -> {
            String[] captures = state.captures.clone();
            Arrays.fill(captures, firstCapture, endCapture, null);
            return body.match(new State(state.input, state.position, captures), result -> {
                if (number == 0) {
                    return next.accept(result);
                }
                String[] updated = result.captures.clone();
                updated[number] = state.input.substring(state.position, result.position);
                return next.accept(new State(result.input, result.position, updated));
            });
        };
    }

    private static Node repeat(Node body, int minimum, int maximum, boolean reluctant) {
        return (state, next) -> repeat(body, minimum, maximum, reluctant, 0, state, next);
    }

    private static boolean repeat(
            Node body, int minimum, int maximum, boolean reluctant, int count, State state, Continuation next) {
        if (reluctant && count >= minimum && next.accept(state)) {
            return true;
        }
        if (count < maximum
                && body.match(state, result -> {
                    if (result.position == state.position && count >= minimum) {
                        // One empty iteration can update captures, but must not loop indefinitely.
                        return next.accept(result);
                    }
                    return repeat(body, minimum, maximum, reluctant, count + 1, result, next);
                })) {
            return true;
        }
        return !reluctant && count >= minimum && next.accept(state);
    }

    /** The input has already passed RegexPatternUtils validation and Java compilation. */
    private static final class Parser {
        private final String source;
        private int index;
        private int nextGroup = 1;

        Parser(String source) {
            this.source = source;
        }

        Node alternatives(int flags) {
            List<Node> branches = new ArrayList<>();
            branches.add(sequence(flags));
            while (this.index < this.source.length() && this.source.charAt(this.index) == '|') {
                this.index++;
                branches.add(sequence(flags));
            }
            return (state, next) -> {
                for (Node branch : branches) {
                    if (branch.match(state, next)) {
                        return true;
                    }
                }
                return false;
            };
        }

        Node sequence(int flags) {
            List<Node> nodes = new ArrayList<>();
            while (this.index < this.source.length() && ")|".indexOf(this.source.charAt(this.index)) < 0) {
                Node atom = atom(flags);
                if (this.index < this.source.length()) {
                    char quantifier = this.source.charAt(this.index);
                    int minimum = 0;
                    int maximum = Integer.MAX_VALUE;
                    if (quantifier == '?' || quantifier == '*' || quantifier == '+' || quantifier == '{') {
                        this.index++;
                        if (quantifier == '?') {
                            maximum = 1;
                        }
                        if (quantifier == '+') {
                            minimum = 1;
                        }
                        if (quantifier == '{') {
                            int start = this.index;
                            while (Character.isDigit(this.source.charAt(this.index))) {
                                this.index++;
                            }
                            minimum = Integer.parseInt(this.source.substring(start, this.index));
                            maximum = minimum;
                            if (this.source.charAt(this.index) == ',') {
                                start = ++this.index;
                                while (Character.isDigit(this.source.charAt(this.index))) {
                                    this.index++;
                                }
                                maximum = start == this.index
                                        ? Integer.MAX_VALUE
                                        : Integer.parseInt(this.source.substring(start, this.index));
                            }
                            this.index++;
                        }
                        boolean reluctant = this.index < this.source.length() && this.source.charAt(this.index) == '?';
                        if (reluctant) {
                            this.index++;
                        }
                        atom = repeat(atom, minimum, maximum, reluctant);
                    }
                }
                nodes.add(atom);
            }
            return XPathBackreferenceMatcher.sequence(nodes);
        }

        Node atom(int flags) {
            int start = this.index++;
            char current = this.source.charAt(start);
            if (current == '(') {
                int firstCapture = this.nextGroup;
                int number = 0;
                if (this.source.charAt(this.index) == '?') {
                    this.index++;
                    while (this.source.charAt(this.index) != ':') {
                        char flag = this.source.charAt(this.index++);
                        if (flag == 'i') {
                            flags |= Pattern.CASE_INSENSITIVE;
                        }
                        if (flag == 'u') {
                            flags |= Pattern.UNICODE_CASE;
                        }
                    }
                    this.index++;
                } else {
                    number = this.nextGroup++;
                }
                Node body = alternatives(flags);
                this.index++;
                return group(body, number, firstCapture, this.nextGroup);
            }
            if (current == '[') {
                int depth = 1;
                while (depth > 0) {
                    char c = this.source.charAt(this.index++);
                    if (c == '\\') {
                        this.index++;
                    } else if (c == '[') {
                        depth++;
                    } else if (c == ']') {
                        depth--;
                    }
                }
            } else if (current == '\\') {
                char escaped = this.source.charAt(this.index++);
                if (escaped >= '1' && escaped <= '9') {
                    int number = escaped - '0';
                    while (this.index < this.source.length() && Character.isDigit(this.source.charAt(this.index))) {
                        long candidate = (long) number * 10 + this.source.charAt(this.index) - '0';
                        if (candidate >= this.nextGroup) {
                            break;
                        }
                        number = (int) candidate;
                        this.index++;
                    }
                    return reference(number, flags);
                }
                if ((escaped == 'p' || escaped == 'P' || escaped == 'x')
                        && this.index < this.source.length()
                        && this.source.charAt(this.index) == '{') {
                    this.index = this.source.indexOf('}', this.index) + 1;
                }
            } else if (Character.isHighSurrogate(current)
                    && this.index < this.source.length()
                    && Character.isLowSurrogate(this.source.charAt(this.index))) {
                this.index++;
            }
            return characterTest(this.source.substring(start, this.index), flags);
        }
    }
}
