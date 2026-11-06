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
package org.rumbledb.expressions.module;

import lombok.Getter;
import lombok.Setter;

import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Node;

/** A prolog declaration. It belongs to one module's static context, even once imports are flattened into a library. */
public abstract class PrologDeclaration extends Node {

    @Getter
    @Setter
    private StaticContext moduleContext;

    protected PrologDeclaration(ExceptionMetadata metadata) {
        super(metadata);
    }
}
