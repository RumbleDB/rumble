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
package org.rumbledb.cli;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.rumbledb.api.Item;
import org.rumbledb.api.Rumble;
import org.rumbledb.api.SequenceOfItems;
import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.CliException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.optimizations.Profiler;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.serialization.SequenceSerializer;
import org.rumbledb.serialization.SerializationParameters;
import org.rumbledb.serialization.Serializer;
import org.rumbledb.serialization.Serializers;

public class JsoniqQueryExecutor {
    private final RumbleConfiguration configuration;
    private final ExternalBindings externalBindings;

    public JsoniqQueryExecutor(RumbleConfiguration configuration) {
        this(configuration, ExternalBindings.empty());
    }

    public JsoniqQueryExecutor(RumbleConfiguration configuration, ExternalBindings externalBindings) {
        this.configuration = configuration;
        this.externalBindings = externalBindings.snapshot();
    }

    private void checkOutputFile(URI outputUri) throws IOException {
        if (FileSystemUtil.exists(outputUri, ExceptionMetadata.EMPTY_METADATA)) {
            if (!this.configuration.output().allowOverwrite()) {
                throw new CliException(
                        "Output path " + outputUri + " already exists. Please use --overwrite to overwrite.");
            } else {
                FileSystemUtil.delete(outputUri, ExceptionMetadata.EMPTY_METADATA);
            }
        }
    }

    public List<Item> runQuery() throws IOException {
        String queryFile = this.configuration.input().queryPath();
        URI queryUri = null;
        if (queryFile != null) {
            queryUri = FileSystemUtil.resolveURIAgainstWorkingDirectory(queryFile, ExceptionMetadata.EMPTY_METADATA);
        }
        String outputPath = this.configuration.output().outputPath();
        URI outputUri = null;
        if (outputPath != null) {
            outputUri = FileSystemUtil.resolveURIAgainstWorkingDirectory(outputPath, ExceptionMetadata.EMPTY_METADATA);
            checkOutputFile(outputUri);
        }

        String logPath = this.configuration.output().logPath();
        URI logUri = null;
        if (logPath != null) {
            logUri = FileSystemUtil.resolveURIAgainstWorkingDirectory(logPath, ExceptionMetadata.EMPTY_METADATA);
            if (FileSystemUtil.exists(logUri, ExceptionMetadata.EMPTY_METADATA)) {
                FileSystemUtil.delete(logUri, ExceptionMetadata.EMPTY_METADATA);
            }
        }

        List<Item> outputList = null;

        long startTime = System.currentTimeMillis();
        Rumble rumble = new Rumble(this.configuration);
        SequenceOfItems sequence = null;
        if (this.configuration.input().query() != null) {
            if (this.configuration.input().queryPath() != null) {
                throw new CliException(
                        "It is not possible to specify both a --query and a --query-path. It is either or.");
            }
            sequence = rumble.runQuery(this.configuration.input().query(), this.externalBindings);
        } else {
            sequence = rumble.runQuery(queryUri, this.externalBindings);
        }

        if (outputPath != null) {
            sequence.write().save(outputPath);
        } else {
            if ("serialize".equals(outputFormat(sequence))) {
                ConsoleOutput.out(sequence.serialize());
            } else {
                outputList = new ArrayList<>();
                long materializationCount = sequence.populateList(
                        outputList, this.configuration.runtime().resultsSizeCap());
                ConsoleOutput.out(displayItems(sequence, outputList));
                if (materializationCount != -1) {
                    issueMaterializationWarning(
                            materializationCount, this.configuration.runtime().resultsSizeCap());
                    ConsoleOutput.warn("To write the complete output, use --output-path to select a destination.");
                }
            }
        }

        if (this.configuration.runtime().shouldApplyUpdates() && sequence.availableAsPUL()) {
            sequence.applyPUL();
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        if (logPath != null) {
            String time = "[ExecTime] " + totalTime;
            time += "\n[ProfilerCount] " + Profiler.get();
            FileSystemUtil.append(logUri, Collections.singletonList(time), ExceptionMetadata.EMPTY_METADATA);
        }
        return outputList;
    }

    public static void issueMaterializationWarning(long materializationCount, long resultSizeCap) {
        if (materializationCount == Long.MAX_VALUE) {
            ConsoleOutput.warn("Warning! The output sequence contains "
                    + "too many items and its materialization was capped at "
                    + resultSizeCap
                    + " items. This value can be increased with --result-size at startup");
        } else {
            ConsoleOutput.warn("Warning! The output sequence contains "
                    + materializationCount
                    + " items but its materialization was capped at "
                    + resultSizeCap
                    + " items. This value can be increased with --result-size at startup");
        }
    }

    private String displayItems(SequenceOfItems sequence, List<Item> items) {
        String format = outputFormat(sequence);
        if (format != null && !format.equals("json") && !format.equals("serialize-each-item")) {
            throw new CliException("Output format "
                    + format
                    + " requires --output-path; use serialize or serialize-each-item for serialized output on screen.");
        }
        SerializationParameters params =
                SerializationParameters.copy(sequence.getRuntimeStaticContext().getSerializationParameters());
        if ("json".equals(format)) {
            params.setMethod("json");
        }
        if ("serialize-each-item".equals(format)) {
            return items.stream()
                    .map(item -> SequenceSerializer.serialize(
                            List.of(item),
                            params,
                            sequence.getRuntimeStaticContext().getMetadata()))
                    .collect(Collectors.joining("\n"));
        }
        Serializer serializer = Serializers.from(params);
        return items.stream().map(serializer::serialize).collect(Collectors.joining("\n"));
    }

    public record InteractiveResult(String output, long count) {}

    private String outputFormat(SequenceOfItems sequence) {
        return this.configuration
                .output()
                .effectiveOutputFormat(sequence.getRuntimeStaticContext().getQueryLanguage());
    }

    public InteractiveResult runInteractive(String query) {
        Rumble rumble = new Rumble(this.configuration);
        SequenceOfItems sequence = rumble.runQuery(query, this.externalBindings);
        List<Item> results = new ArrayList<>();
        boolean serialize = "serialize".equals(outputFormat(sequence));
        long count = serialize
                ? -1
                : sequence.populateList(results, this.configuration.runtime().resultsSizeCap());
        String output = serialize ? sequence.serialize() : displayItems(sequence, results);
        if (this.configuration.runtime().shouldApplyUpdates() && sequence.availableAsPUL()) {
            sequence.applyPUL();
        }
        return new InteractiveResult(output, count);
    }
}
