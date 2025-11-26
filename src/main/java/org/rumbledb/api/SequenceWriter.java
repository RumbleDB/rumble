package org.rumbledb.api;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.DataFrameWriter;
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

public class SequenceWriter {

    private final SequenceOfItems sequence;
    private final RumbleRuntimeConfiguration configuration;
    private final DataFrameWriter<Row> dataFrameWriter;
    private String source;
    private SaveMode mode;
    private final SerializationParameters serializationParameters;

    public SequenceWriter(
            SequenceOfItems sequence,
            DataFrameWriter<Row> dataFrameWriter,
            String source,
            SaveMode mode,
            SerializationParameters serializationParameters,
            RumbleRuntimeConfiguration configuration
    ) {
        this.sequence = sequence;
        this.configuration = configuration;
        this.serializationParameters = serializationParameters;
        this.dataFrameWriter = dataFrameWriter;
        this.source = source;
        this.mode = mode;
        if (dataFrameWriter == null && source == null) {
            throw new OurBadException(
                    "Internal error: it is not possible for both the writer and the source to be null"
            );
        }
        if (dataFrameWriter == null && mode == null) {
            throw new OurBadException("Internal error: it is not possible for both the writer and the mode to be null");
        }
        if (dataFrameWriter == null && serializationParameters == null) {
            throw new OurBadException(
                    "Internal error: it is not possible for both the writer and the serializationParameters to be null"
            );
        }
        if (dataFrameWriter != null && source != null) {
            throw new OurBadException(
                    "Internal error: it is not possible for both the writer and the source to be non null"
            );
        }
        if (dataFrameWriter != null && mode != null) {
            throw new OurBadException(
                    "Internal error: it is not possible for both the writer and the mode to be non null"
            );
        }
        if (dataFrameWriter != null && serializationParameters != null) {
            throw new OurBadException(
                    "Internal error: it is not possible for both the writer and the serializationParameters to be non null"
            );
        }
    }

    public SequenceWriter(SequenceOfItems sequence, RumbleRuntimeConfiguration configuration) {
        this.sequence = sequence;
        this.configuration = configuration;
        this.serializationParameters = configuration.getSerializationParameters();
        DataFrameWriter<Row> w = null;
        String method = this.serializationParameters.getMethod();
        if (method != null && (method.equals("xml-json-hybrid") || method.equals("tyson"))) {
            this.source = method; // Default source
            this.mode = SaveMode.ErrorIfExists; // Default save mode
        } else {
            try {
                w = sequence.getAsDataFrame().write();
            } catch (CannotInferSchemaOnNonStructuredDataException e) {
                this.source = "xml-json-hybrid"; // Default source
                this.mode = SaveMode.ErrorIfExists; // Default save mode
            }
        }
        this.dataFrameWriter = w;
    }

