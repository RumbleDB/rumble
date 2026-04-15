package org.rumbledb.context;

import org.rumbledb.exceptions.OurBadException;

import java.io.Serializable;
import java.util.Map;

public class DecimalFormatRuntimeConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private final DecimalFormatDefinition defaultDecimalFormat;
    private final Map<Name, DecimalFormatDefinition> decimalFormats;
    private final Map<String, String> staticallyKnownNamespaces;

    public DecimalFormatRuntimeConfig(
            DecimalFormatDefinition defaultDecimalFormat,
            Map<Name, DecimalFormatDefinition> decimalFormats,
            Map<String, String> staticallyKnownNamespaces
    ) {
        this.defaultDecimalFormat = defaultDecimalFormat;
        this.decimalFormats = decimalFormats;
        this.staticallyKnownNamespaces = staticallyKnownNamespaces;
    }

    public DecimalFormatDefinition getDefaultDecimalFormat() {
        return this.defaultDecimalFormat;
    }

    public Map<Name, DecimalFormatDefinition> getDecimalFormats() {
        return this.decimalFormats;
    }

    public Map<String, String> getStaticallyKnownNamespaces() {
        return this.staticallyKnownNamespaces;
    }

    public DecimalFormatDefinition getDecimalFormat(Name name) {
        DecimalFormatDefinition result = this.decimalFormats.get(name);
        if (result == null) {
            throw new OurBadException("Unknown decimal format: " + name);
        }
        return result;
    }

    public boolean hasDecimalFormat(Name name) {
        return this.decimalFormats.containsKey(name);
    }
}
