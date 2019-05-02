/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
package sparksoniq.utils;

import org.apache.hadoop.fs.Path;
import sparksoniq.JsoniqQueryExecutor;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static sparksoniq.JsoniqQueryExecutor.TEMP_QUERY_FILE_NAME;

public class FileUtils {

    public static java.nio.file.Path writeToFileInCurrentDirectory(String content) throws IOException {
        List<String> lines = Arrays.asList(content);
        String path = JsoniqQueryExecutor.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (path.endsWith(".jar"))
            path = path.substring(0, path.lastIndexOf(Path.SEPARATOR));
        String decodedPath = URLDecoder.decode(path, "UTF-8");
        java.nio.file.Path file = FileUtils.getUniqueFileName(decodedPath + Path.SEPARATOR + TEMP_QUERY_FILE_NAME);
        Files.write(file, lines, Charset.forName("UTF-8"));
        return file;
    }

    public static java.nio.file.Path getUniqueFileName(String path) {
        Random random = new Random();
        while (Files.exists(Paths.get(path))) {
            path = path + random.nextInt(100);
        }
        return Paths.get(path);
    }
}