    public SequenceWriter mode(String saveMode) {
        if (this.dataFrameWriter != null) {
            return new SequenceWriter(
                    this.sequence,
                    this.dataFrameWriter.mode(saveMode),
                    null,
                    null,
                    null,
                    this.configuration
            );
        } else {
            SaveMode mode = null;
            switch (saveMode.toLowerCase()) {
                case "overwrite":
                    mode = SaveMode.Overwrite;
                    break;
                case "append":
                    mode = SaveMode.Append;
                    break;
                case "ignore":
                    mode = SaveMode.Ignore;
                    break;
                case "error":
                case "errorifexists":
                case "default":
                    mode = SaveMode.ErrorIfExists;
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Unknown save mode: "
                                + saveMode
                                + ". Accepted "
                                +
                                "save modes are 'overwrite', 'append', 'ignore', 'error', 'errorifexists', 'default'."
                    );
            }
            return new SequenceWriter(
                    this.sequence,
                    null,
                    this.source,
                    mode,
                    this.serializationParameters,
                    this.configuration
            );
        }
    }

    public SequenceWriter mode(org.apache.spark.sql.SaveMode saveMode) {
        if (this.dataFrameWriter != null) {
            return new SequenceWriter(
                    this.sequence,
                    this.dataFrameWriter.mode(saveMode),
                    null,
                    null,
                    null,
                    this.configuration
            );
        } else {
            return new SequenceWriter(
                    this.sequence,
                    null,
                    this.source,
                    saveMode,
                    this.serializationParameters,
                    this.configuration
            );
        }
    }

    public SequenceWriter format(String source) {
        if (this.dataFrameWriter != null && !source.equals("xml-json-hybrid") && !source.equals("tyson")) {
            return new SequenceWriter(
                    this.sequence,
                    this.dataFrameWriter.format(source),
                    null,
                    null,
                    null,
                    this.configuration
            );
        } else {
            SerializationParameters params = (this.dataFrameWriter == null)
                ? copySerializationParameters()
                : SerializationParameters.defaults();
            if (params.getMethod() == null) {
                params.setMethod(source);
            }
            return new SequenceWriter(
                    this.sequence,
                    null,
                    source,
                    (this.dataFrameWriter == null) ? this.mode : this.dataFrameWriter.curmode(),
                    params,
                    this.configuration
            );
        }
    }

    public SequenceWriter option(String key, String value) {
        if (this.dataFrameWriter != null) {
            return new SequenceWriter(
                    this.sequence,
                    this.dataFrameWriter.option(key, value),
                    null,
                    null,
                    null,
                    this.configuration
            );
        } else {
            SerializationParameters newParams = copySerializationParameters();
            newParams.getSparkOptions().put(key, value);
            return new SequenceWriter(
                    this.sequence,
                    null,
                    this.source,
                    this.mode,
                    newParams,
                    this.configuration
            );
        }
    }

    public SequenceWriter option(String key, boolean value) {
        if (this.dataFrameWriter != null) {
            return new SequenceWriter(
                    this.sequence,
                    this.dataFrameWriter.option(key, value),
                    null,
                    null,
                    null,
                    this.configuration
            );
        } else {
            SerializationParameters newParams = copySerializationParameters();
            newParams.getSparkOptions().put(key, Boolean.toString(value));
            return new SequenceWriter(
                    this.sequence,
                    null,
                    this.source,
                    this.mode,
                    newParams,
                    this.configuration
            );
        }
    }

    public SequenceWriter option(String key, long value) {
        if (this.dataFrameWriter != null) {
            return new SequenceWriter(
                    this.sequence,
                    this.dataFrameWriter.option(key, value),
                    null,
                    null,
                    null,
                    this.configuration
            );
        } else {
            SerializationParameters newParams = copySerializationParameters();
            newParams.getSparkOptions().put(key, Long.toString(value));
            return new SequenceWriter(
                    this.sequence,
                    null,
                    this.source,
                    this.mode,
                    newParams,
                    this.configuration
            );
        }
    }

    public SequenceWriter option(String key, double value) {
        if (this.dataFrameWriter != null) {
            return new SequenceWriter(
                    this.sequence,
                    this.dataFrameWriter.option(key, value),
                    null,
                    null,
                    null,
                    this.configuration
            );
        } else {
            SerializationParameters newParams = copySerializationParameters();
            newParams.getSparkOptions().put(key, Double.toString(value));
            return new SequenceWriter(
                    this.sequence,
                    null,
                    this.source,
                    this.mode,
                    newParams,
                    this.configuration
            );
        }
    }

    public SequenceWriter options(java.util.Map<String, String> options) {
        if (this.dataFrameWriter != null) {
            return new SequenceWriter(
                    this.sequence,
                    this.dataFrameWriter.options(options),
                    null,
                    null,
                    null,
                    this.configuration
            );
        } else {
            SerializationParameters newParams = copySerializationParameters();
            newParams.getSparkOptions().putAll(options);
            return new SequenceWriter(
                    this.sequence,
                    null,
                    this.source,
                    this.mode,
                    newParams,
                    this.configuration
            );
        }
    }

    public SequenceWriter options(org.apache.spark.sql.util.CaseInsensitiveStringMap options) {
        if (this.dataFrameWriter != null) {
            return new SequenceWriter(
                    this.sequence,
                    this.dataFrameWriter.options(options),
                    null,
                    null,
                    null,
                    this.configuration
            );
        } else {
            SerializationParameters newParams = copySerializationParameters();
            newParams.getSparkOptions().putAll(options);
            return new SequenceWriter(
                    this.sequence,
                    null,
                    this.source,
                    this.mode,
                    newParams,
                    this.configuration
            );
        }
    }

    public SequenceWriter partitionBy(String... colNames) {
        if (this.dataFrameWriter != null) {
            return new SequenceWriter(
                    this.sequence,
                    this.dataFrameWriter.partitionBy(colNames),
                    null,
                    null,
                    null,
                    this.configuration
            );
        } else {
            throw new CliException(
                    "RumbleDB currently does not support repartitioning when the output is not internally a DataFrame."
            );
        }
    }

    public SequenceWriter bucketBy(int numBuckets, String colName, String... colNames) {
        if (this.dataFrameWriter != null) {
            return new SequenceWriter(
                    this.sequence,
                    this.dataFrameWriter.bucketBy(numBuckets, colName, colNames),
                    null,
                    null,
                    null,
                    this.configuration
            );
        } else {
            throw new CliException(
                    "RumbleDB currently does not support bucketBy when the output is not internally a DataFrame."
            );
        }
    }

    public SequenceWriter sortBy(String colName, String... colNames) {
        if (this.dataFrameWriter != null) {
            return new SequenceWriter(
                    this.sequence,
                    this.dataFrameWriter.sortBy(colName, colNames),
                    null,
                    null,
                    null,
                    this.configuration
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
        if (this.dataFrameWriter != null) {
            this.dataFrameWriter.save(FileSystemUtil.convertURIToStringForSpark(outputUri));
            return;
        }
        if (
            !(this.source.equals("json")
                || this.source.equals("tyson")
                || this.source.equals("xml-json-hybrid")
                || this.source.equals("yaml")
                || this.source.equals("delta"))
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
        outputRDD.saveAsTextFile(FileSystemUtil.convertURIToStringForSpark(outputUri));
    }

    public Serializer getSerializer() {
        SerializationParameters params = copySerializationParameters();
        // Ensure method is set from source
        if (params.getMethod() == null) {
            params.setMethod(this.source);
        }
        // Ensure defaults for serialization if not set
        if (params.getItemSeparator() == null) {
            params.setItemSeparator("\n");
        }
        if (params.getEncoding() == null) {
            params.setEncoding("UTF-8");
        }
        return Serializers.from(params);
    }

    /**
     * Creates a deep copy of the serialization parameters.
     * This is needed when creating new SequenceWriter instances with modified options.
     */
    private SerializationParameters copySerializationParameters() {
        if (this.serializationParameters == null) {
            return SerializationParameters.defaults();
        }
        SerializationParameters copy = SerializationParameters.defaults();
        copy.setMethod(this.serializationParameters.getMethod());
        copy.setEncoding(this.serializationParameters.getEncoding());
        copy.setOmitXmlDeclaration(this.serializationParameters.getOmitXmlDeclaration());
        copy.setStandalone(this.serializationParameters.getStandalone());
        copy.setDoctypeSystem(this.serializationParameters.getDoctypeSystem());
        copy.setDoctypePublic(this.serializationParameters.getDoctypePublic());
        copy.setMediaType(this.serializationParameters.getMediaType());
        copy.setNormalizationForm(this.serializationParameters.getNormalizationForm());
        copy.setUndeclarePrefixes(this.serializationParameters.getUndeclarePrefixes());
        copy.setCharacterMaps(new HashMap<>(this.serializationParameters.getCharacterMaps()));
        copy.setCdataSectionElements(new HashSet<>(this.serializationParameters.getCdataSectionElements()));
        copy.setIncludeContentType(this.serializationParameters.getIncludeContentType());
        copy.setEscapeUriAttributes(this.serializationParameters.getEscapeUriAttributes());
        copy.setHtmlVersion(this.serializationParameters.getHtmlVersion());
        copy.setByteOrderMark(this.serializationParameters.getByteOrderMark());
        copy.setIndent(this.serializationParameters.getIndent());
        copy.setIndentSpaces(this.serializationParameters.getIndentSpaces());
        copy.setSuppressIndentation(new HashSet<>(this.serializationParameters.getSuppressIndentation()));
        copy.setItemSeparator(this.serializationParameters.getItemSeparator());
        copy.setAllowDuplicateNames(this.serializationParameters.getAllowDuplicateNames());
        copy.setJsonNodeOutputMethod(this.serializationParameters.getJsonNodeOutputMethod());
        copy.setExtensionParameters(new HashMap<>(this.serializationParameters.getExtensionParameters()));
        copy.setSparkOptions(new HashMap<>(this.serializationParameters.getSparkOptions()));
        return copy;
    }

    public void save() {
        if (this.dataFrameWriter != null) {
            this.dataFrameWriter.save();
            return;
        }
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
