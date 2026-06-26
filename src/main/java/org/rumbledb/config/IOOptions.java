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
 */

package org.rumbledb.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class IOOptions {
    public static final String DEFAULT_INPUT_FORMAT = "json";
    public static final int DEFAULT_NUMBER_OF_OUTPUT_PARTITIONS = -1;

    /**
     * Allowed URI prefixes for read/write (with I/O functions to read data)
     */
    @Default
    private List<String> allowedPrefixes = new ArrayList<>();

    /**
     * Input format for reading from standard input.
     */
    @Default
    private String inputFormat = DEFAULT_INPUT_FORMAT;

    /**
     * Number of output partitions to write to the output path.
     */
    @Default
    private int numberOfOutputPartitions = DEFAULT_NUMBER_OF_OUTPUT_PARTITIONS;

    /**
     * Path from which the JSONiq or XQuery query is to be read.
     */
    private String queryPath;

    /**
     * Path to which the output path should be written.
     */
    private String outputPath;

    /**
     * Log path
     */
    private String logPath;

    /**
     * Query that was passed from the command line
     * (This should be removed later when we have separate classes for CLI and API)
     */
    private String query;

    /**
     * Current shell filter for post-processing output (e.g. JSON beautifier)
     */
    private String shell;
}
