/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
package sparksoniq.spark.iterator.function;

import org.apache.spark.api.java.JavaRDD;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.io.json.StringToItemMapper;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.spark.SparkContextManager;

import javax.naming.OperationNotSupportedException;
import java.util.List;

public class ParseJsonFunctionIterator extends SparkFunctionCallIterator {
    public ParseJsonFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public JavaRDD<Item> getRDD() {
        if (this._rdd == null) {
            JavaRDD<String> strings;
            RuntimeIterator urlIterator = this._children.get(0);
            urlIterator.open(this._currentDynamicContext);
            if (this._children.size() == 1)
                try {
                    strings = SparkContextManager.getInstance().getContext().textFile(urlIterator.next().getStringValue());
                } catch (OperationNotSupportedException e) {
                    throw new IllegalArgumentException("parse-json illegal argument");
                }
            else {
                RuntimeIterator partitionsIterator = this._children.get(1);
                partitionsIterator.open(_currentDynamicContext);
                try {
                    strings = SparkContextManager.getInstance().getContext().textFile(
                            urlIterator.next().getStringValue(),
                            partitionsIterator.next().getIntegerValue());
                } catch (OperationNotSupportedException e) {
                    throw new IllegalArgumentException("parse-json illegal argument");
                }
                partitionsIterator.close();
            }

            _rdd = strings.mapPartitions(new StringToItemMapper(getMetadata()));
            urlIterator.close();
        }
        return _rdd;
    }

}
