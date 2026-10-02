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
package org.rumbledb.api;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.DataFrameWriter;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;

import lombok.extern.log4j.Log4j2;
import scala.Tuple2;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.CannotInferSchemaOnNonStructuredDataException;
import org.rumbledb.exceptions.CliException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.serialization.SequenceSerializer;
import org.rumbledb.serialization.SerializationParameterUtils;
import org.rumbledb.serialization.SerializationParameters;
import org.rumbledb.serialization.SerializedItemOutputFormat;
import org.rumbledb.serialization.Serializer;
import org.rumbledb.serialization.Serializers;
import org.rumbledb.spark.SparkSessionManager;

/**
 * Helper class to configure and materialize the output of a {@link SequenceOfItems}.
 *
 * This class is effectively immutable: all configuration methods such as {@code mode()},
 * {@code format()}, {@code option()}, etc. return a new {@link SequenceWriter} instance
 * instead of mutating the current one.
 *
 * There are two mutually exclusive internal modes:
 *
 * <ul>
 * <li>DataFrame mode: {@code dataFrameWriter != null} and {@code mode == null}.
 * In this case, the sequence can be represented as a Spark {@link Dataset} /
 * {@link DataFrameWriter} and is written using Spark's native writers (json/csv/parquet/...).</li>
 * <li>RDD mode: {@code dataFrameWriter == null} and {@code mode != null}.
 * In this case, the sequence is serialized item-by-item via {@link Serializer}
 * and saved as text files.</li>
 * </ul>
 *
 * Output formats (json/csv/parquet/...) select a file writer, independently of the W3C
 * serialization method. The special output format {@code serialize} writes the entire
 * sequence as one string using its serialization parameters. {@code serialize-each-item}
 * serializes each item independently and separates the resulting strings with newlines.
 */
@Log4j2
public class SequenceWriter {

    private static final int SINGLE_PARTITION_CAP = 1000000000;

    private final SequenceOfItems sequence;
    private final RumbleConfiguration configuration;
    private final DataFrameWriter<Row> dataFrameWriter;
    private SaveMode mode;
    private final SerializationParameters serializationParameters;
    private final String outputFormat;

    /**
     * Internal constructor used by all builder-style methods.
     *
     * Invariants:
     * <ul>
     * <li>Either DataFrame mode: {@code dataFrameWriter != null} and {@code mode == null}.</li>
     * <li>Or RDD mode: {@code dataFrameWriter == null} and {@code mode != null}.</li>
     * <li>{@code serializationParameters} is never {@code null}.</li>
     * <li>{@code serializationParameters.getMethod()} is never {@code null}; serialization uses a predefined
     * method.</li>
     * </ul>
     */
    private SequenceWriter(
            SequenceOfItems sequence,
            DataFrameWriter<Row> dataFrameWriter,
            SaveMode mode,
            SerializationParameters serializationParameters,
            RumbleConfiguration configuration,
            String outputFormat) {
        this.sequence = sequence;
        this.configuration = configuration;
        this.serializationParameters = serializationParameters;
        this.outputFormat = outputFormat;
        this.dataFrameWriter = dataFrameWriter;
        this.mode = mode;
        if (dataFrameWriter == null && mode == null) {
            throw new OurBadException("Internal error: it is not possible for both the writer and the mode to be null");
        }
        if (dataFrameWriter != null && mode != null) {
            throw new OurBadException(
                    "Internal error: it is not possible for both the writer and the mode to be non null");
        }
        if (serializationParameters == null) {
            throw new OurBadException("Internal error: serializationParameters must not be null");
        }
        if (serializationParameters.getMethod() == null) {
            throw new OurBadException("Internal error: serialization method must not be null");
        }
    }

    /** Chooses the file writer from the output format, without changing the serialization method. */
    SequenceWriter(SequenceOfItems sequence) {
        this.sequence = sequence;
        this.configuration = sequence.getRuntimeStaticContext().getConfiguration();
        this.serializationParameters =
                SerializationParameters.copy(sequence.getRuntimeStaticContext().getSerializationParameters());
        this.outputFormat = this.configuration
                .output()
                .effectiveOutputFormat(sequence.getRuntimeStaticContext().getQueryLanguage());
        this.dataFrameWriter = createDataFrameWriter(this.outputFormat);
        this.mode = this.dataFrameWriter == null ? SaveMode.ErrorIfExists : null;
    }

