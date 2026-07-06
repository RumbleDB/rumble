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
 *
 */

package org.rumbledb.cli;

import java.util.List;

import org.rumbledb.cli.commands.Repl;
import org.rumbledb.cli.commands.Run;
import org.rumbledb.exceptions.CliException;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
    name = "rumbledb",
    description = "RumbleDB command line interface.",
    subcommands = {
        Run.class,
        Repl.class
    }
)
public final class CLIArgumentParser {
    public static CLIInvocation parse(String... args) {
        CommandLine commandLine = new CommandLine(new CLIArgumentParser());
        CommandLine.ParseResult parseResult =
            commandLine.parseArgs(args);

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
