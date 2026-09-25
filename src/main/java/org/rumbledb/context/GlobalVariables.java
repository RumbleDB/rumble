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
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

import org.rumbledb.exceptions.OurBadException;

@Getter
public class GlobalVariables implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Set<Name> globalVariables;

    public GlobalVariables() {
        this.globalVariables = new HashSet<>();
    }

    public void addGlobalVariable(Name globalVariable) {
        if (this.globalVariables.contains(globalVariable)) {
            throw new OurBadException("Attempting to register global variable a second time.");
        }
        this.globalVariables.add(globalVariable);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Global variables:\n");
        this.globalVariables.forEach(variable -> sb.append(variable).append("\n"));
        return sb.toString();
    }
}
