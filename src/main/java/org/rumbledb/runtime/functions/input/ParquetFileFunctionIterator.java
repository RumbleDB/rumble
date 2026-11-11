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
package org.rumbledb.runtime.functions.input;

import java.io.Serial;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import org.rumbledb.api.Item;
import org.rumbledb.compiler.typing.ReturnTypeContext;
import org.rumbledb.compiler.typing.ReturnTypeRules;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.spark.SparkSessionManager;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceType;

public class ParquetFileFunctionIterator extends ItemRuntimePlan implements DataFrameRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    public ParquetFileFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext context) {

        String url = this.getChild(0).materializeFirstOrNull(context).getStringValue();

        URI uri = FileSystemUtil.resolveFileSystemURI(this.staticContext.getStaticURI(), url, getMetadata());
        if (!FileSystemUtil.exists(uri, getMetadata())) {
            throw new CannotRetrieveResourceException("File " + uri + " not found.", getMetadata());
        }
        int partitions = -1;
        if (this.getChildren().size() > 1) {
            partitions = this.getChild(1).materializeFirstOrNull(context).getIntValue();
        }
        try {
            Dataset<Row> dataFrame = SparkSessionManager.getInstance()
                    .getOrCreateSession()
                    .read()
                    .parquet(FileSystemUtil.convertURIToStringForSpark(uri));
            if (partitions != -1) {
                dataFrame = dataFrame.repartition(partitions);
            }
            return new HomogeneousItemDataFrame(dataFrame);
        } catch (Exception e) {
            if (e instanceof AnalysisException) {
                throw new CannotRetrieveResourceException("File " + uri + " not found.", getMetadata());
            }
            throw e;
        }
    }

    /** The type of the rows of a file at a literal path, read from its schema. */
    public static Optional<SequenceType> inferReturnType(FunctionCallExpression call, ReturnTypeContext context) {
        return ReturnTypeRules.fileSchemaType(call, context, path -> {
            try {
                return ItemTypeFactory.createItemType(SparkSessionManager.getInstance()
                        .getOrCreateSession()
                        .read()
                        .parquet(path)
                        .schema());
            } catch (Exception e) {
                return null;
            }
        });
    }
}
