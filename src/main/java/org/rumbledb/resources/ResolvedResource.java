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
package org.rumbledb.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Objects;

import lombok.Getter;

@Getter
public final class ResolvedResource implements AutoCloseable {

    private final URI systemId;
    private final InputStream inputStream;

    public ResolvedResource(URI systemId, InputStream inputStream) {
        this.systemId = Objects.requireNonNull(systemId, "systemId must not be null");
        this.inputStream = Objects.requireNonNull(inputStream, "inputStream must not be null");
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }
}
