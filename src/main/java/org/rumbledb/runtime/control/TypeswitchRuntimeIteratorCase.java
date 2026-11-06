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
package org.rumbledb.runtime.control;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import lombok.Getter;

import org.rumbledb.context.Name;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.types.SequenceType;

@Getter
public class TypeswitchRuntimeIteratorCase implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Name variableName;
    private final List<SequenceType> sequenceTypeUnion;
    private final ItemRuntimePlan returnIterator;

    public TypeswitchRuntimeIteratorCase(
            Name variableName, List<SequenceType> sequenceTypeUnion, ItemRuntimePlan returnIterator) {
        this.variableName = variableName;
        this.sequenceTypeUnion = sequenceTypeUnion;
        this.returnIterator = returnIterator;
    }

    public TypeswitchRuntimeIteratorCase(Name variableName, ItemRuntimePlan returnIterator) {
        this.variableName = variableName;
        this.sequenceTypeUnion = null;
        this.returnIterator = returnIterator;
    }
}
