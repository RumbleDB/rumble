package org.rumbledb.optimizations;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Profiler {

    public static int counter = 0;
    public static Map<String, Integer> stacks = new HashMap<>();

    public static void increase() {
        ++counter;
        String stackTrace = String.join(
            "\n",
            Arrays.asList(Thread.currentThread().getStackTrace())
                .stream()
                .map(x -> x.toString())
                .collect(Collectors.toList())
        );
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
            if (stacks.get(key) != max)
                continue;
            System.out.println("Occurrences: " + stacks.get(key));
            System.out.println(key);
            System.out.println();
        }
        System.out.println("Size: " + stacks.size());
        System.out.println("Total: " + total);
        return counter;
    }

}
