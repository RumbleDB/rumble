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
package org.rumbledb.compiler.typing;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.StaticContext;
import org.rumbledb.resources.ResourceResolver;

/**
 * What a {@link ReturnTypeRule} may need besides the call.
 *
 * @param errors where the static type errors found in the call are reported
 * @param configuration the configuration, to read resources with
 * @param resourceResolver the resolver of resources, such as documents
 * @param staticContext the static context of the call
 */
public record ReturnTypeContext(
        StaticTypeErrors errors,
        RumbleConfiguration configuration,
        ResourceResolver resourceResolver,
        StaticContext staticContext) {}
