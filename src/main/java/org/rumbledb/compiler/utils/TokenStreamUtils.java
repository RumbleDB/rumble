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
package org.rumbledb.compiler.utils;

import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

/** Utilities for querying and inspecting ANTLR token streams. */
public final class TokenStreamUtils {

    private TokenStreamUtils() {}

    /**
     * Concatenates hidden-channel tokens immediately following a token.
     *
     * @param tokenStream token stream containing the hidden tokens
     * @param previousTokenIndex index of the token preceding the hidden tokens
     * @return the hidden text, or an empty string when no hidden tokens follow
     */
    public static String getHiddenTextAfter(CommonTokenStream tokenStream, int previousTokenIndex) {
        List<Token> hidden = tokenStream.getHiddenTokensToRight(previousTokenIndex);
        if (hidden == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (Token token : hidden) {
            result.append(token.getText());
        }
        return result.toString();
    }
}
