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

import java.io.Serial;
import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class FunctionIdentifier implements Serializable {
    private int arity;
    private Name name;

    @Serial
    private static final long serialVersionUID = 1L;

    public FunctionIdentifier(Name functionName, int arity) {
        this.name = functionName;
        this.arity = arity;
    }

    public Name getNameWithArity() {
        return this.name.addArityToFunctionName(this.getArity());
    }

    @Override
    public String toString() {
        return this.name + "#" + this.arity;
    }
}
