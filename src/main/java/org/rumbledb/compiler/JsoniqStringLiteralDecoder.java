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

package org.rumbledb.compiler;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.translate.AggregateTranslator;
import org.apache.commons.text.translate.CharSequenceTranslator;

final class JsoniqStringLiteralDecoder {

    private static final CharSequenceTranslator UNESCAPER = new AggregateTranslator(
            StringEscapeUtils.UNESCAPE_JSON,
            StringEscapeUtils.UNESCAPE_XML
    );

    private JsoniqStringLiteralDecoder() {
    }

    static String decode(String raw) {
        return UNESCAPER.translate(raw);
    }
}
