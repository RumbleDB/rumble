package org.rumbledb.api;

import java.net.URI;

import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.DataFrameWriter;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.CannotInferSchemaOnNonStructuredDataException;
import org.rumbledb.exceptions.CliException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.serialization.SerializationParameters;
import org.rumbledb.serialization.Serializer;
import org.rumbledb.serialization.Serializers;

/**
 * Helper class to configure and materialize the output of a {@link SequenceOfItems}.
 *
 * This class is effectively immutable: all configuration methods such as {@code mode()},
 * {@code format()}, {@code option()}, etc. return a new {@link SequenceWriter} instance
 * instead of mutating the current one.
 *
 * There are two mutually exclusive internal modes:
 * - DataFrame mode: {@code dataFrameWriter != null} and {@code mode == null}.
 * In this case, the sequence can be represented as a Spark {@link Dataset} /
 * {@link DataFrameWriter} and is written using Spark's native writers (json/csv/parquet/...).
 * - RDD mode: {@code dataFrameWriter == null} and {@code mode != null}.
 * In this case, the sequence is serialized item-by-item via {@link Serializer}
 * and saved as text files.
 *
 * The serialization method (json, tyson, xml-json-hybrid, yaml, delta, ...) is always taken from
 * {@link SerializationParameters#getMethod()}, which is the single source of truth for the output
 * format.
 */
public class SequenceWriter {

    private static final int SINGLE_PARTITION_CAP = 1000000000;

    private final SequenceOfItems sequence;
    private final RumbleRuntimeConfiguration configuration;
    private final DataFrameWriter<Row> dataFrameWriter;
    private SaveMode mode;
    private final SerializationParameters serializationParameters;

    /**
     * Internal constructor used by all builder-style methods.
     *
     * Invariants:
     * - Either DataFrame mode: {@code dataFrameWriter != null} and {@code mode == null}.
     * - Or RDD mode: {@code dataFrameWriter == null} and {@code mode != null}.
     * - {@code serializationParameters} is never {@code null}.
     * - {@code serializationParameters.getMethod()} is never {@code null}; serialization uses a predefined method.
     */
    private SequenceWriter(
            SequenceOfItems sequence,
            DataFrameWriter<Row> dataFrameWriter,
            SaveMode mode,
            SerializationParameters serializationParameters,
            RumbleRuntimeConfiguration configuration
    ) {
        this.sequence = sequence;
        this.configuration = configuration;
        this.serializationParameters = serializationParameters;
        this.dataFrameWriter = dataFrameWriter;
        this.mode = mode;
        if (dataFrameWriter == null && mode == null) {
            throw new OurBadException("Internal error: it is not possible for both the writer and the mode to be null");
        }
        if (dataFrameWriter != null && mode != null) {
            throw new OurBadException(
                    "Internal error: it is not possible for both the writer and the mode to be non null"
            );
        }
        if (serializationParameters == null) {
            throw new OurBadException("Internal error: serializationParameters must not be null");
        }
        if (serializationParameters.getMethod() == null) {
            throw new OurBadException("Internal error: serialization method must not be null");
        }
    }

    /**
     * Public entry-point constructor used by {@link SequenceOfItems#write()}.
     * TODO: update comment here
     * It determines the initial mode:
     * - If the method is {@code xml-json-hybrid} or {@code tyson}, or if obtaining a DataFrame
     * fails, the writer is created in RDD mode.
     * - Otherwise, the writer is created in DataFrame mode based on the DataFrame returned by
     * {@link SequenceOfItems#getAsDataFrame()}.
     */
    SequenceWriter(SequenceOfItems sequence) {
        this.sequence = sequence;
        this.configuration = sequence.getRuntimeStaticContext().getConfiguration();
        SerializationParameters params = sequence
            .getRuntimeStaticContext()
            .getSerializationParameters();
        this.serializationParameters = SerializationParameters.copy(params);
        DataFrameWriter<Row> w = null;
        String method = this.serializationParameters.getMethod();
        if (method != null && (method.equals("xml-json-hybrid") || method.equals("tyson"))) {
            this.mode = SaveMode.ErrorIfExists; // Default save mode
        } else {
            try {
                Dataset<Row> dataFrame = sequence.getAsDataFrame();
                int requestedPartitions = configuration.getNumberOfOutputPartitions();
                if (requestedPartitions > 0) {
                    dataFrame = dataFrame.repartition(requestedPartitions);
                }
                w = dataFrame.write();
                this.mode = null;
            } catch (CannotInferSchemaOnNonStructuredDataException e) {
                this.serializationParameters.setMethod("xml-json-hybrid"); // Default method
                this.mode = SaveMode.ErrorIfExists; // Default save mode
            }
        }
        this.dataFrameWriter = w;
    }

