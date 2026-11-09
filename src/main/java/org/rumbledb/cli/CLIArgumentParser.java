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
package org.rumbledb.cli;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import org.rumbledb.cli.commands.Repl;
import org.rumbledb.cli.commands.Run;
import org.rumbledb.exceptions.CliException;

@Command(
        name = "rumbledb",
        description = "RumbleDB command line interface.",
        subcommands = {Run.class, Repl.class},
        mixinStandardHelpOptions = true,
        versionProvider = CLIArgumentParser.BannerVersionProvider.class)
public final class CLIArgumentParser {
    public static final class BannerVersionProvider implements CommandLine.IVersionProvider {
        @Override
        public String[] getVersion() throws IOException {
            try (var banner = CLIArgumentParser.class.getResourceAsStream("/assets/banner.txt")) {
                if (banner == null) {
                    throw new IOException("Missing banner resource: /assets/banner.txt");
                }
                return new String(banner.readAllBytes(), StandardCharsets.UTF_8)
                        .lines()
                        .toArray(String[]::new);
            }
        }
    }

    public static CLIInvocation parse(String... args) {
        CommandLine commandLine = new CommandLine(new CLIArgumentParser());
        CommandLine.ParseResult parseResult = commandLine.parseArgs(args);

        try {
            commandLine.getExecutionStrategy().execute(parseResult);
        } catch (CommandLine.ExecutionException e) {
            if (e.getCause() instanceof CliException cliException) {
                throw cliException;
            }
            throw e;
        }

        List<CommandLine> commands = parseResult.asCommandLineList();
        CommandLine activeCommand = commands.get(commands.size() - 1);

        return activeCommand.getExecutionResult();
    }
}
