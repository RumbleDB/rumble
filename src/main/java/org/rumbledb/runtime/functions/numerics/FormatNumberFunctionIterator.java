package org.rumbledb.runtime.functions.numerics;

import org.rumbledb.api.Item;
import org.rumbledb.context.*;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidDecimalFormatName;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.formatting.pictures.FormatNumber.NumberPictureFormatter;

import java.util.List;
import java.util.Map;


public class FormatNumberFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public FormatNumberFunctionIterator(
            List<RuntimeIterator> children,
            RuntimeStaticContext staticContext
    ) {
        super(children, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item valueItem = this.children.get(0).materializeFirstItemOrNull(context);
        Item pictureItem = this.children.get(1).materializeFirstItemOrNull(context);
        Item decimalFormatNameItem = this.children.size() > 2
            ? this.children.get(2).materializeFirstItemOrNull(context)
            : null;

        if (valueItem == null) {
            return ItemFactory.getInstance()
                .createStringItem(
                    this.staticContext.getDefaultDecimalFormat().getNanSymbol()
                );
        }

        DecimalFormatDefinition defaultDecimalFormat = this.staticContext.getDefaultDecimalFormat();
        Map<Name, DecimalFormatDefinition> decimalFormats = this.staticContext.getDecimalFormats();
        Map<String, String> namespaces = this.staticContext.getStaticallyKnownNamespaces();

        DecimalFormatDefinition decimalFormat = defaultDecimalFormat;
        if (decimalFormatNameItem != null) {
            decimalFormat = resolveDecimalFormat(
                decimalFormatNameItem,
                defaultDecimalFormat,
                decimalFormats,
                namespaces,
                getMetadata()
            );
        }

        String result = NumberPictureFormatter.format(
            valueItem,
            pictureItem,
            decimalFormat,
            getMetadata()
        );
        return ItemFactory.getInstance().createStringItem(result);
    }

    private static DecimalFormatDefinition resolveDecimalFormat(
            Item decimalFormatNameItem,
            DecimalFormatDefinition defaultDecimalFormat,
            Map<Name, DecimalFormatDefinition> decimalFormats,
            Map<String, String> namespaces,
            ExceptionMetadata metadata
    ) {
        String lexicalName = decimalFormatNameItem.getStringValue();
        String trimmedName = lexicalName == null ? "" : lexicalName.trim();

        if (trimmedName.isEmpty()) {
            return defaultDecimalFormat;
        }

        Name resolvedName = resolveDecimalFormatName(
            trimmedName,
            namespaces,
            metadata
        );

        if (!decimalFormats.containsKey(resolvedName)) {
            throw new InvalidDecimalFormatName(
                    "Decimal format not found: " + trimmedName,
                    metadata
            );
        }

        return decimalFormats.get(resolvedName);
    }

    private static Name resolveDecimalFormatName(
            String text,
            Map<String, String> staticallyKnownNamespaces,
            ExceptionMetadata metadata
    ) {
        if (text.startsWith("Q{")) {
            int closingBrace = text.indexOf('}');
            if (closingBrace < 0 || closingBrace == text.length() - 1) {
                throw new InvalidDecimalFormatName(
                        "Invalid URIQualifiedName: " + text,
                        metadata
                );
            }

            String namespace = text.substring(2, closingBrace);
            String localName = text.substring(closingBrace + 1);

            if (localName.isEmpty()) {
                throw new InvalidDecimalFormatName(
                        "Invalid URIQualifiedName, missing local name: " + text,
                        metadata
                );
            }

            return new Name(namespace, null, localName);
        }

        int colon = text.indexOf(':');

        if (colon < 0) {
            return Name.createVariableInNoNamespace(text);
        }

        String prefix = text.substring(0, colon);
        String localName = text.substring(colon + 1);

        if (prefix.isEmpty() || localName.isEmpty()) {
            throw new InvalidDecimalFormatName(
                    "Invalid QName: " + text,
                    metadata
            );
        }

        String namespace = staticallyKnownNamespaces.get(prefix);
        if (namespace == null) {
            throw new InvalidDecimalFormatName(
                    "Prefix " + prefix + " could not be resolved against a namespace in scope.",
                    metadata
            );
        }

        return new Name(namespace, prefix, localName);
    }
}
