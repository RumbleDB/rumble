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

public class FileManager {

    public static final String TEST_FILE_EXTENSION = ".jq";

    public static List<File> loadJiqFiles(File directory) {
        List<File> files = new ArrayList<>();
        Arrays.asList(directory.listFiles())
            .stream()
            .filter(file -> file.getName().endsWith(FileManager.TEST_FILE_EXTENSION))
            .forEach(file -> files.add(file));
        Arrays.asList(directory.listFiles())
            .stream()
            .filter(file -> file.isDirectory())
            .forEach(file -> files.addAll(FileManager.loadJiqFiles(file)));
        return files;
    }

}
