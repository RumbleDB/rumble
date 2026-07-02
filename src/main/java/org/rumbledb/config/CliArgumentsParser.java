/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rumbledb.config;

import org.rumbledb.exceptions.CliException;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CliArgumentsParser {

    private static final String SHORTCUT_PREFIX = "-";
    private static final String ARGUMENT_PREFIX = "--";

    private final Map<String, String> shortcutMap;
    private final Set<String> yesNoShortcuts;

    public CliArgumentsParser() {
        this.shortcutMap = new HashMap<>();
        this.shortcutMap.put("q", "query");
        this.shortcutMap.put("o", "output-path");
        this.shortcutMap.put("O", "overwrite");
        this.shortcutMap.put("c", "materialization-cap");
        this.shortcutMap.put("I", "context-item");
        this.shortcutMap.put("i", "context-item-input");
        this.shortcutMap.put("P", "number-of-output-partitions");
        this.shortcutMap.put("v", "show-error-info");
        this.shortcutMap.put("t", "static-typing");
        this.shortcutMap.put("h", "host");
        this.shortcutMap.put("p", "port");

        this.yesNoShortcuts = new HashSet<>();
        this.yesNoShortcuts.add("O");
        this.yesNoShortcuts.add("v");
        this.yesNoShortcuts.add("t");
    }

    public Map<String, String> parse(String[] args) {
        HashMap<String, String> arguments = new HashMap<>();
        for (int i = 0; i < args.length; ++i) {
            if (args[i].startsWith(ARGUMENT_PREFIX)) {
                parseLongArgument(args, arguments, i);
                ++i;
                continue;
            }
            if (args[i].startsWith(SHORTCUT_PREFIX)) {
                i = parseShortArgument(args, arguments, i);
                continue;
            }
            if (i == 0 && args[i].equals("serve")) {
                arguments.put("server", "yes");
                continue;
            }
            if (i == 0 && args[i].equals("repl")) {
                arguments.put("shell", "yes");
                continue;
            }
            if (i == 0 && args[i].equals("run")) {
                // This is the default, do nothing.
                continue;
            }
            if (i == 0) {
                System.err.println("Missing mode (run/serve/repl), assuming run.");
            }
            arguments.put("query-path", args[i]);
        }
        return Collections.unmodifiableMap(arguments);
    }

    private void parseLongArgument(String[] args, HashMap<String, String> arguments, int index) {
        if (args[index].equals("--run") || args[index].equals("--serve") || args[index].equals("--repl")) {
            printDeprecatedLongModeHint();
        }
        String argumentName = args[index].trim().replace(ARGUMENT_PREFIX, "");
        if (
            index + 1 >= args.length
                || (!(args[index + 1].equals("-"))
                    && (args[index + 1].startsWith(ARGUMENT_PREFIX)
                        || args[index + 1].startsWith(SHORTCUT_PREFIX)))
        ) {
            throw new CliException("Missing argument value for a provided argument: " + argumentName + ".");
        }
        String argumentValue = args[index + 1];
        arguments.put(argumentName, argumentValue);
    }

    private int parseShortArgument(String[] args, HashMap<String, String> arguments, int index) {
        String argumentName = args[index].trim().replace(SHORTCUT_PREFIX, "");
        // Handle -o:<option> <value> pattern (distinct from -o output-path) separately
        // from the other shortcuts
        if (argumentName.startsWith("o:") && argumentName.length() > 2) {
            return parseSerializationParameter(args, arguments, index, argumentName);
        }
        if (!this.yesNoShortcuts.contains(argumentName)) {
            if (
                index + 1 >= args.length
                    || (!(args[index + 1].equals("-"))
                        && (args[index + 1].startsWith(ARGUMENT_PREFIX)
                            || args[index + 1].startsWith(SHORTCUT_PREFIX)))
            ) {
                throw new CliException("Missing argument value for a provided argument: " + argumentName + ".");
            }
            arguments.put(this.shortcutMap.get(argumentName), args[index + 1]);
            return index + 1;
        } else {
            arguments.put(this.shortcutMap.get(argumentName), "yes");
            return index;
        }
    }

    private int parseSerializationParameter(
            String[] args,
            HashMap<String, String> arguments,
            int index,
            String argumentName
    ) {
        String optionName = argumentName.substring(2); // Remove "o:" prefix
        if (
            index + 1 < args.length
                && !args[index + 1].startsWith(ARGUMENT_PREFIX)
                && !args[index + 1].startsWith(SHORTCUT_PREFIX)
        ) {
            String optionValue = args[index + 1];
            arguments.put("output:" + optionName, optionValue);
            return index + 1;
        } else {
            throw new CliException("Missing value for serialization parameter: " + optionName + ".");
        }
    }

    private void printDeprecatedLongModeHint() {
        System.err.println("Did you know?  🧑‍🏫");
        System.err.println(
            "The RumbleDB command line interface was extended with convenient shortcuts. For example:"
        );
        System.err.println();
        System.err.println("spark-submit <spark parameters> rumbledb-<version>.jar run query.jq");
        System.err.println("spark-submit <spark parameters> rumbledb-<version>.jar serve -p 8001");
        System.err.println("spark-submit <spark parameters> rumbledb-<version>.jar run -q '1+1'");
        System.err.println("spark-submit <spark parameters> rumbledb-<version>.jar repl -c 10");
        System.err.println();
        System.err.println(
            "The list of single-dash shortcuts is documented in our documentation page, accessible from www.rumbledb.org."
        );
        System.err.println();
        System.err.println("Try it out! The old parameters will continue to work, though.");
    }
}
