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
package org.rumbledb.runtime.functions;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.functions.util.formatting.pictures.FormatInteger.IntegerPictureFormatter;

public class IntegerNumberingSequenceTest {
    private String format(long value, String picture) {
        return IntegerPictureFormatter.format(BigInteger.valueOf(value), picture, "en", ExceptionMetadata.EMPTY_METADATA);
    }

    @Test
    public void qt3Sequences() {
        String[] pictures = { "①", "⑴", "⒈", "Α", "α" };
        String[] expected = { "①|②|③|④|⑤", "⑴|⑵|⑶|⑷|⑸", "⒈|⒉|⒊|⒋|⒌", "Α|Β|Γ|Δ|Ε", "α|β|γ|δ|ε" };
        for (int p = 0; p < pictures.length; p++) {
            StringBuilder actual = new StringBuilder();
            for (int i = 1; i <= 5; i++) {
                if (i > 1) actual.append('|');
                actual.append(format(i, pictures[p]));
            }
            assertEquals(expected[p], actual.toString());
        }
        String[] firstJapanese = { "一", "二", "三", "四", "五", "六", "七", "八", "九", "十",
            "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十", "二十一", "二十二", "二十三" };
        for (int i = 0; i < firstJapanese.length; i++) assertEquals(firstJapanese[i], format(i + 1, "一"));
        long[] numbers = { 10, 11, 20, 23, 151, 302, 469, 2025, 10000, 10001, 100000000 };
        String[] kanji = { "十", "十一", "二十", "二十三", "百五十一", "三百二", "四百六十九", "二千二十五", "一万", "一万一", "一億" };
        for (int i = 0; i < numbers.length; i++) assertEquals(kanji[i], format(numbers[i], "一"));
    }

    @Test
    public void boundariesAndFallback() {
        assertEquals("⑳", format(20, "①"));
        assertEquals("21", format(21, "①"));
        assertEquals("0", format(0, "一"));
        assertEquals("ω", format(24, "α"));
        assertEquals("αα", format(25, "α"));
        assertEquals("σ", format(18, "α"));
        assertEquals("-③", format(-3, "①"));
        assertEquals("١٢", format(12, "١"));
        assertEquals("5", format(5, "β"));
        for (String token : new String[] { "①", "⑴", "⒈", "α", "Α", "一" }) {
            assertEquals("0", format(0, token));
        }
        BigInteger limit = BigInteger.TEN.pow(20);
        assertEquals(limit.toString(), IntegerPictureFormatter.format(limit, "一", "en", ExceptionMetadata.EMPTY_METADATA));
    }
}
