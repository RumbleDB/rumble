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

import org.rumbledb.expressions.module.Module;

/**
 * One ordered compilation stage. Analysis and validation passes may mutate the input and return it;
 * rewrites may return a replacement. Callers must always continue with the returned module.
 * Implementations must not retain visitor state between invocations. Prerequisites and annotation
 * effects are documented by each pass; ordering is owned by CompilationPipeline.
 */
interface CompilationPass<M extends Module> {
    String name();

    M apply(M module, CompilationContext context);
}
