package org.rumbledb.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;
import java.util.Objects;

/**
 * Utility class to manipulate RumbleConfiguration as JSON objects.
 * 
 * This is mainly used for the public configuration API. Our internal Java code should use the RumbleConfiguration class
 * directly, and not rely on this class because it is not type-safe and does not provide compile-time guarantees.
 */
public final class RumbleConfigurationResolver {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private RumbleConfigurationResolver() {
    }

    public static RumbleConfiguration apply(RumbleConfiguration baseConfiguration, Map<String, ?> entries) {
        ObjectNode mergedTree = MAPPER.valueToTree(baseConfiguration);
        deepMerge(mergedTree, toTree(entries));
        return fromTree(mergedTree);
    }

    public static Object get(RumbleConfiguration configuration, String path) {
        return get(configuration, path, Object.class);
    }

    public static <T> T get(RumbleConfiguration configuration, String path, Class<T> valueType) {
        Objects.requireNonNull(configuration, "Configuration cannot be null.");
        Objects.requireNonNull(valueType, "Value type cannot be null.");

        JsonNode value = getPath(MAPPER.valueToTree(configuration), path);
        if (value == null) {
            throw new IllegalArgumentException("Unknown configuration path: " + path);
        }
        return MAPPER.convertValue(value, valueType);
    }

    public static ObjectNode toTree(Map<String, ?> entries) {
        ObjectNode root = MAPPER.createObjectNode();
        for (Map.Entry<String, ?> entry : entries.entrySet()) {
            setPath(root, entry.getKey(), entry.getValue());
        }
        return root;
    }

    public static RumbleConfiguration fromTree(ObjectNode tree) {
        try {
            return MAPPER.treeToValue(tree, RumbleConfiguration.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid configuration.", e);
        }
    }

    private static void setPath(ObjectNode root, String path, Object value) {
        String[] parts = pathSegments(path);
        ObjectNode current = root;
        for (int i = 0; i < parts.length - 1; i++) {
            String segment = parts[i];
            JsonNode child = current.get(segment);
            if (child == null) {
                ObjectNode next = MAPPER.createObjectNode();
                current.set(segment, next);
                current = next;
            } else if (child instanceof ObjectNode) {
                current = (ObjectNode) child;
            } else {
                throw new IllegalArgumentException("Configuration path conflict at: " + path);
            }
        }

        String leaf = parts[parts.length - 1];
        current.set(leaf, MAPPER.valueToTree(value));
    }

    private static JsonNode getPath(JsonNode root, String path) {
        JsonNode current = root;
        for (String segment : pathSegments(path)) {
            current = current.get(segment);
            if (current == null) {
                return null;
            }
        }
        return current;
    }

    private static String[] pathSegments(String path) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Configuration path cannot be blank.");
        }

        String[] segments = path.split("\\.", -1);
        for (String segment : segments) {
            if (segment.isBlank()) {
                throw new IllegalArgumentException("Invalid configuration path: " + path);
            }
        }
        return segments;
    }

    private static void deepMerge(ObjectNode target, ObjectNode updates) {
        updates.fields().forEachRemaining(entry -> {
            JsonNode existing = target.get(entry.getKey());
            JsonNode update = entry.getValue();
            if (existing instanceof ObjectNode && update instanceof ObjectNode) {
                deepMerge((ObjectNode) existing, (ObjectNode) update);
            } else {
                target.set(entry.getKey(), update);
            }
        });
    }
}
