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
package org.rumbledb.config.model;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import org.rumbledb.exceptions.CliException;

/**
 * A supported query language: a standard together with a version of that standard.
 */
public enum QueryLanguage {
    JSONIQ_10(Standard.JSONIQ, Version.V1_0),
    JSONIQ_31(Standard.JSONIQ, Version.V3_1),
    XQUERY_10(Standard.XQUERY, Version.V1_0),
    XQUERY_31(Standard.XQUERY, Version.V3_1);

    public enum Standard {
        JSONIQ,
        XQUERY
    }

    /** Declaration order is version order: {@link #isAtLeast(Version)} relies on it. */
    public enum Version {
        V1_0("1.0"),
        V3_1("3.1");

        private final String label;

        Version(String label) {
            this.label = label;
        }
    }

    private final Standard standard;
    private final Version version;
    private final String id;

    QueryLanguage(Standard standard, Version version) {
        this.standard = standard;
        this.version = version;
        this.id = standard.name().toLowerCase(Locale.ROOT) + version.label.replace(".", "");
    }

    public Standard standard() {
        return this.standard;
    }

    public Version version() {
        return this.version;
    }

    /** The name used by the configuration API and the {@code --default-language} option, such as "xquery31". */
    @JsonValue
    public String id() {
        return this.id;
    }

    public boolean isXQuery() {
        return this.standard == Standard.XQUERY;
    }

    public boolean isJsoniq() {
        return this.standard == Standard.JSONIQ;
    }

    public boolean isAtLeast(Version minimum) {
        return this.version.compareTo(minimum) >= 0;
    }

    /**
     * Parses a configuration value. XQuery 3.0 is accepted as an alias of XQuery 3.1, as in version declarations.
     *
     * @throws CliException if the value does not name a supported query language
     */
    @JsonCreator
    public static QueryLanguage fromId(String id) {
        String normalized = id.trim();
        String resolved = normalized.equals("xquery30") ? XQUERY_31.id : normalized;
        return Arrays.stream(values())
                .filter(language -> language.id.equals(resolved))
                .findFirst()
                .orElseThrow(() -> new CliException("Argument --default-language must be one of "
                        + Arrays.stream(values()).map(QueryLanguage::id).collect(Collectors.joining(", "))
                        + " (was: "
                        + id
                        + ")."));
    }

    /**
     * Resolves the version named by a version declaration, such as {@code xquery version "3.1";}.
     *
     * @return the language, or empty if the standard has no such supported version
     */
    public static Optional<QueryLanguage> fromDeclaration(Standard standard, String version) {
        String resolved = standard == Standard.XQUERY && version.equals("3.0") ? Version.V3_1.label : version;
        return Arrays.stream(values())
                .filter(language -> language.standard == standard && language.version.label.equals(resolved))
                .findFirst();
    }

    @Override
    public String toString() {
        return this.id;
    }
}
