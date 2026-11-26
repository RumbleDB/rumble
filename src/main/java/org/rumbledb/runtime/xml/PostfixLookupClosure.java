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
package org.rumbledb.runtime.xml;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.function.FlatMapFunction;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.functions.arrays.ArrayIndexUtils;

/**
 * Spark closure for postfix lookup with XQuery 3.1 semantics: array index out of bounds
 * raises err:FOAY0001 (propagated from {@link org.rumbledb.exceptions.ArrayIndexOutOfBoundsException}).
 */
public class PostfixLookupClosure implements FlatMapFunction<Item, Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Item> keys;
    private final boolean wildcard;
    private final ExceptionMetadata expressionMetadata;

    public PostfixLookupClosure(List<Item> keys, boolean wildcard, ExceptionMetadata expressionMetadata) {
        this.keys = keys;
        this.wildcard = wildcard;
        this.expressionMetadata = expressionMetadata != null ? expressionMetadata : ExceptionMetadata.EMPTY_METADATA;
    }

    @Override
    public Iterator<Item> call(Item arg0) throws Exception {
        List<Item> results = new ArrayList<>();

        if (arg0.isMap()) {
            if (this.wildcard) {
                if (arg0.isObject()) {
                    results.addAll(arg0.getItemValues());
                } else {
                    for (List<Item> valueSequence : arg0.getSequenceValues()) {
                        results.addAll(valueSequence);
                    }
                }
            } else {
                PostfixLookupIterator.appendMapLookup(arg0, this.keys, results, this.expressionMetadata);
            }
        } else if (arg0.isArray()) {
            if (this.wildcard) {
                if (arg0.isArrayOfItems()) {
                    results = arg0.getItemMembers();
                } else {
                    for (List<Item> member : arg0.getSequenceMembers()) {
                        results.addAll(member);
                    }
                }
            } else {
                for (Item key : ArrayIndexUtils.atomizeLookupKeys(this.keys, this.expressionMetadata)) {
                    int idx = ArrayIndexUtils.getZeroBasedPosition(key, arg0.getSize(), this.expressionMetadata);
                    if (arg0.isArrayOfItems()) {
                        results.add(arg0.getItemAt(idx));
                    } else {
                        results.addAll(arg0.getSequenceAt(idx));
                    }
                }
            }
        }
        return results.iterator();
    }
}
