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
package org.rumbledb.expressions.control;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import lombok.EqualsAndHashCode;

import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CatchPattern implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private final String namespace;

    @EqualsAndHashCode.Include
    private final String localName;

    @EqualsAndHashCode.Include
    private final boolean namespaceWildcard;

    @EqualsAndHashCode.Include
    private final boolean localNameWildcard;

    private final String displayText;

    private CatchPattern(
            String namespace,
            String localName,
            boolean namespaceWildcard,
            boolean localNameWildcard,
            String displayText) {
        this.namespace = namespace;
        this.localName = localName;
        this.namespaceWildcard = namespaceWildcard;
        this.localNameWildcard = localNameWildcard;
        this.displayText = displayText;
    }

    public static CatchPattern catchAll() {
        return new CatchPattern(null, null, true, true, "*");
    }

    public static CatchPattern exact(Name name) {
        return new CatchPattern(name.getNamespace(), name.getLocalName(), false, false, name.toString());
    }

    public static CatchPattern namespaceWildcard(String localName, String displayText) {
        return new CatchPattern(null, localName, true, false, displayText);
    }

    public static CatchPattern localNameWildcard(String namespace, String displayText) {
        return new CatchPattern(namespace, null, false, true, displayText);
    }

    public boolean isCatchAll() {
        return this.namespaceWildcard && this.localNameWildcard;
    }

    public boolean matches(ErrorCode errorCode) {
        return matches(errorCode.getName());
    }

    public boolean matches(Name errorName) {
        if (!this.namespaceWildcard && !Objects.equals(this.namespace, errorName.getNamespace())) {
            return false;
        }
        if (!this.localNameWildcard && !Objects.equals(this.localName, errorName.getLocalName())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.displayText;
    }
}
