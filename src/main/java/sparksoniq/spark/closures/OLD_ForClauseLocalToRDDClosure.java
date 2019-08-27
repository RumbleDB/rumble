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

package sparksoniq.spark.closures;

import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;

import sparksoniq.jsoniq.tuple.FlworTuple;

public class OLD_ForClauseLocalToRDDClosure implements Function<Item, FlworTuple> {

	private static final long serialVersionUID = 1L;
	private final String _variableName;
    private final FlworTuple _inputTuple;


    public OLD_ForClauseLocalToRDDClosure(String variableName, FlworTuple inputTuple) {
        this._variableName = variableName;
        this._inputTuple = inputTuple;
    }

    @Override
    public FlworTuple call(Item item) throws Exception {
        FlworTuple result = new FlworTuple(_inputTuple);
        result.putValue(_variableName, item, true);
        return result;

    }
}
