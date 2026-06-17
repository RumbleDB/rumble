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
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FileManager {

    public static final String TEST_FILE_EXTENSION = ".jq";
    public static final List<String> XQUERY_TEST_FILE_EXTENSIONS = Arrays.asList(".xq", ".xqy", ".xquery");

    public static List<File> loadJiqFiles(File directory) {
        return loadFiles(directory, TEST_FILE_EXTENSION);
    }

    public static List<File> loadXQueryFiles(File directory) {
        return loadFiles(directory, XQUERY_TEST_FILE_EXTENSIONS.toArray(new String[0]));
    }

    public static List<File> loadFiles(File directory, String... extensions) {
        List<File> files = new ArrayList<>();
        List<String> normalizedExtensions = Arrays.stream(extensions)
            .map(extension -> extension.toLowerCase(Locale.ROOT))
            .toList();
        Arrays.asList(directory.listFiles())
            .stream()
            .filter(file -> hasAnyExtension(file, normalizedExtensions))
            .forEach(file -> files.add(file));
        Arrays.asList(directory.listFiles())
            .stream()
            .filter(file -> file.isDirectory())
            .forEach(file -> files.addAll(loadFiles(file, extensions)));
        return files;
    }

    private static boolean hasAnyExtension(File file, List<String> extensions) {
        String lowercaseName = file.getName().toLowerCase(Locale.ROOT);
        return extensions.stream().anyMatch(lowercaseName::endsWith);
    }

}
