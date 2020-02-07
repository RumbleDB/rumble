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
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package sparksoniq.spark.iterator.function;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.runtime.iterator.DataFrameRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.SparkSessionManager;

import java.util.List;

public class LibSVMFileFunctionIterator extends DataFrameRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public LibSVMFileFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata metadata
    ) {
        super(arguments, executionMode, metadata);
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        RuntimeIterator urlIterator = this.children.get(0);
        urlIterator.open(context);
        String url = urlIterator.next().getStringValue();
        urlIterator.close();
        try {
            return SparkSessionManager.getInstance()
                .getOrCreateSession()
                .read()
                .format("libsvm")
                .load(url);
        } catch (Exception e) {
            if (e instanceof AnalysisException) {
                throw new CannotRetrieveResourceException("File " + url + " not found.", getMetadata());
            }
            throw e;
        }
    }
}
