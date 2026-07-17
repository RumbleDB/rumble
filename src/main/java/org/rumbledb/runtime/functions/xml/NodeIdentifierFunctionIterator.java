/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package org.rumbledb.runtime.functions.xml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.xml.NamespaceBindingUtils;

/** Shared implementation of the document-scoped fn:id, fn:element-with-id, and fn:idref functions. */
abstract class NodeIdentifierFunctionIterator extends LocalFunctionCallIterator {
    private static final long serialVersionUID = 1L;

    enum Lookup {
        ID,
        ELEMENT_WITH_ID,
        IDREF
    }

    private final Lookup lookup;
    private transient List<Item> results;
    private transient int resultIndex;

    NodeIdentifierFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext,
            Lookup lookup
    ) {
        super(arguments, staticContext);
        this.lookup = lookup;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        initializeResults(context);
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        initializeResults(context);
    }

    @Override
    public Item next() {
        if (!this.hasNext) {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + "node identifier function",
                    getMetadata()
            );
        }
        Item result = this.results.get(this.resultIndex++);
        this.hasNext = this.resultIndex < this.results.size();
        return result;
    }

    private void initializeResults(DynamicContext context) {
        Set<String> candidates = candidateValues(this.children.get(0).materialize(context));
        Item target = this.children.size() == 2
            ? this.children.get(1).materializeFirstItemOrNull(context)
            : context.getVariableValues().getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata()).get(0);
        Item document = containingDocument(target);
        this.results = new ArrayList<>();
        this.resultIndex = 0;
        if (!candidates.isEmpty()) {
            if (this.lookup == Lookup.IDREF) {
                collectIdRefs(document, candidates, this.results);
            } else {
                collectIds(document, candidates, new HashSet<>(), this.results);
            }
        }
        this.hasNext = !this.results.isEmpty();
    }

    private Set<String> candidateValues(List<Item> arguments) {
        Set<String> result = new LinkedHashSet<>();
        for (Item argument : arguments) {
            String value = argument.getStringValue();
            if (this.lookup == Lookup.IDREF) {
                if (NamespaceBindingUtils.isValidNcName(value)) {
                    result.add(value);
                }
                continue;
            }
            for (String token : value.trim().split("\\s+")) {
                if (NamespaceBindingUtils.isValidNcName(token)) {
                    result.add(token);
                }
            }
        }
        return result;
    }

    private Item containingDocument(Item node) {
        Item root = node;
        while (root != null && root.parent() != null) {
            root = root.parent();
        }
        if (root == null || !root.isDocumentNode()) {
            throw new RumbleException(
                    "The target node of the identifier function must be in a tree rooted at a document node.",
                    ErrorCode.IdentifierFunctionDocumentErrorCode,
                    getMetadata()
            );
        }
        return root;
    }

    private void collectIds(
            Item node,
            Set<String> candidates,
            Set<String> claimedIds,
            List<Item> matches
    ) {
        if (node.isElementNode()) {
            Set<String> elementIds = this.lookup == Lookup.ID
                ? idValuesForId(node)
                : idValuesForElementWithId(node);
            boolean matchesFirstOccurrence = false;
            for (String id : elementIds) {
                if (candidates.contains(id) && claimedIds.add(id)) {
                    matchesFirstOccurrence = true;
                }
            }
            if (matchesFirstOccurrence) {
                matches.add(node);
            }
        }
        for (Item child : node.children()) {
            collectIds(child, candidates, claimedIds, matches);
        }
    }

    private static Set<String> idValuesForId(Item element) {
        Set<String> result = new LinkedHashSet<>();
        addIdentityValues(element, result);
        for (Item attribute : element.attributes()) {
            addIdentityValues(attribute, result);
        }
        return result;
    }

    private static Set<String> idValuesForElementWithId(Item element) {
        Set<String> result = new LinkedHashSet<>();
        for (Item attribute : element.attributes()) {
            addIdentityValues(attribute, result);
        }
        for (Item child : element.children()) {
            if (child.isElementNode()) {
                addIdentityValues(child, result);
            }
        }
        return result;
    }

    private static void addIdentityValues(Item node, Set<String> result) {
        if (!node.isId()) {
            return;
        }
        for (Item value : node.typedValue()) {
            String lexicalValue = value.getStringValue();
            if (NamespaceBindingUtils.isValidNcName(lexicalValue)) {
                result.add(lexicalValue);
            }
        }
    }

    private static void collectIdRefs(Item node, Set<String> candidates, List<Item> matches) {
        if (node.isElementNode()) {
            addIdRefMatch(node, candidates, matches);
            for (Item attribute : node.attributes()) {
                addIdRefMatch(attribute, candidates, matches);
            }
        }
        for (Item child : node.children()) {
            collectIdRefs(child, candidates, matches);
        }
    }

    private static void addIdRefMatch(Item node, Set<String> candidates, List<Item> matches) {
        if (!node.isIdrefs()) {
            return;
        }
        for (String token : node.getStringValue().trim().split("\\s+")) {
            if (NamespaceBindingUtils.isValidNcName(token) && candidates.contains(token)) {
                matches.add(node);
                return;
            }
        }
    }
}
