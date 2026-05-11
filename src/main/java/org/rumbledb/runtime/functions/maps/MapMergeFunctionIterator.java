package org.rumbledb.runtime.functions.maps;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.MapSameKeyWrapper;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * This implementation stays local-only and mirrors that behaviour using the existing MapItem
 * representation and {@code MapAtomicSameKey} key equality via {@code MapItem.getSequenceByKey()}.
 */
public class MapMergeFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator mapsIterator;
    private final RuntimeIterator optionsIterator; // may be null for arity-1

    public MapMergeFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
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
    public Item materializeFirstItemOrNull(DynamicContext context) {
        ExceptionMetadata metadata = getMetadata();

        // 2. Resolve options and duplicates policy.
        DuplicatePolicy policy = DuplicatePolicy.USE_FIRST; // spec default
        if (this.optionsIterator != null) {
            List<Item> optionsSeq = this.optionsIterator.materialize(context);
            if (optionsSeq.isEmpty()) {
                // map-merge-026: second argument must not be empty -> XPTY0004.
                throw new UnexpectedTypeException(
                        "map:merge options argument must be a single map, not the empty sequence [err:XPTY0004].",
                        metadata
                );
            }
            if (optionsSeq.size() != 1 || !optionsSeq.get(0).isMap()) {
                throw new UnexpectedTypeException(
                        "map:merge options argument must be exactly one map(*) [err:XPTY0004].",
                        metadata
                );
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
                            metadata
                    );
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
                            "Invalid value for map:merge option duplicates: " + v + " [err:FOJS0005].",
                            metadata
                    );
                }
            }
        }

        // 3. Implement fold-left over maps with a hashed same-key accumulator.
        // Streaming consumption avoids materializing huge sequences of maps.
        Map<MapSameKeyWrapper, List<Item>> accumulator = new HashMap<>();
        boolean sawAnyMap = false;
        boolean allKeysString = true;
        boolean allValuesSingletons = true;
        this.mapsIterator.open(context);
        try {
            while (this.mapsIterator.hasNext()) {
                Item mapItem = this.mapsIterator.next();
                if (!mapItem.isMap()) {
                    throw new UnexpectedTypeException(
                            "map:merge expects a sequence of map(*) items as first argument [err:XPTY0004].",
                            metadata
                    );
                }
                sawAnyMap = true;
                List<Item> bKeys = mapItem.getItemKeys();
                for (Item bKey : bKeys) {
                    List<Item> bSeq = mapItem.getSequenceByKey(bKey);
                    if (bSeq == null) {
                        bSeq = new ArrayList<>();
                    }

                    MapSameKeyWrapper lookup = new MapSameKeyWrapper(bKey);
                    List<Item> existingSeq = accumulator.get(lookup);
                    if (existingSeq == null) {
                        accumulator.put(lookup, new ArrayList<>(bSeq));
                        if (allKeysString && !bKey.isString()) {
                            allKeysString = false;
                        }
                        if (allValuesSingletons && bSeq.size() != 1) {
                            allValuesSingletons = false;
                        }
                        continue;
                    }

                    switch (policy) {
                        case USE_FIRST:
                            break;
                        case USE_LAST:
                            accumulator.put(lookup, new ArrayList<>(bSeq));
                            if (allKeysString && !bKey.isString()) {
                                allKeysString = false;
                            }
                            if (allValuesSingletons && bSeq.size() != 1) {
                                allValuesSingletons = false;
                            }
                            break;
                        case USE_ANY:
                            break;
                        case COMBINE:
                            List<Item> combined = new ArrayList<>(existingSeq.size() + bSeq.size());
                            combined.addAll(existingSeq);
                            combined.addAll(bSeq);
                            accumulator.put(lookup, combined);
                            if (allKeysString && !bKey.isString()) {
                                allKeysString = false;
                            }
                            if (allValuesSingletons && bSeq.size() != 1) {
                                allValuesSingletons = false;
                            }
                            break;
                        case REJECT:
                            throw new UnexpectedTypeException(
                                    "map:merge encountered duplicate map keys with duplicates=\"reject\" [err:FOJS0003].",
                                    metadata
                            );
                        default:
                            throw new OurBadException("Unexpected duplicates policy in map:merge.");
                    }
                }
            }
        } finally {
            this.mapsIterator.close();
        }

        // Empty input -> empty map.
        if (!sawAnyMap) {
            return ItemFactory.getInstance()
                .createObjectItemOptimized(new HashMap<>(), false);
        }

        if (allKeysString && allValuesSingletons) {
            // fast path: construct an object item
            HashMap<String, Item> newKeyValuePairs = new HashMap<>();
            for (Map.Entry<MapSameKeyWrapper, List<Item>> entry : accumulator.entrySet()) {
                Item key = entry.getKey().getKey();
                List<Item> values = entry.getValue();
                newKeyValuePairs.put(key.getStringValue(), values.get(0));
            }
            return ItemFactory.getInstance().createObjectItemOptimized(newKeyValuePairs, false);
        } else {
            // construct a map item
            // 4. Build final MapItem from accumulator via map overload to avoid duplicate-key verification.
            Map<Item, List<Item>> finalKeyValuePairs = new HashMap<>();
            for (Map.Entry<MapSameKeyWrapper, List<Item>> entry : accumulator.entrySet()) {
                Item key = entry.getKey().getKey();
                List<Item> values = entry.getValue();
                finalKeyValuePairs.put(key, values);
            }
            return ItemFactory.getInstance()
                .createMapItem(finalKeyValuePairs, metadata, false);
        }

    }
}

