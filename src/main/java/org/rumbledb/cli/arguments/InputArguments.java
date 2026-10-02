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
package org.rumbledb.cli.arguments;

import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import org.rumbledb.config.model.InputConfig;

public final class InputArguments {
    @Option(
            names = {"-q", "--query"},
            paramLabel = "query",
            description = "A JSONiq query directly provided as a string.")
    private String query;

    @Option(
            names = "--query-path",
            paramLabel = "path",
            description = "A JSONiq query file to read from (from any file system, even the Web!).")
    private String queryPath;

    @Parameters(
            index = "0",
            arity = "0..1",
            paramLabel = "query-file",
            description = "A JSONiq query file to read from (from any file system, even the Web!).")
    private String positionalQueryPath;

    public InputConfig toConfig() {
        InputConfig.InputConfigBuilder builder = InputConfig.builder();
        OptionConversion.applyIfPresent(
                this.queryPath != null ? this.queryPath : this.positionalQueryPath, builder::queryPath);
        OptionConversion.applyIfPresent(this.query, builder::query);
        return builder.build();
    }
}
