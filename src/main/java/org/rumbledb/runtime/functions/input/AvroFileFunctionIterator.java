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

package org.rumbledb.runtime.functions.input;

import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.spark.SparkSessionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class AvroFileFunctionIterator extends DataFrameRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public AvroFileFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        Item stringItem = this.children.get(0)
            .materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
        String url = stringItem.getStringValue();
        Item optionsObjectItem;
        DataFrameReader dfr = SparkSessionManager.getInstance().getOrCreateSession().read();
        try {
            if (this.children.size() > 1 && ((optionsObjectItem = getObjectItem()) != null)) {
                ObjectItem options = (ObjectItem) optionsObjectItem;
                List<String> keys = options.getKeys();
                List<Item> values = options.getValues();
                for (int i = 0; i < keys.size(); i++) {
                    Item value = values.get(i);
                    if (value.isString()) {
                        if (keys.get(i).equals("avroSchema")) {
                            try {
                                String jsonFormatSchema = new String(
                                        Files.readAllBytes(Paths.get(value.getStringValue()))
                                );
                                dfr.option(keys.get(i), jsonFormatSchema);
                            } catch (IOException e) {
                                throw new CannotRetrieveResourceException(
                                        "File " + value.getStringValue() + " not found.",
                                        this.getMetadata()
                                );
                            }
                        } else {
                            dfr.option(keys.get(i), value.getStringValue());
                        }
                    } else if (value.isBoolean()) {
                        dfr.option(keys.get(i), value.getBooleanValue());
                    } else {
                        throw new UnexpectedTypeException(
                                "Only string and boolean types allowed as values",
                                this.getMetadata()
                        );
                    }
                }
            }
            return dfr.format("avro").load(url);
        } catch (Exception e) {
            if (e instanceof UnexpectedTypeException) {
                throw new UnexpectedTypeException(e.getMessage(), this.getMetadata());
            } else {
                throw new CannotRetrieveResourceException(e.getMessage(), getMetadata());
            }
        }
    }

    private Item getObjectItem() {
        return this.children.get(1).materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
    }
}