    public SequenceWriter mode(String saveMode) {
        if (this.dataFrameWriter != null) {
            return createNewInstance(
                this.dataFrameWriter.mode(saveMode),
                null,
                SerializationParameters.copy(this.serializationParameters)
            );
        } else {
            SaveMode mode = parseSaveMode(saveMode);
            return createNewInstance(null, mode, this.serializationParameters);
        }
    }

    public SequenceWriter mode(org.apache.spark.sql.SaveMode saveMode) {
        if (this.dataFrameWriter != null) {
            return createNewInstance(
                this.dataFrameWriter.mode(saveMode),
                null,
                SerializationParameters.copy(this.serializationParameters)
            );
        } else {
            return createNewInstance(null, saveMode, this.serializationParameters);
        }
    }

    public SequenceWriter format(String source) {
        SerializationParameters params = SerializationParameters.copy(this.serializationParameters);
        params.setMethod(source);
        if (this.dataFrameWriter != null && !source.equals("xml-json-hybrid") && !source.equals("tyson")) {
            return createNewInstance(
                this.dataFrameWriter.format(source),
                null,
                params
            );
        } else {
            SaveMode newMode = (this.dataFrameWriter == null) ? this.mode : this.dataFrameWriter.curmode();
            return createNewInstance(null, newMode, params);
        }
    }