    private DataFrameWriter<Row> createDataFrameWriter(String format) {
        if (isSerializationFormat(format)
                || (format.equals("json")
                        && !this.sequence
                                .getRuntimeStaticContext()
                                .getStaticType()
                                .getItemType()
                                .isObjectItemType())) {
            return null;
        }
        try {
            Dataset<Row> dataFrame = this.sequence.getAsDataFrame();
            int requestedPartitions = this.configuration.output().numberOfOutputPartitions();
            if (requestedPartitions > 0) {
                dataFrame = dataFrame.repartition(requestedPartitions);
            }
            return dataFrame.write().format(format);
        } catch (CannotInferSchemaOnNonStructuredDataException e) {
            return null;
        }
    }

    public SequenceWriter mode(String saveMode) {
        if (this.dataFrameWriter != null) {
            return createNewInstance(
                    this.dataFrameWriter.mode(saveMode),
                    null,
                    SerializationParameters.copy(this.serializationParameters));
        } else {
            SaveMode mode = parseSaveMode(saveMode);
            return createNewInstance(null, mode, this.serializationParameters);
        }
    }

    public SequenceWriter mode(SaveMode saveMode) {
        if (this.dataFrameWriter != null) {
            return createNewInstance(
                    this.dataFrameWriter.mode(saveMode),
                    null,
                    SerializationParameters.copy(this.serializationParameters));
        } else {
            return createNewInstance(null, saveMode, this.serializationParameters);
        }
    }

    public SequenceWriter format(String source) {
        Objects.requireNonNull(source, "Output format cannot be null.");
        DataFrameWriter<Row> writer = this.dataFrameWriter != null && !isSerializationFormat(source)
                ? this.dataFrameWriter.format(source)
                : createDataFrameWriter(source);
        SaveMode saveMode = this.dataFrameWriter == null ? this.mode : this.dataFrameWriter.curmode();
        return new SequenceWriter(
                this.sequence,
                writer == null ? null : writer.mode(saveMode),
                writer == null ? saveMode : null,
                SerializationParameters.copy(this.serializationParameters),
                this.configuration,
                source);
    }

    public SequenceWriter option(String key, String value) {
        SerializationParameters newParams = SerializationParameters.copy(this.serializationParameters);
        SerializationParameterUtils.applyConfigOption(newParams, key, value);
        DataFrameWriter<Row> newWriter =
                (this.dataFrameWriter != null) ? this.dataFrameWriter.option(key, value) : null;
        return createNewInstance(newWriter, this.mode, newParams);
    }

    public SequenceWriter option(String key, boolean value) {
        return option(key, Boolean.toString(value));
    }

    public SequenceWriter option(String key, long value) {
        return option(key, Long.toString(value));
    }

    public SequenceWriter option(String key, double value) {
        return option(key, Double.toString(value));
    }

    public SequenceWriter options(Map<String, String> options) {
        SequenceWriter writer = this;
        for (Map.Entry<String, String> option : options.entrySet()) {
            writer = writer.option(option.getKey(), option.getValue());
        }
        return writer;
    }

    public SequenceWriter options(CaseInsensitiveStringMap options) {
        return options(options.asCaseSensitiveMap());
    }

    public SequenceWriter partitionBy(String... colNames) {
        if (this.dataFrameWriter != null) {
            return createNewInstance(this.dataFrameWriter.partitionBy(colNames), null, this.serializationParameters);
        } else {
            throw new CliException(
                    "RumbleDB currently does not support repartitioning when the output is not internally a DataFrame.");
        }
    }

    public SequenceWriter bucketBy(int numBuckets, String colName, String... colNames) {
        if (this.dataFrameWriter != null) {
            return createNewInstance(
                    this.dataFrameWriter.bucketBy(numBuckets, colName, colNames), null, this.serializationParameters);
        } else {
            throw new CliException(
                    "RumbleDB currently does not support bucketBy when the output is not internally a DataFrame.");
        }
    }

    public SequenceWriter sortBy(String colName, String... colNames) {
        if (this.dataFrameWriter != null) {
            return createNewInstance(
                    this.dataFrameWriter.sortBy(colName, colNames), null, this.serializationParameters);
        } else {
            throw new CliException(
                    "RumbleDB currently does not support sortBy when the output is not internally a DataFrame.");
        }
    }

