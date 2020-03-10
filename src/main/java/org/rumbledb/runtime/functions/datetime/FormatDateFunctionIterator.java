package org.rumbledb.runtime.functions.datetime;

import org.joda.time.DateTime;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ComponentSpecifierNotAvailableException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IncorrectSyntaxFormatDateTimeException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.DynamicContext;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class FormatDateFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;
    private Item valueDateItem = null;
    private Item pictureStringItem = null;

    public FormatDateFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            try {
                if (this.valueDateItem.isNull()) {
                    return this.valueDateItem;
                }

                DateTime dateValue = this.valueDateItem.getDateTimeValue();
                String pictureString = this.pictureStringItem.getStringValue();

                // Start sequence
                int startOfSequence = 0;
                boolean variableMarkerSequence;
                if (pictureString.charAt(0) == '[') {
                    variableMarkerSequence = true;
                    startOfSequence = 1;
                } else {
                    variableMarkerSequence = false;
                    startOfSequence = 0;
                }

                StringBuilder result = new StringBuilder();

                // Iterate over picture
                for (int i = 1; i < pictureString.length(); i++) {
                    char c = pictureString.charAt(i);
                    if (variableMarkerSequence) {
                        if (c == ']') {
                            String variableMarker = pictureString.substring(startOfSequence, i);

                            // get component specifier (required)
                            if (variableMarker.length() == 0) {
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
                                    componentSpecifier = 'u';
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
                            char presentationModifier2 = '0';
                            int minWidth = 1;
                            int maxWidth = -1;

                            String variableMarkerOptionalModifiers = variableMarker.substring(1);

                            if (variableMarkerOptionalModifiers.length() > 0) {
                                List<String> variableMarkerModifiers =
                                    Arrays.asList(variableMarkerOptionalModifiers.split(","));
                                int variableMarkerModifiersSize = variableMarkerModifiers.size();

                                if (variableMarkerModifiersSize > 2) {
                                    // Groups not supported. Only one comma accepted for picture argument.
                                    String message = String.format(
                                        "\"%s\": incorrect syntax",
                                        this.pictureStringItem.serialize()
                                    );
                                    throw new IncorrectSyntaxFormatDateTimeException(message, getMetadata());
                                } else {
                                    if (variableMarkerModifiersSize >= 1) {
                                        // component specifier present
                                        String presentationModifiers = variableMarkerModifiers.get(0);
                                        int presentationModifiersLength = presentationModifiers.length();
                                        if (presentationModifiersLength == 1) {
                                            presentationModifier1 = variableMarkerModifiers.get(0);
                                        } else {
                                            char lastChar = presentationModifiers.charAt(
                                                presentationModifiersLength - 1
                                            );
                                            switch (lastChar) {
                                                case 'a':
                                                    presentationModifier2 = 'a';
                                                    presentationModifier1 = presentationModifiers.substring(
                                                        0,
                                                        presentationModifiersLength - 1
                                                    );
                                                    break;
                                                case 't':
                                                    presentationModifier2 = 't';
                                                    presentationModifier1 = presentationModifiers.substring(
                                                        0,
                                                        presentationModifiersLength - 1
                                                    );
                                                    break;
                                                case 'c':
                                                    presentationModifier2 = 'c';
                                                    presentationModifier1 = presentationModifiers.substring(
                                                        0,
                                                        presentationModifiersLength - 1
                                                    );
                                                    break;
                                                case 'o':
                                                    presentationModifier2 = 'o';
                                                    presentationModifier1 = presentationModifiers.substring(
                                                        0,
                                                        presentationModifiersLength - 1
                                                    );
                                                    break;
                                                default:
                                                    presentationModifier1 = presentationModifiers;
                                            }
                                        }
                                    }
                                    if (variableMarkerModifiersSize == 2) {
                                        // width modifier present
                                        String variableMarkerOptionalWidthModifiers = variableMarkerModifiers.get(1);
                                        if (variableMarkerOptionalWidthModifiers.length() > 0) {
                                            List<String> widthModifier =
                                                Arrays.asList(variableMarkerOptionalWidthModifiers.split("-"));
                                            int widthModifierSize = widthModifier.size();
                                            if (widthModifierSize >= 1 && !widthModifier.get(0).equals("*")) {
                                                minWidth = Integer.parseInt(widthModifier.get(0));
                                            }
                                            if (widthModifierSize == 2 && !widthModifier.get(1).equals("*")) {
                                                maxWidth = Integer.parseInt(widthModifier.get(1));
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
                            if (presentationModifier1.length() > 0) {
                                if (
                                    presentationModifier1.equals("Nn")
                                        || presentationModifier1.equals("N")
                                        || presentationModifier1.equals("n")
                                ) {
                                    if (maxWidth < 1)
                                        maxWidth = 10;
                                    if (
                                        componentSpecifier == 'd'
                                            || componentSpecifier == 'D'
                                            || componentSpecifier == 'F'
                                    )
                                        componentSpecifier = 'E';
                                } else {
                                    char presentationModifierStart = presentationModifier1.charAt(0);
                                    // check if numeric sequence as format token
                                    if (presentationModifierStart >= '0' && presentationModifierStart <= '9') {
                                        if (maxWidth < 1)
                                            maxWidth = presentationModifier1.length();
                                    } else {
                                        maxWidth = 1;
                                    }
                                }
                            } else {
                                if (maxWidth < 1)
                                    maxWidth = 1;
                            }
                            for (int j = minWidth; j <= maxWidth; ++j)
                                pattern.append(componentSpecifier);

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern.toString());
                            Calendar formatCalendar = Calendar.getInstance();
                            formatCalendar.set(
                                dateValue.getYear(),
                                dateValue.getMonthOfYear() - 1,
                                dateValue.getDayOfMonth()
                            );
                            result.append(simpleDateFormat.format(formatCalendar.getTime()));

                            variableMarkerSequence = false;
                            startOfSequence = i + 1;
                        }
                    } else {
                        if (c == ']') {
                            String message = String.format(
                                "\"%s\": incorrect syntax",
                                this.pictureStringItem.serialize()
                            );
                            throw new IncorrectSyntaxFormatDateTimeException(message, getMetadata());
                        } else if (c == '[') {
                            String literalSubstring = pictureString.substring(startOfSequence, i);
                            result.append(literalSubstring);
                            variableMarkerSequence = true;
                            startOfSequence = i + 1;
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
                            startOfSequence,
                            pictureString.length()
                        );
                        result.append(literalSubstring);
                    }
                }
                return ItemFactory.getInstance().createStringItem(result.toString());
            } catch (UnsupportedOperationException | IllegalArgumentException e) {
                String message = String.format(
                    "\"%s\": not castable to type %s",
                    this.valueDateItem.serialize(),
                    "date"
                );
                throw new CastException(message, getMetadata());
            }
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " format-date function",
                    getMetadata()
            );
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this.valueDateItem = this.children.get(0)
            .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
        this.pictureStringItem = this.children.get(1)
            .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.valueDateItem != null && this.pictureStringItem != null;
    }
}
