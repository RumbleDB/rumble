package org.rumbledb.expressions.control;

import java.io.Serializable;
import java.util.Objects;

import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;

public class CatchPattern implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String namespace;
    private final String localName;
    private final boolean namespaceWildcard;
    private final boolean localNameWildcard;
    private final String displayText;

    private CatchPattern(
            String namespace,
            String localName,
            boolean namespaceWildcard,
            boolean localNameWildcard,
            String displayText
    ) {
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

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof CatchPattern)) {
            return false;
        }
        CatchPattern pattern = (CatchPattern) other;
        return Objects.equals(this.namespace, pattern.namespace)
            && Objects.equals(this.localName, pattern.localName)
            && this.namespaceWildcard == pattern.namespaceWildcard
            && this.localNameWildcard == pattern.localNameWildcard;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.namespace, this.localName, this.namespaceWildcard, this.localNameWildcard);
    }
}
