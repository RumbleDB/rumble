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
package org.rumbledb.runtime.functions.maps;

import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.DuplicateJSONKeyException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

/**
 * W3C XPath/XQuery {@code map:merge}:
 *
 * <p>
 * Implements the FO 3.1 conceptual semantics:
 *
 * <pre>
 * let $duplicates-handler := map {
 *   "use-first":   function($a, $b) {$a},
 *   "use-last":    function($a, $b) {$b},
 *   "combine":     function($a, $b) {$a, $b},
 *   "reject":      function($a, $b) {fn:error($FOJS0003)},
 *   "use-any":     function($a, $b) { ... implementation-dependent ... }
 * },
 * $combine-maps := function($A as map(*), $B as map(*), $deduplicator as function(*)) {
 *   fn:fold-left(map:keys($B), $A, function($z, $k){
 *     if (map:contains($z, $k))
 *     then map:put($z, $k, $deduplicator($z($k), $B($k)))
 *     else map:put($z, $k, $B($k))
 *   })
 * }
 * return fn:fold-left($MAPS, map{},
 *   $combine-maps(?, ?, $duplicates-handler(($OPTIONS?duplicates, "use-first")[1])))
 * </pre>
 *
 * <p>
 * This implementation stays local-only, collects the entries using atomic same-key equality, and builds the map
 * once.
 */
public class MapMergeFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan mapsIterator;
    private final ItemRuntimePlan optionsIterator; // may be null for
    // arity-1

    public MapMergeFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
        if (arguments.size() == 1) {
            this.mapsIterator = arguments.get(0);
            this.optionsIterator = null;
        } else if (arguments.size() == 2) {
            this.mapsIterator = arguments.get(0);
            this.optionsIterator = arguments.get(1);
        } else {
            throw new OurBadException("map:merge must have one or two arguments.");
        }
    }

    private enum DuplicatePolicy {
        USE_FIRST,
        USE_LAST,
        USE_ANY,
        COMBINE,
        REJECT
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        ExceptionMetadata metadata = getMetadata();

        // 2. Resolve options and duplicates policy.
        DuplicatePolicy policy = DuplicatePolicy.USE_FIRST; // spec default
        if (this.optionsIterator != null) {
            List<Item> optionsSeq = this.optionsIterator.materialize(context);
            if (optionsSeq.isEmpty()) {
                // map-merge-026: second argument must not be empty -> XPTY0004.
                throw new UnexpectedTypeException(
                        "map:merge options argument must be a single map, not the empty sequence [err:XPTY0004].",
                        metadata);
            }
            if (optionsSeq.size() != 1 || !optionsSeq.get(0).isMap()) {
                throw new UnexpectedTypeException(
                        "map:merge options argument must be exactly one map(*) [err:XPTY0004].", metadata);
            }
            Item optionsMap = optionsSeq.get(0);
            // $OPTIONS?duplicates, fallback to \"use-first\".
            List<Item> duplicatesSeq = optionsMap.getSequenceByKey("duplicates");
            if (duplicatesSeq != null && !duplicatesSeq.isEmpty()) {
                Item d = duplicatesSeq.get(0);
                if (!d.isString()) {
                    // FOJS0005: invalid option value for a recognized key.
                    throw new UnexpectedTypeException(
                            "Invalid value for map:merge option duplicates (expected xs:string) [err:FOJS0005].",
                            metadata);
                }
                String v = d.getStringValue();
                if ("use-first".equals(v)) {
                    policy = DuplicatePolicy.USE_FIRST;
                } else if ("use-last".equals(v)) {
                    policy = DuplicatePolicy.USE_LAST;
                } else if ("use-any".equals(v)) {
                    policy = DuplicatePolicy.USE_ANY;
                } else if ("combine".equals(v)) {
                    policy = DuplicatePolicy.COMBINE;
                } else if ("reject".equals(v)) {
                    policy = DuplicatePolicy.REJECT;
                } else {
                    // FOJS0005: invalid value for a recognized key.
                    throw new UnexpectedTypeException(
                            "Invalid value for map:merge option duplicates: " + v + " [err:FOJS0005].", metadata);
                }
            }
        }

        // 3. Collect the entries, with the key of the latest map that set each one, as a fold of map:put
        // would, then build the map in one go. Streaming consumption avoids materializing huge sequences of maps.
        Map<Item, Integer> positions = new HashMap<>();
        List<Item> keys = new ArrayList<>();
        List<List<Item>> values = new ArrayList<>();
        try (Cursor<Item> maps = this.mapsIterator.getCursor(context)) {
            while (maps.hasNext()) {
                Item mapItem = maps.next();
                if (!mapItem.isMap()) {
                    throw new UnexpectedTypeException(
                            "map:merge expects a sequence of map(*) items as first argument [err:XPTY0004].", metadata);
                }
                for (Item key : mapItem.getItemKeys()) {
                    List<Item> value = mapItem.getSequenceByKey(key);
                    Integer position = positions.putIfAbsent(key, keys.size());
                    if (position == null) {
                        keys.add(key);
                        values.add(value);
                        continue;
                    }
                    switch (policy) {
                        case USE_FIRST:
                        case USE_ANY:
                            continue;
                        case USE_LAST:
                            break;
                        case COMBINE:
                            List<Item> existing = values.get(position);
                            List<Item> combined = new ArrayList<>(existing.size() + value.size());
                            combined.addAll(existing);
                            combined.addAll(value);
                            value = combined;
                            break;
                        case REJECT:
                            throw new DuplicateJSONKeyException(
                                    "map:merge encountered duplicate map keys with duplicates=\"reject\" [err:FOJS0003].",
                                    metadata);
                        default:
                            throw new OurBadException("Unexpected duplicates policy in map:merge.");
                    }
                    keys.set(position, key);
                    values.set(position, value);
                }
            }
        }
        return ItemFactory.getInstance()
                .createMapItem(
                        keys, values, metadata, this.getRuntimeStaticContext().isQuerySideEffecting());
    }
}
