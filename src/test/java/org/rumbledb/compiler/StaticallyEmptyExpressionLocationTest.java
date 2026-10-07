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

import java.net.URI;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.CompilationConfiguration;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.UnexpectedStaticTypeException;

class StaticallyEmptyExpressionLocationTest {
    @Test
    void reportsTheEmptyStepLocation() {
        URI uri = URI.create("file:///empty-step.xq");
        UnexpectedStaticTypeException exception = assertThrows(
                UnexpectedStaticTypeException.class,
                () -> CompilationPipeline.compileMainModule(
                        "let $text := text { \"x\" }\nreturn $text/child",
                        uri,
                        new CompilationConfiguration(RumbleConfiguration.builder()
                                .configureAnalysis(analysis -> analysis.enableStaticTyping(true))
                                .build()),
                        ExternalBindings.empty()));

        assertEquals(uri.toString(), exception.getMetadata().getLocation());
        assertEquals(2, exception.getMetadata().getStart().line());
    }
}