    public void save(String path) {
        URI outputUri = null;
        outputUri = FileSystemUtil.resolveURIAgainstWorkingDirectory(path, ExceptionMetadata.EMPTY_METADATA);
        String format = this.outputFormat;
        // Structured output formats use Spark writers; serialization methods do not select these writers.
        if (this.dataFrameWriter != null) {
            for (Map.Entry<String, String> option :
                    this.serializationParameters.getSparkOptions().entrySet()) {
                log.info("Writing with option " + option.getKey() + " : " + option.getValue());
            }
            log.info("Writing to format " + format);
            DataFrameWriter<Row> writerWithOptions = applyStoredSparkOptions(this.dataFrameWriter);
            String target = FileSystemUtil.convertURIToStringForSpark(outputUri);
            if (format.equalsIgnoreCase("json")) {
                writerWithOptions.json(target);
            } else if (format.equalsIgnoreCase("csv")) {
                writerWithOptions.csv(target);
            } else if (format.equalsIgnoreCase("parquet")) {
                writerWithOptions.parquet(target);
            } else {
                writerWithOptions.format(format).save(target);
            }
            return;
        }
        if (!format.equals("json") && !isSerializationFormat(format)) {
            throw new CliException("Output format "
                    + format
                    + " requires a structured collection. "
                    + "Use annotate() to supply a schema, or select serialize or serialize-each-item.");
        }
        if (format.equals("serialize") && this.configuration.output().numberOfOutputPartitions() > 1) {
            throw new CliException(
                    "Output format serialize writes a single string and does not support multiple output partitions.");
        }
        if (FileSystemUtil.exists(outputUri, ExceptionMetadata.EMPTY_METADATA)) {
            switch (this.mode) {
                case Overwrite:
                    FileSystemUtil.delete(outputUri, ExceptionMetadata.EMPTY_METADATA);
                    break;
                case Ignore:
                    return;
                case ErrorIfExists:
                    throw new CliException("Output path "
                            + outputUri
                            + " already exists. Please change the mode or use --overwrite to overwrite.");
                case Append:
                    throw new CliException("Append currently not supported when the output is not a DataFrame.");
            }
        }
        if (format.equals("serialize")) {
            String result = SequenceSerializer.serialize(
                    this.sequence.getAsList(),
                    this.serializationParameters,
                    this.sequence.getRuntimeStaticContext().getMetadata());
            FileSystemUtil.writeBytes(
                    outputUri,
                    result.getBytes(Charset.forName(this.serializationParameters.getEncoding())),
                    ExceptionMetadata.EMPTY_METADATA);
            return;
        }
        JavaRDD<Item> rdd = this.sequence.getAsRDD();
        SerializationParameters itemParameters = SerializationParameters.copy(this.serializationParameters);
        boolean serializeEachItem = format.equals("serialize-each-item");
        if (!serializeEachItem) {
            itemParameters.setMethod("json");
        }
        // Encoders inside serializers are not Java-serializable. Construct one serializer per executor partition.
        JavaRDD<String> outputRDD = rdd.mapPartitions(items -> {
            Serializer serializer = serializeEachItem ? null : Serializers.from(itemParameters);
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(items, 0), false)
                    .map(item -> serializeEachItem
                            ? SequenceSerializer.serialize(
                                    List.of(item), itemParameters, ExceptionMetadata.EMPTY_METADATA)
                            : serializer.serialize(item))
                    .iterator();
        });
        int requestedPartitions = this.configuration.output().numberOfOutputPartitions();
        if (requestedPartitions == 1) {
            List<String> lines = outputRDD.take(SINGLE_PARTITION_CAP);
            if (serializeEachItem) {
                String content = lines.stream().map(line -> line + "\n").collect(Collectors.joining());
                FileSystemUtil.writeBytes(
                        outputUri,
                        content.getBytes(Charset.forName(itemParameters.getEncoding())),
                        ExceptionMetadata.EMPTY_METADATA);
            } else {
                FileSystemUtil.write(outputUri, lines, ExceptionMetadata.EMPTY_METADATA);
            }
            return;
        }
        if (requestedPartitions > 0) {
            outputRDD = outputRDD.repartition(requestedPartitions);
        }
        if (serializeEachItem) {
            Configuration hadoopConfiguration = new Configuration(
                    SparkSessionManager.getInstance().getJavaSparkContext().hadoopConfiguration());
            hadoopConfiguration.set(SerializedItemOutputFormat.ENCODING, itemParameters.getEncoding());
            outputRDD
                    .mapToPair(value -> new Tuple2<>(NullWritable.get(), new Text(value)))
                    .saveAsNewAPIHadoopFile(
                            FileSystemUtil.convertURIToStringForSpark(outputUri),
                            NullWritable.class,
                            Text.class,
                            SerializedItemOutputFormat.class,
                            hadoopConfiguration);
        } else {
            outputRDD.saveAsTextFile(FileSystemUtil.convertURIToStringForSpark(outputUri));
        }
    }

    private static boolean isSerializationFormat(String format) {
        return format.equals("serialize") || format.equals("serialize-each-item");
    }

    public Serializer getSerializer() {
        return Serializers.from(this.serializationParameters);
    }

    private DataFrameWriter<Row> applyStoredSparkOptions(DataFrameWriter<Row> writer) {
        if (writer == null || this.serializationParameters == null) {
            return writer;
        }
        for (Map.Entry<String, String> option :
                this.serializationParameters.getSparkOptions().entrySet()) {
            writer = writer.option(option.getKey(), option.getValue());
        }
        return writer;
    }

    /**
     * Creates a new SequenceWriter instance with the specified components.
     * This helper method encapsulates the common pattern of creating new instances,
     * reducing code duplication across builder methods.
     *
     * @param newWriter the new DataFrameWriter (null for RDD mode)
     * @param newMode the new SaveMode (null when using DataFrameWriter)
     * @param newParams the new SerializationParameters (may be the same instance if not modified)
     * @return a new SequenceWriter instance
     */
    private SequenceWriter createNewInstance(
            DataFrameWriter<Row> newWriter, SaveMode newMode, SerializationParameters newParams) {
        return new SequenceWriter(this.sequence, newWriter, newMode, newParams, this.configuration, this.outputFormat);
    }

    /**
     * Parses a string into a Spark SaveMode.
     *
     * Accepted values (case-insensitive):
     *
     * <ul>
     * <li>overwrite</li>
     * <li>append</li>
     * <li>ignore</li>
     * <li>error, errorifexists, default → ErrorIfExists</li>
     * </ul>
     *
     * @param saveMode the string representation of the save mode
     * @return the corresponding SaveMode
     * @throws IllegalArgumentException if the save mode is unknown
     */
    private static SaveMode parseSaveMode(String saveMode) {
        if (saveMode == null) {
            throw new IllegalArgumentException("Save mode must not be null.");
        }
        switch (saveMode.toLowerCase()) {
            case "overwrite":
                return SaveMode.Overwrite;
            case "append":
                return SaveMode.Append;
            case "ignore":
                return SaveMode.Ignore;
            case "error":
            case "errorifexists":
            case "default":
                return SaveMode.ErrorIfExists;
            default:
                throw new IllegalArgumentException("Unknown save mode: "
                        + saveMode
                        + ". Accepted "
                        + "save modes are 'overwrite', 'append', 'ignore', 'error', 'errorifexists', 'default'.");
        }
    }

    public void save() {
        if (this.dataFrameWriter != null) {
            applyStoredSparkOptions(this.dataFrameWriter).save();
            return;
        }
        throw new CliException(
                "Calling save() without a target path is only supported when writing through a DataFrameWriter.");
    }

    public void insertInto(String tableName) {
        if (this.dataFrameWriter != null) {
            this.dataFrameWriter.insertInto(tableName);
        }
    }

    public void saveAsTable(String tableName) {
        if (this.dataFrameWriter != null) {
            this.dataFrameWriter.saveAsTable(tableName);
        }
    }

    private SequenceWriter serializeWith(String method) {
        SequenceWriter writer = format("serialize");
        SerializationParameters params = SerializationParameters.copy(writer.serializationParameters);
        params.setMethod(method);
        return writer.createNewInstance(null, writer.mode, params);
    }

    public void tyson(String path) {
        serializeWith("tyson").save(path);
    }

    public void yaml(String path) {
        serializeWith("yaml").save(path);
    }

    public void json(String path) {
        format("json").save(path);
    }

    public void parquet(String path) {
        format("parquet").save(path);
    }

    public void text(String path) {
        serializeWith("text").save(path);
    }

    public void orc(String path) {
        format("orc").save(path);
    }

    public void csv(String path) {
        format("csv").save(path);
    }
}
