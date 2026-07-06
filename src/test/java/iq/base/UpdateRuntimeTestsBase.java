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

import org.apache.spark.SparkConf;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import utils.annotations.AnnotationParseException;
import utils.annotations.AnnotationProcessor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class UpdateRuntimeTestsBase extends SparkAnnotationsTestsBase {

    @Override
    public RumbleRuntimeConfiguration getConfiguration() {
        return new RumbleRuntimeConfiguration(
                new String[] {
                    "--variable:externalUnparsedString",
                    "unparsed string",
                    "--escape-backticks",
                    "yes",
                    "--dates-with-timezone",
                    "yes",
                    "--print-iterator-tree",
                    "yes",
                    "--show-error-info",
                    "yes",
                    "--apply-updates",
                    "yes",
                    "--materialization-cap",
                    "900000",
                    "--result-size",
                    "900000"
                }
        );
    }

    @Override
    protected final List<File> testFiles() throws IOException, AnnotationParseException {
        File selectedDirectory = selectedDirectory();
        Map<Integer, Map<Integer, File>> filesByDimension = new TreeMap<>();
        for (File file : TestFileDiscovery.jsoniqFiles(selectedDirectory)) {
            AnnotationProcessor.UpdateDimensions dimensions;
            try (FileReader reader = new FileReader(file)) {
                dimensions = AnnotationProcessor.readUpdateDimensions(reader);
            }
            filesByDimension
                .computeIfAbsent(dimensions.dimension1(), ignored -> new TreeMap<>())
                .put(dimensions.dimension2(), file);
        }

        List<File> result = new ArrayList<>();
        filesByDimension.values().forEach(files -> result.addAll(files.values()));
        return result;
    }

    private File selectedDirectory() throws IOException {
        String subDirectory = System.getProperty("dir");
        File selected = subDirectory == null || subDirectory.isBlank()
            ? testDirectory()
            : new File(testDirectory(), subDirectory.trim());
        if (!selected.isDirectory()) {
            throw new IOException("Update test directory not found: " + selected.getAbsolutePath());
        }
        return selected;
    }

    @Override
    protected final void configureSpark(SparkConf sparkConfiguration) {
        sparkConfiguration.set("spark.sql.adaptive.enabled", "false");
        configureUpdateStore(sparkConfiguration);
    }

    protected abstract void configureUpdateStore(SparkConf sparkConfiguration);
}
