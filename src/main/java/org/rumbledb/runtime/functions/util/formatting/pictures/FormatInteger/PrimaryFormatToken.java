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
package org.rumbledb.runtime.functions.util.formatting.pictures.FormatInteger;

import lombok.Getter;

import org.rumbledb.runtime.functions.util.formatting.NumericPicture;

@Getter
public class PrimaryFormatToken {

    public static final String DECIMAL = "DECIMAL";
    public static final String ALPHABETIC_UPPER = "ALPHABETIC_UPPER";
    public static final String ALPHABETIC_LOWER = "ALPHABETIC_LOWER";
    public static final String ROMAN_UPPER = "ROMAN_UPPER";
    public static final String ROMAN_LOWER = "ROMAN_LOWER";
    public static final String WORDS_LOWER = "WORDS_LOWER";
    public static final String WORDS_UPPER = "WORDS_UPPER";
    public static final String WORDS_TITLE = "WORDS_TITLE";
    public static final String OTHER = "OTHER";

    private final String type;
    private final NumericPicture numericPicture;
    private final String otherToken;

    private PrimaryFormatToken(String type, NumericPicture numericPicture, String otherToken) {
        this.type = type;
        this.numericPicture = numericPicture;
        this.otherToken = otherToken;
    }

    public static PrimaryFormatToken decimal(NumericPicture numericPicture) {
        return new PrimaryFormatToken(DECIMAL, numericPicture, null);
    }

    public static PrimaryFormatToken alphabeticUpper() {
        return new PrimaryFormatToken(ALPHABETIC_UPPER, null, null);
    }

    public static PrimaryFormatToken alphabeticLower() {
        return new PrimaryFormatToken(ALPHABETIC_LOWER, null, null);
    }

    public static PrimaryFormatToken romanUpper() {
        return new PrimaryFormatToken(ROMAN_UPPER, null, null);
    }

    public static PrimaryFormatToken romanLower() {
        return new PrimaryFormatToken(ROMAN_LOWER, null, null);
    }

    public static PrimaryFormatToken wordsLower() {
        return new PrimaryFormatToken(WORDS_LOWER, null, null);
    }

    public static PrimaryFormatToken wordsUpper() {
        return new PrimaryFormatToken(WORDS_UPPER, null, null);
    }

    public static PrimaryFormatToken wordsTitle() {
        return new PrimaryFormatToken(WORDS_TITLE, null, null);
    }

    public static PrimaryFormatToken other(String otherToken) {
        return new PrimaryFormatToken(OTHER, null, otherToken);
    }
}
