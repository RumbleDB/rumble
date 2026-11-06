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
package org.rumbledb.context;

import lombok.Getter;
import lombok.Setter;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.types.SequenceType;

@Getter
public class InScopeVariable {

    private final Name name;
    private final SequenceType sequenceType;
    private final ExceptionMetadata metadata;

    @Setter
    private ExecutionMode storageMode;

    private final boolean isAssignable;

    public InScopeVariable(
            Name name, SequenceType sequenceType, ExceptionMetadata metadata, ExecutionMode storageMode) {
        this.name = name;
        this.sequenceType = sequenceType;
        this.metadata = metadata;
        this.storageMode = storageMode;
        this.isAssignable = false; // unspecified means false.
    }

    public InScopeVariable(
            Name name, SequenceType type, ExceptionMetadata metadata, ExecutionMode storageMode, boolean isAssignable) {
        this.name = name;
        this.sequenceType = type;
        this.metadata = metadata;
        this.storageMode = storageMode;
        this.isAssignable = isAssignable;
    }
}
