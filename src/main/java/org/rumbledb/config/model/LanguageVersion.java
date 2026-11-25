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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import org.rumbledb.exceptions.CliException;

/**
 * A supported query language at a specific version.
 */
public enum LanguageVersion {
    JSONIQ_10(Language.JSONIQ, Version.V1_0),
    JSONIQ_31(Language.JSONIQ, Version.V3_1),
    XQUERY_10(Language.XQUERY, Version.V1_0),
    /** XQuery 3.0 modules run as XQuery 3.1. */
    XQUERY_31(Language.XQUERY, Version.V3_1, "3.0");

    /** The JSONiq version used when nothing selects one explicitly. */
    public static final LanguageVersion DEFAULT_JSONIQ = JSONIQ_31;

    /** The XQuery version used when nothing selects one explicitly. */
    public static final LanguageVersion DEFAULT_XQUERY = XQUERY_31;

    public enum Language {
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

    private final Language language;
    private final Version version;
    private final String id;
    /** The version's own label and any aliases, as written in version declarations. */
    private final Set<String> acceptedVersions;

    LanguageVersion(Language language, Version version, String... versionAliases) {
        this.language = language;
        this.version = version;
        this.id = idOf(language, version.label);
        Set<String> accepted = new HashSet<>(List.of(versionAliases));
        accepted.add(version.label);
        this.acceptedVersions = Set.copyOf(accepted);
    }

    private static String idOf(Language language, String versionLabel) {
        return language.name().toLowerCase(Locale.ROOT) + versionLabel.replace(".", "");
    }

    /** The version used for a module in the given language when none is selected explicitly. */
    public static LanguageVersion defaultFor(Language language) {
        return language == Language.XQUERY ? DEFAULT_XQUERY : DEFAULT_JSONIQ;
    }

    public Language language() {
        return this.language;
    }

    /** The name used by the configuration API and the {@code --default-language} option, such as "xquery31". */
    @JsonValue
    public String id() {
        return this.id;
    }

    public boolean isXQuery() {
        return this.language == Language.XQUERY;
    }

    public boolean isJsoniq() {
        return this.language == Language.JSONIQ;
    }

    public boolean isAtLeast(Version minimum) {
        return this.version.compareTo(minimum) >= 0;
    }

    /**
     * Parses a configuration value such as "xquery31". Version aliases are accepted, as in version declarations.
     *
     * @throws CliException if the value does not name a supported query language
     */
    @JsonCreator
    public static LanguageVersion fromId(String id) {
        String normalized = id.trim();
        return Arrays.stream(values())
                .filter(candidate -> candidate.acceptedVersions.stream()
                        .anyMatch(version -> idOf(candidate.language, version).equals(normalized)))
                .findFirst()
                .orElseThrow(() -> new CliException("Argument --default-language must be one of "
                        + Arrays.stream(values()).map(LanguageVersion::id).collect(Collectors.joining(", "))
                        + " (was: "
                        + id
                        + ")."));
    }

    /**
     * Resolves the version named by a version declaration, such as {@code xquery version "3.1";}.
     *
     * @return the language version, or empty if the language has no such supported version
     */
    public static Optional<LanguageVersion> fromDeclaration(Language language, String version) {
        return Arrays.stream(values())
                .filter(candidate -> candidate.language == language && candidate.acceptedVersions.contains(version))
                .findFirst();
    }

    @Override
    public String toString() {
        return this.id;
    }
}
