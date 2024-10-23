package org.rumbledb.runtime.functions.delta_lake;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.cli.JsoniqQueryExecutor;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.BooleanItem;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import org.apache.spark.sql.Row;
import sparksoniq.spark.SparkSessionManager;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.apache.spark.sql.functions.lit;
import static org.apache.spark.sql.functions.monotonically_increasing_id;

public class CreateDeltaLakeTableFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    public CreateDeltaLakeTableFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        RuntimeIterator urlIterator = this.children.get(0);
        urlIterator.open(context);
        String url = urlIterator.next().getStringValue();
        urlIterator.close();
        URI uri = FileSystemUtil.resolveURI(this.staticURI, url, getMetadata());
        if (FileSystemUtil.exists(uri, context.getRumbleRuntimeConfiguration(), getMetadata())) {
            throw new CannotRetrieveResourceException("File " + uri + " already exists. Cannot create new delta lake table at this location.", getMetadata());
        }
        try {
            File directory = new File(uri.getPath());
            if (!directory.exists()) {
                boolean mkdirs = directory.mkdirs();
                if (!mkdirs) {
                    throw new RuntimeException("Failed to create directory " + directory);
                }
            }
            Dataset<Row> dataFrame = SparkSessionManager.getInstance()
                    .getOrCreateSession()
                    .emptyDataFrame();
            dataFrame = dataFrame.withColumn(SparkSessionManager.mutabilityLevelColumnName, lit(0));
            dataFrame = dataFrame.withColumn(SparkSessionManager.rowIdColumnName, monotonically_increasing_id());
            dataFrame = dataFrame.withColumn(SparkSessionManager.pathInColumnName, lit(""));
            dataFrame = dataFrame.withColumn(SparkSessionManager.tableLocationColumnName, lit(uri.toString()));

            StructType schema = new StructType()
                    .add(SparkSessionManager.mutabilityLevelColumnName, DataTypes.IntegerType, false)
                    .add(SparkSessionManager.rowIdColumnName, DataTypes.IntegerType, false)
                    .add(SparkSessionManager.pathInColumnName, DataTypes.StringType, false)
                    .add(SparkSessionManager.tableLocationColumnName, DataTypes.StringType, false);

            Row newRow = RowFactory.create(
                    0,
                    0,
                    "",
                    uri.toString()
            );

            Dataset<Row> newRowDataFrame = SparkSessionManager.getInstance().getOrCreateSession().createDataFrame(List.of(newRow), schema);

            Dataset<Row> combinedDataFrame = dataFrame.union(newRowDataFrame);

            combinedDataFrame.write().format("delta").mode("error").save(uri.toString());
            return new BooleanItem(true);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new BooleanItem(false);
        }
    }
}
