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

}
