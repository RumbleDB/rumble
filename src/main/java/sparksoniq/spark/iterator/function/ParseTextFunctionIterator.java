/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

import org.apache.spark.api.java.JavaRDD;

import sparksoniq.io.json.StringMapper;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.SparkSessionManager;

import java.util.List;

public class ParseTextFunctionIterator extends SparkFunctionCallIterator {

	private static final long serialVersionUID = 1L;

	public ParseTextFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        long resultSize = this.getRDD(_currentDynamicContext).count();
        if (resultSize == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDD(DynamicContext context) {
        if (this._rdd == null) {
            JavaRDD<String> strings;
            RuntimeIterator urlIterator = this._children.get(0);
            urlIterator.open(context);
            if (this._children.size() == 1)
                strings = SparkSessionManager.getInstance().getJavaSparkContext().textFile(urlIterator.next().getStringValue());
            else {
                RuntimeIterator partitionsIterator = this._children.get(1);
                partitionsIterator.open(_currentDynamicContext);
                strings = SparkSessionManager.getInstance().getJavaSparkContext().textFile(
                        urlIterator.next().getStringValue(),
                        partitionsIterator.next().getIntegerValue());
                partitionsIterator.close();
            }

            _rdd = strings.mapPartitions(new StringMapper());
            urlIterator.close();
        }
        return _rdd;
    }

}
