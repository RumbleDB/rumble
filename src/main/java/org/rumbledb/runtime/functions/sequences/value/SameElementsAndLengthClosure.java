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
package org.rumbledb.runtime.functions.sequences.value;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.function.FlatMapFunction2;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;

public class SameElementsAndLengthClosure implements FlatMapFunction2<Iterator<Item>, Iterator<Item>, Boolean> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String collation;

    public SameElementsAndLengthClosure(String collation) {
        this.collation = collation;
    }

    @Override
    public Iterator<Boolean> call(Iterator<Item> iterator1, Iterator<Item> iterator2) throws Exception {
        List<Boolean> list = new ArrayList<>();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            if (!DeepEqualFunctionIterator.checkItemsDeepEqual(
                    iterator1.next(), iterator2.next(), this.collation, ExceptionMetadata.EMPTY_METADATA)) {
                list.add(true);
                return list.iterator();
            }
        }
        if (iterator1.hasNext() || iterator2.hasNext()) {
            list.add(true);
        }
        return list.iterator();
    }
}
