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
package org.rumbledb.runtime.functions.sequences.aggregate;

import java.io.Serial;

import org.apache.spark.api.java.function.Function2;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;

public class SumClosure implements Function2<Item, Item, Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ExceptionMetadata metadata;

    public SumClosure(ExceptionMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public Item call(Item v1, Item v2) throws Exception {
        return SumFunctionIterator.addToSum(v1, v2, this.metadata);
    }
}
