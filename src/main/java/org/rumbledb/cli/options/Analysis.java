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

package org.rumbledb.cli.options;

import org.rumbledb.config.AnalysisOptions;

import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

public final class Analysis {
    @Option(
        names = { "-t", "--static-typing" },
        scope = ScopeType.INHERIT,
        negatable = true,
        description = {
            "Activates static type analysis, which annotates the expression tree with inferred types at compile time.",
            "Enables more optimizations (experimental). Deactivated by default."
        }
    )
    private boolean staticTyping;

    @Option(
        names = "--print-inferred-types",
        scope = ScopeType.INHERIT,
        negatable = true,
        description = "Prints inferred types."
    )
    private boolean printInferredTypes;

    public AnalysisOptions toAnalysisOptions() {
        return AnalysisOptions.builder()
            .staticTyping(this.staticTyping)
            .printInferredTypes(this.printInferredTypes)
            .build();
    }
}
