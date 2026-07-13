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

package org.rumbledb.runtime.functions;

import java.net.URI;
import java.net.URISyntaxException;

public final class URIUtils {

    private URIUtils() {
    }

    /**
     * Parses a URI reference and, when it is relative, resolves it against an absolute base URI.
     *
     * @param baseURI the base URI; it may be {@code null} when {@code reference} is absolute
     * @param reference a URI reference
     * @return an absolute URI
     * @throws URISyntaxException if the reference is malformed or cannot be resolved to an absolute URI
     */
    public static URI resolveURIReference(URI baseURI, String reference) throws URISyntaxException {
        if (reference == null) {
            throw new URISyntaxException("null", "The URI reference cannot be null");
        }

        URI referenceURI = new URI(reference);
        if (referenceURI.isAbsolute()) {
            return referenceURI;
        }

        if (baseURI == null || !baseURI.isAbsolute()) {
            throw new URISyntaxException(
                    reference,
                    "A relative URI reference requires an absolute base URI"
            );
        }

        URI resolvedURI = baseURI.resolve(referenceURI);
        if (!resolvedURI.isAbsolute()) {
            throw new URISyntaxException(reference, "The URI reference could not be resolved to an absolute URI");
        }
        return resolvedURI;
    }
}
