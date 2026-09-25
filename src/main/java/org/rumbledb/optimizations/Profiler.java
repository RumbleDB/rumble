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
package org.rumbledb.optimizations;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Profiler {

    public static int counter = 0;
    public static final Map<String, Integer> stacks = new HashMap<>();

    public static void increase() {
        ++counter;
        String stackTrace = String.join(
                "\n",
                Arrays.asList(Thread.currentThread().getStackTrace()).stream()
                        .map(x -> x.toString())
                        .collect(Collectors.toList()));
        if (!stacks.containsKey(stackTrace)) {
            stacks.put(stackTrace, 1);
        } else {
            stacks.put(stackTrace, stacks.get(stackTrace) + 1);
        }
    }

    public static int get() {
        if (stacks.isEmpty()) {
            return 0;
        }
        int max = Collections.max(stacks.values());
        int total = 0;
        for (String key : stacks.keySet()) {
            total += stacks.get(key);
            if (stacks.get(key) != max) continue;
            log.debug(
                    """
                        Occurrences: {}
                        {}\
                        """,
                    stacks.get(key),
                    key);
        }
        log.debug("Size: {}", stacks.size());
        log.debug("Total: {}", total);
        return counter;
    }
}
