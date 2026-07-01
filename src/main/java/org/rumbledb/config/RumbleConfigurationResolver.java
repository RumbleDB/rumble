package org.rumbledb.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

public final class RumbleConfigurationResolver {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private RumbleConfigurationResolver() {
    }

    public static RumbleConfiguration apply(RumbleConfiguration baseConfiguration, Map<String, ?> entries) {
        ObjectNode mergedTree = MAPPER.valueToTree(baseConfiguration);
        deepMerge(mergedTree, toTree(entries));
        return fromTree(mergedTree);
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
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Configuration path cannot be blank.");
        }
        String[] parts = path.split("\\.");
        ObjectNode current = root;
        for (int i = 0; i < parts.length - 1; i++) {
            String segment = parts[i];
            if (segment.isBlank()) {
                throw new IllegalArgumentException("Invalid configuration path: " + path);
            }
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
        if (leaf.isBlank()) {
            throw new IllegalArgumentException("Invalid configuration path: " + path);
        }
        current.set(leaf, MAPPER.valueToTree(value));
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
