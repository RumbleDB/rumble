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
package org.rumbledb.exceptions;

import java.io.Serial;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The static type errors of a module, when there are several. Its code, message and location are those of the
 * first error, and its full message lists them all.
 */
public class StaticTypeErrorsException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<RumbleException> errors;

    public StaticTypeErrorsException(List<RumbleException> errors) {
        super(
                errors.get(0).getJSONiqErrorMessage(),
                errors.get(0).getErrorCode(),
                errors.get(0).getMetadata());
        this.errors = List.copyOf(errors);
    }

    /** The errors in the order they were found. */
    public List<RumbleException> getErrors() {
        return this.errors;
    }

    @Override
    public String getMessage() {
        return this.errors.stream().map(RumbleException::getMessage).collect(Collectors.joining("\n"));
    }
}
