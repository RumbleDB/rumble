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
 *
 * Authors: Matteo Agnoletto (EPMatt)
 *
 */

package org.rumbledb.runtime.xml;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidElementNameExpressionException;
import org.rumbledb.exceptions.InvalidComputedNamespaceConstructorException;
import org.rumbledb.exceptions.UnexpectedStaticTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.sequences.general.AtomizationIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Runtime iterator for computed namespace constructors.
 *
 * @see org.rumbledb.expressions.xml.ComputedNamespaceConstructorExpression
 */
public class ComputedNamespaceConstructorRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private static final Pattern NCNAME_PATTERN = Pattern.compile("[A-Za-z_][A-Za-z0-9._-]*");
    private String staticPrefix;
    private AtomizationIterator prefixIterator;
    private AtomizationIterator uriIterator;

    /**
     * Constructor for static prefix: namespace prefix { uri }
     *
     * @param staticPrefix The static namespace prefix
     * @param uriIterator The URI iterator (wrapped in AtomizationIterator)
     * @param staticContext The runtime static context
     */
    public ComputedNamespaceConstructorRuntimeIterator(
            String staticPrefix,
            AtomizationIterator uriIterator,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(uriIterator), staticContext);
        this.staticPrefix = staticPrefix;
        this.prefixIterator = null;
        this.uriIterator = uriIterator;
    }

    /**
     * Constructor for dynamic prefix: namespace { prefixExpression } { uri }
     *
     * @param prefixIterator The prefix iterator (wrapped in AtomizationIterator)
     * @param uriIterator The URI iterator (wrapped in AtomizationIterator)
     * @param staticContext The runtime static context
     */
    public ComputedNamespaceConstructorRuntimeIterator(
            AtomizationIterator prefixIterator,
            AtomizationIterator uriIterator,
            RuntimeStaticContext staticContext
    ) {
        super(createChildList(prefixIterator, uriIterator), staticContext);
        this.staticPrefix = null;
        this.prefixIterator = prefixIterator;
        this.uriIterator = uriIterator;
    }

    private static List<RuntimeIterator> createChildList(RuntimeIterator... iterators) {
        List<RuntimeIterator> children = new ArrayList<>();
        for (RuntimeIterator iterator : iterators) {
            if (iterator != null) {
                children.add(iterator);
            }
        }
        return children;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        // Spec: "A computed namespace constructor creates a new namespace node, with its own node identity."
        // Spec: "The parent of the newly created namespace node is empty."
        // Spec: "By itself, a computed namespace constructor has no effect on in-scope namespaces, but if an element
        // constructor's content sequence contains a namespace node, the namespace binding it represents is added to the
        // element's in-scope namespaces."
        String prefix = resolvePrefix(dynamicContext);
        String uri = resolveUri(dynamicContext);
        validateNamespaceBinding(prefix, uri);

        this.hasNext = false;

        return ItemFactory.getInstance().createXmlNamespaceNode(prefix, uri);
    }

    private String resolvePrefix(DynamicContext dynamicContext) {
        // Spec: "If the constructor specifies a Prefix, it is used as the prefix for the namespace node."
        if (this.staticPrefix != null) {
            return this.staticPrefix;
        }
        // Spec: "If the constructor specifies a PrefixExpr, the prefix expression is evaluated as follows:"
        // Spec: "Atomization is applied to the result of the PrefixExpr."
        List<Item> atomizedPrefixItems = this.prefixIterator.materialize(dynamicContext);
        // Spec: "If the result is the empty sequence or a zero-length xs:string or xs:untypedAtomic value, the new
        // namespace node has no name (such a namespace node represents a binding for the default namespace)."
        if (atomizedPrefixItems.isEmpty()) {
            return "";
        }
        // Spec: "If the result of atomization is not an empty sequence or a single atomic value of type xs:string or
        // xs:untypedAtomic, a type error is raised [err:XPTY0004]."
        if (atomizedPrefixItems.size() != 1) {
            throw new UnexpectedStaticTypeException(
                    "Computed namespace constructor prefix must evaluate to an empty sequence or a single atomic value of type xs:string or xs:untypedAtomic"
            );
        }
        Item prefixItem = atomizedPrefixItems.get(0);
        if (!prefixItem.isAtomic()) {
            throw new UnexpectedStaticTypeException(
                    "Computed namespace constructor prefix must evaluate to an empty sequence or a single atomic value of type xs:string or xs:untypedAtomic"
            );
        }
        // Spec: "If the result of atomization is an empty sequence or a single atomic value of type xs:string or
        // xs:untypedAtomic, then the following rules are applied in order:"
        String prefix = prefixItem.getStringValue();
        if (prefix.isEmpty()) {
            return "";
        }
        // Spec: "If the result is castable to xs:NCName, then it is used as the local name of the newly constructed
        // namespace node. (The local name of a namespace node represents the prefix part of the namespace binding.)"
        // Spec: "Otherwise, a dynamic error is raised [err:XQDY0074]."
        if (!isValidNCName(prefix)) {
            throw new InvalidElementNameExpressionException(
                    "Computed namespace constructor prefix cannot be cast to xs:NCName.",
                    getMetadata()
            );
        }
        return prefix;
    }

    private String resolveUri(DynamicContext dynamicContext) {
        // Spec: "The content expression is evaluated, and the result is cast to xs:anyURI to create the URI property
        // for the newly created node. An implementation may raise a dynamic error [err:XQDY0074] if the URIExpr of a
        // computed namespace
        // constructor is not a valid instance of xs:anyURI."
        List<Item> atomizedUriItems = this.uriIterator.materialize(dynamicContext);
        if (atomizedUriItems.isEmpty()) {
            return "";
        }
        if (atomizedUriItems.size() != 1) {
            throw new InvalidElementNameExpressionException(
                    "Computed namespace constructor URI must evaluate to a single atomic value"
            );
        }
        Item uriItem = atomizedUriItems.get(0);
        if (!uriItem.isAtomic()) {
            throw new InvalidElementNameExpressionException(
                    "Computed namespace constructor URI must evaluate to a single atomic value"
            );
        }
        return uriItem.getStringValue();
    }

    private void validateNamespaceBinding(String prefix, String uri) {
        // Spec: "An error [err:XQDY0101] is raised if a computed namespace constructor attempts to do any of the
        // following:"
        if (uri == null) {
            throw new InvalidComputedNamespaceConstructorException(
                    "Computed namespace constructor URI cannot be null.",
                    getMetadata()
            );
        }
        // Spec: "Bind any prefix (including the empty prefix) to a zero-length namespace URI."
        if (uri.isEmpty()) {
            throw new InvalidComputedNamespaceConstructorException(
                    "Computed namespace constructor cannot bind a prefix to a zero-length namespace URI.",
                    getMetadata()
            );
        }
        // Spec: "Bind the prefix xml to some namespace URI other than http://www.w3.org/XML/1998/namespace."
        NamespaceBindingUtils.ReservedNamespaceBindingError error = NamespaceBindingUtils
            .getReservedNamespaceBindingError(prefix, uri);
        if (error == null) {
            return;
        }
        switch (error) {
            case XML_PREFIX_WRONG_URI:
                throw new InvalidComputedNamespaceConstructorException(
                        "Computed namespace constructor cannot bind the prefix xml to a non-XML namespace URI.",
                        getMetadata()
                );
            case XMLNS_PREFIX:
                throw new InvalidComputedNamespaceConstructorException(
                        "Computed namespace constructor cannot bind the prefix xmlns.",
                        getMetadata()
                );
            case NON_XML_PREFIX_XML_URI:
                throw new InvalidComputedNamespaceConstructorException(
                        "Computed namespace constructor cannot bind a non-xml prefix to the XML namespace URI.",
                        getMetadata()
                );
            case XMLNS_URI:
                throw new InvalidComputedNamespaceConstructorException(
                        "Computed namespace constructor cannot bind any prefix to the xmlns namespace URI.",
                        getMetadata()
                );
            default:
                return;
        }
    }

    private boolean isValidNCName(String value) {
        return value != null && NCNAME_PATTERN.matcher(value).matches();
    }
}

