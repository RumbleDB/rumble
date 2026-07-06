/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package iq.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public final class TestFileDiscovery {

    private static final String JSONIQ_EXTENSION = ".jq";
    private static final String[] XQUERY_EXTENSIONS = { ".xq", ".xqy", ".xquery" };

    private TestFileDiscovery() {
    }

    public static List<File> jsoniqFiles(File directory) throws IOException {
        return files(directory, JSONIQ_EXTENSION);
    }

    public static List<File> xqueryFiles(File directory) throws IOException {
        return files(directory, XQUERY_EXTENSIONS);
    }

    public static List<File> files(File directory, String... extensions) throws IOException {
        Path root = directory.toPath();
        if (!Files.isDirectory(root)) {
            throw new IOException("Test directory not found: " + directory.getAbsolutePath());
        }

        Set<String> allowedExtensions = Set.copyOf(Arrays.asList(extensions));
        try (Stream<Path> paths = Files.walk(root)) {
            return paths
                .filter(Files::isRegularFile)
                .filter(path -> hasExtension(path, allowedExtensions))
                .sorted(
                    Comparator.comparing((Path path) -> path.getFileName().toString())
                        .thenComparing(Path::toString)
                )
                .map(Path::toFile)
                .toList();
        }
    }

    private static boolean hasExtension(Path path, Set<String> extensions) {
        String fileName = path.getFileName().toString();
        return extensions.stream().anyMatch(fileName::endsWith);
    }
}
