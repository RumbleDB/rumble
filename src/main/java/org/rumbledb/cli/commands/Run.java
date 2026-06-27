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

package org.rumbledb.cli.commands;

import org.rumbledb.cli.options.Input;
import org.rumbledb.cli.options.Output;
import org.rumbledb.config.ExecutionMode;
import org.rumbledb.config.RumbleConfiguration;

import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Parameters;

@Command(name = "run", description = "Executes a query.", mixinStandardHelpOptions = false)
public final class Run extends AbstractCommand {
    @Mixin
    Input input;

    @Mixin
    Output output;

    @Parameters(
        index = "0",
        arity = "0..1",
        paramLabel = "query-file",
        description = "A JSONiq query file to read from (from any file system, even the Web!)."
    )
    String queryPath;

    @Override
    public RumbleConfiguration call() {
        return this.baseConfiguration(ExecutionMode.RUN)
            .input(this.input.toInputOptions(this.queryPath))
            .output(this.output.toOutputOptions())
            .build();
    }
}
