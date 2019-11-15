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

package sparksoniq.spark.closures;

import org.apache.spark.api.java.function.Function;

import scala.Tuple2;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.tuple.FlworTuple;

public class CountClauseClosure implements Function<Tuple2<FlworTuple, Long>, FlworTuple> {

    private static final long serialVersionUID = 1L;
    private String variableName;

    public CountClauseClosure(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public FlworTuple call(Tuple2<FlworTuple, Long> inputTuple) throws Exception {
        FlworTuple result = inputTuple._1;
        result.putValue(
            variableName,
            ItemFactory.getInstance().createIntegerItem(inputTuple._2.intValue()),
            true
        );
        return result;
    }
}
