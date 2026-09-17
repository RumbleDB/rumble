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
package org.rumbledb.compiler;

import lombok.extern.log4j.Log4j2;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.expressions.module.Module;

/** Shared diagnostics for compiler stages. */
@Log4j2
public final class CompilerDiagnostics {
    private CompilerDiagnostics() {}

    public static void debugPrintTree(Module node, RumbleConfiguration conf) {
        if (conf.debug().printIteratorTree() || conf.debug().logging()) {
            log.debug(
                    """
                        ***************
                        Expression tree
                        ***************
                        Unset execution modes: {}
                        {}

                        {}\
                        """,
                    node.numberOfUnsetExecutionModes(),
                    node,
                    node.getStaticContext());
        }
    }

    public static void debugPrintHeader(RumbleConfiguration conf, String header) {
        if (conf.debug().printIteratorTree() || conf.debug().logging()) {
            log.debug(
                    """
                        {}
                        {}
                        {}\
                        """,
                    "*".repeat(header.length()),
                    header,
                    "*".repeat(header.length()));
        }
    }
}
