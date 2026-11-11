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
package org.rumbledb.compiler.wrapper;

public class DescendentSequentialProperties {
    private final boolean hasInterruptStatement;
    private final boolean hasNonExitSequentialStatement;
    private final boolean hasExitStatement;

    public DescendentSequentialProperties(boolean hasNonExitSequentialStatement, boolean hasInterruptStatement) {
        this.hasInterruptStatement = hasInterruptStatement;
        this.hasNonExitSequentialStatement = hasNonExitSequentialStatement;
        this.hasExitStatement = false;
    }

    public DescendentSequentialProperties(
            boolean hasNonExitSequentialStatement, boolean hasInterruptStatement, boolean hasExitStatement) {
        this.hasInterruptStatement = hasInterruptStatement;
        this.hasNonExitSequentialStatement = hasNonExitSequentialStatement;
        this.hasExitStatement = hasExitStatement;
    }

    public boolean isSequential() {
        return this.hasNonExitSequentialStatement || this.hasExitStatement;
    }

    public boolean hasInterruptStatement() {
        return this.hasInterruptStatement;
    }

    public boolean hasExitStatement() {
        return this.hasExitStatement;
    }

    public boolean hasNonExitSequentialStatement() {
        return this.hasNonExitSequentialStatement;
    }
}