    public SequenceWriter option(String key, String value) {
        SerializationParameters newParams = SerializationParameters.copy(this.serializationParameters);
        newParams.getSparkOptions().put(key, value);
        DataFrameWriter<Row> newWriter = (this.dataFrameWriter != null)
            ? this.dataFrameWriter.option(key, value)
            : null;
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

    public SequenceWriter options(java.util.Map<String, String> options) {
        SerializationParameters newParams = SerializationParameters.copy(this.serializationParameters);
        newParams.getSparkOptions().putAll(options);
        DataFrameWriter<Row> newWriter = (this.dataFrameWriter != null)
            ? this.dataFrameWriter.options(options)
            : null;
        return createNewInstance(newWriter, this.mode, newParams);
    }

    public SequenceWriter options(org.apache.spark.sql.util.CaseInsensitiveStringMap options) {
        SerializationParameters newParams = SerializationParameters.copy(this.serializationParameters);
        newParams.getSparkOptions().putAll(options);
        DataFrameWriter<Row> newWriter = (this.dataFrameWriter != null)
            ? this.dataFrameWriter.options(options)
            : null;
        return createNewInstance(newWriter, this.mode, newParams);
    }

    public SequenceWriter partitionBy(String... colNames) {
        if (this.dataFrameWriter != null) {
            return createNewInstance(
                this.dataFrameWriter.partitionBy(colNames),
                null,
                this.serializationParameters
            );
        } else {
            throw new CliException(
                    "RumbleDB currently does not support repartitioning when the output is not internally a DataFrame."
            );
        }
    }

    public SequenceWriter bucketBy(int numBuckets, String colName, String... colNames) {
        if (this.dataFrameWriter != null) {
            return createNewInstance(
                this.dataFrameWriter.bucketBy(numBuckets, colName, colNames),
                null,
                this.serializationParameters
            );
        } else {
            throw new CliException(
                    "RumbleDB currently does not support bucketBy when the output is not internally a DataFrame."
            );
        }
    }

    public SequenceWriter sortBy(String colName, String... colNames) {
        if (this.dataFrameWriter != null) {
            return createNewInstance(
                this.dataFrameWriter.sortBy(colName, colNames),
                null,
                this.serializationParameters
            );
        } else {
            throw new CliException(
                    "RumbleDB currently does not support sortBy when the output is not internally a DataFrame."
            );
        }
    }

    public void save(String path) {
        URI outputUri = null;
        outputUri = FileSystemUtil.resolveURIAgainstWorkingDirectory(
            path,
            this.configuration,
            ExceptionMetadata.EMPTY_METADATA
        );
        String method = this.serializationParameters.getMethod();
        // DataFrame mode: delegate to Spark's DataFrameWriter, using the serialization method
        // as the Spark output format (json/csv/parquet/other).
        if (this.dataFrameWriter != null) {
            Logger logger = LogManager.getLogger(SequenceWriter.class);
            for (Map.Entry<String, String> option : this.serializationParameters.getSparkOptions().entrySet()) {
                logger.info("Writing with option " + option.getKey() + " : " + option.getValue());
            }
            logger.info("Writing to format " + method);
            DataFrameWriter<Row> writerWithOptions = applyStoredSparkOptions(this.dataFrameWriter);
            String target = FileSystemUtil.convertURIToStringForSpark(outputUri);
            if (method.equalsIgnoreCase("json")) {
                writerWithOptions.json(target);
            } else if (method.equalsIgnoreCase("csv")) {
                writerWithOptions.csv(target);
            } else if (method.equalsIgnoreCase("parquet")) {
                writerWithOptions.parquet(target);
            } else {
                writerWithOptions.format(method).save(target);
            }
            return;
        }
        // RDD mode: serialize each item via Serializer and save as text.
        if (
            !(method.equals("json")
                || method.equals("tyson")
                || method.equals("xml-json-hybrid")
                || method.equals("yaml")
                || method.equals("delta"))
        ) {
            throw new CliException(
                    "Rumble cannot output another format than json or tyson or xml-json-hybrid or yaml if the query does not output a structured collection. You can create a structured collection from a sequence of objects by calling the function annotate(<your query here> , <a schema here>)."
            );
        }
        if (FileSystemUtil.exists(outputUri, this.configuration, ExceptionMetadata.EMPTY_METADATA)) {
            switch (this.mode) {
                case Overwrite:
                    FileSystemUtil.delete(outputUri, this.configuration, ExceptionMetadata.EMPTY_METADATA);
                    break;
                case Ignore:
                    return;
                case ErrorIfExists:
                    throw new CliException(
                            "Output path "
                                + outputUri
                                + " already exists. Please change the mode or use --overwrite yes to overwrite."
                    );
                case Append:
                    throw new CliException(
                            "Append currently not supported when the output is not a DataFrame."
                    );
            }
        }
        JavaRDD<Item> rdd = this.sequence.getAsRDD();
        Serializer serializer = getSerializer();
        JavaRDD<String> outputRDD = rdd.map(o -> serializer.serialize(o));
        int requestedPartitions = this.configuration.getNumberOfOutputPartitions();
        if (requestedPartitions == 1) {
            List<String> lines = outputRDD.take(SINGLE_PARTITION_CAP);
            FileSystemUtil.write(outputUri, lines, this.configuration, ExceptionMetadata.EMPTY_METADATA);
            return;
        }
        if (requestedPartitions > 0) {
            outputRDD = outputRDD.repartition(requestedPartitions);
        }
        outputRDD.saveAsTextFile(FileSystemUtil.convertURIToStringForSpark(outputUri));
    }

    public Serializer getSerializer() {
        return Serializers.from(this.serializationParameters);
    }

    private DataFrameWriter<Row> applyStoredSparkOptions(DataFrameWriter<Row> writer) {
        if (writer == null || this.serializationParameters == null) {
            return writer;
        }
        for (Map.Entry<String, String> option : this.serializationParameters.getSparkOptions().entrySet()) {
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
            DataFrameWriter<Row> newWriter,
            SaveMode newMode,
            SerializationParameters newParams
    ) {
        return new SequenceWriter(
                this.sequence,
                newWriter,
                newMode,
                newParams,
                this.configuration
        );
    }

    /**
     * Parses a string into a Spark SaveMode.
     *
     * Accepted values (case-insensitive):
     * - overwrite
     * - append
     * - ignore
     * - error, errorifexists, default → ErrorIfExists
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
                throw new IllegalArgumentException(
                        "Unknown save mode: "
                            + saveMode
                            + ". Accepted "
                            + "save modes are 'overwrite', 'append', 'ignore', 'error', 'errorifexists', 'default'."
                );
        }
    }

    public void save() {
        if (this.dataFrameWriter != null) {
            applyStoredSparkOptions(this.dataFrameWriter).save();
            return;
        }
        throw new CliException(
                "Calling save() without a target path is only supported when writing through a DataFrameWriter."
        );
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

    public void tyson(String path) {
        format("tyson").save(path);
    }

    public void yaml(String path) {
        format("yaml").save(path);
    }

    public void json(String path) {
        format("json").save(path);
    }

    public void parquet(String path) {
        format("parquet").save(path);
    }

    public void text(String path) {
        format("xml-json-hybrid").save(path);
    }

    public void orc(String path) {
        format("orc").save(path);
    }

    public void csv(String path) {
        format("csv").save(path);
    }
}
