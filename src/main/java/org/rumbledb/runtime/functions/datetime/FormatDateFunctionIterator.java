package org.rumbledb.runtime.functions.datetime;

import java.time.OffsetDateTime;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ComponentSpecifierNotAvailableException;
import org.rumbledb.exceptions.IncorrectSyntaxFormatDateTimeException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class FormatDateFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private Item pictureStringItem = null;

    public FormatDateFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item valueDateItem = this.children.get(0)
            .materializeFirstItemOrNull(context);
        this.pictureStringItem = this.children.get(1)
            .materializeFirstItemOrNull(context);
        if (valueDateItem == null || this.pictureStringItem == null) {
            return null;
        }
        try {
            if (valueDateItem.isNull()) {
                return valueDateItem;
            }

            OffsetDateTime dateValue = valueDateItem.getDateTimeValue();
            String pictureString = this.pictureStringItem.getStringValue();

            int startOfSequence = 0;
            boolean variableMarkerSequence = false;

            StringBuilder result = new StringBuilder();

            // Iterate over picture
            for (int i = 0; i < pictureString.length(); i++) {
                char c = pictureString.charAt(i);
                if (variableMarkerSequence) {
                    if (c == ']') {
                        String variableMarker = pictureString.substring(startOfSequence, i);
                        String pattern = parseVariableMarker(variableMarker, result);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                        result.append(formatter.format(dateValue));

                        variableMarkerSequence = false;
                        startOfSequence = i + 1;
                    }
                } else {
                    if (c == ']') {
                        if (i == pictureString.length() - 1 || pictureString.charAt(i + 1) != ']') {
                            String message = String.format(
                                "\"%s\": incorrect syntax",
                                this.pictureStringItem.serialize()
                            );
                            throw new IncorrectSyntaxFormatDateTimeException(message, getMetadata());
                        } else {
                            String literalSubstring = pictureString.substring(startOfSequence, i + 1);
                            result.append(literalSubstring);
                            startOfSequence = i + 2;
                            i++;
                        }
                    } else if (c == '[') {
                        if (i == pictureString.length() - 1) {
                            String message = String.format(
                                "\"%s\": incorrect syntax",
                                this.pictureStringItem.serialize()
                            );
                            throw new IncorrectSyntaxFormatDateTimeException(message, getMetadata());
                        }

                        if (pictureString.charAt(i + 1) == '[') {
                            String literalSubstring = pictureString.substring(startOfSequence, i + 1);
                            result.append(literalSubstring);
                            startOfSequence = i + 2;
                            i++;
                        } else {
                            String literalSubstring = pictureString.substring(startOfSequence, i);
                            result.append(literalSubstring);
                            variableMarkerSequence = true;
                            startOfSequence = i + 1;
                        }
                    }
                }
            }

            if (startOfSequence != pictureString.length()) {
                if (variableMarkerSequence) {
                    String message = String.format(
                        "\"%s\": incorrect syntax",
                        this.pictureStringItem.serialize()
                    );
                    throw new IncorrectSyntaxFormatDateTimeException(message, getMetadata());
                } else {
                    String literalSubstring = pictureString.substring(
                        startOfSequence
                    );
                    result.append(literalSubstring);
                }
            }
            return ItemFactory.getInstance().createStringItem(result.toString());
        } catch (UnsupportedOperationException | IllegalArgumentException e) {
            String message = String.format(
                "\"%s\": not castable to type %s",
                valueDateItem.serialize(),
                "date"
            );
            throw new CastException(message, getMetadata());
        }
    }

    private String parsePresentationModifiers(String presentationModifiers) {
        String presentationModifier1;

        int presentationModifiersLength = presentationModifiers.length();
        if (presentationModifiersLength == 0)
            return "";
        if (presentationModifiersLength == 1) {
            presentationModifier1 = presentationModifiers;
        } else {
            char lastChar = presentationModifiers.charAt(
                presentationModifiersLength - 1
            );
            String message;
            switch (lastChar) {
                case 'a':
                    message = String.format(
                        "\"%s\": alphabetic numbering not supported",
                        this.pictureStringItem.serialize()
                    );
                    throw new UnsupportedFeatureException(message, getMetadata());
                case 't':
                case 'c':
                    presentationModifier1 = presentationModifiers.substring(
                        0,
                        presentationModifiersLength - 1
                    );
                    break;
                case 'o':
                    message = String.format(
                        "\"%s\": ordinal numbering not supported",
                        this.pictureStringItem.serialize()
                    );
                    throw new UnsupportedFeatureException(message, getMetadata());
                default:
                    presentationModifier1 = presentationModifiers;
            }
        }

        return presentationModifier1;
    }

    int parseWidthModifier(String widthModifier) {
        int width = -1;
        if (widthModifier.isEmpty()) {
            String message = String.format(
                "\"%s\": incorrect syntax",
                this.pictureStringItem.serialize()
            );
            throw new IncorrectSyntaxFormatDateTimeException(
                    message,
                    getMetadata()
            );
        }
        if (!widthModifier.equals("*"))
            width = Integer.parseInt(widthModifier);
        return width;
    }

    private String parseVariableMarker(String variableMarker, StringBuilder result) {
        if (variableMarker.isEmpty()) {
            String message = String.format(
                "\"%s\": incorrect syntax",
                this.pictureStringItem.serialize()
            );
            throw new IncorrectSyntaxFormatDateTimeException(message, getMetadata());
        }
        char componentSpecifier;
        switch (variableMarker.charAt(0)) {
            case 'Y':
                componentSpecifier = 'Y';
                break;
            case 'M':
                componentSpecifier = 'M';
                break;
            case 'd':
                componentSpecifier = 'D';
                break;
            case 'D':
                componentSpecifier = 'd';
                break;
            case 'F':
                componentSpecifier = 'E';
                break;
            default:
                String message = String.format(
                    "\"%s\": a component specifier refers to components"
                        + " that are not available in the %s type",
                    this.pictureStringItem.serialize(),
                    "date"
                );
                throw new ComponentSpecifierNotAvailableException(message, getMetadata());
        }

        String presentationModifier1 = "";
        int minWidth = 1;
        int maxWidth = -1;

        String variableMarkerOptionalModifiers = variableMarker.substring(1);

        if (!variableMarkerOptionalModifiers.isEmpty()) {
            List<String> variableMarkerModifiers =
                Arrays.asList(variableMarkerOptionalModifiers.split(","));
            int variableMarkerModifiersSize = variableMarkerModifiers.size();

            if (variableMarkerModifiersSize > 2) {
                // only one comma accepted for picture argument
                String message = String.format(
                    "\"%s\": groups not supported",
                    this.pictureStringItem.serialize()
                );
                throw new UnsupportedFeatureException(message, getMetadata());
            } else {
                if (variableMarkerModifiersSize >= 1) {
                    // presentation modifiers present
                    String presentationModifiers = variableMarkerModifiers.get(0);
                    presentationModifier1 = parsePresentationModifiers(presentationModifiers);
                }
                if (variableMarkerModifiersSize == 2) {
                    // width modifier present
                    String variableMarkerOptionalWidthModifiers = variableMarkerModifiers.get(1);
                    if (!variableMarkerOptionalWidthModifiers.isEmpty()) {
                        List<String> widthModifier =
                            Arrays.asList(variableMarkerOptionalWidthModifiers.split("-"));
                        int widthModifierSize = widthModifier.size();
                        if (widthModifierSize >= 1) {
                            minWidth = parseWidthModifier(widthModifier.get(0));
                            if (minWidth < 1)
                                minWidth = 1;
                        }
                        if (widthModifierSize == 2) {
                            maxWidth = parseWidthModifier(widthModifier.get(1));
                        }
                        if (widthModifierSize > 2) {
                            String message = String.format(
                                "\"%s\": incorrect syntax",
                                this.pictureStringItem.serialize()
                            );
                            throw new IncorrectSyntaxFormatDateTimeException(
                                    message,
                                    getMetadata()
                            );
                        }
                    } else {
                        String message = String.format(
                            "\"%s\": incorrect syntax",
                            this.pictureStringItem.serialize()
                        );
                        throw new IncorrectSyntaxFormatDateTimeException(message, getMetadata());
                    }
                }
            }
        }

        StringBuilder pattern = new StringBuilder();
        if (!presentationModifier1.isEmpty()) {
            if (presentationModifier1.equals("Nn")) {
                if (maxWidth < 1)
                    maxWidth = 4;
                if (
                    componentSpecifier == 'd' || componentSpecifier == 'D'
                )
                    componentSpecifier = 'M';
            } else {
                char presentationModifierStart = presentationModifier1.charAt(0);
                // check if numeric sequence as format token
                if (presentationModifierStart >= '0' && presentationModifierStart <= '9') {
                    int toReduce = 2;
                    if (componentSpecifier == 'Y')
                        toReduce = 4;
                    int prefixLength;
                    if (maxWidth < 1)
                        prefixLength = presentationModifier1.length() - toReduce;
                    else
                        prefixLength = maxWidth - toReduce;
                    pattern.append("0".repeat(Math.max(0, prefixLength)));
                    maxWidth = toReduce;
                } else {
                    String message = String.format(
                        "\"%s\": first presentation modifier not supported: %s",
                        this.pictureStringItem.serialize(),
                        presentationModifier1
                    );
                    throw new UnsupportedFeatureException(message, getMetadata());
                }
            }
        } else {
            if (maxWidth < 1)
                maxWidth = 1;
        }

        pattern.append(String.valueOf(componentSpecifier).repeat(Math.max(0, maxWidth - minWidth + 1)));

        return pattern.toString();
    }

}
