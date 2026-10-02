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
package org.rumbledb.runtime.flwor.udfs;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.sql.api.java.UDF1;

import scala.collection.immutable.ArraySeq;

import org.rumbledb.api.Item;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;

public class GroupClauseSerializeAggregateResultsUDF implements UDF1<ArraySeq<byte[]>, byte[]> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Item> nextResult;
    private final List<List<Item>> deserializedParams;
    private final DataFrameContext dataFrameContext;

    public GroupClauseSerializeAggregateResultsUDF() {
        this.nextResult = new ArrayList<>();
        this.deserializedParams = new ArrayList<>();
        this.dataFrameContext = new DataFrameContext();
    }

    @Override
    public byte[] call(ArraySeq<byte[]> wrappedParameters) {
        this.nextResult.clear();
        this.deserializedParams.clear();
        FlworDataFrameUtils.deserializeWrappedParameters(
                wrappedParameters,
                this.deserializedParams,
                this.dataFrameContext.getKryo(),
                this.dataFrameContext.getInput());

        for (List<Item> deserializedParam : this.deserializedParams) {
            this.nextResult.addAll(deserializedParam);
        }
        return FlworDataFrameUtils.serializeItemList(
                this.nextResult, this.dataFrameContext.getKryo(), this.dataFrameContext.getOutput());
    }
}
